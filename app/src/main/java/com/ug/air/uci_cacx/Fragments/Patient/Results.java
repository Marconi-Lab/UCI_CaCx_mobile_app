package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Facilities.CODE;
import static com.ug.air.uci_cacx.Activities.Login.CREDENTIALS_PREFS;
import static com.ug.air.uci_cacx.Activities.Login.SESSION;
import static com.ug.air.uci_cacx.Activities.Login.TOKEN;
import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_1.IMAGE_PATH;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_2.IMAGE_PATH_2;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_3.IMAGE_PATH_3;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_4.IMAGE_PATH_4;
import static com.ug.air.uci_cacx.Fragments.Patient.Screening_1.SCREEN_METHOD;
import static com.ug.air.uci_cacx.Fragments.Patient.Screening_2.RESULT_VIA;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

import com.google.gson.Gson;
import com.ug.air.uci_cacx.APIs.ApiClient;
import com.ug.air.uci_cacx.APIs.JsonPlaceHolder;
import com.ug.air.uci_cacx.Activities.Home;
import com.ug.air.uci_cacx.Activities.Screening;
import com.ug.air.uci_cacx.Models.Error;
import com.ug.air.uci_cacx.Models.Image;
import com.ug.air.uci_cacx.Models.Message;
import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;
import com.ug.air.uci_cacx.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Results extends Fragment {

    private SharedPreferences.Editor editor, editor_2;
    private SharedPreferences sharedPreferences, sharedPreferences_2;
    View view;
    Button save_btn, fill_btn, home_btn;
    TextView textView_model, textView_clinician, textView_agree;
    RadioGroup radioGroup;
    LinearLayout linearLayout;
    String model, clinician, agree, method, result, model_result, via, token, facility_code;
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
    ProgressDialog progressDialog;
    JsonPlaceHolder jsonPlaceHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_results, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        sharedPreferences_2 = requireActivity().getSharedPreferences(CREDENTIALS_PREFS, Context.MODE_PRIVATE);
        editor_2 = sharedPreferences_2.edit();

        method = sharedPreferences.getString(SCREEN_METHOD, "");
        via = sharedPreferences.getString(RESULT_VIA, "");
//        clinician = sharedPreferences.getString(RESULT, "");
        
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

//        Toast.makeText(requireActivity(), method, Toast.LENGTH_SHORT).show();

        if (method.equals("VIA")){
            imageList.add(new Image(sharedPreferences.getString(IMAGE_PATH, ""), IMAGE_PREDICTION_1, IMAGE_RESULT_1));
            imageList.add(new Image(sharedPreferences.getString(IMAGE_PATH_2, ""), IMAGE_PREDICTION_2, IMAGE_RESULT_2));
            imageList.add(new Image(sharedPreferences.getString(IMAGE_PATH_3, ""), IMAGE_PREDICTION_3, IMAGE_RESULT_3));
            imageList.add(new Image(sharedPreferences.getString(IMAGE_PATH_4, ""), IMAGE_PREDICTION_4, IMAGE_RESULT_4));

//            run_model();
            new runningBackgroundTask(requireActivity(), imageList, editor, sharedPreferences,
                    linearLayout, textView_model, textView_clinician, textView_agree, via).execute();

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
                    String filename = FunctionalUtils.save_file(requireActivity(), true);
                    send_file_to_server(filename);
//                    FunctionalUtils.save_file(requireActivity(), true);
//                    startActivity(new Intent(requireActivity(), Home.class));
//                    Toast.makeText(requireActivity(), "This functionality is not yet available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        return view;
    }

    private static class runningBackgroundTask extends AsyncTask<Void, Void, Void>{

        private Context context;
        private ProgressDialog progressDialog;
        private ArrayList<Image> imageList;
        SharedPreferences.Editor editor;
        SharedPreferences sharedPreferences;
        float negative, positive, pos3;
        String model, clinician, agree, method, result, model_result, via;
        LinearLayout linearLayout;
        TextView textView_model, textView_clinician, textView_agree;

        public runningBackgroundTask(Context context, ArrayList<Image> imageList,
                                     SharedPreferences.Editor editor,
                                     SharedPreferences sharedPreferences, LinearLayout linearLayout,
                                     TextView textView_model, TextView textView_clinician,
                                     TextView textView_agree, String via) {
            this.context = context;
            this.imageList = imageList;
            this.editor = editor;
            this.sharedPreferences = sharedPreferences;
            this.linearLayout = linearLayout;
            this.textView_model = textView_model;
            this.textView_clinician = textView_clinician;
            this.textView_agree = textView_agree;
            this.via = via;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Show a progress dialog before starting the task
            progressDialog = ProgressDialog.show(context, "Running Image Classification model...", "", true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            run_model(imageList);

            return null;
        }

        private void run_model(ArrayList<Image> imageList) {
            for (int i = 0; i < imageList.size(); i++){
                Image image = imageList.get(i);
                run_tensorflow_model(image, i);
            }
        }

        private void run_tensorflow_model(Image image, int index) {
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(image.getImage_path());
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);

                Model model = Model.newInstance(context);

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

                }

            } catch (IOException e) {
                // TODO Handle the exception
            }

        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            // Dismiss the progress dialog after the task is complete
            progressDialog.dismiss();
            linearLayout.setVisibility(View.VISIBLE);
            textView_model.setText(model_result);
            textView_clinician.setText(via);

            if (model_result.equals(via)) {
                textView_agree.setText("Agreement");
            }
            else {
                textView_agree.setText("Disagreement");
            }
            // Show a completion message or perform any post-task actions
        }

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

