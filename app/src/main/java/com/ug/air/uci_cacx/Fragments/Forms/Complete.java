package com.ug.air.uci_cacx.Fragments.Forms;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.ug.air.uci_cacx.Activities.Facilities.CODE;
import static com.ug.air.uci_cacx.Activities.Login.CREDENTIALS_PREFS;
import static com.ug.air.uci_cacx.Activities.Login.SESSION;
import static com.ug.air.uci_cacx.Activities.Login.TOKEN;
import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;
import static com.ug.air.uci_cacx.Fragments.Patient.Clinicians.COMPLETE;
import static com.ug.air.uci_cacx.Fragments.Patient.Identification.FIRST_NAME;
import static com.ug.air.uci_cacx.Fragments.Patient.Identification.LAST_NAME;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_1.IMAGE_PATH;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_2.IMAGE_PATH_2;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_3.IMAGE_PATH_3;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_4.IMAGE_PATH_4;
import static com.ug.air.uci_cacx.Fragments.Patient.Screening_1.SCREEN_METHOD;
import static com.ug.air.uci_cacx.Fragments.Patient.Screening_2.RESULT_VIA;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ug.air.uci_cacx.APIs.ApiClient;
import com.ug.air.uci_cacx.APIs.JsonPlaceHolder;
import com.ug.air.uci_cacx.Activities.Home;
import com.ug.air.uci_cacx.Activities.Screening;
import com.ug.air.uci_cacx.Adapters.FormAdapter;
import com.ug.air.uci_cacx.Models.Error;
import com.ug.air.uci_cacx.Models.Form;
import com.ug.air.uci_cacx.Models.Image;
import com.ug.air.uci_cacx.Models.Message;
import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;
import com.ug.air.uci_cacx.ViewModels.FileUploadViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Complete extends Fragment {

    View view;
    List<Form> formList = new ArrayList<>();
    FormAdapter formAdapter;
    RecyclerView recyclerView;
    Button upload;
    SharedPreferences.Editor editor, editor_2;
    SharedPreferences sharedPreferences, sharedPreferences_2;
    String facility_code, token, session_id;
    JsonPlaceHolder jsonPlaceHolder;
    public static  final String TAG ="UCI_Screening";
    List<String> imagesList = new ArrayList<>();
    FileUploadViewModel fileUploadViewModel;
    List<String> sharedPreferenceFilenames = new ArrayList<>();
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_complete, container, false);

        fileUploadViewModel = new ViewModelProvider(requireActivity()).get(FileUploadViewModel.class);

        // Observe the LiveData for file upload completion
        fileUploadViewModel.getFilesUploadComplete().observe(requireActivity(), isComplete -> {
            if (isComplete) {
                // Update your UI or perform other actions
                Toast.makeText(requireActivity(), "Forms Upload complete", Toast.LENGTH_SHORT).show();
            }
        });

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        sharedPreferences_2 = requireActivity().getSharedPreferences(CREDENTIALS_PREFS, Context.MODE_PRIVATE);
        editor_2 = sharedPreferences_2.edit();

        recyclerView = view.findViewById(R.id.recyclerView);
//        upload = view.findViewById(R.id.upload);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        sharedPreferenceFilenames = FunctionalUtils.getSharedPreferencesFileNames(requireActivity(), true);
        formList = FunctionalUtils.getDataFromSharedPreferences(requireActivity(), sharedPreferenceFilenames);

        formAdapter = new FormAdapter(requireActivity(), formList);
        recyclerView.setAdapter(formAdapter);

        formAdapter.setOnItemClickListener(new FormAdapter.OnItemClickListener() {
            @Override
            public void onShowClick(int position) {
                Form form = formList.get(position);
                String filename = form.getFilename();

                FunctionalUtils.moveSharedPreferences(requireActivity(), filename, "shared_pref");
                Intent intent = new Intent(requireActivity(), Screening.class);
                startActivity(intent);
            }

            @Override
            public void onUploadClick(int position) {
                Form form = formList.get(position);
                String filename = form.getFilename();
                send_file_to_server(filename, position);
            }
        });

