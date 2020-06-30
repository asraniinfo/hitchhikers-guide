package com.cgm.assignment.hitchhikersguide.service;

import java.util.List;

public interface HitchhikerService {
    List<String> getAnswer(String question);
    void addAnswer(String question, List<String> answers);
}
