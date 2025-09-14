package com.org.bank.manage.system.service.convert;

import com.org.bank.manage.system.common.AccountStatus;
import com.org.bank.manage.system.repository.entity.BankAccount;
import com.org.bank.manage.system.request.CreateBankAccountVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author tucker
 */
@Mapper(componentModel = "spring")
public interface BankMangeMapper {
    public BankMangeMapper INSTANCE = Mappers.getMapper(BankMangeMapper.class);

    default long map(LocalDateTime localDateTime) {
        return localDateTime != null
                ? localDateTime.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()
                : 0L;
    }

    @Mappings(
            {
            @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "accountHolderName", source = "req.accountHolderName"),
                    @Mapping(target = "userUid", source = "req.userId"),
                    @Mapping(target = "contactNumber", source = "req.contactNumber"),
                    @Mapping(target = "idCard", source = "req.idCard"),
                    @Mapping(target = "emailAddress", source = "req.emailAddress"),
                    @Mapping(target = "balance", source = "req.balance"),
                    @Mapping(target = "description", source = "req.description"),
                    @Mapping(target = "bankCardNumber", source = "req.bankCardNumber"),
    }
    )
    BankAccount createVoToEntity(CreateBankAccountVO req, AccountStatus status);

    @Mappings({
            @Mapping(target = "balance", source = "amountStr"),
            @Mapping(target = "accountHolderName", source = "bankAccount.accountHolderName"),
            @Mapping(target = "contactNumber", source = "bankAccount.contactNumber"),
            @Mapping(target = "idCard", source = "bankAccount.idCard"),
            @Mapping(target = "emailAddress", source = "bankAccount.emailAddress"),
            @Mapping(target = "description", source = "bankAccount.description"),
            @Mapping(target = "bankCardNumber", source = "bankAccount.bankCardNumber"),
            @Mapping(target = "userId", source = "bankAccount.userUid")
    })
    CreateBankAccountVO entityToVO(BankAccount bankAccount, String amountStr);
}
