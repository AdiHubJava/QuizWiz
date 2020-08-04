//-------------------------------------------------------------------------------//
// QuizWiz App: QuizzoPreview Class
// To show 5 seconds preview when user opens the app
// Design & Development: ADITEY NANDAN (Class: IX-H, DPS NOIDA)
// Last Update: August 03, 2020
// All Rights Reserved. Contact nandan.aditey@gmail.com before using the code -
// in any manner. No liability for any problems while using this code.
//-------------------------------------------------------------------------------//

package com.example.quizzo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class QuizzoPreview extends AppCompatActivity {
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzo_preview);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(QuizzoPreview.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },5000);

    }
}