package com.josycom.mayorjay.flowoverstack.model;

import java.util.List;

public class SearchResponse {

    public String networkState;
    public List<Question> questions;

    public SearchResponse(String networkState, List<Question> questions) {
        this.networkState = networkState;
        this.questions = questions;
    }
}
