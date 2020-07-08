package com.cgm.assignment.hitchhikersguide.runner;

import com.cgm.assignment.hitchhikersguide.exception.InvalidInput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class MyRunnerTests {
    private final MyRunner myRunner = new MyRunner();
    private static final String LENGTH_EXCEED_STRING = "Linz is a city in Austria. The country’s third-largest, it is also the capital and main city of the state of Upper Austria. It is in the north centre of Austria, approximately 30 kilometres (19 miles) south of the Czech border, on both sides of the river Danube. The population of the city is 206,604,[3] and that of the Greater Linz conurbation is about 789,811.206,604,[3] and that of the Greater Linz conurbation is about 789,811.206,604,[3] and that of the Greater Linz conurbation is about 789,811.";
    private static final String QNA = "is Linz a city in Austria? \"yes\" \"also The country’s third largest city.\"";
    private static final String QUESTION = "is Linz a city in Austria?";

    @ParameterizedTest
    @ValueSource(strings = {"a", "A", "1a", LENGTH_EXCEED_STRING})
    @NullAndEmptySource
    void validateQnAInvalidInput(String qNa) {
        assertFalse(validateQnA(qNa));
    }

    private boolean validateQnA(String qNa) {
        return myRunner.validateQnA(qNa);
    }

    @Test
    void validateQnAValidInput() {
        assertTrue(validateQnA(QNA));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "A", "1a", LENGTH_EXCEED_STRING})
    @NullAndEmptySource
    void validateQuestionInvalidInput(String question) {
        assertFalse(validateQuestion(question));
    }

    private boolean validateQuestion(String question) {
        return myRunner.validateQuestion(question);
    }

    @Test
    void validateQuestionValidInput() {
        assertTrue(validateQuestion(QUESTION));
    }

    @Test
    void validateSplitQnA() {
        myRunner.splitQnA(QNA).forEach((question, answers) ->
                assertTrue(QUESTION.equals(question) && answers.stream().allMatch(s ->
                        List.of("yes", "also The country’s third largest city.").stream().anyMatch(s1 -> s1.equals(s)))));
    }

    @Test
    void validateSplitQnAInvalidInput() {
        assertThrows(InvalidInput.class, () -> myRunner.splitQnA(LENGTH_EXCEED_STRING));
    }

}