//    private static void run_model(ArrayList<Image> imageList, Context context) {
//        for (int i = 0; i < imageList.size(); i++){
//            Image image = imageList.get(i);
//            run_tensorflow_model(image, i, context);
//        }
//    }

//    private static void run_tensorflow_model(Image image, int index, Context context){
//        try {
//            Bitmap bitmap = BitmapFactory.decodeFile(image.getImage_path());
//            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
//
//            Model model = Model.newInstance(context);
//
//            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 300, 300, 3}, DataType.FLOAT32);
//
//            TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
//            tensorImage.load(resizedBitmap);
//            ByteBuffer byteBuffer_2 = tensorImage.getBuffer();
//            inputFeature0.loadBuffer(byteBuffer_2);
//
//            Model.Outputs outputs = model.process(inputFeature0);
//            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//
//            model.close();
//
//            negative = outputFeature0.getFloatArray()[0];
//            positive = outputFeature0.getFloatArray()[1];
//
//            if (index == 2){
//                pos3 = positive;
//            }
//
//            editor.putString(image.getPrediction(), "[" + negative +", " + positive +"]");
//            if (negative > positive){
//                result = "Negative";
//            }else {
//                result = "Positive";
//            }
//            Log.d("TAG", "run_tensorflow_model: index" + index  + " results " + result);
//            editor.putString(image.getResult(), result);
//            editor.apply();
//
//            if (index == 3){
//                float threshold = (float) 0.5;
//                String res_img_3 = sharedPreferences.getString(IMAGE_RESULT_3, "");
//                if (res_img_3.equals(result)){
//                    model_result = result;
//                }
//                else {
//                    if (res_img_3.equals("Positive")){
//                        if (pos3 >= threshold){
//                            model_result = "Positive";
//                        }
//                        else {
//                            model_result = "Negative";
//                        }
//                    }
//                    else {
//                        if (positive >= threshold){
//                            model_result = "Positive";
//                        }
//                        else {
//                            model_result = "Negative";
//                        }
//                    }
//                }
//                editor.putString(MODEL_RESULT, model_result);
//                editor.apply();
//                Log.d("TAG", "run_tensorflow_model: result " + model_result);
//                dialog.dismiss();
//
//                load_results();
//            }
//        }
//        catch (IOException e) {
//            // TODO Handle the exception
//        }
//
//    }

