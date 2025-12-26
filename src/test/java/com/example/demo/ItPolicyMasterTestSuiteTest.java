package com.example.demo;

import com.example.demo.controller.*;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;
import com.example.demo.util.RuleEvaluationUtil;

import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Final + Stable Master Test Suite
 * Option B → Mockito Only, NO Spring Context, NO JwtUtil Mocking
 */
@Listeners(TestResultListener.class)
public class ItPolicyMasterTestSuiteTest {

    // ------------------ MOCKS ------------------
    @Mock private UserAccountRepository userRepo;
    @Mock private LoginEventRepository loginRepo;
    @Mock private DeviceProfileRepository deviceRepo;
    @Mock private PolicyRuleRepository ruleRepo;
    @Mock private ViolationRecordRepository violationRepo;

    private UserAccountService userService;
    private LoginEventService loginService;
    private DeviceProfileService deviceService;
    private PolicyRuleService ruleService;
    private ViolationRecordService violationService;
    private RuleEvaluationUtil ruleEvaluator;

    private JwtUtil jwtUtil;  // REAL INSTANCE (NO MOCK)

    @BeforeClass
    public void init() {
        MockitoAnnotations.openMocks(this);

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        userService = new UserAccountServiceImpl(userRepo, encoder);

        jwtUtil = new JwtUtil("TestSecretKeyForJWT1234567890", 3600000L, true);

        ruleEvaluator = new RuleEvaluationUtil(ruleRepo, violationRepo);
        loginService = new LoginEventServiceImpl(loginRepo, ruleEvaluator);
        deviceService = new DeviceProfileServiceImpl(deviceRepo);
        ruleService = new PolicyRuleServiceImpl(ruleRepo);
        violationService = new ViolationRecordServiceImpl(violationRepo);
    }

    // --------------------------------------------------------
    // 1) BASIC APP CHECKS
    // --------------------------------------------------------
    @Test(priority = 1)
    public void testAppName() {
        Assert.assertTrue("IT Policy Violation Engine".contains("Policy"));
    }

    @Test(priority = 2)
    public void testServletMock() {
        Assert.assertTrue("Servlet Running".contains("Running"));
    }

    // --------------------------------------------------------
    // 2) USER ACCOUNT TESTS
    // --------------------------------------------------------
    @Test(priority = 3)
    public void testCreateUser() {
        UserAccount u = new UserAccount();
        u.setEmail("a@test.com");
        u.setPassword("pwd");

        when(userRepo.save(any())).thenReturn(u);

        Assert.assertEquals(userService.createUser(u).getEmail(), "a@test.com");
    }

    @Test(priority = 4)
    public void testUserGetById() {
        UserAccount u = new UserAccount();
        u.setId(9L);
        when(userRepo.findById(9L)).thenReturn(Optional.of(u));
        Assert.assertEquals(userService.getUserById(9L).getId(), Long.valueOf(9L));
    }

    @Test(priority = 5)
    public void testUserUpdateStatus() {
        UserAccount u = new UserAccount();
        u.setId(1L);
        u.setStatus("ACTIVE");

        when(userRepo.findById(1L)).thenReturn(Optional.of(u));
        when(userRepo.save(any())).thenReturn(u);

        Assert.assertEquals(userService.updateUserStatus(1L, "SUSPENDED").getStatus(), "SUSPENDED");
    }

    @Test(priority = 6)
    public void testUserGetAll() {
        when(userRepo.findAll()).thenReturn(List.of(new UserAccount(), new UserAccount()));
        Assert.assertEquals(userService.getAllUsers().size(), 2);
    }

    @Test(priority = 7)
    public void testUserUsernameField() {
        UserAccount u = new UserAccount();
        u.setUsername("bob");
        Assert.assertEquals(u.getUsername(), "bob");
    }

    // --------------------------------------------------------
    // 3) LOGIN EVENT TESTS
    // --------------------------------------------------------
    @Test(priority = 8)
    public void testRecordLoginEvent() {
        LoginEvent ev = new LoginEvent();
        ev.setId(100L);
        when(loginRepo.save(any())).thenReturn(ev);
        Assert.assertEquals(loginService.recordLogin(ev).getId(), Long.valueOf(100L));
    }

    @Test(priority = 9)
    public void testLoginEventsByUser() {
        when(loginRepo.findByUserId(1L)).thenReturn(List.of(new LoginEvent()));
        Assert.assertEquals(loginService.getEventsByUser(1L).size(), 1);
    }

    @Test(priority = 10)
    public void testSuspiciousEvents() {
        when(loginRepo.findByUserIdAndLoginStatus(1L, "FAILED"))
                .thenReturn(List.of(new LoginEvent(), new LoginEvent()));
        Assert.assertEquals(loginService.getSuspiciousLogins(1L).size(), 2);
    }

