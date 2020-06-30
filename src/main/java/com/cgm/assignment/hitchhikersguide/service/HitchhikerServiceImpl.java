package com.cgm.assignment.hitchhikersguide.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HitchhikerServiceImpl implements  HitchhikerService{
    private static final String ANSWER_TO_EVERYTHING ="the answer to life, universe and everything is 42";
    private static final Map<String, List<String>> qNaMap= new HashMap<>();

    @Override
    public List<String> getAnswer(String question) {
        final List<String> answers;
        if (null != qNaMap.get(question)) {
            answers = qNaMap.get(question);
        } else answers = List.of(ANSWER_TO_EVERYTHING);
        return answers;
    }

    @Override
    public void addAnswer(String question, List<String> answers) {
        qNaMap.put(question,answers);
    }
}
