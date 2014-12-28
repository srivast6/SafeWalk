package edu.purdue.safewalk;

import android.content.Context;
import android.preference.PreferenceManager;

import edu.purdue.safewalk.Interfaces.SafeWalkAPIServiceInterface;
import retrofit.RestAdapter;

/**
 * Created by kyle on 12/27/14.
 */
public class SafeWalkAPI {

    public static SafeWalkAPIServiceInterface getAPI(Context context){
        String hostname = PreferenceManager.getDefaultSharedPreferences(context)
                .getString("pref_server", context.getString(R.string.pref_server_default));

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(hostname)
                .build();

        SafeWalkAPIServiceInterface service = restAdapter.create(SafeWalkAPIServiceInterface.class);
        return service;
    }

}
