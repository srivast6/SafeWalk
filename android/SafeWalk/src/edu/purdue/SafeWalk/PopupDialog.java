package edu.purdue.SafeWalk;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;


public class PopupDialog extends DialogFragment {
	private Context context;
	private int index;
	public PopupDialog(Context context, int index){
		super();
		this.context = context;
		this.index = index;
		
	}

	
	   /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onPopUpAcceptClick(View v);
        public void onPopUpMessageClick(View v);
        public void onPopUpCallClick(View v);
    }
    
    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;
    
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
    
    public void onPopUpCallClick(View v){
    	Toast.makeText(this.getActivity(), "Call Me!", Toast.LENGTH_SHORT);
    	
    }
    
    
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	LayoutInflater inflater = this.getActivity().getLayoutInflater();
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(inflater.inflate(R.layout.pop_up, null));
        return builder.create();
    }
}


