package com.example.chatapp;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
EditText username,password;
Button login;
String Tag,use,pass;
ConnectionDetector cd;
Boolean isInternetPresent=false;
JSONParser jsonParser=new JSONParser();
ProgressDialog progressDialog;
CommonIpadd commonIpadd;
SharedPreferences foridpreference;
SharedPreferences.Editor forideditor;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		username=(EditText)findViewById(R.id.username);
		password=(EditText)findViewById(R.id.password);
		login=(Button)findViewById(R.id.login);
		
		cd=new ConnectionDetector(getApplicationContext());
		commonIpadd=new CommonIpadd();
		 foridpreference = getApplicationContext().getSharedPreferences("IDS", Context.MODE_PRIVATE);
		
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				
				use=username.getText().toString();
				pass=password.getText().toString();
				
				if(use.equalsIgnoreCase("")||pass.equalsIgnoreCase("")){
					
					Toast.makeText(getApplicationContext(), "Fill Fields", Toast.LENGTH_SHORT).show();
				}
				
				else{
					isInternetPresent=false;
					isInternetPresent=cd.isConnectingToInternet();
					if(isInternetPresent){
						Tag="login";
						new check().execute();
						username.setText("");
						password.setText("");
						}
					
					              else if(!isInternetPresent){
						    		Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();}
				
				                    	
					   }
				}
			
		});
		
		
		
				
		
		
		
		
	}

	
	
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	class check extends AsyncTask<String, String, String>{
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog = new ProgressDialog(Login.this);
				progressDialog.setMessage("Logging In..");
				progressDialog.setIndeterminate(false);
				progressDialog.setCancelable(false);
				progressDialog.show();
			}

			@Override
			protected String doInBackground(String... arg0) {
				ArrayList<NameValuePair> param=new ArrayList<NameValuePair>();
					param.add(new BasicNameValuePair("tag", Tag));
					param.add(new BasicNameValuePair("username", use));
					param.add(new BasicNameValuePair("password", pass));
					
					JSONObject json=jsonParser.makeHttpRequest(commonIpadd.url, "POST", param);
					try 
					{
						final String Result=json.getString("result" );
						final String id=json.getString("id" );
						
						forideditor=foridpreference.edit();
						forideditor.remove("id");
						forideditor.clear();
						forideditor.commit();
						
						forideditor=foridpreference.edit();
						 forideditor.putString("id", id);
						 forideditor.commit();
						
						//with in doInBackground we must use runOnUiThread for display toast
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								Toast.makeText(getApplicationContext(), Result, Toast.LENGTH_LONG).show();
								
								Intent i=new Intent(Login.this,AddContact.class);
								startActivity(i);
							}
						});
						
					}
					catch (JSONException e) 
					{
							e.printStackTrace();
					}
					
					
			return null;
			}
			
			@Override
			protected void onPostExecute(String result) {
				progressDialog.dismiss();
			}
		
		
		}

}
