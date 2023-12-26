package com.ug.air.uci_cacx.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FileUploadViewModel extends ViewModel {

    private MutableLiveData<Boolean> filesUploadComplete = new MutableLiveData<>();

    public LiveData<Boolean> getFilesUploadComplete(){
        return filesUploadComplete;
    }

    public void setFilesUploadComplete(boolean complete) {
        filesUploadComplete.setValue(complete);
    }

}
