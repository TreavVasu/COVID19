package com.treav.covid19india;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView tvInfected,tvCured,tvDead,tvInfo;
    ImageView imgInfected,imgCured,imgDead,imgInfo;
    private String data = "";
    Switch Switcher;
    private List<String> number=new ArrayList<String>();
    private List<String> nClass=new ArrayList<String>();
    ProgressDialog progressDialog;
    private static String TAG ="#OK";
    private static String url="https://www.mohfw.gov.in/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tvInfected = (TextView) findViewById(R.id.textView);
        tvCured = (TextView) findViewById(R.id.tvcured);
        tvDead = (TextView) findViewById(R.id.tvDead);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        imgInfo=(ImageView) findViewById(R.id.imgInfo);
        imgCured=(ImageView) findViewById(R.id.imgDoc);
        imgDead=(ImageView) findViewById(R.id.imgDead);


        //Switcher=(Switch)findViewById(R.id.OnOff);

        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,InfoAct.class);
                startActivity(intent);
            }
        });

        imgCured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "People Cured", Toast.LENGTH_SHORT).show();
                 }
        });

        imgDead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "People Dead", Toast.LENGTH_SHORT).show();
            }
        });



        if(isConnectedToInternet())
        {

            new Content().execute();   // Run AsyncTask
        }
        else
        {
            // Here I've been added intent to open up data settings
            Intent intent=new Intent(MainActivity.this,NoInternet.class);
            startActivity(intent);
            //ComponentName cName = new ComponentName("com.android.phone","com.android.phone.NetworkSetting");
            //intent.setComponent(cName);
        }
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();


        if(isConnectedToInternet())
        {

            new Content().execute();   // Run AsyncTask
        }
        else
        {
            // Here I've been added intent to open up data settings
            Intent intent=new Intent(MainActivity.this,NoInternet.class);
            startActivity(intent);
            //ComponentName cName = new ComponentName("com.android.phone","com.android.phone.NetworkSetting");
            //intent.setComponent(cName);
        }
    }

    private class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // progressDialog = new ProgressDialog(MainActivity.this);
            //progressDialog.show();
           // progressDialog.dismiss();

        }
        public String title,nCases;
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                System.setProperty("http.proxyhost","127.0.0.1");
                System.setProperty("http.proxyport","3128");
                Document document = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                        .get()
                        ;

                nCases = document.tagName("div.information_row").text();//.absUrl("#card-body status-confirmed");
                Elements element = document.select("div.iblock");
                String x="";;
                number=new ArrayList<String>();
                nClass=new ArrayList<String>();
                for (Element element0 : element) {
                    if (element0!=null){
                        x=element0.select("span.icount").text();
                        number.add(x);
                        x=element0.select("div.info_label").text();
                        nClass.add(x);
                    }

                }
                Log.d(TAG, "doInBackground: Value"+String.valueOf(number)+"\n"+String.valueOf(nClass));
                title = document.title();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            int i = Integer.parseInt(number.get(1));

            ValueAnimator animator = ValueAnimator.ofInt(0,i); //0 is min number, 600 is max number
            animator.setDuration(1000);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    tvInfected.setText(animation.getAnimatedValue().toString());
                }
            });
            animator.start();
            //tvInfected.setText(number.get(1));
            tvCured.setText(number.get(2));
            tvDead.setText(number.get(3));
        }
    }
    public boolean isConnectedToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }
}
