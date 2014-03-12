package com.example.coursesapp;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class DepartmentsAdapter extends BaseAdapter{

	ArrayList<String> deptList = new ArrayList<String>();
	ArrayList<String> hodList = new ArrayList<String>() ;
	LayoutInflater inflater;
	Context context;

	public DepartmentsAdapter(Context c,ArrayList<String> deptList,ArrayList<String> hodList)
	{
		this.deptList = deptList;
		this.hodList = hodList;
		for(int i=0;i < deptList.size();i++)
			Log.i("LIST", deptList.get(i) + "--" +i);
		for(int i=0;i < hodList.size();i++)
			Log.d("LIST", hodList.get(i) + "--" +i);
		context = c;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}
	
	
	@Override
	public int getCount() 
	{
		
		return deptList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return deptList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	
	
	@Override
	public View getView(int pos, View view, ViewGroup root) 
	{
		
		View vi = view;
		Button tvHod,tvDept;
		

		if (view == null) 
		{

			/****** Inflate tabitem.xml file for each row ( Defined below ) *******/
			vi = inflater.inflate(R.layout.department_row, null);
			tvDept = (Button) vi.findViewById(R.id.tvDepartmentName);
			tvHod = (Button) vi.findViewById(R.id.tvHodName);
			
			//holder.tvDept.setMaxWidth(vi.getWidth()/2);
			//holder.tvHod.setMaxWidth(vi.getWidth()/2);
			
			tvDept.setText(deptList.get(pos));
			Log.i("Adding", deptList.get(pos) + "--" +pos);
			tvHod.setText(hodList.get(pos));
		} 
		else
			Log.e("Error","errror");
			

		return vi;
			
			
		}
		
	}


