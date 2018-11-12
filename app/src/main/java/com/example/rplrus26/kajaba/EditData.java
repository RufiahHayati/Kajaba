package com.example.rplrus26.kajaba;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class EditData extends AppCompatActivity {
    String iduser;
    TextView txtUserID;
    EditText edtname, edtpass, edtalamat;
    ubah edit;
    ProgressBar loa;
    Button btnsave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        txtUserID = (TextView)findViewById(R.id.txtUserID);
        edtname = (EditText) findViewById(R.id.edtname);
        edtpass = (EditText) findViewById(R.id.edtpass);
        edit = new ubah();
        edtalamat = (EditText) findViewById(R.id.edtalamat);
        btnsave = (Button) findViewById(R.id.btnsave);
        loa =(ProgressBar)findViewById(R.id.progressBarmutar);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setName(edtname.getText().toString());
                edit.setPassword(edtpass.getText().toString());
                edit.setAlamat(edtalamat.getText().toString());
                new editData().execute();
            }
        });

    }
    @SuppressLint("StaticFieldLeak")
    public class editData extends AsyncTask<Void, Void, JSONObject> {


        @Override
        protected void onPreExecute() {
            //kasih loading
            loa.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;
            Bundle extras = getIntent().getExtras();


            if (extras !=null) {
                iduser = extras.getString("iduser");
            }
            //txtUserID.setText(iduser);
            try {
                String tmpName = edit.getName().replaceAll(" ","%20");
                String tmpAlamat = edit.getAlamat().replaceAll(" ","%20");
                String url = "http://192.168.6.204/login/editData.php?name=" +tmpName+"&password=" +edit.getPassword()+"&alamat=" +tmpAlamat+ "&username=" +iduser+ "";
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
            Log.d("hasil json ", "onPostExecute: " + jsonObject.toString());
            loa.setVisibility(View.INVISIBLE);
            if (jsonObject != null) {
                try {
                    JSONObject Result = jsonObject.getJSONObject("Result");
                    String sukses = Result.getString("Sukses");
                    Log.d("hasil sukses ", "onPostExecute: " + sukses);
                    if (sukses.equals("true")) {
                        Toast.makeText(getApplicationContext(), "Save sukses", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditData.this,ListViewku.class);
                        startActivity(intent);

                        //to main menu
                    } else {
                        Toast.makeText(getApplicationContext(), "Save gagal", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ignored) {
                    System.out.println("erornya "+ignored);
                }
            } else {
            }
        }
    }
}