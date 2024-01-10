package com.ug.air.uci_cacx.Utils;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;
import static com.ug.air.uci_cacx.Fragments.Patient.Clinicians.COMPLETE;
import static com.ug.air.uci_cacx.Fragments.Patient.Clinicians.DATE;
import static com.ug.air.uci_cacx.Fragments.Patient.Clinicians.FILENAME;
import static com.ug.air.uci_cacx.Fragments.Patient.Identification.FIRST_NAME;
import static com.ug.air.uci_cacx.Fragments.Patient.Identification.LAST_NAME;

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

        float changed_height = (float) (height / 100.0);
        float bd = weight / (changed_height * changed_height);
        float roundedFloat = Math.round(bd * 10.0f) / 10.0f;
        return roundedFloat;
    }

    public static float check_float_value(String value){
        float flo;
        if (value.isEmpty()){
            flo = 0.0F;
        }
        else {
            flo = Float.parseFloat(value);
            flo = Math.round(flo * 10.0f) / 10.0f;
        }

        return flo;
    }

    public static int check_int_value(String value){
        int flo;
        if (value.isEmpty()){
            flo = 0;
        }
        else {
            flo = Integer.parseInt(value);
        }

        return flo;
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
        List<CheckBox> checkBoxesToCheck = new ArrayList<>();

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (linearLayout.getChildAt(i) instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                String value = checkBox.getText().toString();

                for (String check : checkBoxList) {
                    if (value.equals(check)) {
                        checkBoxesToCheck.add(checkBox);
                    }
                }
            }
        }

        for (CheckBox checkBox : checkBoxesToCheck) {
            checkBox.setChecked(true);
        }

