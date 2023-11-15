package com.ug.air.uci_cacx.Utils;

import android.widget.RadioButton;
import android.widget.RadioGroup;

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
}
