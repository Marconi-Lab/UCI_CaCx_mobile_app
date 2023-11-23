package com.ug.air.uci_cacx.Fragments.Patient;

import static com.ug.air.uci_cacx.Activities.Screening.SHARED_PREFS;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ug.air.uci_cacx.R;
import com.ug.air.uci_cacx.Utils.FunctionalUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Photo_3 extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View view;
    ImageView imageView;
    Button next_btn, back_btn, take_btn;
    String image_name, image_path;
    public static  final String IMAGE_NAME_3 ="image_name_3";
    public static  final String IMAGE_PATH_3 ="image_path_3";
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 152;
    public static final int GALLERY_REQUEST_CODE = 105;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_photo_3, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        next_btn = view.findViewById(R.id.next);
        back_btn = view.findViewById(R.id.back);

        take_btn = view.findViewById(R.id.take);
        imageView = view.findViewById(R.id.image);

        load_data();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Photo_2());
                fr.commit();
            }
        });

        take_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermission();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image_path.isEmpty()){
                    Toast.makeText(requireActivity(), "Please take a picture", Toast.LENGTH_SHORT).show();
                }
                else {
                    save_data();
                }
            }
        });

        return view;
    }

    private void askCameraPermission() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else{
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {

        if (!image_path.isEmpty()){
            File f = new File(image_path);
            f.delete();
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.ug.air.uci_cacx",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String uuid = FunctionalUtils.generate_uuid();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        image_name = "UCI_CaCx_" + uuid + "_" + timeStamp + ".jpg";
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "UCI_CaCx");
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("UCI_CaCx", "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + image_name);
        image_path = file.getAbsolutePath();

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                File f = new File(image_path);
                imageView.setImageURI(Uri.fromFile(f));
                Log.d("tag", "Absolute URL is " + Uri.fromFile(f));
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                getActivity().sendBroadcast(mediaScanIntent);
            }
        }
    }

    private void save_data() {
        editor.putString(IMAGE_NAME_3, image_name);
        editor.putString(IMAGE_PATH_3, image_path);
        editor.apply();

        FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, new Photo_4());
        fr.addToBackStack(null);
        fr.commit();
    }

    private void load_data(){
        image_path = sharedPreferences.getString(IMAGE_PATH_3, "");

        if (!image_path.isEmpty()){
            File f = new File(image_path);
            imageView.setImageURI(Uri.fromFile(f));
        }

    }

}