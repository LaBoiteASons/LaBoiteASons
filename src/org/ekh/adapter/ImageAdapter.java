package org.ekh.adapter;

import java.lang.reflect.Field;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.ekh.laboiteasons.R;

public class ImageAdapter extends BaseAdapter {
	private Context context;
	private final String[] mobileValues;

	public static int getResId(String variableName, Class<?> c) {
	    Field field = null;
	    int resId = 0;
	    try {
	        field = c.getField(variableName);
	        try {
	            resId = field.getInt(null);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return resId;
	}
	
	public ImageAdapter(Context context, String[] mobileValues) {
		this.context = context;
		this.mobileValues = mobileValues;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		if (convertView == null) {

			gridView = new View(context);

			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.item, null);

			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			textView.setText(mobileValues[position]);

			// set image based on selected text
			ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);

			String mobile = mobileValues[position];

			for(String element : mobileValues){
				if (mobile.equals(element)) {
					imageView.setImageResource(getResId(element, R.drawable.class));
				}
			}

		} else {
			gridView = (View) convertView;
		}

		return gridView;
	}

	@Override
	public int getCount() {
		return mobileValues.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
