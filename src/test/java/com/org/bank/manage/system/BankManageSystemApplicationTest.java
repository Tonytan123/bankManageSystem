package com.org.bank.manage.system;

import com.org.bank.manage.system.common.AccountStatus;
import com.org.bank.manage.system.request.BaseResponse;
import com.org.bank.manage.system.common.ErrorCode;
import com.org.bank.manage.system.common.PageRes;
import com.org.bank.manage.system.common.exception.BusinessException;
import com.org.bank.manage.system.repository.BankAccountRepository;
import com.org.bank.manage.system.repository.entity.BankAccount;
import com.org.bank.manage.system.request.BankTransferVO;
import com.org.bank.manage.system.request.CreateBankAccountVO;
import com.org.bank.manage.system.request.DeleteBankAccountVO;
import com.org.bank.manage.system.request.UpdateBankAccountVO;
import com.org.bank.manage.system.service.impl.BankManageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

// 这是一个用于对银行管理系统进行单元测试的类
@SpringBootTest // 启用 Spring Boot 的测试功能，会加载完整的应用上下文
class BankManageSystemApplicationTest {

    @InjectMocks // 注入一个需要被测试的服务，并把所有@Mock注解的依赖注入到这个对象中
    private BankManageServiceImpl bankManageService;

    @Mock // 模拟一个依赖对象，以隔离测试并控制其行为
    private BankAccountRepository bankAccountRepository;

    private BankAccount bankAccount; // 用于测试的单个银行账户实体对象

    private List<BankAccount> bankAccountList; // 用于分页测试的银行账户列表

    /**
     * 在每个测试方法执行前运行。
     * 主要用于初始化测试环境和数据，确保每个测试用例都是独立的。
     */
    @BeforeEach
    void setUp() {
        // 初始化单个银行账户对象，作为测试的通用数据
        this.bankAccount = new BankAccount();
        bankAccount.setBankCardNumber("123456");
        bankAccount.setAccountHolderName("tucker");
        BigDecimal bigDecimal = new BigDecimal(100);
        bankAccount.setBalance(bigDecimal);
        bankAccount.setDescription("this is a test");
        bankAccount.setContactNumber("131222222222");
        bankAccount.setEmailAddress("xxxx");
        bankAccount.setIdCard("4305*****");
        bankAccount.setUserUid("1234567");
        bankAccount.setStatus(AccountStatus.ACTIVE);

        // 准备测试数据列表（共 30 条），用于分页测试
        this.bankAccountList = IntStream.range(0, 30) // 创建一个从 0 到 29 的整数流
                .mapToObj(i -> { // 将每个整数映射为一个 BankAccount 对象
                    BankAccount account = new BankAccount();
                    account.setId((long) i);
                    account.setBankCardNumber("NO-" + i); // 设置唯一的银行卡号
                    account.setUserUid("userId1");
                    account.setAccountHolderName("userId1");
                    account.setIdCard("userId1");
                    BigDecimal amount = new BigDecimal(100 + i)
                            .setScale(2, RoundingMode.HALF_UP); // 设置带精度的余额
                    account.setBalance(amount);
                    account.setUpdatedAt(LocalDateTime.now());
                    account.setStatus(AccountStatus.ACTIVE);
                    return account;
                })
                .collect(Collectors.toList());
    }

    /**
     * 测试创建银行账户功能，验证正常情况下是否成功。
     * 使用 Mockito 模拟 bankAccountRepository 的行为。
     */
    @Test
    void testCreateBankAccount() {
        // 准备测试用的 VO (Value Object)
        CreateBankAccountVO createBankAccountVO = new CreateBankAccountVO();
        createBankAccountVO.setBankCardNumber("123456");
        createBankAccountVO.setAccountHolderName("tucker");
        BigDecimal bigDecimal = new BigDecimal(100);
        createBankAccountVO.setBalance(bigDecimal);
        createBankAccountVO.setDescription("this is a test");
        createBankAccountVO.setContactNumber("131222222222");
        createBankAccountVO.setEmailAddress("xxxx");
        createBankAccountVO.setIdCard("4305*****");
        createBankAccountVO.setUserId("1234567");

        when(bankAccountRepository
                .findByBankCardNumber("123456")).thenReturn(
                Optional.ofNullable(null)
        );
        when(bankAccountRepository
                .save(any())).thenReturn(any());

        // 调用被测试的方法
        BaseResponse<?> baseResponse = bankManageService.createBankAccount(createBankAccountVO);
        BankAccount bankAccount = (BankAccount)baseResponse.getData();

        // 断言验证，确保返回的数据和预期一致
        assertTrue(bigDecimal.equals(bankAccount.getBalance()));
        assertTrue("123456".equals(bankAccount.getBankCardNumber()));
        assertTrue("tucker".equals(bankAccount.getAccountHolderName()));
        assertTrue("131222222222".equals(bankAccount.getContactNumber()));
        assertTrue("xxxx".equals(bankAccount.getEmailAddress()));
        assertTrue("4305*****".equals(bankAccount.getIdCard()));
        assertTrue("1234567".equals(bankAccount.getUserUid()));
    }

