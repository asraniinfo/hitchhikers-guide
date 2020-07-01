package com.cgm.assignment.hitchhikersguide.runner;

import com.cgm.assignment.hitchhikersguide.exception.InvalidInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
class MyRunnerTests {
    private final MyRunner myRunner = new MyRunner();
    private static final String lengthExceededString = "Linz is a city in Austria. The country’s third-largest, it is also the capital and main city of the state of Upper Austria. It is in the north centre of Austria, approximately 30 kilometres (19 miles) south of the Czech border, on both sides of the river Danube. The population of the city is 206,604,[3] and that of the Greater Linz conurbation is about 789,811.206,604,[3] and that of the Greater Linz conurbation is about 789,811.206,604,[3] and that of the Greater Linz conurbation is about 789,811.";
    private static final String qNa = "is Linz a city in Austria? \"yes\" \"also The country’s third largest city.\"";
    private static final String question = "is Linz a city in Austria?";


    @Test
    void validateQnAInvalidInput() {
        Assertions.assertFalse(myRunner.validateQnA(lengthExceededString));
    }

    @Test
    void validateQnAValidInput() {
        Assertions.assertTrue(myRunner.validateQnA(qNa));
    }


    @Test
    void validateQuestionInvalidInput() {
        Assertions.assertFalse(myRunner.validateQuestion(lengthExceededString));
    }

    @Test
    void validateQuestionValidInput() {
        Assertions.assertTrue(myRunner.validateQuestion(question));
    }

    @Test
    void validateSplitQnA() {
        myRunner.splitQnA(qNa).forEach((question, answers) ->
                Assertions.assertTrue(question.equals("is Linz a city in Austria?") && answers.stream().allMatch(s ->
                        List.of("yes", "also The country’s third largest city.").stream().anyMatch(s1 -> s1.equals(s)))));
    }

    @Test
    void validateSplitQnAInvalidInput() {
        Assertions.assertThrows(InvalidInput.class, () -> myRunner.splitQnA(lengthExceededString));
    }

}
