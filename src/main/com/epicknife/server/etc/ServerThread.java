package com.epicknife.server.etc;

import com.epicknife.server.Server;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class ServerThread extends Thread
{
    public ServerThread(String name)
    {
        super();
        this.setName(name);
        this.setUncaughtExceptionHandler(new ServerThreadExceptionHandler());
    }
    
    private class ServerThreadExceptionHandler implements UncaughtExceptionHandler
    {

        @Override
        public void uncaughtException(Thread thread, Throwable exception)
        {
            Server.log.logSevere(thread.getName(), "An unhandled exception was caught!");
            exception.printStackTrace();
        }
        
    }
}
