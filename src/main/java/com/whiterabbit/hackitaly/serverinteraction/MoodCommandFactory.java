package com.whiterabbit.hackitaly.serverinteraction;

import com.whiterabbit.postman.commands.CommandFactory;
import com.whiterabbit.postman.commands.ServerCommand;
import com.whiterabbit.postman.commands.UnknownCommandException;

/**
 * Created with IntelliJ IDEA.
 * User: fedepaol
 * Date: 7/21/12
 * Time: 10:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class MoodCommandFactory extends CommandFactory{
    @Override
    public ServerCommand createCommand(String simpleClassName) throws UnknownCommandException {
        if(simpleClassName.equals(GetVenuesCommand.class.getSimpleName())){
            GetVenuesCommand c = new GetVenuesCommand();
            return c;
        }

        if(simpleClassName.equals(LoginCommand.class.getSimpleName())){
            LoginCommand c = new LoginCommand();
            return c;
        }

        if(simpleClassName.equals(GetPlaylistCommand.class.getSimpleName())){
            GetPlaylistCommand c = new GetPlaylistCommand();
            return c;
        }


        if(simpleClassName.equals(CheckinCommand.class.getSimpleName())){
            CheckinCommand c = new CheckinCommand();
            return c;
        }

        if(simpleClassName.equals(CheckoutCommand.class.getSimpleName())){
            CheckoutCommand c = new CheckoutCommand();
            return c;
        }

        if(simpleClassName.equals(SetMoodCommand.class.getSimpleName())){
            SetMoodCommand c = new SetMoodCommand();
            return c;
        }
        throw new UnknownCommandException();
}
}
