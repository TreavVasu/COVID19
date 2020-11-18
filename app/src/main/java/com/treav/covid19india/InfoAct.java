package com.treav.covid19india;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class InfoAct extends AppCompatActivity {
    ImageButton bCall, bYoutube, bAware;
    private static final int REQUEST_CALL = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        bAware = (ImageButton) findViewById(R.id.bFaq);
        bCall = (ImageButton) findViewById(R.id.bCall);
        bYoutube = (ImageButton) findViewById(R.id.bYoutube);

        bYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.youtube.com/playlist?list=PL1a9DHjZmejE-Ep2PAu2OR8HBfLP0BLIk";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                // Note the Chooser below. If no applications match,
                // Android displays a system message.So here there is no need for try-catch.
                startActivity(Intent.createChooser(intent, "Browse with"));
            }
        });

        bCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

        bAware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.mohfw.gov.in/awareness.html";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                // Note the Chooser below. If no applications match,
                // Android displays a system message.So here there is no need for try-catch.
                startActivity(Intent.createChooser(intent, "Browse with"));
            }
        });

    }
    private void makePhoneCall() {
        String number = "1123978046";

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

