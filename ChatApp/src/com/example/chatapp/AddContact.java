package com.example.chatapp;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.chatapp.Login.check;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddContact extends Activity {
EditText phone;
Button add,contact;
String Tag,phon,id;
ConnectionDetector cd;
Boolean isInternetPresent=false;
JSONParser jsonParser=new JSONParser();
ProgressDialog progressDialog;
CommonIpadd commonIpadd;
SharedPreferences foridpreference;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		
		phone=(EditText)findViewById(R.id.phone);
		add=(Button)findViewById(R.id.add);
        contact=(Button)findViewById(R.id.contact);
        
		cd=new ConnectionDetector(getApplicationContext());
		commonIpadd=new CommonIpadd();
		
		foridpreference=getApplicationContext().getSharedPreferences("IDS", 0);
		id=foridpreference.getString("id", null);
		
		
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				phon=phone.getText().toString();
				
				if(phon.equalsIgnoreCase("")){
					
					Toast.makeText(getApplicationContext(), "Enter Mobile Number First", Toast.LENGTH_SHORT).show();
				}
				
				else{
					
					isInternetPresent=false;
					isInternetPresent=cd.isConnectingToInternet();
					if(isInternetPresent){
						Tag="contact";
						new check().execute();
						}
					
					              else if(!isInternetPresent){
						    		Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();}
				
				                    	
					   }
					
				}
			
		});
		
		
		contact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(AddContact.this,Contacts.class);
				startActivity(intent);
			}
		});
		
	}

	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	class check extends AsyncTask<String, String, String>{
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog = new ProgressDialog(AddContact.this);
				progressDialog.setMessage("Please wait..");
				progressDialog.setIndeterminate(false);
				progressDialog.setCancelable(false);
				progressDialog.show();
			}

			@Override
			protected String doInBackground(String... arg0) {
				ArrayList<NameValuePair> param=new ArrayList<NameValuePair>();
					param.add(new BasicNameValuePair("tag", Tag));
					param.add(new BasicNameValuePair("userid", id));
					param.add(new BasicNameValuePair("phone", phon));
					
					
					JSONObject json=jsonParser.makeHttpRequest(commonIpadd.url, "POST", param);
					try 
					{
						final String Result=json.getString("result" );
						
						//with in doInBackground we must use runOnUiThread for display toast
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								Toast.makeText(getApplicationContext(), Result , Toast.LENGTH_LONG).show();
							phone.setText("");
								
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
