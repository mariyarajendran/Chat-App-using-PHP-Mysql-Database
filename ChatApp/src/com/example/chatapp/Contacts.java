package com.example.chatapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Contacts extends Activity {
	String Tag,use,pass,id;
	ConnectionDetector cd;
	Boolean isInternetPresent=false;
	JSONParser jsonParser=new JSONParser();
	ProgressDialog progressDialog;
	CommonIpadd commonIpadd;
	SharedPreferences foridpreference;
	
	ListView lstview;

	JSONArray peoples;
	ArrayList<String>  dt;
	HashMap<String,String> listvalues;
	 ArrayList<HashMap<String, String>> wholeList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		
		 dt=new ArrayList<String>();
		
		 wholeList=new ArrayList<HashMap<String,String>>();
		 
		lstview=(ListView)findViewById(R.id.lstview);
		 
		cd=new ConnectionDetector(getApplicationContext());
		commonIpadd=new CommonIpadd();
		
		foridpreference=getApplicationContext().getSharedPreferences("IDS", 0);
		id=foridpreference.getString("id", null);
		
		isInternetPresent=false;
		isInternetPresent=cd.isConnectingToInternet();
		if(isInternetPresent){
			Tag="privatecontact";
			new check().execute();
			}
		
		              else if(!isInternetPresent){
			    		Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();}
	
	              
		
		
		lstview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Contacts.this,MessageSendArea.class);
				 String id = ((TextView) v.findViewById(R.id.phone)).getText().toString();
				 intent.putExtra("id", id);
				 startActivity(intent);
				
			}
			
		});
		
		
		
		
	}

	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	class check extends AsyncTask<String, String, String>{
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog = new ProgressDialog(Contacts.this);
				progressDialog.setMessage("Please Wait..");
				progressDialog.setIndeterminate(false);
				progressDialog.setCancelable(false);
				progressDialog.show();
			}

			@Override
			protected String doInBackground(String... arg0) {
				ArrayList<NameValuePair> param=new ArrayList<NameValuePair>();
					param.add(new BasicNameValuePair("tag", Tag));
					param.add(new BasicNameValuePair("id", id));
					
					
					JSONObject json=jsonParser.makeHttpRequest(commonIpadd.url, "POST", param);
					try 
					{
					//	final String resu=json.getString("res");
						   peoples=json.getJSONArray("res");
				            
				           for(int i=0;i<peoples.length();i++){
				                JSONObject c = peoples.getJSONObject(i);
				                
				              
				              
				                String conname = c.getString("contactname");
				                String phone = c.getString("contactid");
				                String conid = c.getString("phoneno");
				              
				               
				                  
				                listvalues=new HashMap<String, String>();
				               
				               
				                listvalues.put("contactname",conname);
				                listvalues.put("contactid",conid);
				                listvalues.put("phoneno",phone);
				                
				                wholeList.add(listvalues);
				            
				            }
				           
				         
						
						//with in doInBackground we must use runOnUiThread for display toast
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								//Toast.makeText(getApplicationContext(), "Result ", Toast.LENGTH_LONG).show();
								//textView1.setText(Result);
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
				  String[] from=new String[]{
		        			
		        		   "phoneno", 
		        		   "contactid",
		        		   "contactname" 
		        			 
		        		 } ;          
		        		 
		        		 
		        		 int[] to=new int[]{
		        				 R.id.phone,
		        				 R.id.id,
		        				 R.id.name,
		        				
		        		 };
		           
		           ListAdapter adapter = new SimpleAdapter(
		                    Contacts.this, wholeList, R.layout.listv,
		                    from,
		                   to
		            );
		 
		            lstview.setAdapter(adapter);
		            
				
				progressDialog.dismiss();
			}
		
		
		}


}
