package me.naidenov.organizer;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NotesAdapter extends ArrayAdapter<Note> {
	private Context context;
	private int resource;
	private List<Note> data;

	public NotesAdapter(Context context, int resource, List<Note> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.resource = resource;
		this.data = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
		
		View row = inflater.inflate(resource, parent, false);
		TextView tv = (TextView) row.findViewById(R.id.textView_title_row);
		tv.setText(data.get(position).getTitle());
		
		return row;
	}
}
