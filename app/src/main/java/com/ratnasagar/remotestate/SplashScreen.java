package com.ratnasagar.remotestate;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ratnasagar.remotestate.helper.DataBaseHelper;
import com.ratnasagar.remotestate.helper.PermissionHelper;
import com.ratnasagar.remotestate.model.ResponseCode;

public class SplashScreen extends AppCompatActivity {

    private PermissionHelper permissionHelper;
    public static String [] RequiredPermission;
    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        RequiredPermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
        dbHelper = new DataBaseHelper(SplashScreen.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mCheckPermission();//@anurag This method is define for Marshmallow and above required permission
                } else {
                    Intent intent = new Intent(SplashScreen.this,MainActivity2.class);
                    startActivity(intent);
                }
            }
        }, ResponseCode.SPLASH_MILLIS);
    }

    //@pravesh This method is define for Marshmallow and above required permission
    private void mCheckPermission() {
        permissionHelper = new PermissionHelper(this, RequiredPermission, ResponseCode.PERMISSION_REQUEST_CODE);
        permissionHelper.request(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent(SplashScreen.this,MainActivity2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            @Override
            public void onPermissionDenied() {
                Toast.makeText(SplashScreen.this, "Permission Denied.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPermissionDeniedBySystem() {
                Toast.makeText(SplashScreen.this, "Permission Denied By System.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}