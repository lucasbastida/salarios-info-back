package com.example.salariogod.web.publish;

import com.example.salariogod.application.domain.Contract;
import com.example.salariogod.application.domain.TechRole;
import com.example.salariogod.application.service.PublishSalaryService;
import com.example.salariogod.security.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublishSalaryController.class)
@Import(SecurityConfig.class)
class PublishSalaryControllerTest {

    private static final String URL = "/v1/salary";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PublishSalaryService publishSalaryService;

    @DisplayName("when request is invalid then return 400 Bad Request")
    @Test
    void badRequestTest() throws Exception {
        mockMvc.perform(post(URL))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("when publish salary service fails then raise exception")
    @Test
    void serviceFailsTest() {
        final PublishRequest request = build();

        doThrow(RuntimeException.class).when(publishSalaryService).publish(anyList());

        assertThatThrownBy(() -> mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))))
                .hasCauseInstanceOf(RuntimeException.class);
    }

    @DisplayName("when salary is published then 200 Ok")
    @Test
    void serviceSuccessTest() throws Exception {
        final PublishRequest request = build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    private static PublishRequest build() {
        final PublishRequest request = new PublishRequest();
        final PublishPayment payment = new PublishPayment();
        final PublishSalary salary = new PublishSalary();

        payment.setAmount("2");
        payment.setCurrency("USD");
        salary.setPayments(List.of(payment));
        salary.setContract(Contract.STAFF);
        salary.setPaymentDate(LocalDate.now());
        salary.setTechRole(TechRole.BACKEND);
        request.setSalaries(List.of(salary));

        return request;
    }
}