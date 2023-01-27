package com.example.dodamdodam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dodamdodam.R;

import com.example.dodamdodam.activity.Question.QuestionListObject;

import java.util.ArrayList;

public class QuestionAdapter  extends RecyclerView.Adapter<QuestionAdapter.QuetionViewHolder>{

    private ArrayList<QuestionListObject> arrayList;

    public QuestionAdapter(ArrayList<QuestionListObject> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public QuestionAdapter.QuetionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_list, parent, false);
        QuetionViewHolder holder = new QuetionViewHolder(view);



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.QuetionViewHolder holder, int position) {
        holder.tv_ques.setText(arrayList.get(position).getQues());
        holder.tv_ans1.setText(arrayList.get(position).getAns1());
        holder.tv_ans2.setText(arrayList.get(position).getAns2());


    }

    @Override
    public int getItemCount() {
        return (null !=arrayList ? arrayList.size() : 0);
    }

    public class QuetionViewHolder extends RecyclerView.ViewHolder {

        protected TextView tv_ques;
        protected TextView tv_ans1;
        protected TextView tv_ans2;

        public QuetionViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_ques=(TextView) itemView.findViewById(R.id.tv_ques);
            this.tv_ans1=(TextView) itemView.findViewById(R.id.tv_ans1);
            this.tv_ans2=(TextView) itemView.findViewById(R.id.tv_ans2);
        }
    }
}