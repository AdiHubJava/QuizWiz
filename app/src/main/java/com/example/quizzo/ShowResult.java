//-------------------------------------------------------------------------------//
// QuizWiz App: QuizzoPreview Class
// To show result to user at the end of playing a quiz
// Design & Development: ADITEY N (DPS NOIDA)
// Last Update: August 03, 2020
// All Rights Reserved. Contact "pro.aditey@gmail.com" before using the code -
// in any manner. No liability for any problems while using this code.
//-------------------------------------------------------------------------------//

package com.example.quizzo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ShowResult extends AppCompatActivity {
    int TotalQ = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);

        //Get intent from AnswerQuiz activity
        Bundle myBundle = getIntent().getExtras();

        String QtimeUp = myBundle.getString("TimeUp");
        String QuizTitle = myBundle.getString("QuizTitle");
        String QuesUnsolved = myBundle.getString("QuesUnsolved");
        String QuesSolved = myBundle.getString("QuesSolved");
        String QuesRight = myBundle.getString("QuesRight");
        String QuesWrong = myBundle.getString("QuesWrong");

        if (QtimeUp.matches("Yes")) {
            ShowMessageToUser("Alert", "The time for your quiz is over! See Result.");
            QtimeUp = "No";
        }

        if ((QuesUnsolved != null) && (QuesSolved != null)) {
            TotalQ = Integer.parseInt(QuesUnsolved) + Integer.parseInt(QuesSolved);
        }

        //Define text views
        TextView tvQzTitle = findViewById(R.id.tvQuizTitle);
        TextView tvQzTotal = findViewById(R.id.tvTotalQ);
        TextView tvQzAttempted = findViewById(R.id.tvAttempted);
        TextView tvQzUnattempted = findViewById(R.id.tvUnattempted);
        TextView tvQzRight = findViewById(R.id.tvRight);
        TextView tvQzWrong = findViewById(R.id.tvWrong);

        //Populate UI elements
        tvQzTitle.setText(QuizTitle + " Result");
        tvQzTotal.setText("Total Questions: " + String.valueOf(TotalQ));
        tvQzAttempted.setText("Questions Attempted: " + QuesSolved);
        tvQzUnattempted.setText("Questions Not Attempted: " + QuesUnsolved);
        tvQzRight.setText("Correct Answers: " + QuesRight);
        tvQzWrong.setText("Incorrect Answers: " + QuesWrong);
    }

    public void btnGoToMainPage(View v) {
        Intent intentGoBack = new Intent(ShowResult.this, MainActivity.class);
        startActivity(intentGoBack);
    }

    private void ShowMessageToUser(String sTitle, String sMessage) {
        Log.d("QuizWiz" + sTitle, sMessage);

        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
        messageBox.setTitle(sTitle);
        messageBox.setMessage(sMessage);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }
}
