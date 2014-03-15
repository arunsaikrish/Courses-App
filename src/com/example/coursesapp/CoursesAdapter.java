package com.example.coursesapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class CoursesAdapter extends BaseExpandableListAdapter{

	private Context context;
	private ArrayList<String> listHeader;
	private HashMap<String	, ArrayList<String>> listItems;
	private HashMap<String,String> courseToCredits;
	
	public CoursesAdapter(Context context,ArrayList<String> listHeader,HashMap<String , ArrayList<String> > listItems,HashMap<String,String> courseToCredits)
	{
		this.context=context;
		this.listHeader=listHeader;
		this.listItems=listItems;
		this.courseToCredits = courseToCredits;
	}
	
	@Override
	public Object getChild(int groupPos, int childPos) 
	{
		return listItems.get(listHeader.get(groupPos)).get(childPos);
	}

	@Override
	public long getChildId(int arg0, int arg1) 
	{
		return arg1;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLsstChild, View convertView,ViewGroup parent) 
	{
		String childText = (String)getChild(groupPosition, childPosition);
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.course_row, null);
		}
		TextView courseName = (TextView) convertView.findViewById(R.id.tvCourseName);
		TextView credits = (TextView) convertView.findViewById(R.id.tvCredits);
		courseName.setText(childText);
		credits.setText(courseToCredits.get(childText));
		
		return convertView;
	}
	

	@Override
	public int getChildrenCount(int groupPosition) 
	{
		return listItems.get(listHeader.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) 
	{
		return listHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() 
	{
		return listHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) 
	{
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) 
	{
		String headerTitle = (String) getGroup(groupPosition);
		if(convertView == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_header_sem, null);	
		}
		TextView headerName = (TextView)convertView.findViewById(R.id.tvListHeaderSem);
		headerName.setText(headerTitle);
		if(groupPosition%2 == 0)
			headerName.setBackgroundColor(Color.parseColor("#CAEDFF"));
		else
			headerName.setBackgroundColor(Color.parseColor("#94DBFF"));
		return convertView;
	}

	@Override
	public boolean hasStableIds() 
	{
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) 
	{
		return true;
	}

}
