package com.epicknife.server.event;

import com.epicknife.server.Server;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.epicknife.server.event.events.ServerEvent;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 * EventManager is the global class where events
 * can be registered and raised.
 */
public class EventManager
{

    private static List<Class<?>> handlers = new ArrayList<>();

    private EventManager()
    {}
    
    public static void reset()
    {
        handlers = new ArrayList<>();
    }
    
    public static void register(Class<?> clazz)
    {
        if(IEventHandler.class.isAssignableFrom(clazz))
        {
            if(!handlers.contains(clazz)) handlers.add(clazz);
        }
        else
        {
            Server.log.logWarning("EventManager", 
                    "Warning: class \"" + clazz.getName() + "\" is not an event handler!"
            );
        }
    }

    public static boolean containsEventHandler(Class<?> clazz)
    {
        return handlers.contains(clazz);
    }

    public static void raiseEvent(final ServerEvent event)
    {
        if(event.isAsync())
        {
            new Thread()
            {
                @Override
                public void run()
                {
                    raise(event);
                }
            }.start();
        }
        else
        {
            raise(event);
        }
    }

    private static void raise(final ServerEvent event)
    {
        if(handlers.isEmpty()) return; // Fail fast if the Array is Empty

        for(Class<?> handler : handlers)
        {
            Method[] methods = handler.getMethods();

            for(int i = 0; i < methods.length; ++i)
            {
                EventHandler eventHandler = methods[i].getAnnotation(EventHandler.class);
                if(eventHandler != null)
                {
                    Class<?>[] methodParams = methods[i].getParameterTypes();

                    if(methodParams.length < 1) continue;

                    if(!event.getClass().getSimpleName()
                            .equals(methodParams[0].getSimpleName())) continue;

                    // Defense from runtime exceptions:
                    try
                    {
                        methods[i].invoke(handler.newInstance(), event);
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
