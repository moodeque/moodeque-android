package com.whiterabbit.hackitaly.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.whiterabbit.hackitaly.R;
import com.whiterabbit.hackitaly.serverinteraction.LoginCommand;
import com.whiterabbit.hackitaly.serverinteraction.MoodCommandFactory;
import com.whiterabbit.postman.SendingCommandException;
import com.whiterabbit.postman.ServerInteractionHelper;
import com.whiterabbit.postman.ServerInteractionResponseInterface;


public class LoginActivity extends Activity implements View.OnClickListener, ServerInteractionResponseInterface {

    private static final String LOGIN_REQUEST = "LoginRequest";



    Button mGo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ServerInteractionHelper.initWithCommandFactory(new MoodCommandFactory());
        setContentView(R.layout.login);
        mGo = (Button) findViewById(R.id.long_go_button);
        mGo.setOnClickListener(this);
    }


    private void showLoggingIn(){
        // TODO
    }


    @Override
    protected void onResume() {

        ServerInteractionHelper.getInstance().registerEventListener(this, this);
        if(ServerInteractionHelper.getInstance().isRequestAlreadyPending(LOGIN_REQUEST)){
            showLoggingIn();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        ServerInteractionHelper.getInstance().unregisterEventListener(this, this);
        super.onPause();
    }


    @Override
    public void onClick(View view) {
        LoginCommand c = new LoginCommand();

        try {
            ServerInteractionHelper.getInstance().sendCommand(this, c, LOGIN_REQUEST);
        } catch (SendingCommandException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    private void goToVenues(){
        Intent i = new Intent(this, PlacesActivity.class);
        startActivity(i);
    }

    @Override
    public void onServerResult(String result, String requestId) {
        if(requestId.equals(LOGIN_REQUEST)){
            goToVenues();
        }
    }

    @Override
    public void onServerError(String result, String requestId) {
//  TODO Mostrare toast
    }
}