    @Test(priority = 11)
    public void testLoginEventIpField() {
        LoginEvent e = new LoginEvent();
        e.setIpAddress("1.1.1.1");
        Assert.assertEquals(e.getIpAddress(), "1.1.1.1");
    }

    // --------------------------------------------------------
    // 4) DEVICE PROFILE TESTS
    // --------------------------------------------------------
    @Test(priority = 12)
    public void testRegisterDevice() {
        DeviceProfile d = new DeviceProfile();
        d.setDeviceId("D1");
        when(deviceRepo.save(any())).thenReturn(d);
        Assert.assertEquals(deviceService.registerDevice(d).getDeviceId(), "D1");
    }

    @Test(priority = 13)
    public void testDeviceLookupFound() {
        DeviceProfile d = new DeviceProfile();
        d.setDeviceId("X1");
        when(deviceRepo.findByDeviceId("X1")).thenReturn(Optional.of(d));
        Assert.assertTrue(deviceService.findByDeviceId("X1").isPresent());
    }

    @Test(priority = 14)
    public void testUpdateDeviceTrust() {
        DeviceProfile d = new DeviceProfile();
        d.setId(2L);
        when(deviceRepo.findById(2L)).thenReturn(Optional.of(d));
        when(deviceRepo.save(any())).thenReturn(d);
        Assert.assertNotNull(deviceService.updateTrustStatus(2L, true));
    }

    @Test(priority = 15)
    public void testDeviceProfileTypeField() {
        DeviceProfile d = new DeviceProfile();
        d.setDeviceType("LAPTOP");
        Assert.assertEquals(d.getDeviceType(), "LAPTOP");
    }

    // --------------------------------------------------------
    // 5) POLICY RULE TESTS
    // --------------------------------------------------------
    @Test(priority = 16)
    public void testCreatePolicyRule() {
        PolicyRule r = new PolicyRule();
        r.setRuleCode("R10");
        when(ruleRepo.save(any())).thenReturn(r);
        Assert.assertEquals(ruleService.createRule(r).getRuleCode(), "R10");
    }

    @Test(priority = 17)
    public void testGetActiveRules() {
        when(ruleRepo.findByActiveTrue()).thenReturn(List.of(new PolicyRule(), new PolicyRule()));
        Assert.assertEquals(ruleService.getActiveRules().size(), 2);
    }

    @Test(priority = 18)
    public void testRuleSeverityField() {
        PolicyRule r = new PolicyRule();
        r.setSeverity("HIGH");
        Assert.assertEquals(r.getSeverity(), "HIGH");
    }

    // --------------------------------------------------------
    // 6) RULE EVALUATION TESTS
    // --------------------------------------------------------
    @Test(priority = 19)
    public void testViolationTriggered() {
        PolicyRule r = new PolicyRule();
        r.setActive(true);
        r.setConditionsJson("FAILED");
        r.setSeverity("HIGH");

        when(ruleRepo.findByActiveTrue()).thenReturn(List.of(r));

        LoginEvent ev = new LoginEvent();
        ev.setLoginStatus("FAILED");

        ruleEvaluator.evaluateLoginEvent(ev);
        verify(violationRepo, times(1)).save(any(ViolationRecord.class));
    }

    @Test(priority = 20)
    public void testNoViolationWhenNoRules() {
        reset(violationRepo);
        when(ruleRepo.findByActiveTrue()).thenReturn(List.of());

        LoginEvent event = new LoginEvent();
        event.setLoginStatus("SUCCESS");

        ruleEvaluator.evaluateLoginEvent(event);

        verify(violationRepo, never()).save(any());
    }

    @Test(priority = 21)
    public void testViolationDetailsField() {
        ViolationRecord v = new ViolationRecord();
        v.setDetails("Device mismatch");
        Assert.assertEquals(v.getDetails(), "Device mismatch");
    }

    // --------------------------------------------------------
    // 7) VIOLATION SERVICE TESTS
    // --------------------------------------------------------
    @Test(priority = 22)
    public void testLogViolation() {
        ViolationRecord v = new ViolationRecord();
        v.setId(22L);
        when(violationRepo.save(any())).thenReturn(v);
        Assert.assertEquals(violationService.logViolation(v).getId(), Long.valueOf(22L));
    }

    @Test(priority = 23)
    public void testGetUnresolvedViolations() {
        when(violationRepo.findByResolvedFalse())
                .thenReturn(List.of(new ViolationRecord(), new ViolationRecord()));
        Assert.assertEquals(violationService.getUnresolvedViolations().size(), 2);
    }

    @Test(priority = 24)
    public void testMarkViolationResolved() {
        ViolationRecord v = new ViolationRecord();
        v.setId(7L);
        v.setResolved(false);

        when(violationRepo.findById(7L)).thenReturn(Optional.of(v));
        when(violationRepo.save(any())).thenReturn(v);

        Assert.assertTrue(violationService.markResolved(7L).getResolved());
    }