    /**
     * 测试创建银行账户时抛出异常的情况。
     * 当账户已存在时，期望抛出 BusinessException 异常。
     */
    @Test
    void testCreateBankAccountException(){
        // 准备测试数据
        CreateBankAccountVO createBankAccountVO = new CreateBankAccountVO();
        createBankAccountVO.setBankCardNumber("123456");
        createBankAccountVO.setAccountHolderName("tucker");
        BigDecimal bigDecimal = new BigDecimal(100);
        createBankAccountVO.setBalance(bigDecimal);
        createBankAccountVO.setDescription("this is a test");
        createBankAccountVO.setContactNumber("131222222222");
        createBankAccountVO.setEmailAddress("xxxx");
        createBankAccountVO.setIdCard("4305*****");
        createBankAccountVO.setUserId("1234567");

        // 模拟 findByBankCardNumber 方法：返回一个已存在的账户
        when(bankAccountRepository
                .findByBankCardNumberExisted("123456")).thenReturn(
                1L
        );

        // 使用 assertThrows 断言，验证是否抛出了 BusinessException
        BusinessException exception =assertThrows(BusinessException.class, () -> {
            bankManageService.createBankAccount(createBankAccountVO);
        });

        // 验证抛出的异常的错误码是否和预期一致
        assertEquals(ErrorCode.ACCOUNT_ALREADY_EXISTS.getCode(), exception.getCode());
    }

    /**
     * 测试更新银行账户功能，验证正常情况下是否成功。
     */
    @Test
    void testUpdateBankAccount() {
        // 准备更新用的 VO
        UpdateBankAccountVO updateBankAccountVO = new UpdateBankAccountVO();
        updateBankAccountVO.setBankCardNumber("123456");
        updateBankAccountVO.setAccountHolderName("tucker");
        BigDecimal bigDecimal = new BigDecimal(100);
        updateBankAccountVO.setBalance(bigDecimal);
        updateBankAccountVO.setDescription("this is a test");
        updateBankAccountVO.setContactNumber("131222222222");
        updateBankAccountVO.setEmailAddress("xxxx");
        updateBankAccountVO.setIdCard("4305*****");
        updateBankAccountVO.setUserId("1234567");
        updateBankAccountVO.setStatus(AccountStatus.ACTIVE.getDisplayName());

        // 模拟 findByBankCardNumber 方法：返回一个已存在的账户，以便进行更新
        when(bankAccountRepository
                .findByBankCardNumber("123456")).thenReturn(
                Optional.ofNullable(this.bankAccount)
        );
        // 模拟 save 方法：返回任意对象，表示保存成功
        when(bankAccountRepository
                .save(any())).thenReturn(any());

        // 调用被测试方法
        BaseResponse<?> baseResponse = bankManageService.updateBankAccount(updateBankAccountVO);
        String responseData = (String)baseResponse.getMessage();

        // 验证返回消息是否正确
        assertTrue("update success".equals(responseData));
    }

    /**
     * 测试更新银行账户时抛出异常的情况：账户不存在。
     */
    @Test
    void testUpdateBankAccountException() {
        // 准备测试数据
        UpdateBankAccountVO updateBankAccountVO = new UpdateBankAccountVO();
        updateBankAccountVO.setBankCardNumber("123456");
        // ... 省略其他字段设置

        // 模拟 findByBankCardNumber 方法：返回 null，表示账户不存在
        when(bankAccountRepository
                .findByBankCardNumber("123456")).thenReturn(
                Optional.ofNullable(null));

        // 验证是否抛出 BusinessException
        BusinessException exception =assertThrows(BusinessException.class, () -> {
            bankManageService.updateBankAccount(updateBankAccountVO);
        });

        // 验证异常的错误码
        assertEquals(ErrorCode.ACCOUNT_NOT_EXISTS.getCode(), exception.getCode());
    }

