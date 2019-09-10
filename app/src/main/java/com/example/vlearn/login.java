package com.example.vlearn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
//import com.google.android.gms.common.api.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class login extends AppCompatActivity {
   SpotsDialog dialog;
    EditText userame,pass;
    Button Login,gotoRegister;
    String l_name="",l_pass="";

    String json_string;
    String JSON_String;
    JSONArray jsonArray;
    JSONObject jsonObject;
    String emyl;
    String USER_ID,USER_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userame=findViewById(R.id.L_username);
        pass=findViewById(R.id.L_password);
        Login=findViewById(R.id.login);
        gotoRegister=findViewById(R.id.goto_register);
        emyl=userame.getText().toString().trim();


        Log.d("login",l_name+l_pass);


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l_name=userame.getText().toString();
                l_pass=pass.getText().toString();

                new login.BackgroundTask().execute();

            }
        });
        //If User want to create new account this register button send intent to registerActivity
        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(login.this,register.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });
        

    }

    //Retrieve Details Of USER from Database using JSON parsing
    public void getDatafromJSON()
    {
       // Toast.makeText(login.this,"hio"+JSON_String,Toast.LENGTH_LONG).show();
        try {
            jsonObject=new JSONObject(JSON_String);

            int count=0;
            jsonArray=jsonObject.getJSONArray("server_response");

            while(count<jsonArray.length())
            {
                JSONObject jo=jsonArray.getJSONObject(count);
                USER_NAME=jo.getString("UserName");
                USER_ID=jo.getString("User_Id");
                Log.d("ghfiwhfiw:        ",USER_NAME+" "+USER_ID);
                //User_obj=new USER_Class();
                if(USER_NAME.equals("no") || USER_ID.equals("no"))
                {
                    Toast.makeText(login.this,"USER DOES NOT EXIST",Toast.LENGTH_SHORT).show();

                }else {
                    USER_Class.setLoggedUserId(USER_ID);
                    USER_Class.setLoggedUserEmail(l_name);
                    USER_Class.setLoggedUserName(USER_NAME);
                }
                //Toast.makeText(login.this,"hi",Toast.LENGTH_SHORT).show();
                count++;


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //Thread is created to fetch user info as a JSON data
    //which work in background
    class BackgroundTask extends AsyncTask<Void,Void,String>
    {
        String json_url="https://vlearndroidrun.000webhostapp.com/loginCheck.php";
        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                //my
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data= URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(l_name,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(l_pass,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();

                while((json_string=bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(json_string+"\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                JSON_String=stringBuilder.toString().trim();
                return stringBuilder.toString().trim();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "fail";
        }
        public BackgroundTask()
        {
            super();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {

            JSON_String=result;

            Log.d("josn",JSON_String);
            //Toast.makeText(login.this,"asd"+JSON_String,Toast.LENGTH_SHORT).show();
           getDatafromJSON();
           if(USER_NAME.equals("no")|| USER_ID.equals("no"))
           {

           }else{
               Intent i=new Intent(login.this,MainActivity.class);
               i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(i);

           }


            //super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }



}
