package com.schoolmoney.pl.modules.finance.financeAccount.models;

import com.schoolmoney.pl.core.user.models.UserDAO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "finance_account")
public class FinanceAccountDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "iban")
    @Pattern(
            regexp = "^[A-Z]{2}\\d{2}[A-Z0-9]{1,30}$",
            message = "Invalid IBAN format"
    )
    private String IBAN;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "is_treasurer_account")
    private Boolean isTreasurerAccount;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private UserDAO owner;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "archived_at")
    private Instant archivedAt;

    @Column(name = "is_archived")
    private Boolean isArchived = false;


    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FinanceAccountDAO other = (FinanceAccountDAO) obj;

        if (id != null && other.id != null) {
            return Objects.equals(id, other.id);
        }

        return this == other;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [id=" + (id != null ? id : "null") + "]";
    }
}