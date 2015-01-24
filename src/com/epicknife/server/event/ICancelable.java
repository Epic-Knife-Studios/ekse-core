package com.epicknife.server.event;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 * An ICancelable is an event that can be canceled.
 */
public interface ICancelable
{
    
    public boolean isCanceled();

    public void setCanceled(boolean flag);

}