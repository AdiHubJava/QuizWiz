//-------------------------------------------------------------------------------//
// QuizWiz App: MakeQuiz Class
// To capture quiz details from user while making a quiz
// Design & Development: ADITEY N (DPS NOIDA)
// Last Update: August 03, 2020
// All Rights Reserved. Contact "pro.aditey@gmail.com" before using the code -
// in any manner. No liability for any problems while using this code.
//-------------------------------------------------------------------------------//

package com.example.quizzo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MakeQuiz extends AppCompatActivity {
    String quizName, quizTopic, quesCount, checkTime, quizTime;
    String sQuizListFile = "QuizList.csv";
    String CsvFileName = "";
    String sQuizDetails = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_quiz);
    }

    public void timeSet(View view) {
        EditText timeEdit = findViewById(R.id.editText4);
        CheckBox checky = findViewById(R.id.checkboxTime);
        if (checky.isChecked()) {
            timeEdit.setVisibility(View.VISIBLE);
            checkTime = "yes";
        } else {
            timeEdit.setVisibility(View.INVISIBLE);
            checkTime = "no";
        }
    }

    public void buttonSubmitPress(View view) {
        boolean MakeBtnClickable = false;
        Button buttonS = findViewById(R.id.btnSubmit);

        EditText QuizName = findViewById(R.id.editText1);
        EditText QuizTopic = findViewById(R.id.editText2);
        EditText NoQuestions = findViewById(R.id.editText3);
        EditText timeEdit = findViewById(R.id.editText4);
        quizName = QuizName.getText().toString();
        quizTopic = QuizTopic.getText().toString();
        quesCount = NoQuestions.getText().toString();
        quizTime = timeEdit.getText().toString();

        if ((quizName.length() != 0) && (quizTopic.length() != 0) && (quesCount.length() != 0)) {
            if (checkTime != null) {
                if (quizTime.length() != 0) {
                    MakeBtnClickable = true;
                }
            } else if (checkTime == null) {
                MakeBtnClickable = true;
                quizTime = "NONE";
            }
        }

        if (MakeBtnClickable == true) {
            buttonS.isClickable();
            quizName = AdiUtil.fReplaceCharInString(quizName, ",", ";");
            CsvFileName = quizName + ".csv";
            quizTopic = AdiUtil.fReplaceCharInString(quizTopic, ",", ";");
            sQuizDetails = quizName + "," + quizTopic + "," + quesCount + "," + checkTime + "," + quizTime;

            //Check if the quiz by the name already exists
            File file = getBaseContext().getFileStreamPath(CsvFileName);
            if (file.exists()) {
                //Show a warning message to user
                Toast.makeText(getApplicationContext(), "A quiz with this name already exists. It will be overwritten.", Toast.LENGTH_LONG).show();

                //Overwrite the CSV File and Record Quiz Details
                WriteUserDataFile(CsvFileName, sQuizDetails, true);
            } else {
                //Add the name of quiz to sQuizListFile
                WriteUserDataFile(sQuizListFile, quizName, false);
                //Create a CSV File and Record Quiz Details
                WriteUserDataFile(CsvFileName, sQuizDetails, true);
            }
            //Create an intent to pass Quiz title and Number of questions to RecordQuestions activity
            Intent intentVarPass = new Intent(MakeQuiz.this, RecordQuestions.class);
            intentVarPass.putExtra("quizName", quizName);
            intentVarPass.putExtra("quizCount", quesCount);
            startActivity(intentVarPass);
        }
        else{
            ShowMessageToUser("QuizWiz Warning >>> ", "Enter quiz details before you click Submit button!");
        }
    }

    public void WriteUserDataFile(String FileName, String ToWrite, Boolean OverWrite) {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir(getFilesDir().getName(), ContextWrapper.MODE_PRIVATE);
        File file = new File(directory, FileName);
        String userData = ToWrite + ",";

        FileOutputStream outputStream;
        try {
            if (OverWrite == false) {
                outputStream = openFileOutput(FileName, Context.MODE_APPEND);
            } else {
                outputStream = openFileOutput(FileName, Context.MODE_PRIVATE);
            }
            outputStream.write(userData.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
