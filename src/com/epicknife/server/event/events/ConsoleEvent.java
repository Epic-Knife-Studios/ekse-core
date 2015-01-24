package com.epicknife.server.event.events;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class ConsoleEvent extends ServerEvent
{
    
    private final String message;
    
    public ConsoleEvent(String message)
    {
        this.message = message;
    }
    
    public String getRawMessage()
    {
        return this.message;
    }
    
    public String[] getArgs()
    {
        return message.split(" ");
    }
    
    public String getCommand()
    {
        return (message.split(" ")[0]);
    }
    
}