package com.example.eldho.easypermissionlibrarysample;
/**
 * Permissions in manifest
 * Add dependency "https://github.com/googlesamples/easypermissions"
 * The easyPermissions flow "https://s3.ap-south-1.amazonaws.com/mindorks-server-uploads/implementing-easy-permissions-android-flow.png"
 *
 * */

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private final int CAMERA_AND_STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void ClickMe(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            launchCameraAndReadStorage();
        }
    }

    @AfterPermissionGranted(CAMERA_AND_STORAGE_PERMISSION_CODE)
    private void launchCameraAndReadStorage() { //Note : This method must be void and cant able to take any arguments
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}; //Array of permission
        if (EasyPermissions.hasPermissions(this, perms)) { //check permission is granted or not

            /**Executing tasks if permission has granted in here*/
            Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();

        } else {
            /**Rationalte dialog write here*/
            EasyPermissions.requestPermissions(this, "Permission rationale string writes here",
                    CAMERA_AND_STORAGE_PERMISSION_CODE, perms);

            //For finner control of Rationalte dialog use this
//            EasyPermissions.requestPermissions(
//                    new PermissionRequest.Builder(this, CAMERA_AND_STORAGE_PERMISSION_CODE, perms)
//                            .setRationale("Permission rationale string writes here")
//                            .setPositiveButtonText("Okayz")
//                            .setNegativeButtonText("Cancel")
//                            .setTheme(R.style.my_fancy_style)
//                            .build());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        // This will forcefully ask permission again and again if permission denied, Cant able to use the activity unless the permission given
        // Do it in emergency situations only
        launchCameraAndReadStorage();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        /** if some permissions denys the user will SEND TO SETTINGS of the app to manually grand the permission*/

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    /** Code when user came back from the settings <Optional/> >*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE){
            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(this,"Welcome Back", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
