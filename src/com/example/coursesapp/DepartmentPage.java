package com.example.coursesapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

public class DepartmentPage extends Activity {
	CoursesAdapter listAdapter;
	ExpandableListView expListView;
	private ArrayList<String> listHeader = new ArrayList<String>();
	private HashMap<String, ArrayList<String>> listItems = new HashMap<String, ArrayList<String>>();
	private HashMap<String, String> courseToCredits = new HashMap<String, String>();
	private String deptName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dept_page);
		expListView = (ExpandableListView) findViewById(R.id.elvCourses);
		deptName = getIntent().getStringExtra("dept");
		getDepartmentCourses();

		
	}

	private void getDepartmentCourses() {
		final class GetDetails extends AsyncTask<String, String, Boolean> {
			ProgressDialog pdia;
			
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pdia = new ProgressDialog(DepartmentPage.this);
				pdia.setMessage("Loading Data...");
				pdia.show();
			}

			@Override
			protected Boolean doInBackground(String... arg0) {
				
				for(String url:arg0)
				{
					HttpClient httpclient = new DefaultHttpClient();
					
					HttpGet httpget = new HttpGet(url);

					try 
					{						
						HttpResponse response = httpclient.execute(httpget);
						HttpEntity entity = response.getEntity();
						String requestresult = EntityUtils.toString(entity);
						Log.d("Response from dept", requestresult);
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
			protected void onPostExecute(Boolean result) {				
				super.onPostExecute(result);
				if(result.booleanValue())
				{
					listAdapter = new CoursesAdapter(DepartmentPage.this, listHeader, listItems,courseToCredits);
					expListView.setAdapter(listAdapter);
					pdia.dismiss();
				}
			}

		}
		GetDetails g = new GetDetails();
		g.execute("http://www.courseshub.in/departments/"+deptName+".json");
	}

	public Boolean parseJson(String result) 
	{
		try 
		{
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("course_listing");
			JSONArray courseList;
			ArrayList<String> courseNames;			
			listHeader.clear();
			listItems.clear();
			courseToCredits.clear();
			for(int i=0;i<jsonArray.length();i++)
			{
				listHeader.add("\t\tSemester : " + jsonArray.getJSONObject(i).getString("semester"));
				courseList = jsonArray.getJSONObject(i).getJSONArray("course_list");
				courseNames = new ArrayList<String>();
				//Log.d("Headers",listHeader.get(i));
					for(int j=0;j<courseList.length();j++)
					{						
						courseNames.add(courseList.getJSONObject(j).getString("name"));
						courseToCredits.put(courseNames.get(j), "Credits : " + courseList.getJSONObject(j).getString("credits"));
					//	Log.d("Courses",courseNames.get(j));
					}
				listItems.put(listHeader.get(i), courseNames);	
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