//    private void load_results() {
//        linearLayout.setVisibility(View.VISIBLE);
//        textView_model.setText(model_result);
//        textView_clinician.setText(via);
//
//        if (model_result.equals(via)) {
//            textView_agree.setText("Agreement");
//        }
//        else {
//            textView_agree.setText("Disagreement");
//        }
//    }

    private void send_file_to_server(String filename) {
        filename = filename + ".xml";
        File file = new File(requireActivity().getApplicationInfo().dataDir + "/shared_prefs/" + filename);
        Log.d("UCI_CaCx", "send_file_to_server: " + file);
        if (file.exists()){
            progressDialog = ProgressDialog.show(requireActivity(), "Sending form", "Please wait...", true);
            token = sharedPreferences_2.getString(TOKEN, "");
            facility_code = sharedPreferences_2.getString(CODE, "");
            String session_id = sharedPreferences_2.getString(SESSION, "");


            Map<String, RequestBody> map = new HashMap<>();
            map.put("provider_id", toRequestBody(token));
            map.put("facility_id", toRequestBody(facility_code));
            map.put("session_id", toRequestBody(session_id));

            RequestBody filePart = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part fileUpload = MultipartBody.Part.createFormData("file", file.getName(),filePart);

            jsonPlaceHolder = ApiClient.getClient_2().create(JsonPlaceHolder.class);

            if (!method.equals("VIA")){
                Call<Message> call = jsonPlaceHolder.upload_file(fileUpload, map);
                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if (response.isSuccessful()){
                            progressDialog.dismiss();
                            file.delete();
                            Toast.makeText(requireActivity(), "Form submitted successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(requireActivity(), Home.class));
                        }
                        else {
                            try {
                                int statusCode = response.code();
                                if (statusCode == 400 || statusCode == 409 || statusCode == 403 || statusCode == 404){
                                    String error = response.errorBody().string();
                                    Gson gson = new Gson();
                                    Error error1 = gson.fromJson(error, Error.class);
                                    String message = error1.getError();
                                    if (message.equals("patient record already exists")){
                                        progressDialog.dismiss();
                                        Toast.makeText(requireActivity(), "There are some issues with the screening information", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else if(statusCode == 500){
                                    progressDialog.dismiss();
                                    Toast.makeText(requireActivity(), "There was an internal server error", Toast.LENGTH_SHORT).show();

                                }
                                else if(statusCode == 422) {
                                    progressDialog.dismiss();
                                    Toast.makeText(requireActivity(), "Header variables missing", Toast.LENGTH_SHORT).show();
                                }
                                else if(statusCode == 413) {
                                    progressDialog.dismiss();
                                    Toast.makeText(requireActivity(), "Request Entity Too Large", Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (IOException e) {
                                Log.e("UCI_CaCx", "onResponse: exception");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(requireActivity(), "Something went wrong, Please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                MultipartBody.Part[] imagesUpload = new MultipartBody.Part[imageList.size()];
                for(Image image: imageList){
                    Log.d("UCI_CaCx", "" + image.getImage_path());
                    File file2 = new File(image.getImage_path());
                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file2);
                    imagesUpload[imageList.indexOf(image)] = MultipartBody.Part.createFormData("image_files", file2.getPath(), fileBody);
                }

                Call<Message> call = jsonPlaceHolder.upload_files(imagesUpload, fileUpload, map);
                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if (response.isSuccessful()){
                            progressDialog.dismiss();
                            file.delete();
                            Toast.makeText(requireActivity(), "Form submitted successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(requireActivity(), Home.class));
                        }
                        else {
                            try {
                                int statusCode = response.code();
                                if (statusCode == 400 || statusCode == 409 || statusCode == 403 || statusCode == 404){
                                    String error = response.errorBody().string();
                                    Gson gson = new Gson();
                                    Error error1 = gson.fromJson(error, Error.class);
                                    String message = error1.getError();
                                    if (message.equals("patient record already exists")){
                                        progressDialog.dismiss();
                                        Toast.makeText(requireActivity(), "There are some issues with the screening information", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else if(statusCode == 500){
                                    progressDialog.dismiss();
                                    Toast.makeText(requireActivity(), "There was an internal server error", Toast.LENGTH_SHORT).show();

                                }
                                else if(statusCode == 422) {
                                    progressDialog.dismiss();
                                    Toast.makeText(requireActivity(), "Header variables missing", Toast.LENGTH_SHORT).show();
                                }
                                else if(statusCode == 413) {
                                    progressDialog.dismiss();
                                    Toast.makeText(requireActivity(), "Request Entity Too Large", Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (IOException e) {
                                Log.e("UCI_CaCx", "onResponse: exception");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(requireActivity(), "Something went wrong, Please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else {
            Toast.makeText(requireActivity(), "Something went wrong while saving the form.", Toast.LENGTH_SHORT).show();
        }
    }

    public static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }

}