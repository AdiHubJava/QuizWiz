//-------------------------------------------------------------------------------//
// QuizWiz App: RecordQuestions Class
// To capture user's inputs while making a quiz, one question at a time
// Design & Development: ADITEY NANDAN (Class: IX-H, DPS NOIDA)
// Last Update: August 03, 2020
// All Rights Reserved. Contact nandan.aditey@gmail.com before using the code -
// in any manner. No liability for any problems while using this code.
//-------------------------------------------------------------------------------//

package com.example.quizzo;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class RecordQuestions extends AppCompatActivity implements Button.OnClickListener {
    int iNextCount = 1;
    String sQuesDetails = "";
    Button btnQuesNext;
    EditText etQuestion, etChoice1, etChoice2, etChoice3, etChoice4;
    TextView tvNumber;
    Spinner spOptions;
    LinearLayout linearLayout;

    int countQuestions;
    String tempCount, quizTitle, questionText, choiceAText, choiceBText, choiceCText, choiceDText, answerChoice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_questions);

        LinearLayout.LayoutParams loparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout = findViewById(R.id.linearMain);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        loparams.setMargins(20, 20, 20, 20);

        //Get Quiz Parameters from MakeQuiz Java file
        Bundle myBundle = getIntent().getExtras();
        if (myBundle != null) {
            quizTitle = myBundle.getString("quizName");
            tempCount = myBundle.getString("quizCount");
        }
        countQuestions = AdiUtil.fIntParser(tempCount);

        //Prepare the UI for taking questions
        tvNumber = new TextView(this);
        tvNumber.setText("Question " + String.valueOf(iNextCount) + ": ");
        etQuestion = new EditText(this);
        etQuestion.setHint("Please enter your question here         ");
        etChoice1 = new EditText(this);
        etChoice1.setHint("Choice-A");
        etChoice2 = new EditText(this);
        etChoice2.setHint("Choice-B");
        etChoice3 = new EditText(this);
        etChoice3.setHint("Choice-C");
        etChoice4 = new EditText(this);
        etChoice4.setHint("Choice-D");

        TextView tvAnswer = new TextView(this);
        tvAnswer.setText("Select a choice as correct answer:");

        spOptions = new Spinner(this);
        String answers[] = {"", "A", "B", "C", "D"};
        ArrayAdapter<String> spinnerArrayAdapter;

        spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, answers);
        spOptions.setAdapter(spinnerArrayAdapter);

        linearLayout.addView(tvNumber, loparams);
        linearLayout.addView(etQuestion, loparams);
        linearLayout.addView(etChoice1, loparams);
        linearLayout.addView(etChoice2, loparams);
        linearLayout.addView(etChoice3, loparams);
        linearLayout.addView(etChoice4, loparams);
        linearLayout.addView(tvAnswer, loparams);
        linearLayout.addView(spOptions, loparams);

        btnQuesNext = new Button(this);
        btnQuesNext.setId(Integer.valueOf(1));
        btnQuesNext.setText("Next");
        btnQuesNext.setWidth(100);
        btnQuesNext.setHeight(30);
        linearLayout.addView(btnQuesNext, loparams);

        //Make Next button unclickable
        btnQuesNext.setClickable(false);

        //Apply Listener on Next button
        btnQuesNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        boolean MakeBtnClickable = false;

        //Capture User's input for question in variables
        questionText = etQuestion.getText().toString();
        choiceAText = etChoice1.getText().toString();
        choiceBText = etChoice2.getText().toString();
        choiceCText = etChoice3.getText().toString();
        choiceDText = etChoice4.getText().toString();
        answerChoice = spOptions.getSelectedItem().toString();

        if ((questionText.length() != 0) && (choiceAText.length() != 0) && (choiceBText.length() != 0) && (choiceCText.length() != 0) && (choiceDText.length() != 0) && (answerChoice.length() != 0)){
            btnQuesNext.setClickable(true);
            MakeBtnClickable = true;
        }

        if (MakeBtnClickable == true){
            String CsvFileName = quizTitle + ".csv";

            questionText = AdiUtil.fReplaceCharInString (questionText, ",", ";");
            choiceAText = AdiUtil.fReplaceCharInString (choiceAText, ",", ";");
            choiceBText = AdiUtil.fReplaceCharInString (choiceBText, ",", ";");
            choiceCText = AdiUtil.fReplaceCharInString (choiceCText, ",", ";");
            choiceDText = AdiUtil.fReplaceCharInString (choiceDText, ",", ";");
            answerChoice = AdiUtil.fReplaceCharInString (answerChoice, ",", ";");

            sQuesDetails = questionText + "," + choiceAText + "," + choiceBText + "," + choiceCText + "," + choiceDText + "," + answerChoice;

            //Update CSV file with question parameters
            WriteUserDataFile(CsvFileName, sQuesDetails, false);

            iNextCount++;
            if (iNextCount > countQuestions) {
                GetBackToMakeQuizActivity();
            }else {
                //Clean up the screen for taking next question
                tvNumber.setText("Question " + String.valueOf(iNextCount) + ": ");
                etQuestion.setText("");
                etChoice1.setText("");
                etChoice2.setText("");
                etChoice3.setText("");
                etChoice4.setText("");
                spOptions.setSelection(0);
                etQuestion.setHint("Please enter your question here         ");
                etChoice1.setHint("Choice-A");
                etChoice2.setHint("Choice-B");
                etChoice3.setHint("Choice-C");
                etChoice4.setHint("Choice-D");
            }
        }else{
            ShowMessageToUser("QuizWiz Warning >>> ", "Enter question details before you click Next button!");
        }
    }

    private void GetBackToMakeQuizActivity() {
        //Create an intent to go back to MakeQuiz activity
        Intent intentGoBack = new Intent(RecordQuestions.this, MainActivity.class);
        startActivity(intentGoBack);
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

