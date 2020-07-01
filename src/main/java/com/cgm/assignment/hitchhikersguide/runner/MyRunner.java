package com.cgm.assignment.hitchhikersguide.runner;

import com.cgm.assignment.hitchhikersguide.exception.InvalidInput;
import com.cgm.assignment.hitchhikersguide.service.HitchhikerService;
import com.cgm.assignment.hitchhikersguide.service.HitchhikerServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.lang.System.out;

@Profile("!test")
@Component
public class MyRunner implements CommandLineRunner {
    private static final HitchhikerService hitchhikerService = new HitchhikerServiceImpl();
    private static final String readException = "There is error in reading input, Please try again later \n";
    private static final String operationsList = "\nPlease choose the operation from list \n" +
            "To add question and answer type: 1 \n" +
            "To find the answer to your question type: 2 \n" +
            "To exit application type: 0 \n";
    private static final String invalidInput = "Invalid input value/format, Please try again with correct value";
    private static final String invalidSelection = "Invalid operation \" %s \". please type the correct value";
    private static final String note = "\nPLEASE NOTE:\n";
    private static final String charLimitQuestion = "•\tA Question is a String with max 255 chars\n";
    private static final String charLimitAnswer = "•\tAn Answer is a String with max 255 chars\n";
    private static final String questionFormat = "\nPlease type your question in following format" +
            "•\t<question>? here Char “?” is the identifier of question";
    private static final String qNaFormat = "\nPlease type your question and answer in following format: \n" +
            "•\t<question>? “<answer1>” “<answer2>” “<answerX>”\n" +
            "•\tEvery Question needs to have at least one answer but can have unlimited answers all inside of char “ \n" +
            "•\tSpecial character not allowed except('),(.)(’)“ \n";
    private static final String addQnASuccess = "Answer to the question is added in the system successfully";
    private static final String qNaRegex = "^[a-zA-Z0-9 '.’]{1,255}\\?( \\\"[a-zA-Z0-9 '.’]{1,255}\\\")+$";
    private static final String questionRegex = "^[a-zA-Z0-9 '.’]{1,255}\\?";

    @Override
    public void run(String... args) throws IOException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(System.in);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            while (true) {
                out.println(operationsList);
                try {
                    int inputOperation = Integer.parseInt(bufferedReader.readLine());
                    if (inputOperation == 1) {
                        out.println(note + charLimitQuestion + charLimitAnswer);
                        out.println(qNaFormat);
                        Map<String, List<String>> qNa = splitQnA(bufferedReader.readLine());
                        qNa.forEach(hitchhikerService::addAnswer);
                        out.println(addQnASuccess);
                    } else if (inputOperation == 2) {
                        out.println(note + charLimitQuestion);
                        out.println(questionFormat);
                        String question = bufferedReader.readLine();
                        if (!validateQuestion(question)) {
                            throw new InvalidInput(invalidInput);
                        }
                        hitchhikerService.getAnswer(question).forEach(s -> out.println("•\t"+s));
                    } else if (inputOperation == 0) {
                        break;
                    } else {
                        out.println(String.format(invalidSelection, inputOperation));
                    }
                } catch (NumberFormatException | InputMismatchException ex) {
                    out.println(invalidInput);
                } catch (InvalidInput ex) {
                    out.println(ex.getMessage());
                } catch (IOException e) {
                    out.println(readException);
                    break;
                }
            }
        }

    }

    /**
     * validate question & answer Strings
     *
     * @param qNa question & answer string
     * @return boolean decision
     */
    boolean validateQnA(String qNa) {
        return Pattern.matches(qNaRegex, qNa);
    }

    /**
     * validate length & required character
     *
     * @param question string
     * @return boolean decision
     */
    boolean validateQuestion(String question) {
        return Pattern.matches(questionRegex, question);
    }

    /**
     * will return splitted question and answers string into an array
     *
     * @param questionAndAnswer string
     * @return question and answer map
     */
    Map<String, List<String>> splitQnA(String questionAndAnswer) {
        if (validateQnA(questionAndAnswer)) {
            String[] data = questionAndAnswer.split("[?]");
            Map<String, List<String>> qNaMap = new HashMap<>();
            qNaMap.put(data[0] + "?", List.of(data[1].trim().substring(1, data[1].length() - 2).split("\" \"")));
            return qNaMap;
        } else {
            throw new InvalidInput(invalidInput);
        }
    }
}