package com.org.bank.manage.system.service;

import com.org.bank.manage.system.request.BaseResponse;
import com.org.bank.manage.system.request.BankTransferVO;
import com.org.bank.manage.system.request.CreateBankAccountVO;
import com.org.bank.manage.system.request.DeleteBankAccountVO;
import com.org.bank.manage.system.request.UpdateBankAccountVO;

/**
 * @author tucker
 */
public interface BankManageService {

    BaseResponse<?> createBankAccount(CreateBankAccountVO createBankAccountVO);

    BaseResponse<?> updateBankAccount(UpdateBankAccountVO editBankAccountReq);

    BaseResponse<?> deleteBankAccount(DeleteBankAccountVO vo);

    BaseResponse<?> listAccount(Integer pageNo, Integer pageSize, String userId);

    BaseResponse<?> detailAccount(String bankCardNumber);

    BaseResponse<?> bankTransfer(BankTransferVO bankTransferVO);
}
