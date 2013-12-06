package edu.purdue.app.map;

import java.util.List;

import edu.purdue.app.R;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MapDrawerListAdapter extends BaseAdapter {
	
	private Context context;
	private List<String> list;
	private Resources res;
	
	public MapDrawerListAdapter(Context context, List<String> list, Resources res) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.map_drawer_item, parent, false);
		TextView tv = (TextView) row.findViewById(R.id.map_drawer_item_textview);
		
		tv.setText(list.get(position));
		
		return row;
	}
	
	

}
