package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_1.IMAGE_PATH;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_2.IMAGE_PATH_2;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_3.IMAGE_PATH_3;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_4.IMAGE_PATH_4;
import static com.ug.air.uci_cacx.Fragments.Patient.Screening_1.SCREEN_METHOD;
import static com.ug.air.uci_cacx.Fragments.Patient.Screening_2.RESULT;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ug.air.uci_cacx.Activities.Home;
import com.ug.air.uci_cacx.Activities.Screening;
import com.ug.air.uci_cacx.Models.Image;
import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;
import com.ug.air.uci_cacx.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;


public class Results extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    Button save_btn, fill_btn, home_btn;
    TextView textView_model, textView_clinician, textView_agree;
    RadioGroup radioGroup;
    LinearLayout linearLayout;
    String model, clinician, agree, method, result, model_result;
    String consult = "";
    float negative, positive, pos3;
    public static  final String MODEL_RESULT ="model_result";
    public static  final String AGREE ="consult_gynecologist";
    public static  final String IMAGE_RESULT_1 ="image_1_model_result";
    public static  final String IMAGE_PREDICTION_1 ="image_1_model_prediction";
    public static  final String IMAGE_RESULT_2 ="image_2_model_result";
    public static  final String IMAGE_PREDICTION_2 ="image_2_model_prediction";
    public static  final String IMAGE_RESULT_3 ="image_3_model_result";
    public static  final String IMAGE_PREDICTION_3 ="image_3_model_prediction";
    public static  final String IMAGE_RESULT_4 ="image_4_model_result";
    public static  final String IMAGE_PREDICTION_4 ="image_4_model_prediction";
    ArrayList<Image> imageList = new ArrayList<>();
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_results, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        method = sharedPreferences.getString(SCREEN_METHOD, "");
        clinician = sharedPreferences.getString(RESULT, "");
        
        save_btn = view.findViewById(R.id.send);
        fill_btn = view.findViewById(R.id.form);
        home_btn = view.findViewById(R.id.home);

        textView_agree = view.findViewById(R.id.agree);
        textView_clinician = view.findViewById(R.id.clinician);
        textView_model = view.findViewById(R.id.model);
        linearLayout = view.findViewById(R.id.via_results);
        radioGroup = view.findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = view.findViewById(checkedId);
                consult = selectedRadioButton.getText().toString();
            }
        });

        if (method.equals("VIA")){
            imageList.add(new Image(sharedPreferences.getString(IMAGE_PATH, ""), IMAGE_PREDICTION_1, IMAGE_RESULT_1));
            imageList.add(new Image(sharedPreferences.getString(IMAGE_PATH_2, ""), IMAGE_PREDICTION_2, IMAGE_RESULT_2));
            imageList.add(new Image(sharedPreferences.getString(IMAGE_PATH_3, ""), IMAGE_PREDICTION_3, IMAGE_RESULT_3));
            imageList.add(new Image(sharedPreferences.getString(IMAGE_PATH_4, ""), IMAGE_PREDICTION_4, IMAGE_RESULT_4));

            run_model();
            
        }
        
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_consult()) {
                    FunctionalUtils.save_file(requireActivity(), true);
                    startActivity(new Intent(requireActivity(), Home.class));
                }
            }
        });

        fill_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_consult()) {
                    FunctionalUtils.save_file(requireActivity(), true);
                    startActivity(new Intent(requireActivity(), Screening.class));
                }
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_consult()) {
//                    FunctionalUtils.save_file(requireActivity(), true);
//                    startActivity(new Intent(requireActivity(), Home.class));
                    Toast.makeText(requireActivity(), "This functionality is not yet available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        return view;
    }
    
    private boolean check_consult(){
        boolean layout;
        if (linearLayout.getVisibility() == View.VISIBLE && consult.isEmpty()){
            Toast.makeText(requireActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            layout = false;
        }
        else {
            editor.putString(AGREE, consult);
            editor.apply();
            layout = true;
        }
        
        return layout;
    }

    private void run_model() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage("Running model predictions, Please wait...");
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
        for (int i = 0; i < imageList.size(); i++){
            Image image = imageList.get(i);
            run_tensorflow_model(image, i);
        }
    }

    private void run_tensorflow_model(Image image, int index){
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(image.getImage_path());
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);

            Model model = Model.newInstance(requireActivity());

            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 300, 300, 3}, DataType.FLOAT32);

            TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
            tensorImage.load(resizedBitmap);
            ByteBuffer byteBuffer_2 = tensorImage.getBuffer();
            inputFeature0.loadBuffer(byteBuffer_2);

            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            model.close();

            negative = outputFeature0.getFloatArray()[0];
            positive = outputFeature0.getFloatArray()[1];

            if (index == 2){
                pos3 = positive;
            }

            editor.putString(image.getPrediction(), "[" + negative +", " + positive +"]");
            if (negative > positive){
                result = "Negative";
            }else {
                result = "Positive";
            }
            Log.d("TAG", "run_tensorflow_model: index" + index  + " results " + result);
            editor.putString(image.getResult(), result);
            editor.apply();

            if (index == 3){
                float threshold = (float) 0.5;
                String res_img_3 = sharedPreferences.getString(IMAGE_RESULT_3, "");
                if (res_img_3.equals(result)){
                    model_result = result;
                }
                else {
                    if (res_img_3.equals("Positive")){
                        if (pos3 >= threshold){
                            model_result = "Positive";
                        }
                        else {
                            model_result = "Negative";
                        }
                    }
                    else {
                        if (positive >= threshold){
                            model_result = "Positive";
                        }
                        else {
                            model_result = "Negative";
                        }
                    }
                }
                editor.putString(MODEL_RESULT, model_result);
                editor.apply();
                Log.d("TAG", "run_tensorflow_model: result " + model_result);
                dialog.dismiss();

                load_results();
            }
        }
        catch (IOException e) {
            // TODO Handle the exception
        }

    }

    private void load_results() {
        linearLayout.setVisibility(View.VISIBLE);
        textView_model.setText(model_result);
        textView_clinician.setText(clinician);

        if (model_result.equals(clinician)) {
            textView_agree.setText("Agreement");
        }
        else {
            textView_agree.setText("Disagreement");
        }
    }

}