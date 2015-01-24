package com.epicknife.server.event.events;

import com.epicknife.server.event.ICancelable;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class ServerEvent implements ICancelable
{
    private boolean async = false;
    private boolean canceled = false;
    
    protected void setSync(final boolean flag)
    {
        this.async = flag;
    }
    
    public boolean isAsync()
    {
        return this.async;
    }

    @Override
    public boolean isCanceled()
    {
        return canceled;
    }

    @Override
    public void setCanceled(boolean flag)
    {
        this.canceled = flag;
    }

}
