package me.naidenov.organizer;

import me.naidenov.organizer.TwoTextArrayAdapter.RowType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class Header implements Item {
	private final String name;

	public Header(LayoutInflater inflater, String name) {
		this.name = name;
	}

	@Override
	public int getViewType() {
		return RowType.HEADER_ITEM.ordinal();
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		View view;
		if (convertView == null) {
			view = (View) inflater.inflate(R.layout.header, null);
			// Do some initialization
		} else {
			view = convertView;
		}

		TextView text = (TextView) view.findViewById(R.id.separator);
		text.setText(name);

		return view;
	}

}