package com.schoolmoney.pl.modules.finance.collections.models;

import com.schoolmoney.pl.modules.classes.models.ClassDAO;
import com.schoolmoney.pl.modules.finance.attachments.models.CollectionAttachmentDAO;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountDAO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "collection")
public class CollectionDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "logo")
    private String logo;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "goal")
    private Long goal;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "finance_account_id")
    private FinanceAccountDAO financeAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CollectionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private ClassDAO aClass;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "goal_reached_at")
    private Instant goalReachedAt;

    @Column(name = "archived_at")
    private Instant archivedAt;

    @Builder.Default
    @Column(name = "is_archived")
    private Boolean isArchived = false;

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CollectionAttachmentDAO> attachments = new ArrayList<>();

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CollectionDAO other = (CollectionDAO) obj;

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