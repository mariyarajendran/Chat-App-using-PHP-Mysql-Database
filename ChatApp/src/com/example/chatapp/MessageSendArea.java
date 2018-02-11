package com.example.chatapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
import android.database.DataSetObserver;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MessageSendArea extends Activity {
String slaveid,masterid,masterslaveid,slavemasterid,Tagload,uniquetotal;

int count=0;
SharedPreferences foridpreference;
String Tag,msg;
ConnectionDetector cd;
Boolean isInternetPresent=false;
JSONParser jsonParser=new JSONParser();
ProgressDialog progressDialog;
CommonIpadd commonIpadd;
JSONArray mas=null;
int tot;
ArrayList<String> arrayListid,arraylistmsg,bovalueid,statusarray;

HashMap<String, String> listvalues;
ArrayList<HashMap<String, String>> wholeList;

private static final String TAG = "ChatActivity";

private ChatArrayAdapter chatArrayAdapter;
private ListView listView;
private EditText chatText;
private Button buttonSend;

Intent intent;
private boolean side ;
private ScheduledExecutorService scheduleTaskExecutor;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_send_area);
		
		listView=(ListView)findViewById(R.id.listView1);
		
		arrayListid=new ArrayList<String>();
		arraylistmsg=new ArrayList<String>();
		bovalueid=new ArrayList<String>();
		statusarray=new ArrayList<String>();
		foridpreference=getApplicationContext().getSharedPreferences("IDS", 0);
		masterid=foridpreference.getString("id", null);
		wholeList=new ArrayList<HashMap<String,String>>();
		cd=new ConnectionDetector(getApplicationContext());
		commonIpadd=new CommonIpadd();
		
		
		 buttonSend = (Button) findViewById(R.id.buttonSend);

	        listView = (ListView) findViewById(R.id.listView1);

	        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage);
	     

	        chatText = (EditText) findViewById(R.id.chatText);
		
		
		
	
		Intent intent=getIntent();
		 slaveid=intent.getStringExtra("id");
		
		 masterslaveid=masterid+"."+slaveid;
		 
		 slavemasterid=slaveid+"."+masterid;
		 tot=Integer.valueOf(masterid)+Integer.valueOf(slaveid);
				 
		 uniquetotal=String.valueOf(tot);
		 
		 
		 Tagload="loadmessage";
   		 new loadmessage().execute();
		
   		 scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
		 scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
		        @Override
		        public void run() {
		            // Do stuff here!

		            runOnUiThread(new Runnable() {
		                @Override
		                public void run() {
		                    // Do stuff to update UI here!
		                	//
		                	Tagload="loadmessagefrequently";
		           		 new loadmessagesfre().execute();
		           		
		                // Toast.makeText(MessageSendArea.this, "Its been 5 seconds", Toast.LENGTH_SHORT).show();
		                   
		                   
		                  
		                 // chatArrayAdapter.notifyDataSetChanged();
		                    
		                }
		            });

		        }
		    }, 5, 7, TimeUnit.SECONDS); 
		 
		 
		  Intent i = getIntent();
			
			
			
		
		        chatText.setOnKeyListener(new OnKeyListener() {
		            public boolean onKey(View v, int keyCode, KeyEvent event) {
		                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
		                    sendChatMessage();
		                }
		                return false;
		            }
		        });
		        buttonSend.setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View arg0) {
		                sendChatMessage();
		            }
		        });

		        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		       // listView.setAdapter(chatArrayAdapter);

		        //to scroll the list view to bottom on data change
		        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
		            @Override
		            public void onChanged() {
		                super.onChanged();
		                listView.setSelection(chatArrayAdapter.getCount() - 1);
		            }
		        });
			
	}
	
	 private void sendChatMessage(){
		 msg=chatText.getText().toString();
		 if(msg.equalsIgnoreCase("")){
			 Toast.makeText(getApplicationContext(), "Please Enter Something", Toast.LENGTH_SHORT).show();
	        }
		 else{
			 Tag="sendmessage";
			
			 new check().execute();
		        chatArrayAdapter.add(new ChatMessage(true, msg));
		        chatText.setText("");
			
		 }
	       
	        
	       
	    }
	 
	
	

	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	class check extends AsyncTask<String, String, String>{
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			/*	progressDialog = new ProgressDialog(MessageSendArea.this);
				progressDialog.setMessage("Please Wait..");
				progressDialog.setIndeterminate(false);
				progressDialog.setCancelable(false);
				progressDialog.show();*/
			}

			@Override
			protected String doInBackground(String... arg0) {
				ArrayList<NameValuePair> param=new ArrayList<NameValuePair>();
					param.add(new BasicNameValuePair("tag", Tag));
					param.add(new BasicNameValuePair("masterslaveid", masterslaveid));
					param.add(new BasicNameValuePair("message", msg));
					param.add(new BasicNameValuePair("total", uniquetotal));
					
					JSONObject json=jsonParser.makeHttpRequest(commonIpadd.url, "POST", param);
					try 
					{
						final String Result=json.getString("result" );
						//final String id=json.getString("id" );
						
						
						//with in doInBackground we must use runOnUiThread for display toast
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
							//	Toast.makeText(getApplicationContext(), Result, Toast.LENGTH_LONG).show();
								chatText.setText("");
								
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
				//progressDialog.dismiss();
			}
		
		
		}

	    
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	class loadmessage extends AsyncTask<String, String, String>{
			
		@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog = new ProgressDialog(MessageSendArea.this);
				progressDialog.setMessage("Please wait..");
				progressDialog.setIndeterminate(false);
				progressDialog.setCancelable(false);
				progressDialog.show();
			}

			@Override
			protected String doInBackground(String... arg0) {
				ArrayList<NameValuePair> param=new ArrayList<NameValuePair>();
					param.add(new BasicNameValuePair("tag", Tagload));
					param.add(new BasicNameValuePair("loadmsgtotal", uniquetotal));
					
					
					
					JSONObject jsons=jsonParser.makeHttpRequest(commonIpadd.url, "POST", param);
					try 
					{
						
					mas=jsons.getJSONArray("res");
					
					 for(int i=0;i<mas.length();i++){
						 
						  JSONObject c = mas.getJSONObject(i);
						//  String allocid=c.getString("allocate_id");
						  String msgtxt=c.getString("message_text");
						  String msgid=c.getString("message_fromto");
						
						  
						 if(msgid.contains(masterslaveid)){
						  arraylistmsg.add(msgtxt);
						  arrayListid.add(msgid);
						 
						
						 bovalueid.add("true");
						
						  }
						  else{
							  arraylistmsg.add(msgtxt);
							  arrayListid.add(msgid);
						
							  bovalueid.add("false");
							  
						  }
						
						 
					 }
				
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								//Toast.makeText(getApplicationContext(), arraylistmsg+" Result "+arrayListid+" "+bovalueid, Toast.LENGTH_LONG).show();
								
								
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
				
				  for(int i=0;i<arrayListid.size();i++){
			             Log.i("for loop", "for loop");
						
			             boolean temp=Boolean.parseBoolean(bovalueid.get(i).toString());
			             side=temp;
			             chatArrayAdapter.add(new ChatMessage(side, arraylistmsg.get(i).toString()));
			          
					  
					  }
			arraylistmsg.clear();
			bovalueid.clear();
			arrayListid.clear();
		
			  listView.setAdapter(chatArrayAdapter);
			
			  chatArrayAdapter.notifyDataSetChanged();
			 
			  progressDialog.dismiss();
			}
			
		
		}
	
	
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	class loadmessagesfre extends AsyncTask<String, String, String>{
			
		/*	@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog = new ProgressDialog(MessageSendArea.this);
				progressDialog.setMessage("Logging In..");
				progressDialog.setIndeterminate(false);
				progressDialog.setCancelable(false);
				progressDialog.show();
			}*/

			@Override
			protected String doInBackground(String... arg0) {
				ArrayList<NameValuePair> param=new ArrayList<NameValuePair>();
					param.add(new BasicNameValuePair("tag", Tagload));
					param.add(new BasicNameValuePair("loadmsgtotal", uniquetotal));
					
					
					
					JSONObject jsons=jsonParser.makeHttpRequest(commonIpadd.url, "POST", param);
					try 
					{
						
					mas=jsons.getJSONArray("res");
					
					 for(int i=0;i<mas.length();i++){
						 
						  JSONObject c = mas.getJSONObject(i);
						//  String allocid=c.getString("allocate_id");
						  String msgtxt=c.getString("message_text");
						  String msgid=c.getString("message_fromto");
						  String state=c.getString("status");
						
						  
						 if(msgid.contains(masterslaveid)){
						/*  arraylistmsg.add(msgtxt);
						  arrayListid.add(msgid);
						 
						
						 bovalueid.add("true");*/
						
						  }
						  else{
							  arraylistmsg.add(msgtxt);
							  arrayListid.add(msgid);
						statusarray.add(state);
						bovalueid.add("false");
							  
						  }
						
						 
					 }
				
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								//Toast.makeText(getApplicationContext(), arraylistmsg+" Result "+arrayListid+" "+bovalueid, Toast.LENGTH_LONG).show();
								
								
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
				
				
				if(statusarray.isEmpty()){
					
					
				 }
				
				else{
					
				
				 for(int i=0;i<arrayListid.size();i++){
		             Log.i("for loop", "for loop");
					
		             boolean temp=Boolean.parseBoolean(bovalueid.get(i).toString());
		             side=temp;
		             chatArrayAdapter.add(new ChatMessage(side, arraylistmsg.get(i).toString()));
		          
				  
				  }
				 
				  listView.setAdapter(chatArrayAdapter);
					
				  chatArrayAdapter.notifyDataSetChanged();
		arraylistmsg.clear();
		bovalueid.clear();
		arrayListid.clear();
	statusarray.clear();
	
		
		  //Toast.makeText(getApplicationContext(), " msg found", Toast.LENGTH_SHORT).show();
			}
			}
		
		}
	
	
	
	
	 @Override
	    public void onBackPressed() {   
		 
		 count+=1;
		 if(count==1){
			
			 Toast.makeText(getApplicationContext(), "Press Again to exit", Toast.LENGTH_LONG).show();
		
		}
		 else{
			
			 scheduleTaskExecutor.shutdown(); 
			 finish();
			// Toast.makeText(getApplicationContext(), "stopped", Toast.LENGTH_LONG).show();
		 }
		
	 }
	 
	 

	
	
	}
