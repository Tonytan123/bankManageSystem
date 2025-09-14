package com.org.bank.manage.system.repository;

import com.org.bank.manage.system.common.AccountStatus;
import com.org.bank.manage.system.repository.entity.BankAccount;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author tucker
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    // 根据 contactNumber,bankCardNumber 查询账户（用于唯一性校验）
    @Query("SELECT t FROM BankAccount t WHERE t.bankCardNumber= :bankCardNumber and t.status != 'DELETED'")
    Optional<BankAccount> findByBankCardNumber(@Param("bankCardNumber") String bankCardNumber);

    @Query("SELECT count(*) FROM BankAccount t WHERE t.bankCardNumber= :bankCardNumber")
    Long findByBankCardNumberExisted(@Param("bankCardNumber") String bankCardNumber);

    @Modifying
    @Transactional
    @Query("UPDATE BankAccount t SET t.balance = :balance," +
            "t.idCard=:idCard," +
            "t.contactNumber=:contactNumber," +
            "t.description=:description," +
            "t.updatedAt=:updatedAt WHERE t.bankCardNumber= :bankCardNumber ")
    void updateBankAccount(@Param("balance") BigDecimal balance,
                           @Param("idCard") String idCard,
                           @Param("contactNumber") String contactNumber,
                           @Param("bankCardNumber") String bankCardNumber,
                           @Param("description") String description,
                           @Param("updatedAt") LocalDateTime updatedAt);

    @Modifying
    @Transactional
    @Query("UPDATE BankAccount t SET t.balance = :balance,"
            + "t.updatedAt=:updatedAt  WHERE t.bankCardNumber= :bankCardNumber ")
    void updateBankAccountBalance(@Param("balance") BigDecimal balance,
                           @Param("updatedAt") LocalDateTime updatedAt,
                           @Param("bankCardNumber") String bankCardNumber);
    @Modifying
    @Transactional
    @Query("UPDATE BankAccount t SET t.status = :status " +
            " WHERE t.bankCardNumber = :bankCardNumber ")
    void updateBankAccountStatus(@Param("status") AccountStatus status,
                                 @Param("bankCardNumber") String bankCardNumber);

    // 分页查询账户（支持排序和分页）
    @Query("SELECT t FROM BankAccount t WHERE t.userUid=:userUid")
    Page<BankAccount> findAllWithPagination(Pageable pageable, @Param("userUid") String userUid);
}
