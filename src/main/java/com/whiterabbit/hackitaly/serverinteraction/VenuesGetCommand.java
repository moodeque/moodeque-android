package com.whiterabbit.hackitaly.serverinteraction;

import android.content.Context;
import android.content.Intent;
import com.whiterabbit.hackitaly.Utils.Constants;
import com.whiterabbit.postman.commands.JSONRestServerCommand;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: fedepaol
 * Date: 7/21/12
 * Time: 10:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class VenuesGetCommand extends JSONRestServerCommand {



    @Override
    public String getJSONPayload() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void processJSONResult(String result, Context context) throws JSONException {
        JSONArray jsonResponse = new JSONArray(result);
        JSONObject userJson = jsonResponse.getJSONObject(0);
        //StoreUtils.setProfile(desc, c);
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
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void fromIntent(Intent i) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}