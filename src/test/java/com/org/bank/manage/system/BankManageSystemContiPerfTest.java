package com.org.bank.manage.system;

import com.org.bank.manage.system.common.BaseResponse;
import com.org.bank.manage.system.common.PageRes;
import com.org.bank.manage.system.request.BankTransferVO;
import com.org.bank.manage.system.request.CreateBankAccountVO;
import com.org.bank.manage.system.request.DeleteBankAccountVO;
import com.org.bank.manage.system.request.UpdateBankAccountVO;
import com.org.bank.manage.system.service.impl.BankManageServiceImpl;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankManageSystemApplication.class)
public class BankManageSystemContiPerfTest {
    @Rule
    public ContiPerfRule i = new ContiPerfRule();

    @Autowired
    private BankManageServiceImpl bankManageService;

    @Test
    // 配置并发测试：
    // threads = 50：使用 50 个并发线程
    // invocations = 500：每个线程执行 500 次，总共执行 25000 次
    // warmUp = 100：预热 100 次调用
    @PerfTest(threads = 50, invocations = 500, warmUp = 100)
    // average = 50：平均响应时间不能超过 500ms
    // max = 150：最大响应时间不能超过 900ms
    @Required(average = 500, max = 900)
    public void testCreateBankAccountPerformance() {
        // 创建一个模拟请求对象，用于每次调用
        CreateBankAccountVO vo = new CreateBankAccountVO();
        vo.setBankCardNumber("622202" + UUID.randomUUID().toString()); // 使用唯一值
        vo.setAccountHolderName("张三");
        vo.setBalance(new BigDecimal("0.01"));
        vo.setUserId("1234");
        vo.setIdCard("440101199003071234"+ System.currentTimeMillis());
        vo.setContactNumber("13800138000");

        // 调用银行账户管理服务的方法
        bankManageService.createBankAccount(vo);
    }

    @Test
    // 配置并发测试：
    // threads = 50：使用 50 个并发线程
    // invocations = 500：每个线程执行 500 次，总共执行 25000 次
    // warmUp = 100：预热 100 次调用
    @PerfTest(threads = 50, invocations = 500, warmUp = 100)
    public void testUpdateBankAccountPerformance() {
        // 创建一个模拟请求对象，用于每次调用
        String bankIdCard = "622202" + UUID.randomUUID().toString();
        String idCard = "440101199003071234"+ System.currentTimeMillis();
        CreateBankAccountVO vo = new CreateBankAccountVO();
        vo.setBankCardNumber(bankIdCard); // 使用唯一值
        vo.setAccountHolderName("张三");
        vo.setBalance(new BigDecimal("0.01"));
        vo.setUserId("1234");
        vo.setIdCard(idCard);
        vo.setContactNumber("13800138000");

        // 调用银行账户管理服务的方法
        bankManageService.createBankAccount(vo);

        UpdateBankAccountVO updateBankAccountVO = new UpdateBankAccountVO();
        updateBankAccountVO.setAccountHolderName("changed");
        updateBankAccountVO.setBalance(new BigDecimal("0.03"));
        updateBankAccountVO.setBankCardNumber(bankIdCard);
        updateBankAccountVO.setUserId("1234");
        updateBankAccountVO.setIdCard(idCard);
        updateBankAccountVO.setContactNumber("13800138000");
        // 调用银行账户更新的方法
        bankManageService.updateBankAccount(updateBankAccountVO);
    }

    @Test
    // 配置并发测试：
    // threads = 50：使用 50 个并发线程
    // invocations = 500：每个线程执行 500 次，总共执行 25000 次
    // warmUp = 100：预热 100 次调用
    @PerfTest(threads = 50, invocations = 500, warmUp = 100)
    public void testDeleteBankAccountPerformance() {
        // 创建一个模拟请求对象，用于每次调用
        String bankIdCard = "622202" + UUID.randomUUID().toString();
        String idCard = "440101199003071234"+ System.currentTimeMillis();
        CreateBankAccountVO vo = new CreateBankAccountVO();
        vo.setBankCardNumber(bankIdCard); // 使用唯一值
        vo.setAccountHolderName("张三");
        vo.setBalance(new BigDecimal("0.01"));
        vo.setUserId("1234");
        vo.setIdCard(idCard);
        vo.setContactNumber("13800138000");

        // 调用银行账户管理服务的方法
        bankManageService.createBankAccount(vo);

        DeleteBankAccountVO deleteBankAccountVO = new DeleteBankAccountVO();
        deleteBankAccountVO.setBankCardNumber(bankIdCard);
        // 调用银行账户更新的方法
        bankManageService.deleteBankAccount(deleteBankAccountVO);
    }

    @Test
    @PerfTest(threads = 100, duration = 1500)
    public void pageBankAccountByUser() {
        // 为每个线程分配不同的分页参数（1~5 页）
        int page = ThreadLocalRandom.current().nextInt(1, 6);
        int pageSize = 10;
        String userUid = "userId1";

        // 执行分页查询
        BaseResponse<?> res = bankManageService.listAccount(page, pageSize, userUid);
        PageRes<CreateBankAccountVO> result = (PageRes<CreateBankAccountVO>)res.getData();

        // 1. 验证分页信息
        assertEquals(page, result.getPage());
        assertEquals(pageSize, result.getPageSize());
        assertTrue(result.getTotal() >= 0);
        assertTrue(result.getData().size() <= pageSize);

        // 2. 验证数据不为空
        assertNotNull(result.getData());
    }

    @Test
    @PerfTest(threads = 100, duration = 1500)
    public void testAccountTransfer() {
        String bankIdCard = "622202" + UUID.randomUUID().toString();
        CreateBankAccountVO bankAccount = new CreateBankAccountVO();
        bankAccount.setBankCardNumber(bankIdCard);
        bankAccount.setAccountHolderName("tucker");
        BigDecimal bigDecimal = new BigDecimal(100);
        bankAccount.setBalance(bigDecimal);
        bankAccount.setDescription("this is a test");
        bankAccount.setContactNumber("131222222222");
        bankAccount.setEmailAddress("xxxx");
        bankAccount.setIdCard("4305*****");
        bankAccount.setUserId("1234567");
        bankManageService.createBankAccount(bankAccount);


        // 准备接收方账户对象
        String bankIdCardTony = "622203" + UUID.randomUUID().toString();
        CreateBankAccountVO bankAccountTony = new CreateBankAccountVO();
        bankAccountTony.setBankCardNumber(bankIdCardTony);
        bankAccountTony.setAccountHolderName("tony");
        bankAccountTony.setBalance(new BigDecimal(100));
        bankAccountTony.setDescription("this is a test");
        bankAccountTony.setContactNumber("131222222222");
        bankAccountTony.setEmailAddress("xxxx");
        bankAccountTony.setIdCard("4305*****1");
        bankAccountTony.setUserId("1234568");

        // 准备转账请求 VO
        BankTransferVO bankTransferVO = new BankTransferVO();
        bankTransferVO.setAmount(new BigDecimal("0.01"));
        bankTransferVO.setSendBankCardNumber(bankIdCard);
        bankTransferVO.setSendAccountHolderName("tucker");
        bankTransferVO.setReceiveBankCardNumber(bankIdCardTony);
        bankTransferVO.setReceiveAccountHolderName("tony");
        bankManageService.createBankAccount(bankAccountTony);

        // 调用被测试方法
        bankManageService.bankTransfer(bankTransferVO);

    }
}
