package com.cgm.assignment.hitchhikersguide.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@ExtendWith(SpringExtension.class)
class HitchhikerServiceImplTests {

    private final HitchhikerService hitchhikerService= new HitchhikerServiceImpl();


    @Test
    void serviceTest() {
        String question= "what?";
        List<String> answers = List.of("Answer1","Answer2","Answer3");
        hitchhikerService.addAnswer(question, answers);
        assertTrue("has 3 answers to the question",hitchhikerService.getAnswer(question).size()==3);
        assertTrue("has matching answers to the question",hitchhikerService.getAnswer(question).stream().allMatch(s -> answers.stream().anyMatch(s1 -> s1.equals(s))));
    }

    @Test
    void serviceTestNoAddedQuestion() {
        String question= "what is?";
        assertTrue("has matching answers to the question",hitchhikerService.getAnswer(question).stream().allMatch(s -> s.equals("the answer to life, universe and everything is 42")));
    }

}
