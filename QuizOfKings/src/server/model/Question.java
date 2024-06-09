package server.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {

    private String type;

    private String question;

    private String answer;

    private ArrayList<String> tests;

    public Question(String type, String question, ArrayList<String> tests, String answer) {
        this.type = type;
        this.question = question;
        this.tests = tests;
        this.answer =answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getTests() {
        return tests;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "*********************\n"+
                type+"\n"+
                question+"\n"+
                "1."+tests.get(0)+"\n"+
                "2."+tests.get(1)+"\n"+
                "3."+tests.get(2)+"\n"+
                "4."+tests.get(3)+"\n"+
                "********************";
    }
}
