package com.dodamdodam.dodamdodam.activity.Question;

public class QuestionListObject {

    private String ques;
    private String list_num;

    public QuestionListObject(String ques,String list_num) {
        this.ques = ques;
        this.list_num=list_num;
    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;}


    public String getList_num() {
        return list_num;
    }

    public void setList_num(String list_num) {
        this.list_num = list_num;
    }
}