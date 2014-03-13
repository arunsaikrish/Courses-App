package com.example.coursesapp;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class DepartmentsActivity extends Activity
{
ListView lv;
ArrayList<String> deptList = new  ArrayList<String>();
ArrayList<String> hodList = new  ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_departments);
		lv = (ListView)findViewById(R.id.lvDepartmentsList);
		loadDepts();
	}
	

	public void loadDepts()
	{
		final class LoadData extends AsyncTask<String, Void, Boolean>
		{
			ProgressDialog pdia;
			
			@Override
			protected void onPreExecute() 
			{
				super.onPreExecute();
				pdia = new ProgressDialog(DepartmentsActivity.this);
				pdia.setMessage("Loading Data...");
				pdia.show();
			}

			@Override
			protected Boolean doInBackground(String... urls) 
			{
				for(String url:urls)
				{
					HttpClient httpclient = new DefaultHttpClient();
				
					HttpGet httpget = new HttpGet(url);

					try 
					{						
						Log.d("About to Send", "sending");
						HttpResponse response = httpclient.execute(httpget);
						Log.d("Sending", "sending");
						HttpEntity entity = response.getEntity();
						String requestresult = EntityUtils.toString(entity);
						Log.d("Response", requestresult);
						return parseJson(requestresult);
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
					}

				}
				return false;
			}
			
			@Override
			protected void onPostExecute(Boolean result) 
			{
				super.onPostExecute(result);
				if(result.booleanValue())
					{
					//Toast.makeText(DepartmentsActivity.this, "Data Loaded", Toast.LENGTH_SHORT).show();
					DisplayMetrics displaymetrics = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
					int height = displaymetrics.heightPixels;
					int width = displaymetrics.widthPixels;
					
					DepartmentsAdapter adp = new DepartmentsAdapter(DepartmentsActivity.this,deptList, hodList,width,height);
					lv.setAdapter(adp);
					}
				else
					Toast.makeText(DepartmentsActivity.this, "Loading Failed", Toast.LENGTH_SHORT).show();
				pdia.dismiss();
				
				
			}
			
			
		}
		
		LoadData ld = new LoadData();
		ld.execute("http://courses.nitt.edu/departments.json");
		
	}
	
	
	
	boolean parseJson(String result)
	{
		try 
		{
			JSONArray jsonArray = new JSONArray(result);
			deptList.clear();
			hodList.clear();
			for(int i=0;i<jsonArray.length();i++)
			{
				deptList.add(jsonArray.getJSONObject(i).getString("name"));
				hodList.add(jsonArray.getJSONObject(i).getString("hod"));
				Log.d("LIST", deptList.get(i) + "-->" + hodList.get(i));
			}
			return true;
		}		
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return false;
	}
}
