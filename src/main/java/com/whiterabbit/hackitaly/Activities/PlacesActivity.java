package com.whiterabbit.hackitaly.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.whiterabbit.hackitaly.R;
import com.whiterabbit.hackitaly.Storage.DbHelper;
import com.whiterabbit.hackitaly.Utils.PreferencesStore;
import com.whiterabbit.hackitaly.serverinteraction.GetVenuesCommand;
import com.whiterabbit.postman.SendingCommandException;
import com.whiterabbit.postman.ServerInteractionHelper;
import com.whiterabbit.postman.ServerInteractionResponseInterface;


public class PlacesActivity extends SherlockActivity implements ServerInteractionResponseInterface, AdapterView.OnItemClickListener {



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
            TextView description = (TextView) view.findViewById(R.id.places_list_element_description);

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


    public static final String GET_VENUES = "GetVenues";
    private static final String LOG_TO_VENUE = "LogToVenue";
    DbHelper mDb;
    PlacesListAdapter mAdapter;
    ListView mListView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.places_activity);
        mDb = new DbHelper(this);
        mListView = (ListView) findViewById(R.id.places_list);
        mListView.setOnItemClickListener(this);
        setSupportProgressBarIndeterminateVisibility(false);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         menu.add("Refresh")
                .setIcon(R.drawable.ic_refresh_inverse)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        updateList();
        return true;
    }

    public ServerInteractionHelper getInteractionHelper(){
        return ServerInteractionHelper.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();
        mDb.open();
        getInteractionHelper().registerEventListener(this, this);

        if(getInteractionHelper().isRequestAlreadyPending(GET_VENUES)){
            showGettingVenues();
        } else {
            getVenues();
            showGettingVenues();
        }

    }

    private void showGettingVenues(){
        setSupportProgressBarIndeterminateVisibility(true);
    }


    @Override
    protected void onPause() {
        super.onPause();
        getInteractionHelper().unregisterEventListener(this, this);

        mDb.close();
    }



    private void updateList(){
        if(getInteractionHelper().isRequestAlreadyPending(GET_VENUES) == false){
            getVenues();
        }
    }



    public void reload(){
        Cursor c  = mDb.getAllPlace();
        c.moveToFirst();
        mAdapter = new PlacesListAdapter(this, c);
        mListView.setAdapter(mAdapter);
        mAdapter.changeCursor(c);
    }


    private void getVenues(){
        GetVenuesCommand c = new GetVenuesCommand(PreferencesStore.getUsername(this));
        try {
            getInteractionHelper().sendCommand(this, c, GET_VENUES);
            showGettingVenues();
        } catch (SendingCommandException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    @Override
    public void onServerResult(String result, String requestId) {
        setSupportProgressBarIndeterminateVisibility(false);

        if(requestId.equals(GET_VENUES)){
            reload();
        }

        if(requestId.equals(LOG_TO_VENUE)){

        }
    }

    @Override
    public void onServerError(String result, String requestId) {
        setSupportProgressBarIndeterminateVisibility(false);
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Cursor c = mDb.getPlace(l);

        int venueId = c.getInt(DbHelper.PLACE_IDENTIFIER_COLUMN);

        Intent intent = new Intent(this, InVenueActivity.class);

        intent.putExtra(InVenueActivity.VENUE, venueId);
        startActivity(intent);
    }
}
