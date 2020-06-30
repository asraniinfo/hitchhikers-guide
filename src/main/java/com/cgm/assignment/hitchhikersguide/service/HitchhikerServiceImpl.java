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
        final List<String> strings;
        if (null != qNaMap.get(question)) {
            strings = qNaMap.get(question);
        } else strings = List.of(ANSWER_TO_EVERYTHING);
        return strings;
    }

    @Override
    public void addAnswer(String question, List<String> answer) {
        qNaMap.put(question,answer);
    }
}
