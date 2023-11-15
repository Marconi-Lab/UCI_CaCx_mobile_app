package com.ug.air.uci_cacx.Dialogs;

import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.ug.air.uci_cacx.Activities.Permissions;

import java.util.List;

public class MultiplePermissions implements MultiplePermissionsListener {
    private final Permissions permissions;

    public MultiplePermissions(Permissions permissions){
        this.permissions = permissions;
    }

    @Override
    public void onPermissionsChecked(MultiplePermissionsReport report) {
        for (PermissionGrantedResponse response : report.getGrantedPermissionResponses()){
            permissions.showPermissionGranted(response.getPermissionName());
        }

        for (PermissionDeniedResponse response : report.getDeniedPermissionResponses()){
            permissions.showPermissionsDenied(response.getPermissionName());
        }
    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken token) {
        permissions.showPermissionRational(token);
    }
}
