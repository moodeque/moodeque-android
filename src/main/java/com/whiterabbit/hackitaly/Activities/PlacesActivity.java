package com.whiterabbit.hackitaly.Activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.whiterabbit.hackitaly.serverinteraction.GetVenuesCommand;
import com.whiterabbit.postman.SendingCommandException;
import com.whiterabbit.postman.ServerInteractionHelper;
import com.whiterabbit.postman.ServerInteractionResponseInterface;


public class PlacesActivity extends ListActivity implements ServerInteractionResponseInterface {
    private static final String GET_VENUES = "GetVenues";
    private static final String LOG_TO_VENUE = "LogToVenue";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        }
    }


    private void getVenues(){
        GetVenuesCommand c = new GetVenuesCommand();
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
