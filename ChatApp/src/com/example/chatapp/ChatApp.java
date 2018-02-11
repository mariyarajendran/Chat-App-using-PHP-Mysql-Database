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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChatApp extends Activity {
EditText name,phone,username,password;
ConnectionDetector cd;
Boolean isInternetPresent=false;
JSONParser jsonParser=new JSONParser();
ProgressDialog progressDialog;
CommonIpadd commonIpadd;
String Tag,nam,pho,use,pass;
Button signup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_app);
		
		name=(EditText)findViewById(R.id.name);
		phone=(EditText)findViewById(R.id.phone);
		username=(EditText)findViewById(R.id.username);
		password=(EditText)findViewById(R.id.password);
		signup=(Button)findViewById(R.id.signup);
		
		cd=new ConnectionDetector(getApplicationContext());
		commonIpadd=new CommonIpadd();
		
		
		
		signup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				nam=name.getText().toString();
				pho=phone.getText().toString();
				use=username.getText().toString();
				pass=password.getText().toString();
				
				if(nam.equalsIgnoreCase("")||pho.equalsIgnoreCase("")||use.equalsIgnoreCase("")||pass.equalsIgnoreCase("")){
					
					Toast.makeText(getApplicationContext(), "Fill Fields", Toast.LENGTH_SHORT).show();
				}
				
				else{
					isInternetPresent=false;
					isInternetPresent=cd.isConnectingToInternet();
					if(isInternetPresent){
						Tag="insert";
						new check().execute();
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
			progressDialog = new ProgressDialog(ChatApp.this);
			progressDialog.setMessage("Logging In..");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			ArrayList<NameValuePair> param=new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("tag", Tag));
				param.add(new BasicNameValuePair("name", nam));
				param.add(new BasicNameValuePair("phonenumber", pho));
				param.add(new BasicNameValuePair("username", use));
				param.add(new BasicNameValuePair("password", pass));
				
				JSONObject json=jsonParser.makeHttpRequest(commonIpadd.url, "POST", param);
				try 
				{
					final String Result=json.getString("result" );
					
					//with in doInBackground we must use runOnUiThread for display toast
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), Result , Toast.LENGTH_LONG).show();
							name.setText("");
							phone.setText("");
							username.setText("");
							password.setText("");
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
