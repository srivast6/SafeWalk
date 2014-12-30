package edu.purdue.safewalk.Adapters;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.purdue.safewalk.R;
import edu.purdue.safewalk.DataStructures.Requester;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RequesterListAdapter extends BaseAdapter{
    private final Context context;
    private final List<Requester> requests;

    public RequesterListAdapter(Context context, List<Requester> requests) {
        super();
        this.context = context;
        this.requests = requests;
    }

    // This sets the values of the Elements in list_item_view, each item in itemsArrayList will have its own 
    // section in the list
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater 
        LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.list_item_view, parent, false);

        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.secondLine);
        TextView valueView = (TextView) rowView.findViewById(R.id.thirdLine);
        
        Requester requester = requests.get(position);
        // 4. Set the text for textView 
        valueView.setText("Name:"+requester.getName());
        DateFormat df = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss.SSS'Z'");
        String date = "";
        try {
            date = df.parse(requester.getCreatedAt()).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        labelView.setText("Time Of Request: "+ date );

        // 5. retrn rowView
        return rowView;
    }

	@Override
	public int getCount() {
		return requests.size();
	}

	@Override
	public Object getItem(int position) {
		return requests.get(position);
	}

	/**
	 * Not really being used...
	 */

	public long getItemId(int position) {
		return 0;
	}

}

