package com.whiterabbit.hackitaly.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.whiterabbit.hackitaly.R;
import com.whiterabbit.hackitaly.Utils.PreferencesStore;
import com.whiterabbit.hackitaly.serverinteraction.LoginCommand;
import com.whiterabbit.hackitaly.serverinteraction.MoodCommandFactory;
import com.whiterabbit.postman.SendingCommandException;
import com.whiterabbit.postman.ServerInteractionHelper;
import com.whiterabbit.postman.ServerInteractionResponseInterface;

public class LoginActivity extends SherlockActivity implements View.OnClickListener, ServerInteractionResponseInterface {

    private static final String LOGIN_REQUEST = "LoginRequest";


    EditText mUser;
    Button mGo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ServerInteractionHelper.initWithCommandFactory(new MoodCommandFactory());
        setContentView(R.layout.login);
        mGo = (Button) findViewById(R.id.login_go_button);
        mGo.setOnClickListener(this);

        mUser = (EditText) findViewById(R.id.login_name_text);
        mUser.setText(PreferencesStore.getUsername(this));
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
        PreferencesStore.setUserName(this, mUser.getText().toString());
        LoginCommand c = new LoginCommand(mUser.getText().toString());

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
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }
}
