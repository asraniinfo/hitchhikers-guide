package com.cgm.assignment.hitchhikersguide.runner;

import com.cgm.assignment.hitchhikersguide.exception.InvalidInput;
import com.cgm.assignment.hitchhikersguide.exception.LengthExceededException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
class MyRunnerTests {
    private final MyRunner myRunner = new MyRunner();
    private static final String lengthExceededString = "Linz is a city in Austria. The country’s third-largest, it is also the capital and main city of the state of Upper Austria. It is in the north centre of Austria, approximately 30 kilometres (19 miles) south of the Czech border, on both sides of the river Danube. The population of the city is 206,604,[3] and that of the Greater Linz conurbation is about 789,811.";
    private static final String ques = "is Linz a city in Austria";
    @Test
    void validateQnALengthException() {
        String[] qNa = {lengthExceededString, "this is an answer"};
        Assertions.assertThrows(LengthExceededException.class, () -> myRunner.validateQnA(qNa));
    }

    @Test
    void validateQnAInvalidInput() {
        String[] qNa = {lengthExceededString};
        Assertions.assertThrows(InvalidInput.class, () -> myRunner.validateQnA(qNa));
    }

    @Test
    void validateQnAValidInput() {
        String[] qNa = {ques, " Yes Linz is a city in Austria"};
        Assertions.assertTrue(myRunner.validateQnA(qNa));
    }

    @Test
    void validateQuestionLengthException() {
        Assertions.assertThrows(LengthExceededException.class, () -> myRunner.validateQuestion(lengthExceededString));
    }

    @Test
    void validateQuestionInvalidInput() {
        Assertions.assertThrows(InvalidInput.class, () -> myRunner.validateQuestion(ques));
    }

    @Test
    void validateQuestionValidInput() {
        String question = "is Linz a city in Austria?";
        Assertions.assertTrue(myRunner.validateQuestion(question));
    }

    @Test
    void validateSplittQnA() {
        String qNa = "is Linz a city in Austria? \"answers\"";
        Assertions.assertSame(myRunner.splittQnA(qNa).length, 2);
        String[] qNaArray = myRunner.splittQnA(qNa);
        Assertions.assertEquals(qNaArray[0], ques);
        Assertions.assertEquals(qNaArray[1].trim(), "\"answers\"");
    }

    @Test
    void validateSplittQnAMultiAnswer() {
        String qNa = "is Linz a city in Austria? \"answers1\" \"answers2\"";
        Assertions.assertSame(myRunner.splittQnA(qNa).length, 2);
        String[] qNaArray = myRunner.splittQnA(qNa);
        Assertions.assertEquals(qNaArray[0], ques);
        Assertions.assertEquals(qNaArray[1].trim(), "\"answers1\" \"answers2\"");
    }

    @Test
    void validatesplittAnswersInvalidInput() {
        String answer = "\" \" \" \" \" \"";
        Assertions.assertThrows(InvalidInput.class, () -> myRunner.splittAnswers(answer));
    }

    @Test
    void validatesplittAnswersLengthExceeded() {
        String answer = "\"" + lengthExceededString + "\"";
        Assertions.assertThrows(LengthExceededException.class, () -> myRunner.splittAnswers(answer));
    }

    @Test
    void validatesplittAnswersValidInput() {
        List<String> listOfAnswers = List.of("•\tLinz is a city in Austria.", "•\tThe country’s third-largest, it is also the capital and main city of the state of Upper Austria.");
        String answer = "\"Linz is a city in Austria.\" \"The country’s third-largest, it is also the capital and main city of the state of Upper Austria.\"";

        Assertions.assertTrue(myRunner.splittAnswers(answer).stream().allMatch(s -> listOfAnswers.stream().anyMatch(s1 -> s1.equals(s))));
    }
}
