package com.example.rplrus26.kajaba;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ListViewku extends AppCompatActivity {

    JSONArray Hasiljson;
    ListView listView;
    int index;
    ArrayList<siswi> arraylist;
    LinearLayout listdata, listload;
    private static CustomerAdaptor adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_viewku);
        listView=(ListView)findViewById(R.id.listku);
        listdata = (LinearLayout) findViewById(R.id.listdata);
        listload = (LinearLayout) findViewById(R.id.listload);
        this.registerForContextMenu(listView);

        new ambildata().execute();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.listku){
            this.getMenuInflater().inflate(R.menu.menu_context_main, menu);
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        index = info.position;

        switch (item.getItemId()) {
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete")
                        .setMessage("Apakah Anda yakin ingin menghapus data?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new Deletedata().execute();
                                Toast.makeText(getApplicationContext(), "" + arraylist.get(index).getUsername(), Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true ;
            case R.id.edit:
                Intent i = new Intent( ListViewku.this,EditData.class);
                String iduser = arraylist.get(index).getUsername().toString();
                i.putExtra("iduser", iduser);
                startActivity(i);
                return true;
            default:
                return false;
        }
    }
    @SuppressLint("StaticFieldLeak")
    public class ambildata extends AsyncTask<Void, Void, JSONObject> {


        @Override
        protected void onPreExecute() {
            //kasih loading
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;
            try {

                String url="http://192.168.6.204/login/getData.php";
                System.out.println("url ku " +url);
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
            //System.out.println("Hasilnya Adalah " +jsonObject.toString());
            listload.setVisibility(View.GONE);
            listdata.setVisibility(View.VISIBLE);
            try {
                arraylist = new ArrayList<>();
                Hasiljson = jsonObject.getJSONArray("hasil");
                for (int i = 0; i < Hasiljson.length(); i++) {
                    siswi a = new siswi();
                    a.setName(Hasiljson.getJSONObject(i).getString("name"));
                    a.setUsername(Hasiljson.getJSONObject(i).getString("username"));
                    a.setPassword(Hasiljson.getJSONObject(i).getString("password"));
                    a.setAlamat(Hasiljson.getJSONObject(i).getString("alamat"));

                    arraylist.add(a);
                }
                //pasang data arraylist ke lisview
                adapter = new CustomerAdaptor(arraylist, getApplicationContext());
                listView.setAdapter((ListAdapter) adapter);

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }
    @SuppressLint("StaticFieldLeak")
    public class Deletedata extends AsyncTask<Void, Void, JSONObject> {


        @Override
        protected void onPreExecute() {
            //kasih loading
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;
            try {
                String url = "http://192.168.6.204/login/Deletedata.php?username="+arraylist.get(index).getUsername()+"";
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
            if (jsonObject != null) {
                try {
                    JSONObject Result = jsonObject.getJSONObject("Result");
                    String sukses = Result.getString("Sukses");
                    Log.d("hasil sukses ", "onPostExecute: " + sukses);

                    if (sukses.equals("true")) {
                        Toast.makeText(getApplicationContext(), "Delete berhasil", Toast.LENGTH_SHORT).show();
                        //to main menu
                        new ambildata().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Delete gagal", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ignored) {
                    System.out.println("erornya "+ignored);
                }
            } else {
            }
        }
    }
}