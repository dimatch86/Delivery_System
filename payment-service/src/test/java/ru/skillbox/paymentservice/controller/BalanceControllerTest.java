package ru.skillbox.paymentservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.paymentservice.domain.UserBalance;
import ru.skillbox.paymentservice.service.UserBalanceService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(BalanceController.class)
class BalanceControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserBalanceService userBalanceService;
    @Mock
    private HttpServletRequest httpServletRequest;

    @Configuration
    @ComponentScan(basePackageClasses = {BalanceController.class})
    public static class TestConf {
    }

    private UserBalance currentUserBalance;

    @BeforeEach
    public void setUp() {
        currentUserBalance = new UserBalance("Carl", 150.55);
    }

    @Test
    void getMyBalance() throws Exception {
        Mockito.when(userBalanceService.getMyBalance(any(HttpServletRequest.class))).thenReturn(Optional.of(currentUserBalance));
        mvc.perform(get("/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Carl")));
    }

    @Test
    void addMoney() throws Exception {
        //when(httpServletRequest.getRemoteUser()).thenReturn(TEST_USER_NAME);
        Mockito.when(userBalanceService.increaseUserBalance(any(Double.class), any(HttpServletRequest.class))).thenReturn(Optional.of(currentUserBalance));
        mvc.perform(
                        post("/add")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"amount\":1000}"
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(150.55)));
    }

    @Test
    void takeMoney() throws Exception {
        Mockito.when(userBalanceService.decreaseUserBalance(any(Double.class), any(HttpServletRequest.class))).thenReturn(Optional.of(currentUserBalance));
        mvc.perform(
                        post("/take")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"amount\":1000}"
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }
}
