//-------------------------------------------------------------------------------//
// QuizWiz App: MainActivity Class
// To present two options to user: Make Quiz or Play Quiz, Make a sample quiz
// Design & Development: ADITEY NANDAN (Class: IX-H, DPS NOIDA)
// Last Update: August 03, 2020
// All Rights Reserved. Contact nandan.aditey@gmail.com before using the code -
// in any manner. No liability for any problems while using this code.
//-------------------------------------------------------------------------------//

package com.example.quizzo;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickPlayQuiz(View view) {
        //Check if QuizList.csv file exists or not
        String sQuizListFile = "QuizList.csv";
        File file = getBaseContext().getFileStreamPath(sQuizListFile);
        if(file.exists()){
            //Intent to move to PlayQuiz activity
        } else {
            //Make a sample GK Quiz
            //Add the name of quiz to sQuizListFile
            WriteUserDataFile(sQuizListFile, "Sample Quiz", true);

            //Create a CSV File and Record Quiz Details
            String CsvFileName = "Sample Quiz.csv";
            String sQuizDetails = "Sample Quiz,General Knowledge,10,yes,5,Which country agreed to accept rupee instead of dollar as payment for oil products to India?,Kuwait,UAE,Iran,Iraq,C,Which organization decided to cancel 2G spectrum licenses issued by the government?,CAG of India,Supreme Court,TRAI,CII,B,Which nation approved a new constitution in a referendum through secret voting held in Feb. 2012?,Libya,UAE,Iraq,Syria,D,Sensitive index of National Stock Exchange of India is popularly known as,NIFTY,SENSEX,CRIS,CSE,A,Proceeds from disinvestment of various public-sector undertaking are channelized into,Rural Innovation Fund,National Investment Fund,National Growth Fund,RBI,B,World Day for Water is observed on which of the following dates?,22 March,22 April,22 July,22 October,A,Which country announced to freeze its nuclear program to avail food aid from US and others?,Pakistan,Iran,Syria,North Korea,D,NASA's Curiosity rover successfully landed on,Moon,Mars,Mercury,Venus,B,Which of the following is not correctly matched?,N Chandra - Tata,Sheryl Sandberg - Facebook,Larry Page - Google,Deepak Parekh - HFCL,D,Which of the following does not produce hormone?,Heart,Kidney,Esophagus,None of these,D";

            WriteUserDataFile(CsvFileName, sQuizDetails, true);
        }

        //Intent to move to PlayQuiz activity
        Intent intent = new Intent(MainActivity.this, PlayQuiz.class);
        startActivity(intent);
        finish();
    }


    public void onClickMakeQuiz(View view) {
        //Intent to move to MakeQuiz activity
        Intent intent = new Intent(MainActivity.this, MakeQuiz.class);
        startActivity(intent);
        finish();
    }

    private void ShowMessageToUser(String sTitle, String sMessage){
        Log.d("QuizWiz" + sTitle, sMessage);

        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
        messageBox.setTitle(sTitle);
        messageBox.setMessage(sMessage);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
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
}