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

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private ArrayList<QuestionListObject> arrayList;
    private Context context;

    public QuestionAdapter(ArrayList<QuestionListObject> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_list,parent,false);
        QuestionViewHolder holder =new QuestionViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        holder.tv_ques.setText(arrayList.get(position).getQues());
        holder.tv_ans1.setText(arrayList.get(position).getAns1());
        holder.tv_ans2.setText(arrayList.get(position).getAns2());
    }

    @Override
    public int getItemCount() {
        return (arrayList !=null ? arrayList.size() : 0);
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ques;
        TextView tv_ans1;
        TextView tv_ans2;
        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_ques=itemView.findViewById(R.id.tv_ques);
            this.tv_ans1=itemView.findViewById(R.id.tv_ans1);
            this.tv_ans2=itemView.findViewById(R.id.tv_ans2);

        }
    }
}
