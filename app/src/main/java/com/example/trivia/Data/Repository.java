package com.example.trivia.Data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trivia.Controller.AppController;
import com.example.trivia.Model.Question;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    ArrayList<Question> questionArrayList = new ArrayList<>();
    String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json\n" +
            "\n";

    public List<Question> getQuestions(final AnswerListAsyncResponse callback){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET
                , url, null, response -> {

            for (int i = 0; i < response.length() ; i++) {
                try {

                    Question question = new Question(response.getJSONArray(i).get(0).toString(),
                            response.getJSONArray(i).getBoolean(1) );

                    //add question to question arraylist
                    questionArrayList.add(question);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (null != callback) callback.proccessFinished(questionArrayList);

        }, error -> {
            Log.d("TAG", "Oncreate: failed");
        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionArrayList;
    }
}
