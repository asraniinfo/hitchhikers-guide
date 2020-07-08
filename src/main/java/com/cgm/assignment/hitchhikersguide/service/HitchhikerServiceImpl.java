package com.cgm.assignment.hitchhikersguide.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HitchhikerServiceImpl implements  HitchhikerService{
    private static final String ANSWER_TO_EVERYTHING ="the answer to life, universe and everything is 42";
    private static final Map<String, List<String>> qNaMap= new HashMap<>();

    @Override
    public List<String> getAnswer(String question) {
        return Optional.ofNullable(qNaMap.get(question))
                .orElse(List.of(ANSWER_TO_EVERYTHING));
    }

    @Override
    public void addAnswer(String question, List<String> answers) {
        qNaMap.put(question,answers);
    }
}
