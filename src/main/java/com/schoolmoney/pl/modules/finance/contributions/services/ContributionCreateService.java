package com.schoolmoney.pl.modules.finance.contributions.services;

import com.schoolmoney.pl.core.student.management.StudentManager;
import com.schoolmoney.pl.core.student.management.StudentMapper;
import com.schoolmoney.pl.core.student.management.StudentNotFoundException;
import com.schoolmoney.pl.core.student.models.Student;
import com.schoolmoney.pl.core.student.models.StudentDAO;
import com.schoolmoney.pl.core.user.management.UserManager;
import com.schoolmoney.pl.core.user.management.UserMapper;
import com.schoolmoney.pl.core.user.models.User;
import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.classMember.management.ClassMemberManager;
import com.schoolmoney.pl.modules.classMember.management.ClassMemberSpecifications;
import com.schoolmoney.pl.modules.classMember.models.ClassMemberDAO;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionManager;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionMapper;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionNotFoundException;
import com.schoolmoney.pl.modules.finance.collections.models.Collection;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionDAO;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionStatus;
import com.schoolmoney.pl.modules.finance.contributions.management.*;
import com.schoolmoney.pl.modules.finance.contributions.models.*;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountManager;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountMapper;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountNotFoundException;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccount;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountDAO;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContributionCreateService {
    private final HttpServletRequest request;
    private final ContributionManager contributionManager;
    private final ContributionMapper contributionMapper;
    private final CollectionManager collectionManager;
    private final CollectionMapper collectionMapper;
    private final StudentManager studentManager;
    private final StudentMapper studentMapper;
    private final UserManager userManager;
    private final UserMapper userMapper;
    private final FinanceAccountManager financeAccountManager;
    private final FinanceAccountMapper financeAccountMapper;
    private final ClassMemberManager classMemberManager;

    @Transactional
    public ContributionResponse createContribution(
            UUID collectionId,
            ContributionCreateRequest contributionRequest
    ) {
        log.info("Creating contribution for collection: {}, student: {}, amount: {}",
                collectionId, contributionRequest.studentId(), contributionRequest.amount());

        // 1. Get authenticated user
        UserDAO payerDAO = (UserDAO) request.getAttribute("user");
        User payer = userMapper.mapToDomain(payerDAO, new CycleAvoidingMappingContext());

        // 2. Load and validate collection
        CollectionDAO collectionDAO = collectionManager.findById(collectionId)
                .orElseThrow(CollectionNotFoundException::new);
        Collection collection = collectionMapper.mapToDomain(
                collectionDAO,
                new CycleAvoidingMappingContext()
        );
        validateCollectionStatus(collection);

        // 3. Load and validate student
        StudentDAO studentDAO = studentManager.findById(contributionRequest.studentId())
                .orElseThrow(StudentNotFoundException::new);
        Student student = studentMapper.mapToDomain(studentDAO, new CycleAvoidingMappingContext());
        validateStudentMembership(studentDAO, collectionDAO);

        // 4. Get payer's finance account
        FinanceAccountDAO payerAccountDAO = financeAccountManager
                .findByOwnerId(payerDAO.getId())
                .orElseThrow(() -> new FinanceAccountNotFoundException(
                        "Payer must have a finance account to make contributions"
                ));
        FinanceAccount payerAccount = financeAccountMapper.mapToDomain(
                payerAccountDAO,
                new CycleAvoidingMappingContext()
        );

        // 5. Calculate payment limits
        PaymentLimits limits = calculatePaymentLimits(
                collectionDAO,
                studentDAO,
                payerAccountDAO.getBalance()
        );

        // 6. Validate requested amount
        validateContributionAmount(contributionRequest.amount(), limits);

        // 7. Create contribution
        Contribution contribution = ContributionBuilders.buildCompletedContribution(
                collection,
                student,
                payer,
                payerAccount,
                contributionRequest.amount(),
                contributionRequest.note()
        );

        // 8. Process payment (deduct from payer, credit to collection account)
        processPayment(
                payerAccountDAO,
                collectionDAO.getFinanceAccount(),
                contributionRequest.amount()
        );

        // 9. Save contribution
        ContributionDAO contributionDAO = contributionMapper.mapToEntity(
                contribution,
                new CycleAvoidingMappingContext()
        );
        ContributionDAO savedContribution = contributionManager.saveToDatabase(contributionDAO);

        // 10. Check if goal reached and update collection status
        checkAndUpdateGoalStatus(collectionDAO, contributionRequest.amount());

        log.info("Successfully created contribution: {}", savedContribution.getId());

        // 11. Convert to response
        return buildContributionResponse(savedContribution);
    }

    private void validateCollectionStatus(Collection collection) {
        if (collection.getIsArchived()) {
            throw new CollectionGoalReachedException("Collection is archived");
        }
        if (collection.getStatus() == CollectionStatus.GOAL_REACHED) {
            throw new CollectionGoalReachedException(
                    "Collection goal has been reached, no more payments accepted"
            );
        }
        if (collection.getStatus() == CollectionStatus.ARCHIVED) {
            throw new CollectionGoalReachedException("Collection is archived");
        }
    }

    private void validateStudentMembership(StudentDAO student, CollectionDAO collection) {
        // Check if student is a confirmed member of the collection's class
        Specification<ClassMemberDAO> spec = Specification
                .where(ClassMemberSpecifications.findByClassId(collection.getAClass().getId()))
                .and(ClassMemberSpecifications.findByStudentId(student.getId()))
                .and(ClassMemberSpecifications.findConfirmed())
                .and(ClassMemberSpecifications.findNotArchived());

        long count = classMemberManager.count(spec);
        if (count == 0) {
            throw new InvalidContributionAmountException(
                    "Student must be a confirmed member of the collection's class"
            );
        }
    }

    private PaymentLimits calculatePaymentLimits(
            CollectionDAO collection,
            StudentDAO student,
            Double payerBalance
    ) {
        // Get total confirmed students in class
        Specification<ClassMemberDAO> spec = Specification
                .where(ClassMemberSpecifications.findByClassId(collection.getAClass().getId()))
                .and(ClassMemberSpecifications.findConfirmed())
                .and(ClassMemberSpecifications.findNotArchived());

        long totalStudents = classMemberManager.count(spec);

        if (totalStudents == 0) {
            throw new InvalidContributionAmountException("No confirmed students in class");
        }

        // Calculate per-student goal
        double perStudentGoal = collection.getGoal().doubleValue() / totalStudents;

        // Get total already contributed to collection
        double totalCollected = contributionManager.sumByCollectionAndStatus(
                collection.getId(),
                ContributionStatus.COMPLETED
        );

        // Get amount already paid for this student
        double studentPaid = contributionManager.sumByCollectionAndStudentAndStatus(
                collection.getId(),
                student.getId(),
                ContributionStatus.COMPLETED
        );

        // Calculate limits
        double studentRemaining = perStudentGoal - studentPaid;
        double collectionRemaining = collection.getGoal() - totalCollected;
        double maxAllowed = Math.min(
                Math.min(studentRemaining, collectionRemaining),
                payerBalance
        );

        return new PaymentLimits(
                perStudentGoal,
                studentPaid,
                studentRemaining,
                totalCollected,
                collectionRemaining,
                payerBalance,
                maxAllowed
        );
    }

    private void validateContributionAmount(Double requestedAmount, PaymentLimits limits) {
        if (requestedAmount == null || requestedAmount <= 0) {
            throw new InvalidContributionAmountException("Amount must be positive");
        }

        if (limits.maxAllowed() <= 0) {
            throw new ContributionExceedsLimitException(
                    "Cannot contribute: student quota is full or collection goal is reached"
            );
        }

        if (requestedAmount > limits.maxAllowed()) {
            throw new ContributionExceedsLimitException(
                    String.format(
                            "Maximum allowed: %.2f PLN (student remaining: %.2f, " +
                                    "collection remaining: %.2f, your balance: %.2f)",
                            limits.maxAllowed(),
                            limits.studentRemaining(),
                            limits.collectionRemaining(),
                            limits.payerBalance()
                    )
            );
        }

        if (requestedAmount > limits.payerBalance()) {
            throw new InsufficientBalanceException(
                    String.format("Insufficient balance. Available: %.2f PLN", limits.payerBalance())
            );
        }
    }

    @Transactional
    private void processPayment(
            FinanceAccountDAO fromAccount,
            FinanceAccountDAO toAccount,
            Double amount
    ) {
        // Deduct from payer
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        financeAccountManager.saveToDatabase(fromAccount);

        // Credit to collection account
        toAccount.setBalance(toAccount.getBalance() + amount);
        financeAccountManager.saveToDatabase(toAccount);

        log.info("Payment processed: {} PLN from account {} to account {}",
                amount, fromAccount.getId(), toAccount.getId());
    }

    private void checkAndUpdateGoalStatus(CollectionDAO collection, Double newAmount) {
        double totalCollected = contributionManager.sumByCollectionAndStatus(
                collection.getId(),
                ContributionStatus.COMPLETED
        );

        if (totalCollected >= collection.getGoal()) {
            collection.setStatus(CollectionStatus.GOAL_REACHED);
            collection.setGoalReachedAt(Instant.now());
            collectionManager.saveToDatabase(collection);

            log.info("Collection {} has reached its goal of {} PLN",
                    collection.getId(), collection.getGoal());
        }
    }

    private ContributionResponse buildContributionResponse(ContributionDAO contribution) {
        return ContributionResponse.builder()
                .contributionId(contribution.getId())
                .collection(CollectionSimpleResponse.builder()
                        .collectionId(contribution.getCollection().getId())
                        .title(contribution.getCollection().getTitle())
                        .logo(contribution.getCollection().getLogo())
                        .build())
                .student(StudentSimpleResponse.builder()
                        .studentId(contribution.getStudent().getId())
                        .firstName(contribution.getStudent().getFirstName())
                        .lastName(contribution.getStudent().getLastName())
                        .avatar(contribution.getStudent().getAvatar())
                        .build())
                .payer(UserSimpleResponse.builder()
                        .userId(contribution.getPayer().getId())
                        .name(contribution.getPayer().getName())
                        .surname(contribution.getPayer().getSurname())
                        .avatar(contribution.getPayer().getAvatar())
                        .build())
                .amount(contribution.getAmount())
                .note(contribution.getNote())
                .status(contribution.getStatus())
                .createdAt(contribution.getCreatedAt())
                .processedAt(contribution.getProcessedAt())
                .build();
    }

    // Helper record for payment limits
    private record PaymentLimits(
            double perStudentGoal,
            double studentPaid,
            double studentRemaining,
            double totalCollected,
            double collectionRemaining,
            double payerBalance,
            double maxAllowed
    ) {
    }
}