    /**
     * 测试更新银行账户时抛出异常的情况：账户状态不允许。
     * 注意：这个测试用例的逻辑有点问题，因为 findByBankCardNumber 返回 null，
     * 应该先触发账户不存在的异常，而不是账户状态异常。
     */
    @Test
    void testUpdateBankAccountExceptionWithNoSuitableException() {
        // 准备测试数据
        UpdateBankAccountVO updateBankAccountVO = new UpdateBankAccountVO();
        updateBankAccountVO.setBankCardNumber("123456");
        // ... 省略其他字段设置
        updateBankAccountVO.setStatus(AccountStatus.FROZEN.getDisplayName());

        // 模拟 findByBankCardNumber 方法：返回 null，表示账户不存在
        when(bankAccountRepository
                .findByBankCardNumber("123456")).thenReturn(
                Optional.ofNullable(null));

        // 验证是否抛出 BusinessException
        BusinessException exception =assertThrows(BusinessException.class, () -> {
            bankManageService.updateBankAccount(updateBankAccountVO);
        });

        // 验证异常的错误码
        // 这个断言可能永远不会通过，因为根据上面的模拟，会先抛出 ACCOUNT_NOT_EXISTS 异常
        assertEquals(ErrorCode.ACCOUNT_STATUS_NOT_ALLOWED.getCode(), exception.getCode());
    }

    /**
     * 测试删除银行账户功能，验证正常情况下是否成功。
     */
    @Test
    void testDeleteBankAccount() {
        // 准备测试数据
        DeleteBankAccountVO deleteBankAccountVO = new DeleteBankAccountVO();
        deleteBankAccountVO.setBankCardNumber("123456");

        // 模拟 findByBankCardNumber 方法：返回一个已存在的账户
        when(bankAccountRepository.findByBankCardNumber("123456")).thenReturn(
                Optional.of(bankAccount)
        );

        // 调用被测试方法
        BaseResponse<?> baseResponse = bankManageService.deleteBankAccount(deleteBankAccountVO);
        String responseData = (String)baseResponse.getMessage();

        // 验证返回消息
        assertTrue("delete success".equals(responseData));
    }

    /**
     * 测试删除银行账户时抛出异常的情况：账户不存在。
     */
    @Test
    void testDeleteBankAccountException_Account() {
        // 准备测试数据
        DeleteBankAccountVO deleteBankAccountVO = new DeleteBankAccountVO();
        deleteBankAccountVO.setBankCardNumber("123456");

        // 模拟 findByBankCardNumber 方法：返回 null，表示账户不存在
        when(bankAccountRepository.findByBankCardNumber("123456")).thenReturn(
                Optional.ofNullable(null)
        );

        // 验证是否抛出 BusinessException
        BusinessException exception =assertThrows(BusinessException.class, () -> {
            bankManageService.deleteBankAccount(deleteBankAccountVO);
        });

        // 验证异常的错误码
        assertEquals(ErrorCode.ACCOUNT_NOT_EXISTS.getCode(), exception.getCode());
    }

    /**
     * 测试分页查询账户功能。
     */
    @Test
    void testListAccount() {
        // 准备分页参数和模拟数据
        int pageNo = 1;
        int pageSize = 10;
        String userUid = "userId1";
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<BankAccount> page = new PageImpl<>(bankAccountList.subList(0, 10),
                pageable, bankAccountList.size());

        // 模拟 findAllWithPagination 方法：当传入特定参数时，返回之前准备好的分页数据
        when(bankAccountRepository.findAllWithPagination(eq(pageable), eq(userUid)))
                .thenReturn(page);

        // 调用被测试方法
        BaseResponse<?> result = bankManageService.listAccount(pageNo, pageSize, userUid);
        PageRes<CreateBankAccountVO> res = (PageRes<CreateBankAccountVO>)result.getData();

        // 验证返回的分页数据是否和预期一致
        assertEquals(pageNo, res.getPage());
        assertEquals(pageSize, res.getPageSize());
        assertEquals(30, res.getTotal());
        assertEquals(10, res.getData().size());

        // 验证列表中的每条数据是否正确
        for (int i = 0; i < 10; i++) {
            CreateBankAccountVO vo = res.getData().get(i);
            assertEquals("NO-" + i, vo.getBankCardNumber());
            assertTrue( vo.getBalance().compareTo(new BigDecimal((100 + i) + ".00")) == 0);
        }
    }

