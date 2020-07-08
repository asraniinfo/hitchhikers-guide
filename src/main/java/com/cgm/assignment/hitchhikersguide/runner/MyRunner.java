package com.cgm.assignment.hitchhikersguide.runner;

import com.cgm.assignment.hitchhikersguide.exception.InvalidInput;
import com.cgm.assignment.hitchhikersguide.service.HitchhikerService;
import com.cgm.assignment.hitchhikersguide.service.HitchhikerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.lang.System.out;

@Profile("!test")
@Component
public class MyRunner implements CommandLineRunner {
    private static final HitchhikerService hitchhikerService = new HitchhikerServiceImpl();
    private static final String OPERATIONS_LIST = "\nPlease choose the operation from list \n" +
            "To add question and answer type: 1 \n" +
            "To find the answer to your question type: 2 \n" +
            "To exit application type: 0 \n";
    private static final String INVALID_INPUT = "\nInvalid input value/format, Please try again with correct value";
    private static final String INVALID_SELECTION = "\nInvalid operation \" %s \". please type the correct value";
    private static final String NOTE = "\nPLEASE NOTE:\n";
    private static final String CHAR_LIMIT_QUESTION = "•\tA Question is a String with max 255 chars\n";
    private static final String CHAR_LIMIT_ANSWER = "•\tAn Answer is a String with max 255 chars\n";
    private static final String QUESTION_FORMAT = "\nPlease type your question in following format" +
            "•\t<question>? here Char “?” is the identifier of question";
    private static final String QNA_FORMAT = "\nPlease type your question and answer in following format: \n" +
            "•\t<question>? “<answer1>” “<answer2>” “<answerX>”\n" +
            "•\tEvery Question needs to have at least one answer but can have unlimited answers all inside of char “ \n" +
            "•\tSpecial character not allowed except('),(.)(’)“ \n";
    private static final String ADD_QNA_SUCCESS = "\nAnswer to the question is added in the system successfully";
    private static final String QNA_REGEX = "^[a-zA-Z0-9 '.’]{1,255}\\?( \"[a-zA-Z0-9 '.’]{1,255}\")+$";
    private static final String QUESTION_REGEX = "^[a-zA-Z0-9 '.’]{1,255}\\?";

    @Override
    public void run(String... args) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(System.in); BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            operations(bufferedReader);
        } catch (IOException e) {
            out.println("Technical error. Please contact system administrator");
        }

    }

    private void operations(BufferedReader bufferedReader) throws IOException{
        do {
            out.println(OPERATIONS_LIST);
            try {
                int inputOperation = Integer.parseInt(bufferedReader.readLine());
                if (inputOperation == 1) {
                    addQnA(bufferedReader);
                } else if (inputOperation == 2) {
                    getAnswer(bufferedReader);
                } else if (inputOperation == 0) {
                    break;
                } else {
                    out.println(String.format(INVALID_SELECTION, inputOperation));
                }
            } catch (NumberFormatException | InputMismatchException ex) {
                out.println(INVALID_INPUT);
            } catch (InvalidInput ex) {
                out.println(ex.getMessage());
            }
        } while (true);
    }

    /**
     * add question and answer opration
     *
     * @param reader buffered reader
     * @throws IOException if read failed
     */
    private void addQnA(BufferedReader reader) throws IOException {
        out.println(NOTE + CHAR_LIMIT_QUESTION + CHAR_LIMIT_ANSWER);
        out.println(QNA_FORMAT);
        Map<String, List<String>> qNa = splitQnA(reader.readLine());
        qNa.forEach(hitchhikerService::addAnswer);
        out.println(ADD_QNA_SUCCESS);
    }

    /**
     * print answers to the question
     *
     * @param reader buffered reader
     * @throws IOException if read failed
     */
    private void getAnswer(BufferedReader reader) throws IOException {
        out.println(NOTE + CHAR_LIMIT_QUESTION);
        out.println(QUESTION_FORMAT);
        String question = reader.readLine();
        if (validateQuestion(question)) {
            hitchhikerService.getAnswer(question).forEach(s -> out.println("•\t" + s));
        } else {
            throw new InvalidInput(INVALID_INPUT);
        }
    }

    /**
     * validate question & answer Strings
     *
     * @param qNa question & answer string
     * @return boolean decision
     */
    boolean validateQnA(String qNa) {
        return Optional.ofNullable(qNa).map(s -> Pattern.matches(QNA_REGEX, s))
                .orElse(false);
    }

    /**
     * validate length & required character
     *
     * @param question string
     * @return boolean decision
     */
    boolean validateQuestion(String question) {
        return Optional.ofNullable(question).map(s -> Pattern.matches(QUESTION_REGEX, s))
                .orElse(false);
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
            List<String> answers = List.of(data[1].trim().substring(1, data[1].length() - 2).split("\" \""));
            return Map.of(data[0]+"?", answers);
        } else {
            throw new InvalidInput(INVALID_INPUT);
        }
    }
}