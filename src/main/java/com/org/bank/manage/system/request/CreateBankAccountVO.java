package com.org.bank.manage.system.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author tucker
 */
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBankAccountVO implements Serializable {
    @NotBlank(message ="accountHolderName cannot be empty")
    @NotNull(message = "accountHolderName cannot be null")
    private String accountHolderName;

    @NotBlank(message ="contactNumber cannot be empty")
    @NotNull(message = "contactNumber cannot be null")
    private String contactNumber;

    @NotBlank(message ="idCard cannot be empty")
    @NotNull(message = "idCard cannot be null")
    private String idCard;

    private String emailAddress;

    @NotNull(message = "balance is required")
    @DecimalMin(value = "0.01", message = "balance must be greater than zero")
    private BigDecimal balance;

    @NotBlank(message ="description cannot be empty")
    @NotNull(message = "description cannot be null")
    private String description;

    @NotBlank(message ="bankCardNumber cannot be empty")
    @NotNull(message = "bankCardNumber cannot be null")
    private String bankCardNumber;

    @NotBlank(message ="userId cannot be empty")
    @NotNull(message = "userId cannot be null")
    @Size(max = 100, message = "User ID must not exceed 100 characters")
    private String userId;

//
//    private LocalDateTime createdAt;
//
//    private LocalDateTime updatedAt;

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
