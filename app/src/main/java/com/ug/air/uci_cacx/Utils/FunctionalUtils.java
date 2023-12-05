package com.ug.air.uci_cacx.Utils;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;
import static com.ug.air.uci_cacx.Fragments.Patient.Clinicians.COMPLETE;
import static com.ug.air.uci_cacx.Fragments.Patient.Clinicians.DATE;
import static com.ug.air.uci_cacx.Fragments.Patient.Clinicians.FILENAME;
import static com.ug.air.uci_cacx.Fragments.Patient.Identification.SCREENING_NUMBER;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ug.air.uci_cacx.Models.Form;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class FunctionalUtils {

    public static float bmi(int height, float weight){
        height = (height * height) / 100;
        float bd = weight / height;
        return bd;
    }

    public static void setRadioButton(RadioGroup radioGroup, String radio_button){
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            if (radioGroup.getChildAt(i) instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                String value = radioButton.getText().toString();

                if (value.equals(radio_button)){
                    radioButton.setChecked(true);
                }
            }
        }
    }

    public static String convertListToString(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : list) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(item);
        }
        return stringBuilder.toString();
    }

    public static void checkBoxes(LinearLayout linearLayout, List<String> checkBoxList){
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (linearLayout.getChildAt(i) instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                String value = checkBox.getText().toString();

                for (String check: checkBoxList){
                    if (value.equals(check)){
                        checkBox.setChecked(true);
                    }
                }

            }
        }
    }

    public static List<String> convertStringToList(String value){
        String[] staffArray = value.split("\\s*,\\s*");
        List<String> staffList = new ArrayList<>(Arrays.asList(staffArray));
        return staffList;

    }

    public static void checkZeroValue(EditText editText, int value){
        if (value == 0){
            editText.setText("");
        }
        else {
            editText.setText(String.valueOf(value));
        }
    }

    public static void checkZeroValueFloat(EditText editText, float value){
        if (value == 0.0){
            editText.setText("");
        }
        else {
            editText.setText(String.valueOf(value));
        }
    }

    public static String generate_uuid(){
        UUID uuid = UUID.randomUUID();
        String shortUUID = uuid.toString().replaceAll("-", "");
        shortUUID = shortUUID.substring(0, 8);
        return shortUUID;
    }


    public static void save_file(Context context, boolean status){
        SharedPreferences sharedPreferences, sharedPreferencesX;
        SharedPreferences.Editor editor, editorX;

        sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
        String formattedDate = df.format(currentTime);

        UUID uuid = UUID.randomUUID();
        String filename = uuid.toString();

        editor.putString(DATE, formattedDate);
        editor.putBoolean(COMPLETE, status);
        editor.putString(FILENAME, filename);
        Log.d("TAG", "saveData: file saved");
        editor.apply();

        sharedPreferencesX = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        editorX = sharedPreferencesX.edit();

        Map<String, ?> allEntries = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                editorX.putString(key, (String) value);
            } else if (value instanceof Integer) {
                editorX.putInt(key, (Integer) value);
            } else if (value instanceof Long) {
                editorX.putLong(key, (Long) value);
            } else if (value instanceof Float) {
                editorX.putFloat(key, (Float) value);
            } else if (value instanceof Boolean) {
                editorX.putBoolean(key, (Boolean) value);
            }
        }

        editorX.commit();
        editor.clear();
        editor.commit();

    }

    public static List<String> getSharedPreferencesFileNames(Context context) {
        List<String> fileNames = new ArrayList<>();

        // Get the list of SharedPreferences files in the shared_prefs folder
        File sharedPrefsDir = new File(context.getApplicationInfo().dataDir + "/shared_prefs");
        File[] files = sharedPrefsDir.listFiles();

        // Iterate over the files and extract the filenames
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                if (fileName.endsWith(".xml") && !fileName.equals("shared_pref.xml") && !fileName.equals("credentials.xml")) {
                    String preferenceName = fileName.substring(0, fileName.length() - 4);
                    SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
                    boolean status = sharedPreferences.getBoolean(COMPLETE, false);

                    if (status){
                        fileNames.add(preferenceName);
                        Log.d("TAG", "getSharedPreferencesFileNames: " + fileNames);
                    }

                }
            }
        }

        return fileNames;
    }

    public static List<Form> getDataFromSharedPreferences(Context context, List<String> fileNames) {

        List<Form> formList = new ArrayList<>();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

        for (String fileName : fileNames) {
            Log.d("TAGGING", "getDataFromSharedPreferences: " + fileName);
            SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);

            String date = sharedPreferences.getString(DATE, "");
            String screening = sharedPreferences.getString(SCREENING_NUMBER, "");

            Log.d("TAGGING", "getDataFromSharedPreferences: " + date);
            Log.d("TAGGING", "getDataFromSharedPreferences: " + screening);

            Form form = new Form("Patient: " + screening, "Saved on: " + date);
            formList.add(form);

        }

        return formList;
    }

}
