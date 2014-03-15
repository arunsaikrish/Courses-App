package com.example.coursesapp;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DepartmentsAdapter extends BaseAdapter {

	ArrayList<String> deptList = new ArrayList<String>();
	ArrayList<String> hodList = new ArrayList<String>();
	LayoutInflater inflater;
	Context context;
	int width,height;

	public DepartmentsAdapter(Context c, ArrayList<String> deptList,ArrayList<String> hodList,int width,int height) {
		this.deptList = deptList;
		this.hodList = hodList;
		this.width = width/2;
		this.height = height/2;
		
		context = c;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {

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
	
	public class ViewHolder
	{
		TextView bHod,bDept;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup root) {
		
		View vi = convertView;
		ViewHolder holder;
		
		if (convertView == null) {

			/****** Inflate tabitem.xml file for each row ( Defined below ) *******/
			vi = inflater.inflate(R.layout.department_row, null);
			holder = new ViewHolder();
			holder.bHod = (TextView) vi.findViewById(R.id.tvHodName);
			holder.bDept = (TextView) vi.findViewById(R.id.tvDepartmentName);
			vi.setTag(holder);
		} else
			holder = (ViewHolder) vi.getTag();
		
		
		holder.bHod.setText(hodList.get(pos));
		holder.bDept.setText(deptList.get(pos));
		
		return vi;

	}

}
