package com.schoolmoney.pl.modules.classes.service;

import com.schoolmoney.pl.core.user.management.UserMapper;
import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.core.user.models.UserResponse;
import com.schoolmoney.pl.files.storage.services.StorageService;
import com.schoolmoney.pl.modules.classes.management.ClassManager;
import com.schoolmoney.pl.modules.classes.management.ClassSpecifications;
import com.schoolmoney.pl.modules.classes.models.ClassDAO;
import com.schoolmoney.pl.modules.classes.models.ClassGetPageResponse;
import com.schoolmoney.pl.modules.classes.models.ClassGetResponse;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClassGetService {
    private final ClassManager classManager;
    private final UserMapper userMapper;
    private final HttpServletRequest request;
    private final StorageService storageService;

    public ClassGetPageResponse getUserTreasurerClass(int page, int limit, boolean isTreasurer){
        log.info("Getting user classes");
        UserDAO userDAO = (UserDAO) request.getAttribute("user");

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<ClassDAO> classes;

        if (isTreasurer) {
            Specification<ClassDAO> specification = ClassSpecifications.findByUserTreasurer(userDAO.getId());
            classes = classManager.findUserTreasurerClasses(specification, pageable);
        } else {
            classes = classManager.findByParentId(userDAO.getId(), pageable);
        }

        List<ClassGetResponse> list = classes.getContent().stream()
                .map(item -> {
                    UserResponse userResponse = userMapper.mapToResponseFromEntity(
                            item.getTreasurer(),
                            new CycleAvoidingMappingContext(),
                            storageService
                    );


                    return ClassGetResponse.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .treasurer(userResponse)
                            .build();
                })
                .toList();

        return ClassGetPageResponse.builder()
                .count(classes.getTotalElements())
                .data(list)
                .build();

    }
}
