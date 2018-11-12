package com.example.rplrus26.kajaba;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    login user;
    EditText textusername, textpassword;
    Button btnlogin, btndaftar;
    ProgressBar loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textusername = (EditText) findViewById(R.id.textusername);
        textpassword = (EditText) findViewById(R.id.textpassword);

        user = new login();

        btnlogin = (Button) findViewById(R.id.btnlogin);
        btndaftar = (Button) findViewById(R.id.btnregistrasi);
        loading = (ProgressBar)findViewById(R.id.progressBarmutar);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cek ke server

                user.setUsername(textusername.getText().toString());
                user.setPassword(textpassword.getText().toString());
                if (user.getUsername().equals("") || user.getPassword().equals("")){
                    Toast.makeText(getApplicationContext(),"Notway Empty", Toast.LENGTH_SHORT).show();
                }else {
                    new LoginProcess().execute();
                }
            }
        });

        btndaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MainRegistrasi.class);
                startActivity(intent);
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    public class LoginProcess extends AsyncTask<Void, Void, JSONObject> {


        @Override
        protected void onPreExecute() {
            //kasih loading
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;
            try {
                String url = "http://192.168.6.204/login/masuk.php?username=" +user.getUsername()+ "&password=" +user.getPassword()+ "";
                System.out.println("urlnya : "+url);
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        inputStream, "iso-8859-1"
                ), 8);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                inputStream.close();
                String json = stringBuilder.toString();
                jsonObject = new JSONObject(json);
            } catch (Exception e) {
                jsonObject = null;
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            String url = "http://192.168.6.204/login/getData.php";
            System.out.println("hasil" +jsonObject.toString());
            Log.d("hasil json ", "onPostExecute: " + jsonObject.toString());
            loading.setVisibility(View.INVISIBLE);
            if (jsonObject != null) {
                try {
                    JSONObject Result = jsonObject.getJSONObject("Result");
                    String sukses = Result.getString("Sukses");
                    Log.d("hasil sukses ", "onPostExecute: " + sukses);

                    if (sukses.equals("true")) {
                        Toast.makeText(getApplicationContext(), "login berhasil", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,MenuUtama.class);
                        startActivity(intent);
                        //to main menu
                    } else {
                        Toast.makeText(getApplicationContext(), "login gagal", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ignored) {
                }
            } else {
            }
        }
    }
}