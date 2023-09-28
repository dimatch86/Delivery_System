package ru.skillbox.paymentservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.skillbox.paymentservice.domain.UserBalance;
import ru.skillbox.paymentservice.repository.UserBalanceRepository;
import ru.skillbox.util.CurrentUserUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserBalanceServiceTest {

    @Mock
    private UserBalanceRepository userBalanceRepository;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private UserBalanceServiceImpl userBalanceService;

    private static final String TEST_USER_NAME = "test_user";
    private static final Double AMOUNT = 100.0;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIncreaseUserBalanceExistingUser() {
        UserBalance existingUserBalance = new UserBalance(TEST_USER_NAME, 50.0);
        try (MockedStatic<CurrentUserUtils> utilsMockedStatic = Mockito.mockStatic(CurrentUserUtils.class)){
            utilsMockedStatic.when(() -> CurrentUserUtils.getCurrentUser(httpServletRequest)).thenReturn(TEST_USER_NAME);

            when(userBalanceRepository.findUserBalanceByUserName(TEST_USER_NAME))
                    .thenReturn(existingUserBalance);


            Optional<UserBalance> result = userBalanceService.increaseUserBalance(AMOUNT, httpServletRequest);

            assertTrue(result.isPresent());
            UserBalance updatedUserBalance = result.get();
            assertEquals(TEST_USER_NAME, updatedUserBalance.getUserName());
            assertEquals(150.0, updatedUserBalance.getBalance());

        }
        verify(userBalanceRepository, times(1)).save(existingUserBalance);
    }

    @Test
    void testIncreaseUserBalanceNewUser() {
        when(userBalanceRepository.findUserBalanceByUserName(TEST_USER_NAME))
                .thenReturn(null);
        when(userBalanceRepository.save(any(UserBalance.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        try(MockedStatic<CurrentUserUtils> utilsMockedStatic = Mockito.mockStatic(CurrentUserUtils.class)) {
            utilsMockedStatic.when(() -> CurrentUserUtils.getCurrentUser(httpServletRequest)).thenReturn(TEST_USER_NAME);
            Optional<UserBalance> result = userBalanceService.increaseUserBalance(AMOUNT, httpServletRequest);

            assertTrue(result.isPresent());
            UserBalance newUserBalance = result.get();
            assertEquals(TEST_USER_NAME, newUserBalance.getUserName());
            assertEquals(AMOUNT, newUserBalance.getBalance());
        }
        verify(userBalanceRepository, times(1)).save(any(UserBalance.class));
    }

    @Test
    void testDecreaseUserBalanceExistingUser() {
        UserBalance existingUserBalance = new UserBalance(TEST_USER_NAME, 50.0);
        try (MockedStatic<CurrentUserUtils> utilsMockedStatic = Mockito.mockStatic(CurrentUserUtils.class)){
            utilsMockedStatic.when(() -> CurrentUserUtils.getCurrentUser(httpServletRequest)).thenReturn(TEST_USER_NAME);

            when(userBalanceRepository.findUserBalanceByUserName(TEST_USER_NAME))
                    .thenReturn(existingUserBalance);


            Optional<UserBalance> result = userBalanceService.decreaseUserBalance(10.0, httpServletRequest);

            assertTrue(result.isPresent());
            UserBalance updatedUserBalance = result.get();
            assertEquals(TEST_USER_NAME, updatedUserBalance.getUserName());
            assertEquals(40.0, updatedUserBalance.getBalance());

        }
        verify(userBalanceRepository, times(1)).save(existingUserBalance);
    }

    @Test
    void testDecreaseUserBalanceNewUser() {
        when(userBalanceRepository.findUserBalanceByUserName(TEST_USER_NAME))
                .thenReturn(null);
        when(userBalanceRepository.save(any(UserBalance.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        try(MockedStatic<CurrentUserUtils> utilsMockedStatic = Mockito.mockStatic(CurrentUserUtils.class)) {
            utilsMockedStatic.when(() -> CurrentUserUtils.getCurrentUser(httpServletRequest)).thenReturn(TEST_USER_NAME);
            Optional<UserBalance> result = userBalanceService.decreaseUserBalance(AMOUNT, httpServletRequest);

            assertTrue(result.isPresent());
            UserBalance newUserBalance = result.get();
            assertEquals(TEST_USER_NAME, newUserBalance.getUserName());
            assertEquals(0.0, newUserBalance.getBalance());
        }
        verify(userBalanceRepository, times(1)).save(any(UserBalance.class));
    }

    @Test
    void testGetMyBalanceExistingUser() {
        UserBalance existingUserBalance = new UserBalance(TEST_USER_NAME, 100.0);
        try (MockedStatic<CurrentUserUtils> utilsMockedStatic = Mockito.mockStatic(CurrentUserUtils.class)){
            utilsMockedStatic.when(() -> CurrentUserUtils.getCurrentUser(httpServletRequest)).thenReturn(TEST_USER_NAME);
            when(userBalanceRepository.findUserBalanceByUserName(TEST_USER_NAME))
                .thenReturn(existingUserBalance);

            Optional<UserBalance> result = userBalanceService.getMyBalance(httpServletRequest);
            assertTrue(result.isPresent());
            assertEquals(existingUserBalance, result.get());
        }
    }

    @Test
    void testGetMyBalanceUserNotFound() {
        try (MockedStatic<CurrentUserUtils> utilsMockedStatic = Mockito.mockStatic(CurrentUserUtils.class)){
            utilsMockedStatic.when(() -> CurrentUserUtils.getCurrentUser(httpServletRequest)).thenReturn(TEST_USER_NAME);
            when(userBalanceRepository.findUserBalanceByUserName(TEST_USER_NAME))
                    .thenReturn(null);

            Optional<UserBalance> result = userBalanceService.getMyBalance(httpServletRequest);

            assertEquals(Optional.empty(), result);
        }
    }
}
