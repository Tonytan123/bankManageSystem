package com.org.bank.manage.system.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author tucker
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteBankAccountVO implements Serializable {
    @NotBlank(message ="bankCardNumber cannot be empty")
    @NotNull(message = "bankCardNumber cannot be null")
    private String bankCardNumber;
}
