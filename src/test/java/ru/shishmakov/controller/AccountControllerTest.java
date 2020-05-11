package ru.shishmakov.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.shishmakov.dao.AccountRepository;
import ru.shishmakov.dao.LogRepository;
import ru.shishmakov.model.Account;
import ru.shishmakov.model.Log;
import ru.shishmakov.model.Transfer;
import ru.shishmakov.service.AccountService;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyIterable;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test Web layer without JPA
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    private static final Instant ct = Instant.now();

    private JacksonTester<Object> json;
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private LogRepository logRepository;
    @SpyBean
    private AccountService accountService;

    @Before
    public void setUp() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Instant.class, new JsonSerializer<>() {
            @Override
            public void serialize(Instant instant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(DateTimeFormatter.ISO_INSTANT.format(instant));
            }
        });
        JacksonTester.initFields(this, new ObjectMapper().registerModule(simpleModule));
    }

    @Test
    public void rootApiShouldReturnWelcomeMessage() throws Exception {
        mockMvc.perform(get("/api"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(equalTo("RESTfull API for money transfer")));
    }

    @Test
    public void getLogsShouldReturnAllAvailableTransactions() throws Exception {
        List<Log> data = List.of(
                Log.builder().date(ct).amount(new BigDecimal("2.0")).toNumber(100L).description("deposit").build(),
                Log.builder().date(ct).amount(new BigDecimal("1.0")).fromNumber(100L).description("withdraw").build(),
                Log.builder().date(ct).amount(new BigDecimal("1.0")).fromNumber(100L).toNumber(200L).description("transfer").build()
        );
        doReturn(data).when(logRepository).findAll();

        MockHttpServletResponse response = mockMvc.perform(get("/api/logs")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(OK.value());
        assertThat(response.getContentType()).contains(APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(json.write(data).getJson());

        verify(accountService).getLogRecords();
        verify(logRepository).findAll();
    }

    @Test
    public void getAccountsShouldReturnAllAvailableAccounts() throws Exception {
        List<Account> data = List.of(
                Account.builder().accNumber(100L).amount(new BigDecimal("1.0")).lastUpdate(ct).build(),
                Account.builder().accNumber(200L).amount(new BigDecimal("2.0")).lastUpdate(ct).build(),
                Account.builder().accNumber(300L).amount(new BigDecimal("3.0")).lastUpdate(ct).build()
        );
        doReturn(data).when(accountRepository).findAll();

        MockHttpServletResponse response = mockMvc.perform(get("/api/accounts")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(OK.value());
        assertThat(response.getContentType()).contains(APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(json.write(data).getJson());

        verify(accountService).getAccounts();
        verify(accountRepository).findAll();
    }

    @Test
    public void getAccountShouldReturnAccountIfAvailable() throws Exception {
        Account account = Account.builder().accNumber(100L).amount(new BigDecimal("1.0")).lastUpdate(ct).build();
        doReturn(Optional.of(account)).when(accountRepository).findByAccNumber(anyLong());

        MockHttpServletResponse response = mockMvc.perform(get("/api/account/1")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(OK.value());
        assertThat(response.getContentType()).contains(APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(json.write(account).getJson());

        verify(accountService).getAccount(anyLong());
        verify(accountRepository).findByAccNumber(anyLong());
    }

    @Test
    public void getAccountShouldNotReturnAccountIfNotAvailable() throws Exception {
        doReturn(Optional.empty()).when(accountRepository).findByAccNumber(anyLong());

        MockHttpServletResponse response = mockMvc.perform(get("/api/account/1")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.value());
        assertThat(response.getContentType()).isNull();
        assertThat(response.getContentAsString()).isBlank();

        verify(accountService).getAccount(anyLong());
        verify(accountRepository).findByAccNumber(anyLong());
    }

    @Test
    public void putTransferShouldPerformSuccessfullyIfRequestValid() throws Exception {
        // 1.0 ->(+1.0)-> 1.0
        Account from = Account.builder().accNumber(100L).amount(new BigDecimal("1.0")).lastUpdate(ct).build();
        Account to = Account.builder().accNumber(200L).amount(new BigDecimal("1.0")).lastUpdate(ct).build();
        Transfer transfer = Transfer.builder().from(from.getAccNumber()).to(to.getAccNumber()).amount(new BigDecimal("1.0")).build();
        doReturn(Optional.of(from)).doReturn(Optional.of(to)).when(accountRepository).findByAccNumberAndLock(anyLong());
        doAnswer(in -> in.getArguments()[0]).when(accountRepository).saveAll(anyList());
        doNothing().when(accountService).updateDate(any(Account.class));

        MockHttpServletResponse response = mockMvc
                .perform(put("/api/accounts/transfer")
                        .contentType(APPLICATION_JSON)
                        .content(json.write(transfer).getJson()))
                .andReturn().getResponse();

        // 0.0 : 2.0
        assertThat(response.getStatus()).isEqualTo(OK.value());
        assertThat(response.getContentType()).contains(APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(json.write(List.of(
                Account.builder().accNumber(100L).amount(new BigDecimal("0.0")).lastUpdate(ct).build(),
                Account.builder().accNumber(200L).amount(new BigDecimal("2.0")).lastUpdate(ct).build()
        )).getJson());

        verify(accountService).transfer(anyLong(), anyLong(), any(BigDecimal.class));
        verify(accountRepository, times(2)).findByAccNumberAndLock(anyLong());
        verify(accountRepository).saveAll(anyIterable());
    }

    @Test
    public void putTransferShouldFailIfAmountNegative() throws Exception {
        // 1.0 ->(-1.0)-> 1.0
        Account from = Account.builder().accNumber(100L).amount(new BigDecimal("1.0")).lastUpdate(ct).build();
        Account to = Account.builder().accNumber(200L).amount(new BigDecimal("1.0")).lastUpdate(ct).build();
        Transfer transfer = Transfer.builder().from(from.getAccNumber()).to(to.getAccNumber()).amount(new BigDecimal("-1.0")).build();

        mockMvc.perform(put("/api/accounts/transfer")
                .contentType(APPLICATION_JSON)
                .content(json.write(transfer).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(isEmptyOrNullString()));

        verify(accountService).transfer(anyLong(), anyLong(), any(BigDecimal.class));
        verify(accountRepository, never()).findByAccNumberAndLock(anyLong());
        verify(accountRepository, never()).saveAll(anyIterable());
    }

    @Test
    public void putTransferShouldFailIfTransferObjectNotValid() throws Exception {
        Transfer transfer = Transfer.builder().amount(new BigDecimal("-1.0")).build();

        mockMvc.perform(put("/api/accounts/transfer")
                .contentType(APPLICATION_JSON)
                .content(json.write(transfer).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(isEmptyOrNullString()));

        verify(accountService, never()).transfer(anyLong(), anyLong(), any(BigDecimal.class));
    }

    @Test
    public void putDepositShouldPerformSuccessfullyIfRequestValid() throws Exception {
        // (+1.0) -> 1.0
        Account to = Account.builder().accNumber(200L).amount(new BigDecimal("1.0")).lastUpdate(ct).build();
        Transfer transfer = Transfer.builder().to(to.getAccNumber()).amount(new BigDecimal("1.0")).build();
        doReturn(Optional.of(to)).when(accountRepository).findByAccNumberAndLock(anyLong());
        doAnswer(in -> in.getArguments()[0]).when(accountRepository).save(any(Account.class));
        doNothing().when(accountService).updateDate(any(Account.class));

        MockHttpServletResponse response = mockMvc
                .perform(put("/api/account/deposit")
                        .contentType(APPLICATION_JSON)
                        .content(json.write(transfer).getJson()))
                .andReturn().getResponse();

        // 2.0
        assertThat(response.getStatus()).isEqualTo(OK.value());
        assertThat(response.getContentType()).contains(APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(json.write(
                Account.builder().accNumber(200L).amount(new BigDecimal("2.0")).lastUpdate(ct).build()
        ).getJson());

        verify(accountService).deposit(anyLong(), any(BigDecimal.class));
        verify(accountRepository).findByAccNumberAndLock(anyLong());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    public void putDepositShouldFailIfAmountNegative() throws Exception {
        // (-1.0) -> 1.0
        Account to = Account.builder().accNumber(200L).amount(new BigDecimal("1.0")).lastUpdate(ct).build();
        Transfer transfer = Transfer.builder().to(to.getAccNumber()).amount(new BigDecimal("-1.0")).build();

        mockMvc.perform(put("/api/account/deposit")
                .contentType(APPLICATION_JSON)
                .content(json.write(transfer).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(isEmptyOrNullString()));

        verify(accountService).deposit(anyLong(), any(BigDecimal.class));
        verify(accountRepository, never()).findByAccNumberAndLock(anyLong());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void putDepositShouldFailIfTransferObjectNotValid() throws Exception {
        Transfer transfer = Transfer.builder().amount(new BigDecimal("1.0")).build();

        mockMvc.perform(put("/api/account/deposit")
                .contentType(APPLICATION_JSON)
                .content(json.write(transfer).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(isEmptyOrNullString()));

        verify(accountService, never()).deposit(anyLong(), any(BigDecimal.class));
    }

    @Test
    public void putWithdrawShouldPerformSuccessfullyIfRequestValid() throws Exception {
        // (+1.0) <- 1.0
        Account from = Account.builder().accNumber(200L).amount(new BigDecimal("1.0")).lastUpdate(ct).build();
        Transfer transfer = Transfer.builder().from(from.getAccNumber()).amount(new BigDecimal("1.0")).build();
        doReturn(Optional.of(from)).when(accountRepository).findByAccNumberAndLock(anyLong());
        doAnswer(in -> in.getArguments()[0]).when(accountRepository).save(any(Account.class));
        doNothing().when(accountService).updateDate(any(Account.class));

        MockHttpServletResponse response = mockMvc
                .perform(put("/api/account/withdraw")
                        .contentType(APPLICATION_JSON)
                        .content(json.write(transfer).getJson()))
                .andReturn().getResponse();

        // 0.0
        assertThat(response.getStatus()).isEqualTo(OK.value());
        assertThat(response.getContentType()).contains(APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(json.write(
                Account.builder().accNumber(200L).amount(new BigDecimal("0.0")).lastUpdate(ct).build()
        ).getJson());

        verify(accountService).withdraw(anyLong(), any(BigDecimal.class));
        verify(accountRepository).findByAccNumberAndLock(anyLong());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    public void putWithdrawShouldFailIfAmountNegative() throws Exception {
        // (-1.0) <- 1.0
        Account from = Account.builder().accNumber(200L).amount(new BigDecimal("1.0")).lastUpdate(ct).build();
        Transfer transfer = Transfer.builder().from(from.getAccNumber()).amount(new BigDecimal("-1.0")).build();

        mockMvc.perform(put("/api/account/withdraw")
                .contentType(APPLICATION_JSON)
                .content(json.write(transfer).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(isEmptyOrNullString()));

        verify(accountService).withdraw(anyLong(), any(BigDecimal.class));
        verify(accountRepository, never()).findByAccNumberAndLock(anyLong());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void putWithdrawShouldFailIfTransferObjectNotValid() throws Exception {
        Transfer transfer = Transfer.builder().amount(new BigDecimal("1.0")).build();

        mockMvc.perform(put("/api/account/withdraw")
                .contentType(APPLICATION_JSON)
                .content(json.write(transfer).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(isEmptyOrNullString()));

        verify(accountService, never()).withdraw(anyLong(), any(BigDecimal.class));
    }

}