    /**
     * 测试账户转账功能，验证正常情况下是否成功。
     */
    @Test
    void testAccountTransfer() {
        // 准备转账请求 VO
        BankTransferVO bankTransferVO = new BankTransferVO();
        bankTransferVO.setAmount(new BigDecimal("0.01"));
        bankTransferVO.setSendBankCardNumber("123456");
        bankTransferVO.setSendAccountHolderName("tucker");
        bankTransferVO.setReceiveBankCardNumber("1235");
        bankTransferVO.setReceiveAccountHolderName("tony");

        // 准备接收方账户对象
        BankAccount bankAccountTony = new BankAccount();
        bankAccountTony.setBankCardNumber("1235");
        bankAccountTony.setAccountHolderName("tony");
        BigDecimal bigDecimal = new BigDecimal(100);
        bankAccountTony.setBalance(bigDecimal);
        bankAccountTony.setDescription("this is a test");
        bankAccountTony.setContactNumber("131222222222");
        bankAccountTony.setEmailAddress("xxxx");
        bankAccountTony.setIdCard("4305*****1");
        bankAccountTony.setUserUid("1234568");
        bankAccountTony.setStatus(AccountStatus.ACTIVE);

        // 模拟 findByBankCardNumber 方法，返回转出方和转入方账户
        when(bankAccountRepository.findByBankCardNumber("123456")).thenReturn(Optional.of(this.bankAccount));
        when(bankAccountRepository.findByBankCardNumber("1235")).thenReturn(Optional.of(bankAccountTony));

        // 调用被测试方法
        BaseResponse<?> result = bankManageService.bankTransfer(bankTransferVO);

        // 验证返回消息
        assertTrue("transfer cash successfully".equals(result.getMessage()));
    }

    /**
     * 测试转账功能时抛出异常的情况：转账金额不正确（超过余额）。
     */
    @Test
    void testAccountTransferException_wrongTransferAmount() {
        // 准备测试数据，设置转账金额超过余额
        BankTransferVO bankTransferVO = new BankTransferVO();
        bankTransferVO.setAmount(new BigDecimal("101"));
        bankTransferVO.setSendBankCardNumber("123456");
        bankTransferVO.setSendAccountHolderName("tucker");
        bankTransferVO.setReceiveBankCardNumber("1235");
        bankTransferVO.setReceiveAccountHolderName("tony");

        // 准备接收方账户对象
        BankAccount bankAccountTony = new BankAccount();
        bankAccountTony.setBankCardNumber("1235");
        bankAccountTony.setAccountHolderName("tony");
        BigDecimal bigDecimal = new BigDecimal(100);
        bankAccountTony.setBalance(bigDecimal);
        bankAccountTony.setDescription("this is a test");
        bankAccountTony.setContactNumber("131222222222");
        bankAccountTony.setEmailAddress("xxxx");
        bankAccountTony.setIdCard("4305*****1");
        bankAccountTony.setUserUid("1234568");
        bankAccountTony.setStatus(AccountStatus.ACTIVE);

        // 模拟 findByBankCardNumber 方法
        when(bankAccountRepository.findByBankCardNumber("123456")).thenReturn(Optional.of(this.bankAccount));
        when(bankAccountRepository.findByBankCardNumber("1235")).thenReturn(Optional.of(bankAccountTony));

        // 验证是否抛出 BusinessException
        BusinessException exception =assertThrows(BusinessException.class, () -> {
            bankManageService.bankTransfer(bankTransferVO);
        });

        // 验证异常的错误码
        assertEquals(ErrorCode.TRANSFER_NOT_ALLOWED.getCode(), exception.getCode());
    }

    /**
     * 测试转账功能时抛出异常的情况：转出方姓名不匹配。
     */
    @Test
    void testAccountTransferException_wrongTransferName() {
        // 准备测试数据，设置错误的转出方姓名
        BankTransferVO bankTransferVO = new BankTransferVO();
        bankTransferVO.setAmount(new BigDecimal("0.11"));
        bankTransferVO.setSendBankCardNumber("123456");
        bankTransferVO.setSendAccountHolderName("tuckerWrong"); // 错误的姓名
        bankTransferVO.setReceiveBankCardNumber("1235");
        bankTransferVO.setReceiveAccountHolderName("tony");

        // 准备接收方账户对象
        BankAccount bankAccountTony = new BankAccount();
        bankAccountTony.setBankCardNumber("1235");
        bankAccountTony.setAccountHolderName("tony");
        BigDecimal bigDecimal = new BigDecimal(100);
        bankAccountTony.setBalance(bigDecimal);
        bankAccountTony.setDescription("this is a test");
        bankAccountTony.setContactNumber("131222222222");
        bankAccountTony.setEmailAddress("xxxx");
        bankAccountTony.setIdCard("4305*****1");
        bankAccountTony.setUserUid("1234568");
        bankAccountTony.setStatus(AccountStatus.ACTIVE);

        // 模拟 findByBankCardNumber 方法
        when(bankAccountRepository.findByBankCardNumber("123456")).thenReturn(Optional.of(this.bankAccount));
        when(bankAccountRepository.findByBankCardNumber("1235")).thenReturn(Optional.of(bankAccountTony));

        // 验证是否抛出 BusinessException
        BusinessException exception =assertThrows(BusinessException.class, () -> {
            bankManageService.bankTransfer(bankTransferVO);
        });

        // 验证异常的错误码
        assertEquals(ErrorCode.TRANSFER_NOT_ALLOWED.getCode(), exception.getCode());
    }
}