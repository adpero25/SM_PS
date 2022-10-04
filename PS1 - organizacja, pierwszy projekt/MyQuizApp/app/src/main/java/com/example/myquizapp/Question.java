package com.example.myquizapp;

public class Question {
    private int questionId;
    private boolean trueAnswer;

    public Question(int _questionId, boolean _trueAnswer) {
        this.questionId = _questionId;
        this.trueAnswer = _trueAnswer;
    }

    public boolean isTrueAnswer() {
        return trueAnswer;
    }

    public int getQuestionId() {
        return questionId;
    }
}
