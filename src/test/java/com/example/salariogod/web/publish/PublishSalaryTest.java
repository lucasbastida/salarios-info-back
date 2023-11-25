package com.example.salariogod.web.publish;

import com.example.salariogod.application.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublishSalaryTest {

    @Mock
    private PublishPayment payment;

    private PublishSalary publishSalary;

    @BeforeEach
    void setUp() {
        when(payment.to()).thenReturn(new Payment());

        publishSalary = new PublishSalary();
        publishSalary.setPayments(List.of(payment));
        publishSalary.setPaymentDate(LocalDate.now());
        publishSalary.setContract(Contract.STAFF);
    }

    @DisplayName("to")
    @Nested
    class ToTest {
        @DisplayName("when other role and otherRole is not empty then create other role domain entity")
        @Test
        void whenOtherAndOtherRoleValidTest() {
            publishSalary.setTechRole(TechRole.OTHER);
            publishSalary.setOtherRole("machine learning engineer");

            final Salary expected = buildExpected(TechRole.OTHER, "machine learning engineer");

            final Salary actual = publishSalary.to();

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        @DisplayName("when other role and empty value then create salary domain entity without other role entity")
        @NullAndEmptySource
        @ParameterizedTest
        void whenOtherAndOtherRoleValidTest(String otherRole) {
            publishSalary.setTechRole(TechRole.OTHER);
            publishSalary.setOtherRole(otherRole);

            final Salary expected = buildExpected(TechRole.OTHER, otherRole);

            final Salary actual = publishSalary.to();

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        @DisplayName("when role is not other and otherRole is empty then create domain entity without other role entity")
        @MethodSource("provideNotOtherRoles")
        @ParameterizedTest
        void whenNotOtherTest(TechRole role, String otherRole) {
            publishSalary.setTechRole(role);
            publishSalary.setOtherRole(otherRole);

            final Salary expected = buildExpected(role, otherRole);

            final Salary actual = publishSalary.to();

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        @DisplayName("when role is not other and otherRole is not empty then create domain entity without other role entity")
        @EnumSource(value = TechRole.class, names = "OTHER", mode = EnumSource.Mode.EXCLUDE)
        @ParameterizedTest
        void whenNotOtherTest(TechRole role) {
            publishSalary.setTechRole(role);
            publishSalary.setOtherRole("foo");

            final Salary expected = buildExpected(role, null);

            final Salary actual = publishSalary.to();

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        private static Salary buildExpected(TechRole role, String otherRole) {
            final Salary expected = Salary.builder()
                    .payments(List.of(new Payment()))
                    .paymentDate(LocalDate.now())
                    .contract(Contract.STAFF)
                    .techRole(role)
                    .otherRole(null)
                    .build();

            if (StringUtils.hasText(otherRole)) {
                final OtherRole other = new OtherRole();
                other.setName(otherRole);
                expected.setOtherRole(List.of(other));
            }

            return expected;
        }

        private static Stream<Arguments> provideNotOtherRoles() {
            return Arrays.stream(TechRole.values())
                    .filter(techRole -> !TechRole.OTHER.equals(techRole))
                    .flatMap(techRole -> Stream.of(
                            Arguments.of(techRole, ""),
                            Arguments.of(techRole, null)));
        }
    }
}