package com.example.rplrus26.kajaba;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class MainRegistrasi extends AppCompatActivity {

    EditText txtusername, txtname, txtpassword, txtalamat;
    Button btndaftar;
    ProgressBar loading;
    daftar user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrasi_main);

        txtusername = (EditText)findViewById(R.id.txtusername);
        txtname =(EditText)findViewById(R.id.txtname);
        txtpassword =(EditText)findViewById(R.id.txtpassword);
        txtalamat = (EditText)findViewById(R.id.txtalamat);

        user = new daftar();

        btndaftar = (Button)findViewById(R.id.btndaftar);
        loading =(ProgressBar)findViewById(R.id.progressBarmutar);

        btndaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(txtusername.getText().toString());
                user.setName(txtname.getText().toString());
                user.setPassword(txtpassword.getText().toString());
                user.setAlamat(txtalamat.getText().toString());
                new MainRegistrasi.LoginProcess().execute();
                if (user.getUsername().equals("") || user.getPassword().equals("")){
                    Toast.makeText(getApplicationContext(),"Notway Empty", Toast.LENGTH_SHORT).show();
                }else {
                    new LoginProcess().execute();
                }
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
                String tmpName = user.getName().replaceAll(" ","%20");
                String tmpAlamat = user.getAlamat().replaceAll(" ","%20");

                String url = "http://192.168.6.204/login/registrasi.php?username=" +user.getUsername() +"&name=" +tmpName +"&password=" +user.getPassword() +"&alamat=" +tmpAlamat +"";
                System.out.println("urlnya : " +url);
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
            System.out.println("hasil"+jsonObject.toString());
            Log.d("hasil json ", "onPostExecute: " + jsonObject.toString());
            loading.setVisibility(View.INVISIBLE);
            if (jsonObject != null) {
                try {
                    JSONObject Result = jsonObject.getJSONObject("Result");
                    String sukses = Result.getString("Sukses");
                    Log.d("hasil sukses ", "onPostExecute: " + sukses);

                    if (sukses.equals("true")) {
                        Toast.makeText(getApplicationContext(), "Pendaftaran Sukses", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainRegistrasi.this,MenuUtama.class);
                        startActivity(intent);
                        //to main menu
                    } else {
                        Toast.makeText(getApplicationContext(), "Pendaftaran gagal", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ignored) {
                }
            } else {
            }
        }
    }
}
