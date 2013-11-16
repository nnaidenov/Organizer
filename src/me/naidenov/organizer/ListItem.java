package me.naidenov.organizer;

import me.naidenov.organizer.TwoTextArrayAdapter.RowType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ListItem implements Item {
	private final String str1;
	private final String str2;
	private final LayoutInflater inflater;

	public ListItem(LayoutInflater inflater, String text1, String text2) {
		this.str1 = text1;
		this.str2 = text2;
		this.inflater = inflater;
	}

	@Override
	public int getViewType() {
		return RowType.LIST_ITEM.ordinal();
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		View view;
		if (convertView == null) {
			view = (View) inflater.inflate(R.layout.my_list_item, null);
			// Do some initialization
		} else {
			view = convertView;
		}

		TextView text1 = (TextView) view.findViewById(R.id.list_content1);
		TextView text2 = (TextView) view.findViewById(R.id.list_content2);
		text1.setText(str1);
		text2.setText(str2);

		return view;
	}

}