package com.example.trivia.data;

import android.util.Log;


import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trivia.Controller.AppController;
import com.example.trivia.Model.Questions;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    ArrayList<Questions> questionsArrayList = new ArrayList<>();
    String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Questions> getQuestions(final Queslistresponce callBack){


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            Questions questions = new Questions(response.getJSONArray(i).get(0).toString(),
                                    response.getJSONArray(i).getBoolean(1));
                            questionsArrayList.add(questions);
                            // Log.d("Main", "getQuestions: " + questions.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    };

                    if (null!= callBack) callBack.processFinished(questionsArrayList);
                      },
                error -> Log.d("TAG", error.getMessage()));

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionsArrayList;
    }
}
