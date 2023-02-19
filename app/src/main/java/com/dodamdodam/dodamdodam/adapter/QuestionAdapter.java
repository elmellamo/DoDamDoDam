package com.dodamdodam.dodamdodam.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dodamdodam.dodamdodam.R;

import com.dodamdodam.dodamdodam.activity.Question.AnswerPop;
import com.dodamdodam.dodamdodam.activity.Question.QuestionListObject;

import java.util.ArrayList;

public class QuestionAdapter  extends RecyclerView.Adapter<QuestionAdapter.QuetionViewHolder>{

    private ArrayList<QuestionListObject> arrayList;
    private Context mContext;

    public QuestionAdapter(Context context,ArrayList<QuestionListObject> arrayList) {
        this.arrayList = arrayList;
        this.mContext=context;
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
        holder.tv_list_num.setText("#"+arrayList.get(position).getList_num());

    }

    @Override
    public int getItemCount() {
        return (null !=arrayList ? arrayList.size() : 0);
    }

    public class QuetionViewHolder extends RecyclerView.ViewHolder {

        protected TextView tv_ques;
        protected TextView tv_list_num;
        public QuetionViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_ques=(TextView) itemView.findViewById(R.id.tv_ques);
            this.tv_list_num=(TextView) itemView.findViewById(R.id.tv_list_num);
            /*
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=getAdapterPosition()+1;
                    if(pos!=RecyclerView.NO_POSITION){
                        Intent intent=new Intent(mContext,AnswerPop.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("List_Num",Integer.toString(pos));
                        mContext.startActivity(intent);
                    }
                }
            });

             */
        }
    }

}