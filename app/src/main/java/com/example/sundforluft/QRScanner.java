package com.example.sundforluft;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.DAO.ClassroomModel;
import com.example.sundforluft.cloud.ATTCommunicator;
import com.example.sundforluft.cloud.DAO.ATTDevice;
import com.example.sundforluft.fragments.CloudDetailedFragment;
import com.example.sundforluft.services.Globals;
import com.example.sundforluft.teacher.AddCloudActivity;
import com.example.sundforluft.teacher.TeacherMainActivity;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;
    Toolbar toolbar;
    DataAccessLayer dataAccessLayer;

    private String lastScannedQR  = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        scannerView = findViewById(R.id.zxscan);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String name = Globals.hasTeacherRights() ? "Lærer" : "Gæst";
        getSupportActionBar().setTitle(String.format("%s - QR Scanner", name));

        // Arrow Click
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dataAccessLayer = DataAccessLayer.getInstance();
        QRScanner self = this;

        //Request permission
        Dexter.withActivity(self)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(self);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(self, "You need to accept permission", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    @Override
    public void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }
    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void handleResult(Result rawResult){
        String deviceId = rawResult.getText();
        ZXingScannerView.ResultHandler resultSelf = this;
        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
        if (lastScannedQR.equals(deviceId)) {
            scannerView.resumeCameraPreview(resultSelf);
            return;
        }
        lastScannedQR = deviceId;

        ATTCommunicator.getInstance().waitForLoad();
        ATTDevice foundDevice = ATTCommunicator.getInstance().getDeviceById(deviceId);

        if (foundDevice == null) {
            scannerView.resumeCameraPreview(resultSelf);
        } else {
            if (Globals.hasTeacherRights()) {
                boolean isRemove = getIntent().getBooleanExtra("isRemove", false);
                Intent i;
                if (isRemove){
                    //Remove Cloud directly
                    ClassroomModel model = dataAccessLayer.getClassroomByDeviceId(deviceId);
                    if (model == null){
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Dette device er ikke registreret, vil du registrere det nu?");
                        builder.setPositiveButton("Ja", (dialog, which) -> {
                            Intent intent = new Intent(getApplicationContext(), AddCloudActivity.class);
                            intent.putExtra("deviceId", deviceId);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        });
                        builder.setNegativeButton("Nej", (dialog, which) -> {
                            Intent intent = new Intent(getApplicationContext(), TeacherMainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        }).create().show();
                        return;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("DeviceId: "+model.deviceId+"\nNavn: "+model.name+"\nEr du sikker på at du vil fjerne dette device?");
                    builder.setPositiveButton("Ja", (dialog, which) -> {
                        dataAccessLayer.removeClassroom(model);
                        Intent intent = new Intent(getApplicationContext(), TeacherMainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    });
                    builder.setNegativeButton("Nej", (dialog, which) -> {
                        Intent intent = new Intent(getApplicationContext(), TeacherMainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }).create().show();
                } else {
                    // Go to AddCloud view.
                    i = new Intent(QRScanner.this, AddCloudActivity.class);
                    i.putExtra("deviceId", deviceId);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            } else {
                Fragment fragment = new CloudDetailedFragment();
                Bundle bundle = new Bundle();
                bundle.putString("deviceId", deviceId);
                fragment.setArguments(bundle);

                findViewById(R.id.zxscan).setAlpha(0);


                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.scannerFragment, fragment).commit();

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        /*Intent intent = new Intent(QRScanner.this, StartActivity.class);
        intent.putExtra("animation", false);
        startActivity(intent);*/
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
