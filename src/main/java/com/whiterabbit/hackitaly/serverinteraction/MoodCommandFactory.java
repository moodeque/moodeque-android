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
        if(simpleClassName.equals(VenuesGetCommand.class.getSimpleName())){
            VenuesGetCommand c = new VenuesGetCommand();
            return c;
        }
        throw new UnknownCommandException();
}
}
