package com.whiterabbit.hackitaly.Activities;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.whiterabbit.hackitaly.R;
import com.whiterabbit.hackitaly.Storage.DbHelper;
import com.whiterabbit.hackitaly.Utils.PreferencesStore;
import com.whiterabbit.hackitaly.serverinteraction.GetVenuesCommand;
import com.whiterabbit.postman.SendingCommandException;
import com.whiterabbit.postman.ServerInteractionHelper;
import com.whiterabbit.postman.ServerInteractionResponseInterface;


public class PlacesActivity extends ListActivity implements ServerInteractionResponseInterface {

    class PlacesListAdapter extends CursorAdapter {

        Context mContext;
        private LayoutInflater mInflater;

        public PlacesListAdapter(Context context, Cursor c) {
            super(context, c, true);
            mContext = context;
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }


        @Override
        protected synchronized void onContentChanged() {
            super.onContentChanged();
        }




        @Override
        public void bindView(View view, Context context, final Cursor cursor) {
            // Reset the view (in case it was recycled) and prepare for binding

            TextView name = (TextView) view.findViewById(R.id.places_list_element_name);
            TextView description = (TextView) view.findViewById(R.id.places_list_element_name);

            String plName = cursor.getString(DbHelper.PLACE_NAME_COLUMN);
            String plDescription = cursor.getString(DbHelper.PLACE_DESCRIPTION_COLUMN);

            final Long rowId = cursor.getLong(0);
            name.setText(plName);
            description.setText(plDescription);


        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return mInflater.inflate(R.layout.places_list_element, parent, false);
        }

    }


    private static final String GET_VENUES = "GetVenues";
    private static final String LOG_TO_VENUE = "LogToVenue";
    DbHelper mDb;
    PlacesListAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = new DbHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ServerInteractionHelper.getInstance().registerEventListener(this, this);

        if(ServerInteractionHelper.getInstance().isRequestAlreadyPending(GET_VENUES)){
            showGettingVenues();
        } else {
            getVenues();
        }

    }

    private void showGettingVenues(){
        // TODO
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    private void updateList(){
        if(ServerInteractionHelper.getInstance().isRequestAlreadyPending(GET_VENUES) == false){
            reload();
        }
    }



    public void reload(){
        String[] columns = new String[] { DbHelper.PLACE_NAME_KEY, DbHelper.PLACE_DESCRIPTION_KEY};
        int[] to = new int[] { R.id.places_list_element_name};
        Cursor c  = mDb.getAllPlace();
        c.moveToFirst();
        mAdapter = new PlacesListAdapter(this, c);
        setListAdapter(mAdapter);
        mAdapter.changeCursor(c);
    }


    private void getVenues(){
        GetVenuesCommand c = new GetVenuesCommand(PreferencesStore.getUsername(this));
        try {
            ServerInteractionHelper.getInstance().sendCommand(this, c, GET_VENUES);
        } catch (SendingCommandException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    @Override
    public void onServerResult(String result, String requestId) {
        if(requestId.equals(GET_VENUES)){
            // TODO Aggiornare
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onServerError(String result, String requestId) {
        // TODO Mostrare toast

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Pick item


    }
}
