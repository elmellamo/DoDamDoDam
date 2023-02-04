package com.example.dodamdodam.activity.Question;

public class QuestionListObject {

    private String ques;
    private String ans1;
    private String ans2;
    private String list_num;

    public QuestionListObject(String ques, String ans1, String ans2,String list_num) {
        this.ques = ques;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.list_num=list_num;
    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;}

    public String getAns1() {
        return ans1;
    }

    public void setAns1(String ans1) {
        this.ans1 = ans1;
    }

    public String getAns2() {
        return ans2;
    }

    public void setAns2(String ans2) {
        this.ans2 = ans2;
    }

    public String getList_num() {
        return list_num;
    }

    public void setList_num(String list_num) {
        this.list_num = list_num;
    }
}