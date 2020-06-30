package com.cgm.assignment.hitchhikersguide.runner;

import com.cgm.assignment.hitchhikersguide.exception.InvalidInput;
import com.cgm.assignment.hitchhikersguide.exception.LengthExceededException;
import com.cgm.assignment.hitchhikersguide.service.HitchhikerService;
import com.cgm.assignment.hitchhikersguide.service.HitchhikerServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.out;

@Profile("!test")
@Component
public class MyRunner implements CommandLineRunner {
    private static final HitchhikerService hitchhikerService = new HitchhikerServiceImpl();
    private static final String readException = "There is error in reading input, Please try again later";
    private static final String operationsList = "\nPlease choose the operation from list \n" +
            "To add question and answer type: 1 \n" +
            "To find the answer to your question type: 2 \n" +
            "To exit application type: 0 \n" +
            "Please press ENTER after each input";
    private static final String invalidInput = "Invalid input value/format, Please try again with correct value";
    private static final String invalidSelection = "Invalid operation \" %s \". please type the correct value";
    private static final String note = "\nPLEASE NOTE:\n";
    private static final String charLimitQuestion = "•\tA Question is a String with max 255 chars\n";
    private static final String charLimitAnswer = "•\tAn Answer is a String with max 255 chars\n";
    private static final String questionFormat = "\nPlease type your question in following format" +
            "•\t<question>? here Char “?” is the identifier of question";
    private static final String qNaFormat = "\nPlease type your question and answer in following format: \n" +
            "•\t<question>? “<answer1>” “<answer2>” “<answerX>”\n" +
            "•\tChar “?” is the separator between question and answers\n" +
            "•\tChar “ ” is the separator between answers\n" +
            "•\tEvery Question needs to have at least one answer but can have unlimited answers all inside of char “";
    private static final String addQnASuccess = "Answer to the question is added in the system successfully";
    private static final String lengthExceeded = "Length of the %s is exceeded";
    private static final String invalidValue = "Invalid value of the %s, type correct value/format";
    private static final String questionText = "question";
    private static final String answerText = "answer";

    @Override
    public void run(String... args) throws IOException{


        try(InputStreamReader inputStreamReader = new InputStreamReader(System.in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            while (true) {
                out.println(operationsList);
                try {
                    int inputOperation = Integer.parseInt(bufferedReader.readLine());
                    if (inputOperation == 1) {
                        out.println(note + charLimitQuestion + charLimitAnswer);
                        out.println(qNaFormat);
                        String[] qNa = splittQnA(bufferedReader.readLine());
                        validateQnA(qNa);
                        hitchhikerService.addAnswer(qNa[0], splittAnswers(qNa[1]));
                        out.println(addQnASuccess);
                    } else if (inputOperation == 2) {
                        out.println(note + charLimitQuestion);
                        out.println(questionFormat);
                        String question = bufferedReader.readLine();
                        validateQuestion(question);
                        hitchhikerService.getAnswer(question).forEach(out::println);
                    } else if (inputOperation == 0) {
                        break;
                    } else {
                        out.println(String.format(invalidSelection, inputOperation));
                    }
                } catch (NumberFormatException | InputMismatchException ex) {
                    out.println(invalidInput);
                } catch (IOException e) {
                    out.println(readException);
                } catch (LengthExceededException | InvalidInput ex) {
                    out.println(ex.getMessage());
                }
            }
        }

    }

    boolean validateQnA(String[] qNa) {
        if (qNa.length > 1) {
            qNa[0] = qNa[0] + "?";
            if (!validateLength(qNa[0])) {
                throw new LengthExceededException(String.format(lengthExceeded, questionText));
            }
        } else {
            throw new InvalidInput(invalidInput);
        }
        return true;
    }

    boolean validateQuestion(String question) {
        if (!validateLength(question)) {
            throw new LengthExceededException(String.format(lengthExceeded, questionText));
        }
        if (question.toCharArray()[question.length() - 1] != '?') {
            throw new InvalidInput(String.format(invalidValue, questionText));
        }
        return true;
    }

    String[] splittQnA(String questionAndAnswer) {
        return questionAndAnswer.split("[?]");
    }

    List<String> splittAnswers(String answer) {
        List<String> answers = Arrays.asList(answer.split("\""));
        if (answers.stream().allMatch(this::validateLength)) {
            answers = answers.stream().filter(s -> (s.trim().length() > 0)).distinct().map(s -> "•\t" + s).collect(Collectors.toList());
        } else {
            throw new LengthExceededException(String.format(lengthExceeded, answerText));
        }
        if (answers.isEmpty()) {
            throw new InvalidInput(String.format(invalidValue, answerText));
        } else {
            return answers;
        }
    }

    private boolean validateLength(String str) {
        return str.length() < 256;
    }
}