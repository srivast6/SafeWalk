package edu.purdue.safewalk.Interfaces;

import java.util.List;

import edu.purdue.safewalk.DataStructures.Requester;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.Callback;

/**
 * Created by kyle on 12/27/14.
 */
public interface SafeWalkAPIServiceInterface {
    @POST("/request")
    void newRequest(@Body Requester requester, Callback<Requester> cb);

    @GET("/request")
    void getRequests( Callback<List<Requester>> cb);
}


