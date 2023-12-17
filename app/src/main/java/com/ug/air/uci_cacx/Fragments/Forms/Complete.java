package com.ug.air.uci_cacx.Fragments.Forms;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.ug.air.uci_cacx.Activities.Facilities.CODE;
import static com.ug.air.uci_cacx.Activities.Login.CREDENTIALS_PREFS;
import static com.ug.air.uci_cacx.Activities.Login.TOKEN;
import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;
import static com.ug.air.uci_cacx.Fragments.Patient.Clinicians.COMPLETE;
import static com.ug.air.uci_cacx.Fragments.Patient.Identification.FIRST_NAME;
import static com.ug.air.uci_cacx.Fragments.Patient.Identification.LAST_NAME;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_1.IMAGE_PATH;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_2.IMAGE_PATH_2;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_3.IMAGE_PATH_3;
import static com.ug.air.uci_cacx.Fragments.Patient.Photo_4.IMAGE_PATH_4;
import static com.ug.air.uci_cacx.Fragments.Patient.Screening_2.RESULT_VIA;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ug.air.uci_cacx.APIs.ApiClient;
import com.ug.air.uci_cacx.APIs.JsonPlaceHolder;
import com.ug.air.uci_cacx.Activities.Screening;
import com.ug.air.uci_cacx.Adapters.FormAdapter;
import com.ug.air.uci_cacx.Models.Form;
import com.ug.air.uci_cacx.Models.Message;
import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

import java.io.File;
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
    String facility_code, token;
    JsonPlaceHolder jsonPlaceHolder;
    public static  final String TAG ="UCI_Screening";
    List<String> imagesList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_complete, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        sharedPreferences_2 = requireActivity().getSharedPreferences(CREDENTIALS_PREFS, Context.MODE_PRIVATE);
        editor_2 = sharedPreferences_2.edit();

        recyclerView = view.findViewById(R.id.recyclerView);
        upload = view.findViewById(R.id.upload);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        List<String> sharedPreferenceFilenames = FunctionalUtils.getSharedPreferencesFileNames(requireActivity(), true);
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
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_data_to_server();
            }
        });

        return view;
    }

    private void send_data_to_server() {

        File sharedPrefsDir = new File(requireActivity().getApplicationInfo().dataDir + "/shared_prefs");
        File[] files = sharedPrefsDir.listFiles();

        // Iterate over the files and extract the filenames
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                if (fileName.endsWith(".xml") && !fileName.equals("shared_pref.xml") && !fileName.equals("credentials.xml")) {
                    String preferenceName = fileName.substring(0, fileName.length() - 4);
                    SharedPreferences sharedPreferences_3 = requireActivity().getSharedPreferences(preferenceName, Context.MODE_PRIVATE);

                    boolean status = sharedPreferences_3.getBoolean(COMPLETE, false);
                    Log.d(TAG, "status: " + status);
                    String first_name = sharedPreferences_3.getString(FIRST_NAME, "");
                    String last_name = sharedPreferences_3.getString(LAST_NAME, "");
                    String via_screening_method = sharedPreferences_3.getString(RESULT_VIA, "");
                    String name = first_name + " " + last_name;
                    Log.d(TAG, "loading file: " + name);
                    if (status) {
                        Log.d(TAG, "loading file: " + name);
                        send_file(file, via_screening_method, sharedPreferences_3);
                    }
                }
            }
        }

    }

    private void send_file(File file, String via_screening_method, SharedPreferences sharedPreferences_4) {
        token = sharedPreferences_2.getString(TOKEN, "");
        facility_code = sharedPreferences_2.getString(CODE, "");

        Map<String, RequestBody> map = new HashMap<>();
        map.put("provider_id", toRequestBody(token));
        map.put("facility_id", toRequestBody(facility_code));

        RequestBody filePart = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileUpload = MultipartBody.Part.createFormData("file", file.getName(),filePart);

        jsonPlaceHolder = ApiClient.getClient_2().create(JsonPlaceHolder.class);

        if (via_screening_method.isEmpty()){
            Call<Message> call = jsonPlaceHolder.upload_file(fileUpload, map);
            call.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        int code = response.code();
                        Toast.makeText(requireActivity(), code + ": file upload filed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    Toast.makeText(requireActivity(), "file upload filed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            String image_path_1 = sharedPreferences_4.getString(IMAGE_PATH, "");
            imagesList.add(image_path_1);
            String image_path_2 = sharedPreferences_4.getString(IMAGE_PATH_2, "");
            imagesList.add(image_path_2);
            String image_path_3 = sharedPreferences_4.getString(IMAGE_PATH_3, "");
            imagesList.add(image_path_3);
            String image_path_4 = sharedPreferences_4.getString(IMAGE_PATH_4, "");
            imagesList.add(image_path_4);

            MultipartBody.Part[] imagesUpload = new MultipartBody.Part[imagesList.size()];
            for(String url: imagesList){
                Log.d("UCI_CaCx", "" + url);
                File file2 = new File(url);
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file2);
                imagesUpload[imagesList.indexOf(url)] = MultipartBody.Part.createFormData("image_files", file2.getPath(), fileBody);
            }

            Call<Message> call = jsonPlaceHolder.upload_files(imagesUpload, fileUpload, map);
            call.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        int code = response.code();
                        Toast.makeText(requireActivity(), code + ": files upload filed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    Toast.makeText(requireActivity(), "files upload filed", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }
}