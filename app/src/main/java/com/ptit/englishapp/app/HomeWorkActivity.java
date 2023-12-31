package com.ptit.englishapp.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.ptit.englishapp.R;
import com.ptit.englishapp.model.DatabaseAccess;
import com.ptit.englishapp.model.Word;

public class HomeWorkActivity extends LessonActivity {

    private String topic;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void getView() {
        setContentView(R.layout.activity_home_work);
        btnCheck = findViewById(R.id.buttonCheck);
        tatResult = findViewById(R.id.txtRsQuestion);
        txtCorrectAnswer = findViewById(R.id.txtCorrectAnswer);
        ctlPanel = findViewById(R.id.ctrPanel);
        prgTask = findViewById(R.id.task_progress_bar);
        getActionBar();
        LoadData();
    }

    protected void LoadData() {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        topic = databaseAccess.getLastLearnedTopic();
        list = databaseAccess.GetTopic(topic);
        databaseAccess.close();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    public void onCheck(View view) {
        if (btnCheck.getText().toString().equals("Kiểm tra")) {
            btnCheck.setText("TIẾP TỤC");
            QuestionTypeAFragment fragment = (QuestionTypeAFragment) getSupportFragmentManager().findFragmentById(R.id.fragment4);
            String answer = fragment.getAnswer();
            if (answer.equals(currentAnswer)) {
                tatResult.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.correct));
                ctlPanel.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.bg_correct));
                tatResult.setText("Chính xác");
                index++;
            } else {
                ctlPanel.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.bg_incorrect));
                tatResult.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.incorrect));
                btnCheck.setBackgroundResource(R.drawable.incorrect_button);
                tatResult.setText("Không chính xác");
                txtCorrectAnswer.setText("Đáp án đúng là: " + currentAnswer);
                Word tmp = list.get(index);
                list.remove(index);
                list.add(tmp);
            }
        } else {
            btnCheck.setText("Kiểm tra");
            btnCheck.setBackgroundResource(R.drawable.correct_button);
            tatResult.setText(null);
            txtCorrectAnswer.setText(null);
            ctlPanel.setBackgroundColor(Color.argb(0, 0, 0, 0));
            if (index == 20) {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                databaseAccess.open();
                databaseAccess.SaveDate(topic);
                databaseAccess.close();
                Intent intent = new Intent(this, TopicActivity.class);
                startActivity(intent);
            } else {
                prgTask.setProgress(index);
                CreateQuestion();
            }
        }
    }

}
