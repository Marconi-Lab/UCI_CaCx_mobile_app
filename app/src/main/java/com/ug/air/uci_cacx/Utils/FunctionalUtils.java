package com.ug.air.uci_cacx.Utils;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;
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

}