//        for (int i = 0; i < linearLayout.getChildCount(); i++) {
//            if (linearLayout.getChildAt(i) instanceof CheckBox) {
//                CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
//                String value = checkBox.getText().toString();
//
//                for (String check: checkBoxList){
//                    if (value.equals(check)){
//                        checkBox.setChecked(true);
//                    }
//                }
//
//            }
//        }
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
        shortUUID = shortUUID.substring(0, 7);
        return shortUUID;
    }


    public static String save_file(Context context, boolean status){
        SharedPreferences sharedPreferences, sharedPreferencesX;
        SharedPreferences.Editor editor, editorX;

        sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
        String formattedDate = df.format(currentTime);

//        UUID uuid = UUID.randomUUID();
        String filename = sharedPreferences.getString(FILENAME, "");
        Log.d("UCI_CaCx", "onCreate: " + filename);

        editor.putString(DATE, formattedDate);
        editor.putString("patient_id", filename);
        editor.putBoolean(COMPLETE, status);
//        editor.putString(FILENAME, filename);
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

        return filename;

    }

    public static List<String> getSharedPreferencesFileNames(Context context, boolean complete) {
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
                    String first_name = sharedPreferences.getString(FIRST_NAME, "");

                    if (!first_name.isEmpty() && status == complete){
                        fileNames.add(preferenceName);
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
            String first_name = sharedPreferences.getString(FIRST_NAME, "");
            String filename = sharedPreferences.getString(FILENAME, "");
            boolean complete = sharedPreferences.getBoolean(COMPLETE, false);

            Form form = new Form(first_name, date, filename, complete);
            formList.add(form);

        }

        return formList;
    }

    public static void moveSharedPreferences(Context context, String sourceFileName, String targetFileName) {
        SharedPreferences sourceSharedPreferences = context.getSharedPreferences(sourceFileName, Context.MODE_PRIVATE);
        SharedPreferences targetSharedPreferences = context.getSharedPreferences(targetFileName, Context.MODE_PRIVATE);

        SharedPreferences.Editor targetEditor = targetSharedPreferences.edit();

        // Get all key-value pairs from the source SharedPreferences
        Map<String, ?> allEntries = sourceSharedPreferences.getAll();

        // Copy all key-value pairs to the target SharedPreferences
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                targetEditor.putString(key, (String) value);
            } else if (value instanceof Integer) {
                targetEditor.putInt(key, (Integer) value);
            } else if (value instanceof Long) {
                targetEditor.putLong(key, (Long) value);
            } else if (value instanceof Float) {
                targetEditor.putFloat(key, (Float) value);
            } else if (value instanceof Boolean) {
                targetEditor.putBoolean(key, (Boolean) value);
            }
        }

        // Commit the changes to the target SharedPreferences
        targetEditor.apply();

        // Clear the source SharedPreferences to delete all key-value pairs
        SharedPreferences.Editor sourceEditor = sourceSharedPreferences.edit();
        sourceEditor.clear();
        sourceEditor.apply();

    }

    public static ArrayList<String> get_countries(String value) {
        String[] countries = {"Afghanistan",
                                "Albania",
                                "Algeria",
                                "Andorra",
                                "Angola",
                                "Antigua and Barbuda",
                                "Argentina",
                                "Armenia",
                                "Australia",
                                "Austria",
                                "Azerbaijan",
                                "Bahamas",
                                "Bahrain",
                                "Bangladesh",
                                "Barbados",
                                "Belarus",
                                "Belgium",
                                "Belize",
                                "Benin",
                                "Bhutan",
                                "Bolivia",
                                "Bosnia and Herzegovina",
                                "Botswana",
                                "Brazil",
                                "Brunei",
                                "Bulgaria",
                                "Burkina Faso",
                                "Burundi",
                                "Cabo Verde",
                                "Cambodia",
                                "Cameroon",
                                "Canada",
                                "Central African Republic",
                                "Chad",
                                "Chile",
                                "China",
                                "Colombia",
                                "Comoros",
                                "Congo",
                                "Costa Rica",
                                "Côte d'Ivoire",
                                "Croatia",
                                "Cuba",
                                "Cyprus",
                                "Czechia",
                                "Denmark",
                                "Djibouti",
                                "Dominica",
                                "Dominican Republic",
                                "East Timor (Timor-Leste)",
                                "Ecuador",
                                "Egypt",
                                "El Salvador",
                                "Equatorial Guinea",
                                "Eritrea",
                                "Estonia",
                                "Eswatini",
                                "Ethiopia",
                                "Fiji",
                                "Finland",
                                "France",
                                "Gabon",
                                "Gambia",
                                "Georgia",
                                "Germany",
                                "Ghana",
                                "Greece",
                                "Grenada",
                                "Guatemala",
                                "Guinea",
                                "Guinea-Bissau",
                                "Guyana",
                                "Haiti",
                                "Honduras",
                                "Hungary",
                                "Iceland",
                                "India",
                                "Indonesia",
                                "Iran",
                                "Iraq",
                                "Ireland",
                                "Israel",
                                "Italy",
                                "Jamaica",
                                "Japan",
                                "Jordan",
                                "Kazakhstan",
                                "Kenya",
                                "Kiribati",
                                "Korea, North",
                                "Korea, South",
                                "Kosovo",
                                "Kuwait",
                                "Kyrgyzstan",
                                "Laos",
                                "Latvia",
                                "Lebanon",
                                "Lesotho",
                                "Liberia",
                                "Libya",
                                "Liechtenstein",
                                "Lithuania",
                                "Luxembourg",
                                "Madagascar",
                                "Malawi",
                                "Malaysia",
                                "Maldives",
                                "Mali",
                                "Malta",
                                "Marshall Islands",
                                "Mauritania",
                                "Mauritius",
                                "Mexico",
                                "Micronesia",
                                "Moldova",
                                "Monaco",
                                "Mongolia",
                                "Montenegro",
                                "Morocco",
                                "Mozambique",
                                "Myanmar",
                                "Namibia",
                                "Nauru",
                                "Nepal",
                                "Netherlands",
                                "New Zealand",
                                "Nicaragua",
                                "Niger",
                                "Nigeria",
                                "North Macedonia",
                                "Norway",
                                "Oman",
                                "Pakistan",
                                "Palau",
                                "Panama",
                                "Papua New Guinea",
                                "Paraguay",
                                "Peru",
                                "Philippines",
                                "Poland",
                                "Portugal",
                                "Qatar",
                                "Romania",
                                "Russia",
                                "Rwanda",
                                "Saint Kitts and Nevis",
                                "Saint Lucia",
                                "Saint Vincent and the Grenadines",
                                "Samoa",
                                "San Marino",
                                "Sao Tome and Principe",
                                "Saudi Arabia",
                                "Senegal",
                                "Serbia",
                                "Seychelles",
                                "Sierra Leone",
                                "Singapore",
                                "Slovakia",
                                "Slovenia",
                                "Solomon Islands",
                                "Somalia",
                                "South Africa",
                                "South Sudan",
                                "Spain",
                                "Sri Lanka",
                                "Sudan",
                                "Suriname",
                                "Sweden",
                                "Switzerland",
                                "Syria",
                                "Taiwan",
                                "Tajikistan",
                                "Tanzania",
                                "Thailand",
                                "Togo",
                                "Tonga",
                                "Trinidad and Tobago",
                                "Tunisia",
                                "Turkey",
                                "Turkmenistan",
                                "Tuvalu",
                                "Uganda",
                                "Ukraine",
                                "United Arab Emirates",
                                "United Kingdom",
                                "United States",
                                "Uruguay",
                                "Uzbekistan",
                                "Vanuatu",
                                "Vatican City",
                                "Venezuela",
                                "Vietnam",
                                "Yemen",
                                "Zambia",
                                "Zimbabwe"};

        ArrayList<String> con = new ArrayList<>();

        for (String country: countries){
            if (country.toLowerCase().contains(value.toLowerCase())){
                con.add(country);
            }
        }

        return con;

    }

    public static ArrayList<String> get_districts(String value) {
        String[] districts = {"Abim",
                                "Adjumani",
                                "Agago",
                                "Alebtong",
                                "Amolatar",
                                "Amudat",
                                "Amuria",
                                "Amuru",
                                "Apac",
                                "Arua",
                                "Budaka",
                                "Bududa",
                                "Bugiri",
                                "Buhweju",
                                "Buikwe",
                                "Bukedea",
                                "Bukomansimbi",
                                "Bukwo",
                                "Bulambuli",
                                "Buliisa",
                                "Bundibugyo",
                                "Bushenyi",
                                "Busia",
                                "Butaleja",
                                "Butambala",
                                "Butebo",
                                "Buvuma",
                                "Buyende",
                                "Dokolo",
                                "Gomba",
                                "Gulu",
                                "Hoima",
                                "Ibanda",
                                "Iganga",
                                "Isingiro",
                                "Jinja",
                                "Kaabong",
                                "Kabale",
                                "Kabarole",
                                "Kaberamaido",
                                "Kalangala",
                                "Kaliro",
                                "Kalungu",
                                "Kampala",
                                "Kamuli",
                                "Kamwenge",
                                "Kanungu",
                                "Kapchorwa",
                                "Kasese",
                                "Katakwi",
                                "Kayunga",
                                "Kibale",
                                "Kiboga",
                                "Kisoro",
                                "Kitgum",
                                "Koboko",
                                "Kotido",
                                "Kumi",
                                "Kween",
                                "Kyankwanzi",
                                "Kyegegwa",
                                "Kyenjojo",
                                "Lamwo",
                                "Lira",
                                "Luuka",
                                "Luweero",
                                "Lwengo",
                                "Lyantonde",
                                "Manafwa",
                                "Maracha",
                                "Masaka",
                                "Masindi",
                                "Mayuge",
                                "Mbale",
                                "Mbarara",
                                "Mitooma",
                                "Mityana",
                                "Moroto",
                                "Moyo",
                                "Mpigi",
                                "Mubende",
                                "Mukono",
                                "Nakapiripirit",
                                "Nakaseke",
                                "Nakasongola",
                                "Namayingo",
                                "Namisindwa",
                                "Namutumba",
                                "Napak",
                                "Nebbi",
                                "Ngora",
                                "Ntoroko",
                                "Ntungamo",
                                "Nwoya",
                                "Otuke",
                                "Oyam",
                                "Pader",
                                "Pakwach",
                                "Pallisa",
                                "Rakai",
                                "Rubanda",
                                "Rubirizi",
                                "Rukiga",
                                "Rukungiri",
                                "Sembabule",
                                "Serere",
                                "Sheema",
                                "Sironko",
                                "Soroti",
                                "Tororo",
                                "Wakiso",
                                "Yumbe",
                                "Zombo"};

        ArrayList<String> con = new ArrayList<>();

        for (String district: districts){
            if (district.toLowerCase().contains(value.toLowerCase())){
                con.add(district);
            }
        }

        return con;

    }

}
