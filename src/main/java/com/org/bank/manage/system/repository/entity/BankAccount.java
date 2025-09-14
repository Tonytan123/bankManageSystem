package com.org.bank.manage.system.repository.entity;

import com.org.bank.manage.system.common.AccountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author tucker
 */
@Entity
@Table(name = "bankaccount",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "bank_card_number")
                //@UniqueConstraint(columnNames = "id_card")
        },
        indexes = {
                @Index(name = "idx_user_uid", columnList = "user_uid")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "User ID is required")
    @Size(max = 100, message = "User ID must not exceed 100 characters")
    @Column(name = "user_uid", length = 100, nullable = false)
    private String userUid;  // 用户uid

    @NotBlank(message = "ID Card is required")
    @Size(max = 100, message = "ID Card must not exceed 100 characters")
    @Column(name = "id_card", length = 100, nullable = false)
    private String idCard;

    @NotBlank(message = "accountHolderName is required")
    @Size(max = 100, message = "accountHolderName must not exceed 100 characters")
    @Column(name = "account_holder_name", length = 100, nullable = false)
    private String accountHolderName;

    @NotBlank(message = "Contact Number is required")
    @Size(max = 100, message = "Contact Number must not exceed 100 characters")
    @Column(name = "contact_number", length = 100, nullable = false)
    private String contactNumber;

    @NotBlank(message ="bankCardNumber cannot be empty")
    @Size(max = 100, message = "bankCardNumber must not exceed 100 characters")
    @Column(name = "bank_card_number", length = 100, nullable = false)
    private String bankCardNumber;

    @NotNull(message = "balance is required")
    @DecimalMin(value = "0.01", message = "balance must be greater than zero")
    @Column(name = "balance", precision = 15, scale = 2, nullable = false)
    private BigDecimal balance;

    @Column(name = "status", length = 20,nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status; // Active, Frozen, Suspended,Dormant, Closed， Deleted

    @Size(max = 200)
    @Column(name = "description")
    private String description;

    @Size(max = 200)
    @Column(name = "email_address")
    private String emailAddress;
    @NotNull(message = "Timestamp is required")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
