//-------------------------------------------------------------------------------//
// QuizWiz App: PlayQuiz Class
// To provide list of available quizzes to user and show details of selected quiz
// Design & Development: ADITEY NANDAN (Class: IX-H, DPS NOIDA)
// Last Update: August 03, 2020
// All Rights Reserved. Contact nandan.aditey@gmail.com before using the code -
// in any manner. No liability for any problems while using this code.
//-------------------------------------------------------------------------------//

package com.example.quizzo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class PlayQuiz extends AppCompatActivity {
    String sQuizListFile = "QuizList.csv";
    String SelectedQuizFile = "";
    String[] UserDataArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_quiz);

        //Instantiate Spinner UI element
        Spinner spQlist = findViewById(R.id.spQuizList);

        //Call function to read the list of quizzes in QuizList.csv file
        Context context = getApplicationContext();
        String TmpString = ReadUserDataFile(context, sQuizListFile);

        //Parse the read user data string into an array
        String[] UserDataArrayOrg = TmpString.split(",");
        //Copy in another array while removing the last item which is blank
        UserDataArray = Arrays.copyOf(UserDataArrayOrg, UserDataArrayOrg.length-1);
        //Sort the array in alphabetical order
        Arrays.sort(UserDataArray);

        ArrayAdapter<String> spinnerArrayAdapter;
        spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, UserDataArray);
        spQlist.setAdapter(spinnerArrayAdapter);

        spQlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedQuizFile = UserDataArray[position] + ".csv";

                //Call function to Read corresponding Quiz file
                Context context = getApplicationContext();
                String TmpString = ReadUserDataFile(context, SelectedQuizFile);

                //Instantiate other remaining UI elements
                TextView tvQname = findViewById(R.id.tvQuizName);
                TextView tvQtopic = findViewById(R.id.tvQuizTopic);
                TextView tvQnum = findViewById(R.id.tvQuesNum);
                TextView tvQtbound = findViewById(R.id.tvTimeBound);
                TextView tvQtime = findViewById(R.id.tvTotalTime);

                //Parse the read user data string into an array
                String[] QuizParams = TmpString.split(",");
                tvQname.setText("Quiz Name: " + QuizParams[0]);
                tvQtopic.setText("Quiz Topic: " + QuizParams[1]);
                tvQnum.setText("Number Of Questions: " + QuizParams[2]);
                if (QuizParams[3].matches("yes")) {
                    tvQtbound.setText("Timed Quiz: " + QuizParams[3]);
                    tvQtime.setText("Stipulated Time: " + QuizParams[4] + " Minutes");
                } else {
                    tvQtbound.setText("Timed Quiz: No");
                    tvQtime.setText("Stipulated Time: None");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void OnPlayQuizBtnClick(View v) {
        //Create an intent to pass Quiz title and Number of questions to RecordQuestions activity
        Intent intentVarPass = new Intent(PlayQuiz.this, AnswerQuiz.class);
        intentVarPass.putExtra("QuizFile", SelectedQuizFile);
        startActivity(intentVarPass);
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
}