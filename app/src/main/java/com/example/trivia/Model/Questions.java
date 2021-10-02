package com.example.trivia.Model;

import androidx.annotation.NonNull;

public class Questions {
    private String answer;
    private boolean ansTrue;

    public Questions() {
    }

    public Questions(String answer, boolean ansTrue) {
        this.answer = answer;
        this.ansTrue = ansTrue;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isAnsTrue() {
        return ansTrue;
    }

    public void setAnsTrue(boolean ansTrue) {
        this.ansTrue = ansTrue;
    }

//    @Override
//    public String toString() {
//        return "Questions{" +
//                "answer='" + answer + '\'' +
//                ", ansTrue=" + ansTrue +
//                '}';
   // }
}
