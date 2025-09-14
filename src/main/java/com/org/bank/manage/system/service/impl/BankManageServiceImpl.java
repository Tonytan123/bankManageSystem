package com.org.bank.manage.system.service.impl;

import com.org.bank.manage.system.common.*;
import com.org.bank.manage.system.common.exception.BusinessException;
import com.org.bank.manage.system.repository.BankAccountRepository;
import com.org.bank.manage.system.repository.entity.BankAccount;
import com.org.bank.manage.system.request.BankTransferVO;
import com.org.bank.manage.system.request.CreateBankAccountVO;
import com.org.bank.manage.system.request.DeleteBankAccountVO;
import com.org.bank.manage.system.request.UpdateBankAccountVO;
import com.org.bank.manage.system.service.BankManageService;
import com.org.bank.manage.system.service.convert.BankMangeMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author tucker
 */
@Component
public class BankManageServiceImpl implements BankManageService {
    @Autowired
    BankAccountRepository bankAccountRepository;
    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(value = "bankaccounts", allEntries = true),
            @CacheEvict(value = "bankaccount", key = "#createBankAccountVO.bankCardNumber",
                    condition = "#createBankAccountVO.bankCardNumber != null")
    })
    public BaseResponse<?> createBankAccount(CreateBankAccountVO createBankAccountVO) {
        String bankCardNumber = createBankAccountVO.getBankCardNumber();
        Long isExisted =
                bankAccountRepository
                        .findByBankCardNumberExisted(bankCardNumber);
        if(isExisted>=1L) {
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
        }
        BankAccount bankAccount =
                BankMangeMapper.INSTANCE.createVoToEntity(createBankAccountVO,
                        AccountStatus.ACTIVE);
        try{
            this.bankAccountRepository.save(bankAccount);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
        }
        return ResultUtils.success(bankAccount);
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(value = "bankaccounts", allEntries = true),
            @CacheEvict(value = "bankaccount", key = "#editBankAccountReq.bankCardNumber",
                    condition = "#editBankAccountReq.bankCardNumber != null")
    })
    public BaseResponse<?> updateBankAccount(UpdateBankAccountVO editBankAccountReq) {
        String bankCardNumber = editBankAccountReq.getBankCardNumber();
        Optional<BankAccount> bankAccountOptional =
                bankAccountRepository
                        .findByBankCardNumber(bankCardNumber);
        if(bankAccountOptional.isPresent() == false) {
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_EXISTS);
        }
        // 状态校验：仅允许正常状态的账户被修改
        if(AccountStatus.ACTIVE.equals(bankAccountOptional.get().getStatus()) == false) {
            throw new BusinessException(ErrorCode.ACCOUNT_STATUS_NOT_ALLOWED);
        }
        try{
            this.bankAccountRepository.updateBankAccount(
                    editBankAccountReq.getBalance(),
                    editBankAccountReq.getIdCard(),
                    editBankAccountReq.getContactNumber(),
                    editBankAccountReq.getBankCardNumber(),
                    editBankAccountReq.getDescription(),
                    LocalDateTime.now());
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
        }
        return ResultUtils.success("update success");
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(value = "bankaccounts", allEntries = true),
            @CacheEvict(value = "bankaccount", key = "#vo.bankCardNumber",
                    condition = "#vo.bankCardNumber != null")
    })
    public BaseResponse<?> deleteBankAccount(DeleteBankAccountVO vo) {
        String bankCardNumber = vo.getBankCardNumber();
        Optional<BankAccount> bankAccountOptional =
                bankAccountRepository
                        .findByBankCardNumber(bankCardNumber);
        if(bankAccountOptional.isPresent() == false) {
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_EXISTS);
        }
        try{
            this.bankAccountRepository.updateBankAccountStatus(
                    AccountStatus.DELETED, bankCardNumber
            );
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
        }
        return ResultUtils.success("delete success");
    }

    @Override
    @Cacheable(value = "bankaccounts", key = "#pageNo + '-' + #pageSize + '-' + #userId")
    public BaseResponse<?> listAccount(Integer pageNo, Integer pageSize, String userId){
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        DecimalFormat df = new DecimalFormat("0.00");
        Page<BankAccount> allWithPagination = bankAccountRepository
                .findAllWithPagination(pageable, userId);
        int total = (int) allWithPagination.getTotalElements();
        PageRes<CreateBankAccountVO> result = new PageRes<>(pageNo,pageSize,total);
        List<CreateBankAccountVO> content = allWithPagination.getContent().stream().map(t->{
            String amountStr = df.format(t.getBalance());
            return BankMangeMapper.INSTANCE.entityToVO(t,amountStr);
        }).collect(Collectors.toList());
        result.setData(content);
        return ResultUtils.success(result);
    }

    @Override
    @Cacheable(value = "bankaccounts", key = "#bankCardNumber")
    public BaseResponse<?> detailAccount(String bankCardNumber) {
        Optional<BankAccount> bankAccountOptional =
                bankAccountRepository
                        .findByBankCardNumber(bankCardNumber);
        if(bankAccountOptional.isPresent() == false) {
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_EXISTS);
        }
        BankAccount bankAccount = bankAccountOptional.get();
        DecimalFormat df = new DecimalFormat("0.00");
        String amountStr = df.format(bankAccount.getBalance());
        return ResultUtils
                .success(BankMangeMapper.INSTANCE.entityToVO(bankAccount,amountStr));
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(value = "bankaccounts", allEntries = true),
            @CacheEvict(value = "bankaccount", key = "#bankTransferVO.sendBankCardNumber",
                    condition = "#bankTransferVO.sendBankCardNumber != null"),
            @CacheEvict(value = "bankaccount", key = "#bankTransferVO.receiveBankCardNumber",
                    condition = "#bankTransferVO.receiveBankCardNumber != null")
    })
    public BaseResponse<?> bankTransfer(BankTransferVO bankTransferVO) {
        String sendAccountHolderName = bankTransferVO.getSendAccountHolderName();
        String sendBankCardNumber = bankTransferVO.getSendBankCardNumber();
        String receiveAccountHolderName = bankTransferVO.getReceiveAccountHolderName();
        String receiveBankCardNumber = bankTransferVO.getReceiveBankCardNumber();
        BigDecimal amount = bankTransferVO.getAmount();
        Optional<BankAccount> bankAccountOptional =
                bankAccountRepository
                        .findByBankCardNumber(sendBankCardNumber);
        if(!bankAccountOptional.isPresent()) {
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_EXISTS, "账户"+sendBankCardNumber +"不存在");
        }
        if(!bankAccountOptional.get().getAccountHolderName().equals(sendAccountHolderName.trim())) {
            throw new BusinessException(ErrorCode.TRANSFER_NOT_ALLOWED, "转账出的账户名不正确");
        }
        if(bankAccountOptional.get().getBalance().compareTo(amount) < 0) {
            throw new BusinessException(ErrorCode.TRANSFER_NOT_ALLOWED, "转账出的余额不足");
        }

        Optional<BankAccount> receiveBankAccountOptional =
                bankAccountRepository
                        .findByBankCardNumber(receiveBankCardNumber);
        if(!receiveBankAccountOptional.isPresent()) {
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_EXISTS, "账户"+receiveBankCardNumber +"不存在");
        }
        if(!receiveBankAccountOptional.get().getAccountHolderName()
                .equals(receiveAccountHolderName.trim())) {
            throw new BusinessException(ErrorCode.TRANSFER_NOT_ALLOWED, "入账的账户名不正确");
        }

        BigDecimal balanceRemained =bankAccountOptional.get().getBalance().subtract(amount);
        this.bankAccountRepository.updateBankAccountBalance(
                    balanceRemained,
                    LocalDateTime.now(), bankAccountOptional.get().getUserUid());

        BigDecimal balanceAdded = receiveBankAccountOptional.get().getBalance().add(amount);

        this.bankAccountRepository.updateBankAccountBalance(
                    balanceAdded,
                    LocalDateTime.now(), receiveBankAccountOptional.get().getUserUid());

        return ResultUtils.success("transfer cash successfully");
    }
}
