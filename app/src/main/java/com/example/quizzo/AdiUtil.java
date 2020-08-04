//-------------------------------------------------------------------------------//
// Utility Function Class
// Design & Development: ADITEY NANDAN (Class: IX-H, DPS NOIDA)
// Last Update: August 03, 2020
// All Rights Reserved. Contact nandan.aditey@gmail.com before using the code -
// in any manner. No liability for any problems while using this code.
//-------------------------------------------------------------------------------//

package com.example.quizzo;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class AdiUtil {
    // Show a toast message
    // Use Code: AdiUtil.fShowToast (getApplicationContext(), "A New Test");
    public static void fShowToast(Context cContext, String sMsg) {
        Toast.makeText(cContext, sMsg, Toast.LENGTH_LONG).show();
    }

    // Show a toast message
    // Use Code: AdiUtil.fShowMessageBox ("Error", "A New Test", getApplicationContext();
    public static void fShowMessageBox(String sTitle, String sMessage, Context cContext) {
        Log.d("QuizWiz" + sTitle, sMessage);

        AlertDialog.Builder messageBox = new AlertDialog.Builder(cContext);
        messageBox.setTitle(sTitle);
        messageBox.setMessage(sMessage);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }

    // To replace a character with another in a string
    // Use Code: AdiUtil.fReplaceCharInString (String1, char1, char2);
    public static String fReplaceCharInString (String OrigStr, String CharToReplace, String CharToPut){
        String FinalStr;

        FinalStr = OrigStr.replaceAll(CharToReplace, CharToPut);
        return FinalStr;
    }

    // String to Int Parser
    // Use Code: AdiUtil.fIntParser ("Argument");
    public static int fIntParser(String sArg) {
        int retVal;
        try {
            retVal = Integer.parseInt(sArg);
        } catch (NumberFormatException nfe) {
            retVal = 0; // or null if that is your preference
        }
        return retVal;
    }

    // Write user data from an array into a CSV file
    // Use Code: AdiUtil.fWriteCsvFile(getApplicationContext(), "filename.csv", UserData[], WriteMode);
    // WriteMode = "Append", "Overwrite"
    public static void fWriteCsvFile(Context cContext, String UserDataFile, String[] UserDataList, String WriteMode) {
        Boolean FileMode = true;

        ContextWrapper contextWrapper = new ContextWrapper(cContext);
        File directory = contextWrapper.getDir(cContext.getFilesDir().getName(), Context.MODE_PRIVATE);

        if (WriteMode == "Append") {
            FileMode = true;
        }
        if (WriteMode == "Overwrite") {
            FileMode = false;
        }

        try {
            FileWriter writer = new FileWriter(directory + UserDataFile, FileMode);

            for (int j = 0; j < UserDataList.length; j++) {
                writer.append(String.valueOf(UserDataList[j]));
                writer.append(",");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read user data from a CSV file and put it in an array
    // Use Code: AdiUtil.fReadCsvFile(getApplicationContext(), "filename.csv");
    public static String[] fReadCsvFile(Context cContext, String UserDataFile) {
        String TmpString;
        String[] ReadStrArray = new String[0];

        try {
            FileInputStream fis = cContext.openFileInput(UserDataFile);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader r = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line).append("\n");
            }

            TmpString = sb.toString();
            ReadStrArray = TmpString.split(",");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ReadStrArray;
    }
}
