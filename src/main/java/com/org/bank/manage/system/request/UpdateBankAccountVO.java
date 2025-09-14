package com.org.bank.manage.system.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tucker
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBankAccountVO extends CreateBankAccountVO {
    @NotNull(message = "status cannot be null")
    private String status;
}
