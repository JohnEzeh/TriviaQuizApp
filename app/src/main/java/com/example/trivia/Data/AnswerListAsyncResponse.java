package com.example.trivia.Data;

import com.example.trivia.Model.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void proccessFinished(ArrayList<Question> questionArrayList);
}