//        upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(requireActivity(), Home.class);
//                new upload_data(requireActivity(), editor_2, sharedPreferences_2, facility_code,
//                        token, intent, formList, formAdapter, fileUploadViewModel, sharedPreferenceFilenames).execute();
//            }
//        });

        return view;
    }

//    private static class upload_data extends AsyncTask<Void, Void, Void>{
//
//        private Context context;
//        private ProgressDialog progressDialog;
//        private SharedPreferences.Editor editor, editor_2;
//        private SharedPreferences sharedPreferences, sharedPreferences_2;
//        private String facility_code, token;
//        private JsonPlaceHolder jsonPlaceHolder;
//        public static  final String TAG ="UCI_Screening";
//        private List<String> imagesList = new ArrayList<>();
//        private Intent intent;
//        private int successfulUploads = 0;
//        private int failedUploads = 0;
//        List<Form> formList = new ArrayList<>();
//        FormAdapter formAdapter;
//        private FileUploadViewModel viewModel;
//        List<String> sharedPreferenceFilenames;
//
//        public upload_data(Context context, SharedPreferences.Editor editor_2,
//                           SharedPreferences sharedPreferences_2, String facility_code,
//                           String token, Intent intent, List<Form> formList, FormAdapter formAdapter,
//                           FileUploadViewModel viewModel, List<String> sharedPreferenceFilenames) {
//            this.context = context;
//            this.editor_2 = editor_2;
//            this.sharedPreferences_2 = sharedPreferences_2;
//            this.facility_code = facility_code;
//            this.token = token;
//            this.intent = intent;
//            this.formList = formList;
//            this.formAdapter = formAdapter;
//            this.viewModel = viewModel;
//            this.sharedPreferenceFilenames = sharedPreferenceFilenames;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog = ProgressDialog.show(context, "Sending forms...", "", true);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            send_data_to_server();
//
//            return null;
//        }
//
//        private void send_data_to_server() {
//
//            File sharedPrefsDir = new File(context.getApplicationInfo().dataDir + "/shared_prefs");
//            File[] files = sharedPrefsDir.listFiles();
//
//            // Iterate over the files and extract the filenames
//            if (files != null) {
//                for (File file : files) {
//                    String fileName = file.getName();
//                    if (fileName.endsWith(".xml") && !fileName.equals("shared_pref.xml") && !fileName.equals("credentials.xml")) {
//                        String preferenceName = fileName.substring(0, fileName.length() - 4);
//                        SharedPreferences sharedPreferences_3 = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
//
//                        boolean status = sharedPreferences_3.getBoolean(COMPLETE, false);
//                        String first_name = sharedPreferences_3.getString(FIRST_NAME, "");
//                        String last_name = sharedPreferences_3.getString(LAST_NAME, "");
//                        String screening_method = sharedPreferences_3.getString(SCREEN_METHOD, "");
//                        String name = first_name + " " + last_name;
//                        if (status) {
//                            Log.d(TAG, "status: " + status);
//                            Log.d(TAG, "loading file: " + name);
//                            Log.d(TAG, "loading file: " + screening_method);
//                            send_file(file, screening_method, sharedPreferences_3, preferenceName);
//                        }
//                    }
//                }
//            }
//
//        }
//
//        private void send_file(File file, String screening_method, SharedPreferences sharedPreferences_4, String filename) {
//            token = sharedPreferences_2.getString(TOKEN, "");
//            facility_code = sharedPreferences_2.getString(CODE, "");
//
//            Map<String, RequestBody> map = new HashMap<>();
//            map.put("provider_id", toRequestBody(token));
//            map.put("facility_id", toRequestBody(facility_code));
//
//            RequestBody filePart = RequestBody.create(MediaType.parse("*/*"), file);
//            MultipartBody.Part fileUpload = MultipartBody.Part.createFormData("file", file.getName(),filePart);
//
//            jsonPlaceHolder = ApiClient.getClient_2().create(JsonPlaceHolder.class);
//
//            if (!screening_method.equals("VIA")){
//                Call<Message> call = jsonPlaceHolder.upload_file(fileUpload, map);
//                call.enqueue(new Callback<Message>() {
//                    @Override
//                    public void onResponse(Call<Message> call, Response<Message> response) {
//                        if (response.isSuccessful()){
//                            file.delete();
//                            successfulUploads++;
//                        }
//                        else {
//                            try {
//                                int statusCode = response.code();
//                                if (statusCode == 400 || statusCode == 409 || statusCode == 403 || statusCode == 404){
//                                    String error = response.errorBody().string();
//                                    Gson gson = new Gson();
//                                    Error error1 = gson.fromJson(error, Error.class);
//                                    String message = error1.getError();
//                                    if (message.equals("patient record already exists")){
//                                        file.delete();
//                                    }
//                                }
//                                failedUploads++;
//                            }
//                            catch (IOException e) {
//                                Log.e(TAG, "onResponse: exception");
//                            }
//                        }
//                        sharedPreferenceFilenames.remove(filename);
//                        formList = FunctionalUtils.getDataFromSharedPreferences(context, sharedPreferenceFilenames);
//                        formAdapter.notifyDataSetChanged();
//                        if (successfulUploads + failedUploads == formList.size()) {
//                            viewModel.setFilesUploadComplete(true);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Message> call, Throwable t) {
//                        failedUploads++;
//                        if (successfulUploads + failedUploads == formList.size()) {
//                            viewModel.setFilesUploadComplete(true);
//                        }
//                    }
//                });
//            }
//            else {
//                String image_path_1 = sharedPreferences_4.getString(IMAGE_PATH, "");
//                imagesList.add(image_path_1);
//                String image_path_2 = sharedPreferences_4.getString(IMAGE_PATH_2, "");
//                imagesList.add(image_path_2);
//                String image_path_3 = sharedPreferences_4.getString(IMAGE_PATH_3, "");
//                imagesList.add(image_path_3);
//                String image_path_4 = sharedPreferences_4.getString(IMAGE_PATH_4, "");
//                imagesList.add(image_path_4);
//
//                MultipartBody.Part[] imagesUpload = new MultipartBody.Part[imagesList.size()];
//                for(String url: imagesList){
//                    Log.d("UCI_CaCx", "" + url);
//                    File file2 = new File(url);
//                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file2);
//                    imagesUpload[imagesList.indexOf(url)] = MultipartBody.Part.createFormData("image_files", file2.getPath(), fileBody);
//                }
//
//                Call<Message> call = jsonPlaceHolder.upload_files(imagesUpload, fileUpload, map);
//                call.enqueue(new Callback<Message>() {
//                    @Override
//                    public void onResponse(Call<Message> call, Response<Message> response) {
//                        if (response.isSuccessful()){
//                            file.delete();
//                            successfulUploads++;
//                        }
//                        else {
//                            try {
//                                int statusCode = response.code();
//                                if (statusCode == 400 || statusCode == 409 || statusCode == 403 || statusCode == 404){
//                                    String error = response.errorBody().string();
//                                    Gson gson = new Gson();
//                                    Error error1 = gson.fromJson(error, Error.class);
//                                    String message = error1.getError();
//                                    if (message.equals("patient record already exists")){
//                                        file.delete();
//                                    }
//                                    failedUploads++;
//                                }
//                            }
//                            catch (IOException e) {
//                                Log.e(TAG, "onResponse: exception");
//                            }
//                        }
//                        sharedPreferenceFilenames.remove(filename);
//                        formList = FunctionalUtils.getDataFromSharedPreferences(context, sharedPreferenceFilenames);
//                        formAdapter.notifyDataSetChanged();
//                        if (successfulUploads + failedUploads == formList.size()) {
//                            viewModel.setFilesUploadComplete(true);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Message> call, Throwable t) {
//                        failedUploads++;
//                        if (successfulUploads + failedUploads == formList.size()) {
//                            viewModel.setFilesUploadComplete(true);
//                        }
//                    }
//                });
//            }
//        }
//
//
//        @Override
//        protected void onPostExecute(Void unused) {
//            super.onPostExecute(unused);
//            if (progressDialog != null && progressDialog.isShowing()) {
//                progressDialog.dismiss();
//                context.startActivity(intent);
//            }
////            if (f_value > 0){
////                List<String> sharedPreferenceFilenames = FunctionalUtils.getSharedPreferencesFileNames(context, true);
////                formList = FunctionalUtils.getDataFromSharedPreferences(context, sharedPreferenceFilenames);
////
////                formAdapter.notifyDataSetChanged();
////                context.startActivity(intent);
////
////            }
////
////            if (x_value > 0){
////                Toast.makeText(context, x_value + " forms weren't sent because you already sent them", Toast.LENGTH_SHORT).show();
////            }
////
////            if (y_value > 0){
////                Toast.makeText(context, y_value + " forms weren't sent because there was an issue with the internet connection", Toast.LENGTH_SHORT).show();
////            }
//        }
//    }
//
//
//    public static RequestBody toRequestBody (String value) {
//        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
//        return body ;
//    }

    private void send_file_to_server(String filename, int position) {
        String file_name = filename + ".xml";
        File file = new File(requireActivity().getApplicationInfo().dataDir + "/shared_prefs/" + file_name);
        Log.d("UCI_CaCx", "send_file_to_server: " + file);

        if (file.exists()){
            progressDialog = ProgressDialog.show(requireActivity(), "Sending form", "Please wait...", true);
            token = sharedPreferences_2.getString(TOKEN, "");
            facility_code = sharedPreferences_2.getString(CODE, "");
            session_id = sharedPreferences_2.getString(SESSION, "");

            SharedPreferences sharedPreferences1 = requireActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);

            String method = sharedPreferences1.getString(SCREEN_METHOD, "");

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
                            formList.remove(position);
                            formAdapter.notifyItemRemoved(position);
                            Log.d(TAG, "onFailure: Form List - " + formList);
                            Toast.makeText(requireActivity(), "Form submitted successfully", Toast.LENGTH_SHORT).show();
                            if (formList.size() == 0){
                                startActivity(new Intent(requireActivity(), Home.class));
                            }
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
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        Toast.makeText(requireActivity(), t.getMessage() + ": Please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            else {

                String image_path_1 = sharedPreferences1.getString(IMAGE_PATH, "");
                imagesList.add(image_path_1);
                String image_path_2 = sharedPreferences1.getString(IMAGE_PATH_2, "");
                imagesList.add(image_path_2);
                String image_path_3 = sharedPreferences1.getString(IMAGE_PATH_3, "");
                imagesList.add(image_path_3);
                String image_path_4 = sharedPreferences1.getString(IMAGE_PATH_4, "");
                imagesList.add(image_path_4);

                MultipartBody.Part[] imagesUpload = new MultipartBody.Part[imagesList.size()];
                for(String url: imagesList){
                    Log.d("UCI_CaCx", "send_file_to_server: " + url);
                    File file2 = new File(url);
                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file2);
                    imagesUpload[imagesList.indexOf(url)] = MultipartBody.Part.createFormData("image_files", file2.getPath(), fileBody);
                }

                Log.d(TAG, "send_file_to_server: " + imagesList);

                Call<Message> call = jsonPlaceHolder.upload_files(imagesUpload, fileUpload, map);
                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if (response.isSuccessful()){
                            progressDialog.dismiss();
                            file.delete();
                            formList.remove(position);
                            formAdapter.notifyItemRemoved(position);
                            Log.d(TAG, "onFailure: Form List - " + formList);
                            for(String url: imagesList){
                                File file2 = new File(url);
                                file2.delete();
                            }
                            Toast.makeText(requireActivity(), "Form submitted successfully", Toast.LENGTH_SHORT).show();
                            if (formList.size() == 0){
                                startActivity(new Intent(requireActivity(), Home.class));
                            }
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
                        Toast.makeText(requireActivity(), t.getMessage() + ": Please try again later", Toast.LENGTH_SHORT).show();
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