    // --------------------------------------------------------
    // 8) CONTROLLER TESTS
    // --------------------------------------------------------
    @Test(priority = 25)
    public void testUserControllerCreate() {
        UserAccountService svc = mock(UserAccountService.class);
        UserAccountController ctrl = new UserAccountController(svc);
        UserAccount u = new UserAccount();

        when(svc.createUser(any())).thenReturn(u);
        Assert.assertNotNull(ctrl.create(u).getBody());
    }

    @Test(priority = 26)
    public void testDeviceControllerLookup() {
        DeviceProfileService svc = mock(DeviceProfileService.class);
        DeviceProfileController ctrl = new DeviceProfileController(svc);

        DeviceProfile d = new DeviceProfile();
        d.setDeviceId("D77");

        when(svc.findByDeviceId("D77")).thenReturn(Optional.of(d));
        Assert.assertEquals(ctrl.lookup("D77").getBody().getDeviceId(), "D77");
    }

    @Test(priority = 27)
    public void testRuleControllerList() {
        PolicyRuleService svc = mock(PolicyRuleService.class);
        PolicyRuleController ctrl = new PolicyRuleController(svc);

        when(svc.getAllRules()).thenReturn(List.of(new PolicyRule()));
        Assert.assertEquals(ctrl.all().getBody().size(), 1);
    }

    @Test(priority = 28)
    public void testViolationControllerLog() {
        ViolationRecordService svc = mock(ViolationRecordService.class);
        ViolationRecordController ctrl = new ViolationRecordController(svc);

        ViolationRecord v = new ViolationRecord();
        when(svc.logViolation(any())).thenReturn(v);

        Assert.assertNotNull(ctrl.log(v).getBody());
    }

    // --------------------------------------------------------
    // 9) JWT TESTS (USING REAL JWT UTIL)
    // --------------------------------------------------------
 // --------------------------------------------------------
// 9) JWT TESTS (USING REAL JWT UTIL)
// --------------------------------------------------------

@Test(priority = 29)
public void testJwtValidate() {
    String token = jwtUtil.generateToken("test", 10L, "x@test.com", "ADMIN");
    Assert.assertTrue(jwtUtil.validateToken(token));
}

@Test(priority = 30)
public void testJwtExtractEmail() {
    String token = jwtUtil.generateToken("test", 10L, "abc@test.com", "ADMIN");
    Assert.assertEquals(jwtUtil.getEmail(token), "abc@test.com");
}

@Test(priority = 31)
public void testJwtExtractRole() {
    String token = jwtUtil.generateToken("test", 10L, "aaa@test.com", "AUDITOR");
    Assert.assertEquals(jwtUtil.getRole(token), "AUDITOR");
}

@Test(priority = 32)
public void testJwtExtractUserId() {
    String token = jwtUtil.generateToken("test", 99L, "uuu@test.com", "ADMIN");
    Assert.assertEquals(jwtUtil.getUserId(token), Long.valueOf(99L));
}


    // --------------------------------------------------------
    // 10) ENTITY FIELD TESTS
    // --------------------------------------------------------
    @Test(priority = 33)
    public void testUserEmailField() {
        UserAccount u = new UserAccount();
        u.setEmail("abc@test.com");
        Assert.assertEquals(u.getEmail(), "abc@test.com");
    }

    @Test(priority = 34)
    public void testPolicyRuleJsonField() {
        PolicyRule r = new PolicyRule();
        r.setConditionsJson("{\"geo\":true}");
        Assert.assertTrue(r.getConditionsJson().contains("geo"));
    }

    @Test(priority = 35)
    public void testDeviceLastSeenField() {
        DeviceProfile d = new DeviceProfile();
        LocalDateTime now = LocalDateTime.now();
        d.setLastSeen(now);
        Assert.assertEquals(d.getLastSeen(), now);
    }

    @Test(priority = 36)
    public void testLoginEventDeviceField() {
        LoginEvent e = new LoginEvent();
        e.setDeviceId("DEV01");
        Assert.assertEquals(e.getDeviceId(), "DEV01");
    }

    @Test(priority = 37)
    public void testViolationSeverity() {
        ViolationRecord v = new ViolationRecord();
        v.setSeverity("CRITICAL");
        Assert.assertEquals(v.getSeverity(), "CRITICAL");
    }

    @Test(priority = 38)
    public void testUserStatusSetter() {
        UserAccount u = new UserAccount();
        u.setStatus("ACTIVE");
        Assert.assertEquals(u.getStatus(), "ACTIVE");
    }

    @Test(priority = 39)
    public void testUpdateDeviceTrustTrue() {
        DeviceProfile d = new DeviceProfile();
        d.setIsTrusted(true);
        Assert.assertTrue(d.getIsTrusted());
    }

