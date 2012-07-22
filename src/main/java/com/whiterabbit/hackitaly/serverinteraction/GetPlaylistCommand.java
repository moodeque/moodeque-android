package com.whiterabbit.hackitaly.serverinteraction;

import android.content.Context;
import android.content.Intent;
import com.whiterabbit.hackitaly.Storage.DbHelper;
import com.whiterabbit.hackitaly.Utils.Constants;
import com.whiterabbit.hackitaly.Utils.PreferencesStore;
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
public class GetPlaylistCommand extends JSONRestServerCommand {
    private final String VENUE = "venue";
    private long mVenue;
    private DbHelper mDb;



    public GetPlaylistCommand(long venue){
        super(Action.GET);
        mVenue = venue;
    }

    public GetPlaylistCommand(){
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
        JSONArray venues = jsonResponse.getJSONArray("playlist");

        mDb.open();
        mDb.removeAllSong();
        int last = venues.length() - 1;

       for (int i = 0; i < venues.length(); i++) {
            JSONObject jo = venues.getJSONObject(i);
            Long id = jo.getLong("id");
            String name = jo.getString("title");
            String artist = jo.getString(DbHelper.SONG_ARTIST_KEY);

            mDb.addSong(id, name, artist);

            if(i == last){
                PreferencesStore.setCurrentlyPlaying(context, name);
            }
        }
        mDb.close();
    }



    @Override
    protected String getUrl(Action a) {
        return Constants.SERVER_URL + "/venues/" + mVenue + "/playlist";
    }

    @Override
    protected void authenticate(HttpRequestBase req) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void fillIntent(Intent i) {
        i.putExtra(VENUE, mVenue);

    }

    @Override
    protected void fromIntent(Intent i) {
        mVenue = i.getLongExtra(VENUE, -1);
    }


    @Override
    public boolean fakeExecute(Context c){
        if(Constants.TESTING){
            mDb = new DbHelper(c);     // I know this is not the healthiest practice. On the other hand, I am short of time

            mDb.open();
            mDb.removeAllSong();
            PreferencesStore.setCurrentlyPlaying(c, "Fava");


            mDb.addSong(Long.valueOf(0), "Fava", "Gigi d'alessio");
            mDb.addSong(Long.valueOf(1), "Rava1", "Autore 1");
            mDb.addSong(Long.valueOf(2), "Rava2", "Autore 2");
            mDb.close();

            return true;
        }
        return false;
    }
}
