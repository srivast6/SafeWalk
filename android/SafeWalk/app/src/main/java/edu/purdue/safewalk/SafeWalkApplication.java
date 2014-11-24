package edu.purdue.safewalk;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by etemplin on 11/20/14.
 */
public class SafeWalkApplication extends Application {
    public void onCreate() {
        Parse.initialize(this, "GjlGGMBrZc8ND6K9NKKzkLdSC3iWgAvBFuAzZAYO", "8X5KQCJH4SZGtJInDhyzjgScF7I4VG8V2PKpMZP8");
    }

}
