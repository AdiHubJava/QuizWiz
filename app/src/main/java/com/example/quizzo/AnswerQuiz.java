//-------------------------------------------------------------------------------//
// QuizWiz App: AnswerQuiz Class
// To present questions to users and to take user's answer
// Design & Development: ADITEY NANDAN (Class: IX-H, DPS NOIDA)
// Last Update: August 03, 2020
// All Rights Reserved. Contact nandan.aditey@gmail.com before using the code -
// in any manner. No liability for any problems while using this code.
//-------------------------------------------------------------------------------//

package com.example.quizzo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class AnswerQuiz extends AppCompatActivity {
    String QuizFile = "";
    String CorrAns = "";
    String QuizTitle = "";
    String TimeUp = "";
    int QuesSolved = 0;
    int QuesUnsolved = 0;
    int QuesRight = 0;
    int QuesWrong = 0;
    int ArrIndex = 5;
    String AnsByUser = "";
    int TotalQuestions = 0;
    int TimerVal = 0;
    boolean ApplyTimer = false;
    int QuesNum = 0;
    String[] QuesParams;
    TextView tvQTimer;
    private static long START_TIME_IN_MILLIS;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_quiz);

        //Make a Spinner for Answer to be selected
        Spinner spOptions = findViewById(R.id.spAnswer);
        String answers[] = {"", "A", "B", "C", "D"};
        ArrayAdapter<String> spinnerArrayAdapter;
        spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, answers);
        spOptions.setAdapter(spinnerArrayAdapter);

        //Get intent from PlayQuiz activity
        //Get Quiz Parameters from MakeQuiz Java file
        Bundle myBundle = getIntent().getExtras();
        if (myBundle != null) {
            QuizFile = myBundle.getString("QuizFile");
            QuizTitle = QuizFile.substring(0, QuizFile.length() - 4);
        }

        //Read the QuizFile to populate the UI
        Context context = getApplicationContext();
        String TmpString = ReadUserDataFile(context, QuizFile);

        //Instantiate other remaining UI elements
        tvQTimer = findViewById(R.id.tvTimer);
        TextView tvQsNum = findViewById(R.id.tvQnum);
        TextView tvQTitle = findViewById(R.id.tvQuizTitle);
        TextView tvQText = findViewById(R.id.tvQuesText);
        TextView tvOA = findViewById(R.id.tvOptA);
        TextView tvOB = findViewById(R.id.tvOptB);
        TextView tvOC = findViewById(R.id.tvOptC);
        TextView tvOD = findViewById(R.id.tvOptD);

        //Fetch all data from TmpString, extract first question of the quiz (from QuesParams array -
        // Index 5 to 10) and populate UI elements, Store 10th item (answer) in a variable
        QuesParams = TmpString.split(",");
        TotalQuestions = Integer.parseInt(QuesParams[2]);

        tvQTitle.setText(QuizTitle);
        tvQsNum.setText("Q-1" + "/" + TotalQuestions);
        tvQText.setText(QuesParams[ArrIndex]);
        ArrIndex++;
        tvOA.setText("A: " + QuesParams[ArrIndex]);
        ArrIndex++;
        tvOB.setText("B: " + QuesParams[ArrIndex]);
        ArrIndex++;
        tvOC.setText("C: " + QuesParams[ArrIndex]);
        ArrIndex++;
        tvOD.setText("D: " + QuesParams[ArrIndex]);
        ArrIndex++;
        CorrAns = QuesParams[ArrIndex];
        ArrIndex++;

        //Handle 'Next' button click and augment QuesSolved, QuesRight & QuesWrong
        //Method: btnGoToNextQues

        //Implement Timer
        if (QuesParams[3].matches("yes")) {
            ApplyTimer = true;
        } else {
            TimeUp = "Null";
        }

        if (ApplyTimer == true) {
            TimerVal = Integer.parseInt(QuesParams[4]);
            START_TIME_IN_MILLIS = TimerVal * 60000;
            mTimeLeftInMillis = START_TIME_IN_MILLIS;

            if (mTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }

            updateCountDownText();
        }
    }

    public void btnGoToNextQues(View v) {
        TextView tvQsNum = findViewById(R.id.tvQnum);
        TextView tvQText = findViewById(R.id.tvQuesText);
        TextView tvOA = findViewById(R.id.tvOptA);
        TextView tvOB = findViewById(R.id.tvOptB);
        TextView tvOC = findViewById(R.id.tvOptC);
        TextView tvOD = findViewById(R.id.tvOptD);
        Spinner spUserAns = findViewById(R.id.spAnswer);

        AnsByUser = spUserAns.getSelectedItem().toString();
        spUserAns.setSelection(0);

        //Handle correct / inccorect / no answer by the user
        if (AnsByUser.matches("") || (AnsByUser == null)) {
            QuesUnsolved++;
        } else if (AnsByUser.matches(CorrAns)) {
            QuesSolved++;
            QuesRight++;
        } else {
            QuesSolved++;
            QuesWrong++;
        }

        //Move to nex question
        QuesNum++;

        // If QuesNum is less than TotalQuestions then prepare UI for next question
        // otherwise, move to next activity - ShowResult
        if (QuesNum < TotalQuestions) {
            int txtQnum = QuesNum + 1;

            //Populate next question in the UI
            tvQsNum.setText("Q-" + String.valueOf(txtQnum) + "/" + TotalQuestions);
            tvQText.setText(QuesParams[ArrIndex]);
            ArrIndex++;
            tvOA.setText("A: " + QuesParams[ArrIndex]);
            ArrIndex++;
            tvOB.setText("B: " + QuesParams[ArrIndex]);
            ArrIndex++;
            tvOC.setText("C: " + QuesParams[ArrIndex]);
            ArrIndex++;
            tvOD.setText("D: " + QuesParams[ArrIndex]);
            ArrIndex++;
            CorrAns = QuesParams[ArrIndex];
            ArrIndex++;

            if (txtQnum == TotalQuestions){
                Button btnSubmit = findViewById(R.id.btnPlayNext);
                btnSubmit.setText("Submit");
            }
        } else {
            //Kill the countdown timer

            //Prep and move to ShowResult activity
            //Create an intent to pass Quiz title and Number of questions to RecordQuestions activity
            InvokeIntentToGoToResults();
        }
    }

    //Method to implement intent - for going to Show Result
    public void InvokeIntentToGoToResults() {
        Intent intentResult = new Intent(AnswerQuiz.this, ShowResult.class);

        intentResult.putExtra("QuizTitle", QuizTitle);
        intentResult.putExtra("QuesSolved", String.valueOf(QuesSolved));
        intentResult.putExtra("QuesRight", String.valueOf(QuesRight));
        intentResult.putExtra("QuesWrong", String.valueOf(QuesWrong));

        if (mTimerRunning) {
            TimeUp = "No";

            intentResult.putExtra("TimeUp", TimeUp);
            intentResult.putExtra("QuesUnsolved", String.valueOf(QuesUnsolved));
        } else {
            if (ApplyTimer == true){
                TimeUp = "Yes";
            }
            intentResult.putExtra("TimeUp", TimeUp);
            intentResult.putExtra("QuesUnsolved", String.valueOf(TotalQuestions - QuesSolved));
        }

        if (ApplyTimer == true) {
            mCountDownTimer.cancel();
            mTimeLeftInMillis = 0;
            mTimerRunning = false;
        }

        startActivity(intentResult);
    }

    //Function for Reading the User Data CSV File
    public String ReadUserDataFile(Context context, String FileToRead) {
        try {
            FileInputStream fis = context.openFileInput(FileToRead);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader r = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            return "Error: File Not Found";
        } catch (UnsupportedEncodingException e) {
            return "Error: Unsupported Encoding";
        } catch (IOException e) {
            return "Error: IO Exception";
        }
    }

    // Timer Function Code
    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
            }
        }.start();
        mTimerRunning = true;
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tvQTimer.setText(timeLeftFormatted);

        //Make timer text red if 1/4 of stipualted time is left
        tvQTimer = findViewById(R.id.tvTimer);
        if (mTimeLeftInMillis <= (START_TIME_IN_MILLIS / 4)) {
            tvQTimer.setTextColor(Color.parseColor("#FFE13535"));
        }

        //if time left is less than 1 sec, move to ShowResult
        if (mTimeLeftInMillis < 1000) {
            mTimerRunning = false;
            Toast TimeUpToast = Toast.makeText(getApplicationContext(), "Time Up!", Toast.LENGTH_LONG);
            TimeUpToast.show();

            //Go to Show Result Activity
            InvokeIntentToGoToResults();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("millisLeft", mTimeLeftInMillis);
        outState.putBoolean("timerRunning", mTimerRunning);
        outState.putLong("endTime", mEndTime);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTimeLeftInMillis = savedInstanceState.getLong("millisLeft");
        mTimerRunning = savedInstanceState.getBoolean("timerRunning");
        updateCountDownText();

        if (mTimerRunning) {
            mEndTime = savedInstanceState.getLong("endTime");
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            startTimer();
        }
    }
}
