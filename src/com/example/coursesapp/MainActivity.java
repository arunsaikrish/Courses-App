package com.example.coursesapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	//String HOST_URL="http://10.1.63.164:3000";
	
	SharedPreferences sharedpreferences;
	public static final String MyPREFERENCES = "MyPrefs" ;

	public static final String HOST_URL="http://courses.nitt.edu";
	
	
	EditText username,password;
	Button submit;
	
	String userNameGlobal,passwordGlobal;
	
	ProgressDialog pdia;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		username=(EditText)findViewById(R.id.uname);
		password=(EditText)findViewById(R.id.pwd);
		
		password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		
		submit=(Button)findViewById(R.id.submit);
		
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				//Toast.makeText(getApplicationContext(), "HI", Toast.LENGTH_LONG).show();
				
				//courses.nitt.edu/login   post    username: password: 
				//response : status ,  user_id
				
				String uname=username.getEditableText().toString();
				String pwd=password.getEditableText().toString();
				
				if(uname.equals("")||pwd.equals("")){
					
					Toast.makeText(MainActivity.this, "Please enter username and password", Toast.LENGTH_LONG).show();
					username.setText("");
					password.setText("");
					
				}else {
					
					userNameGlobal=uname;
					passwordGlobal=pwd;
					new FetchTask().execute();
					
				}
				
			}
		});
		
	}
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		String value = sharedpreferences.getString("user_id",null);
		Toast.makeText(MainActivity.this, value, Toast.LENGTH_LONG).show();
		
		if (value != null) {
		    // the key does not exist
			
			Intent intent;
			
			intent = new Intent(MainActivity.this, DepartmentsActivity.class);
			startActivity(intent);
			
			finish();
			
		} 
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@SuppressLint("NewApi")
	public class FetchTask extends AsyncTask<Void, Void, JSONObject> {
	    @Override
	    protected JSONObject doInBackground(Void... params) {
	        try {
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(HOST_URL+"/login.json");
	            
	           
	            
	            // Add your data
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	            
	            
	            nameValuePairs.add(new BasicNameValuePair("username", userNameGlobal));
	            nameValuePairs.add(new BasicNameValuePair("password", passwordGlobal));
	          
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            
	         
	            

	            // Execute HTTP Post Request
	            HttpResponse response = httpclient.execute(httppost);
	            
	           // Toast.makeText(MainActivity.this, "EXECUTE", Toast.LENGTH_LONG).show();

	            
	            
	            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
	            StringBuilder sb = new StringBuilder();
	            sb.append(reader.readLine() + "\n");
	            String line = "0";
	           
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            reader.close();
	            String login_res = sb.toString();
	            
	            
	            //Toast.makeText(MainActivity.this, login_res, Toast.LENGTH_LONG).show();
	            Log.d("result", login_res);
	            // parsing data
	            return new JSONObject(login_res);
	        } catch (Exception e) {
	            e.printStackTrace();
	            try {
					return new JSONObject("{\"status\":\"5\"}");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            return null;
	        }
	    }
	    
	    @Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			pdia = new ProgressDialog(MainActivity.this);
			pdia.setMessage("Logging In ...");
			pdia.show();
		}

	    @Override
	    protected void onPostExecute(JSONObject result) {
	    	
	    	System.out.println("Inside on post execute");
	    	
	    	pdia.dismiss();
	    	
	    	String res;
	    	
	    	try {
				//Toast.makeText(MainActivity.this,"HII"+result.getString("status"), Toast.LENGTH_LONG).show();
				
				res = result.getString("status");
				
				if(res.equals("1")){
					
					//Login
					Log.d("Login status ", res);
					
					Editor edit = sharedpreferences.edit();
					edit.putString("user_id", result.getString("user_id"));
					edit.apply(); 
					
					Intent intent;
					
					intent = new Intent(MainActivity.this, DepartmentsActivity.class);
					startActivity(intent);
					
				}else if(res.equals("0")){
					
					Log.d("Login status ", res);
					Toast.makeText(MainActivity.this, "Wrong Password ... Try Again !", Toast.LENGTH_LONG).show();
					
					password.setText("");
					
				}else if(res.equals("2")){
					
					Log.d("Login status ", res);
					Toast.makeText(MainActivity.this, "Authenticate your account and try again ...", Toast.LENGTH_LONG).show();
					
					password.setText("");
					
				}else {
					
					Log.d("Login status ", res);
					Toast.makeText(MainActivity.this, "An error occured while logging in ... Please try again!", Toast.LENGTH_LONG).show();
					
					password.setText("");
					
				}			
				
				
			} catch (JSONException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
	    }
	}
	

}
