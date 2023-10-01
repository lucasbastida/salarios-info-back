package com.example.salariogod.web.publish;

import com.example.salariogod.application.domain.TechRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PublishSalaryValidatorTest {

    private PublishSalaryValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PublishSalaryValidator();
    }

    @DisplayName("when OTHER techRole then otherRole can be any value")
    @NullAndEmptySource
    @ValueSource(strings = "foo")
    @ParameterizedTest
    void otherTechRoleTest(String otherRole) {
        final PublishSalary publishSalary = new PublishSalary();
        publishSalary.setTechRole(TechRole.OTHER);
        publishSalary.setOtherRole(otherRole);

        final boolean actual = validator.isValid(publishSalary, null);

        assertThat(actual).isTrue();
    }

    @DisplayName("when techRole is not OTHER and otherRole has text then invalid")
    @EnumSource(value = TechRole.class, names = {"OTHER"}, mode = EnumSource.Mode.EXCLUDE)
    @ParameterizedTest
    void whenTechRoleNotOtherAndOtherRoleHasText_then_isInvalid(TechRole techRole) {
        final PublishSalary publishSalary = new PublishSalary();
        publishSalary.setTechRole(techRole);
        publishSalary.setOtherRole("foo");

        final boolean actual = validator.isValid(publishSalary, null);

        assertThat(actual).isFalse();
    }

    @DisplayName("when techRole is not OTHER and otherRole is empty then is valid")
    @MethodSource("provideValuesForInvaldNotOtherRoleCombination")
    @ParameterizedTest
    void whenTechRoleNotOtherAndOtherRoleHasText_then_isInvalida(TechRole techRole, String otherRole) {
        final PublishSalary publishSalary = new PublishSalary();
        publishSalary.setTechRole(techRole);
        publishSalary.setOtherRole(otherRole);

        final boolean actual = validator.isValid(publishSalary, null);

        assertThat(actual).isTrue();
    }

    private static Stream<Arguments> provideValuesForInvaldNotOtherRoleCombination() {
        return Arrays.stream(TechRole.values()).flatMap(techRole -> Stream.of(
                Arguments.of(techRole, null),
                Arguments.of(techRole, "")));
    }
}