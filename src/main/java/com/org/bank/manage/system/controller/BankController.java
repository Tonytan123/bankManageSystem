package com.org.bank.manage.system.controller;

import com.org.bank.manage.system.common.BaseResponse;
import com.org.bank.manage.system.request.BankTransferVO;
import com.org.bank.manage.system.request.CreateBankAccountVO;
import com.org.bank.manage.system.request.DeleteBankAccountVO;
import com.org.bank.manage.system.request.UpdateBankAccountVO;
import com.org.bank.manage.system.service.BankManageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author tucker
 */
@RestController
@RequestMapping("/bank/account/manage/v1/")
public class BankController {

    @Autowired
    BankManageService bankManageService;

    @PostMapping("/createBankAccount")
    @ResponseBody
    public BaseResponse<?> createBankAccount(@Valid @Validated @RequestBody @NotNull CreateBankAccountVO req){
        return this.bankManageService.createBankAccount(req);
    }

    @PostMapping("/updateBankAccount")
    @ResponseBody
    public BaseResponse<?> updateBankAccount(@Valid @Validated @RequestBody @NotNull UpdateBankAccountVO req){
        return this.bankManageService.updateBankAccount(req);
    }

    @PostMapping("/deleteBankAccount")
    public BaseResponse<?> updateBankAccount(@Valid @Validated @RequestBody @NotNull DeleteBankAccountVO req){
        return this.bankManageService.deleteBankAccount(req);
    }

    @GetMapping("/getBankAccountPage/userId/{userId}")
    public BaseResponse<?> getBankAccountPage(@PathVariable("userId") @NotNull String userId,
                                              @RequestParam("pageNo") @NotNull Integer pageNo,
                                              @RequestParam("pageSize") @NotNull Integer pageSize){
        return this.bankManageService.listAccount(pageNo, pageSize, userId);
    }

    @GetMapping("/getBankAccountDetail/bankCardNumber/{bankCardNumber}")
    public BaseResponse<?> getBankAccountDetail(@PathVariable("bankCardNumber") @NotNull String bankCardNumber){
        return this.bankManageService.detailAccount(bankCardNumber);
    }

    @PostMapping("/bankTransfer")
    public BaseResponse<?> bankTransfer(@Valid @Validated @RequestBody @NotNull BankTransferVO bankTransferVO){
        return this.bankManageService.bankTransfer(bankTransferVO);
    }
}
