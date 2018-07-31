package ru.shishmakov.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Before;
import org.junit.Test;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test Web layer without JPA
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ApiController.class)
//@SpringBootTest
//@AutoConfigureMockMvc
public class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private LogRepository logRepository;
    @SpyBean
    private AccountService accountService;

    private JacksonTester<List<Log>> jsonLogs;
    private JacksonTester<List<Account>> jsonAccounts;
    private JacksonTester<Account> jsonAccount;

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
    public void apiGetLogsShouldReturnArray() throws Exception {
        List<Log> data = List.of(
                Log.builder().date(Instant.now()).amount(new BigDecimal("2.0")).toNumber(1L).description("deposit").build(),
                Log.builder().date(Instant.now()).amount(new BigDecimal("1.0")).fromNumber(1L).description("withdraw").build(),
                Log.builder().date(Instant.now()).amount(new BigDecimal("1.0")).fromNumber(1L).toNumber(2L).description("transfer").build()
        );
        doReturn(data).when(logRepository).findAll();

        MockHttpServletResponse response = mockMvc.perform(get("/api/logs")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(OK.value());
        assertThat(response.getContentType()).contains(APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(jsonLogs.write(data).getJson());
        verify(accountService).getLogRecords();
        verify(logRepository).findAll();
    }

    @Test
    public void apiGetAccountsShouldReturnArray() throws Exception {
        List<Account> data = List.of(
                Account.builder().accNumber(1L).amount(new BigDecimal("1.0")).lastUpdate(Instant.now()).build(),
                Account.builder().accNumber(2L).amount(new BigDecimal("2.0")).lastUpdate(Instant.now()).build(),
                Account.builder().accNumber(3L).amount(new BigDecimal("3.0")).lastUpdate(Instant.now()).build()
        );
        doReturn(data).when(accountRepository).findAll();

        MockHttpServletResponse response = mockMvc.perform(get("/api/accounts")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(OK.value());
        assertThat(response.getContentType()).contains(APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(jsonAccounts.write(data).getJson());
        verify(accountService).getAccounts();
        verify(accountRepository).findAll();
    }

    @Test
    public void apiGetAccountShouldReturnAccountIfAvailable() throws Exception {
        Account account = Account.builder().accNumber(1L).amount(new BigDecimal("1.0")).lastUpdate(Instant.now()).build();
        doReturn(Optional.of(account)).when(accountRepository).findByAccNumber(anyLong());

        MockHttpServletResponse response = mockMvc.perform(get("/api/account/1")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(OK.value());
        assertThat(response.getContentType()).contains(APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(jsonAccount.write(account).getJson());
        verify(accountService).getAccount(eq(1L));
        verify(accountRepository).findByAccNumber(eq(1L));
    }

    @Test
    public void apiGetAccountShouldNotReturnAccountIfNotAvailable() throws Exception {
        doReturn(Optional.empty()).when(accountRepository).findByAccNumber(anyLong());

        MockHttpServletResponse response = mockMvc.perform(get("/api/account/1")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.value());
        assertThat(response.getContentType()).isNull();
        assertThat(response.getContentAsString()).isBlank();
        verify(accountService).getAccount(eq(1L));
        verify(accountRepository).findByAccNumber(eq(1L));
    }
}
