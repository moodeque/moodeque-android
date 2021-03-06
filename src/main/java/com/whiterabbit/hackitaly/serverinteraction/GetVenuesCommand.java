package com.whiterabbit.hackitaly.serverinteraction;

import android.content.Context;
import android.content.Intent;
import com.whiterabbit.hackitaly.Storage.DbHelper;
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
 * Time: 10:23 AM
 * Command to log into the server
 */
public class GetVenuesCommand extends JSONRestServerCommand {
    private final String USERNAME = "username";
    private String mUserId;
    private DbHelper mDb;



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
        mDb = new DbHelper(context);     // I know this is not the healthiest practice. On the other hand, I am short of time
        JSONObject jsonResponse = new JSONObject(result);
        JSONArray venues = jsonResponse.getJSONArray("venues");

        mDb.open();
        mDb.removeAllPlace();

       for (int i = 0; i < venues.length(); i++) {
            JSONObject jo = venues.getJSONObject(i);
            Long id = jo.getLong("venueid");
            String name = jo.getString(DbHelper.PLACE_NAME_KEY);
            String description = jo.getString(DbHelper.PLACE_DESCRIPTION_KEY);

            mDb.addPlace(id, name, description, Long.valueOf(0), Long.valueOf(0));



        }
        mDb.close();
    }



    @Override
    protected String getUrl(Action a) {
        return Constants.SERVER_URL + "/venues";
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

    @Override
    public boolean fakeExecute(Context c){
        if(Constants.TESTING){
            mDb = new DbHelper(c);     // I know this is not the healthiest practice. On the other hand, I am short of time

            mDb.open();
            mDb.removeAllPlace();

            mDb.addPlace(Long.valueOf(0), "Hackitaly", "Siamo qua", Long.valueOf(0), Long.valueOf(0));
            mDb.addPlace(Long.valueOf(1), "Pisa", "Speriamo di tornacci", Long.valueOf(0), Long.valueOf(0));
            mDb.addPlace(Long.valueOf(2), "Empoli", "Tanta brutta gente", Long.valueOf(0), Long.valueOf(0));
            mDb.close();

            return true;
        }
        return false;
    }
}
