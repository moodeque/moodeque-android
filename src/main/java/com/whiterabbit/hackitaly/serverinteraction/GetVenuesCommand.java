package com.whiterabbit.hackitaly.serverinteraction;

import android.content.Context;
import android.content.Intent;
import com.whiterabbit.hackitaly.Utils.Constants;
import com.whiterabbit.postman.commands.JSONRestServerCommand;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONException;

/**
 * Created with IntelliJ IDEA.
 * User: fedepaol
 * Date: 7/21/12
 * Time: 10:23 AM
 * Command to log into the server
 */
public class GetVenuesCommand extends JSONRestServerCommand {
    private final String USERNAME = "username";
    private String mUserId;


    public GetVenuesCommand(String username){
        super(Action.GET);
        mUserId = username;
    }

    public GetVenuesCommand(){
        super();
    }

    @Override
    public String getJSONPayload() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void processJSONResult(String result, Context context) throws JSONException {
        // TODO
    }

    @Override
    protected String getUrl(Action a) {
        return Constants.SERVER_URL + "/venues/";
    }

    @Override
    protected void authenticate(HttpRequestBase req) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void fillIntent(Intent i) {
        i.putExtra(USERNAME, mUserId);

    }

    @Override
    protected void fromIntent(Intent i) {
        mUserId = i.getStringExtra(USERNAME);
    }
}
