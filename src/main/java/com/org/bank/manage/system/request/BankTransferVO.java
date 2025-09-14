package com.org.bank.manage.system.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author tucker
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankTransferVO implements Serializable {
    @NotBlank(message ="sendAccountHolderName cannot be empty")
    @NotNull(message = "sendAccountHolderName cannot be null")
    private String sendAccountHolderName;

    @NotBlank(message ="sendBankCardNumber cannot be empty")
    @NotNull(message = "sendBankCardNumber cannot be null")
    private String sendBankCardNumber;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", message = "amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message ="sendAccountHolderName cannot be empty")
    @NotNull(message = "sendAccountHolderName cannot be null")
    private String receiveAccountHolderName;

    @NotBlank(message ="sendBankCardNumber cannot be empty")
    @NotNull(message = "sendBankCardNumber cannot be null")
    private String receiveBankCardNumber;
}