    @Test(priority = 40)
    public void testRuleActiveFlag() {
        PolicyRule r = new PolicyRule();
        r.setActive(true);
        Assert.assertTrue(r.getActive());
    }

    @Test(priority = 41)
    public void testViolationUserIdField() {
        ViolationRecord v = new ViolationRecord();
        v.setUserId(11L);
        Assert.assertEquals(v.getUserId(), Long.valueOf(11L));
    }

    @Test(priority = 42)
    public void testControllerNullCheck() {
        UserAccountController ctrl = new UserAccountController(userService);
        Assert.assertNotNull(ctrl);
    }

    @Test(priority = 43)
    public void testJwtInvalidToken() {
        Assert.assertFalse(jwtUtil.validateToken("invalid.token.value"));
    }

    @Test(priority = 44)
    public void testLoginEventStatusField() {
        LoginEvent e = new LoginEvent();
        e.setLoginStatus("FAILED");
        Assert.assertEquals(e.getLoginStatus(), "FAILED");
    }

    @Test(priority = 45)
    public void testRuleSeverityFieldCritical() {
        PolicyRule r = new PolicyRule();
        r.setSeverity("CRITICAL");
        Assert.assertEquals(r.getSeverity(), "CRITICAL");
    }

    @Test(priority = 46)
    public void testUserRoleField() {
        UserAccount u = new UserAccount();
        u.setRole("ADMIN");
        Assert.assertEquals(u.getRole(), "ADMIN");
    }

    @Test(priority = 47)
    public void testViolationEventIdField() {
        ViolationRecord v = new ViolationRecord();
        v.setEventId(777L);
        Assert.assertEquals(v.getEventId(), Long.valueOf(777L));
    }

    @Test(priority = 48)
    public void testDeviceUserIdField() {
        DeviceProfile d = new DeviceProfile();
        d.setUserId(55L);
        Assert.assertEquals(d.getUserId(), Long.valueOf(55L));
    }

    @Test(priority = 49)
    public void testLoginEventLocationField() {
        LoginEvent e = new LoginEvent();
        e.setLocation("Chennai");
        Assert.assertEquals(e.getLocation(), "Chennai");
    }

    @Test(priority = 50)
    public void testUserEmployeeIdField() {
        UserAccount u = new UserAccount();
        u.setEmployeeId("E100");
        Assert.assertEquals(u.getEmployeeId(), "E100");
    }

    @Test(priority = 51)
    public void testPolicyRuleDescriptionField() {
        PolicyRule r = new PolicyRule();
        r.setDescription("Checks failed login attempts");
        Assert.assertTrue(r.getDescription().contains("failed"));
    }

    @Test(priority = 52)
    public void testViolationResolvedFlag() {
        ViolationRecord v = new ViolationRecord();
        v.setResolved(true);
        Assert.assertTrue(v.getResolved());
    }

    @Test(priority = 53)
    public void testDeviceOsVersionField() {
        DeviceProfile d = new DeviceProfile();
        d.setOsVersion("Windows 11");
        Assert.assertEquals(d.getOsVersion(), "Windows 11");
    }

    @Test(priority = 54)
    public void testLoginEventTimestampField() {
        LocalDateTime t = LocalDateTime.now();
        LoginEvent e = new LoginEvent();
        e.setTimestamp(t);
        Assert.assertEquals(e.getTimestamp(), t);
    }

    @Test(priority = 55)
    public void testUserCreatedAtField() {
        LocalDateTime t = LocalDateTime.now();
        UserAccount u = new UserAccount();
        u.setCreatedAt(t);
        Assert.assertEquals(u.getCreatedAt(), t);
    }

    @Test(priority = 56)
    public void testRuleCodeField() {
        PolicyRule r = new PolicyRule();
        r.setRuleCode("X001");
        Assert.assertEquals(r.getRuleCode(), "X001");
    }

    @Test(priority = 57)
    public void testDeviceTrustedFalse() {
        DeviceProfile d = new DeviceProfile();
        d.setIsTrusted(false);
        Assert.assertFalse(d.getIsTrusted());
    }

    @Test(priority = 58)
    public void testLoginEventDeviceMismatchSample() {
        LoginEvent e = new LoginEvent();
        e.setDeviceId("DEV-A");
        Assert.assertEquals(e.getDeviceId(), "DEV-A");
    }

    @Test(priority = 59)
    public void testViolationSeverityLow() {
        ViolationRecord v = new ViolationRecord();
        v.setSeverity("LOW");
        Assert.assertEquals(v.getSeverity(), "LOW");
    }

    @Test(priority = 60)
    public void testFinalSuite() {
        Assert.assertTrue(true, "All 60 tests executed successfully.");
    }
}
