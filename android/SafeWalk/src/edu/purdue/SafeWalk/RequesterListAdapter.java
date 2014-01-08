package edu.purdue.SafeWalk;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RequesterListAdapter extends ArrayAdapter<String>{
    private final Context context;
    private final ArrayList<String> itemsArrayList;
    private final ArrayList<Requester> requests;

    public RequesterListAdapter(Context context, ArrayList<String> itemsArrayList, ArrayList<Requester> requests) {
        super(context, R.layout.list_item_view, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
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
        labelView.setText("Time Of Request:"+ requester.getTimeOfRequest());

        // 5. retrn rowView
        return rowView;
    }
}

