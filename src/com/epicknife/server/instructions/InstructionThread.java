package com.epicknife.server.instructions;

import java.util.concurrent.LinkedBlockingDeque;

import com.epicknife.server.Server;

import com.epicknife.server.etc.ServerThread;
import com.epicknife.server.event.EventManager;
import com.epicknife.server.event.events.InstructionEvent;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class InstructionThread extends ServerThread
{
    
    private static final LinkedBlockingDeque<Instruction> instructions = new LinkedBlockingDeque<Instruction>();
    
    public InstructionThread()
    {
        super("InstructionThread");
    }
    
    public static void addInstruction(Instruction in)
    {
        instructions.add(in);
    }
    
    public static boolean isQueueEmpty()
    {
        return instructions.isEmpty();
    }

    @Override
    public void run()
    {
        
        Instruction in = null;
        
        while(Server.isRunning())
        {
            while((in = instructions.poll()) != null)
            {
                EventManager.raiseEvent(new InstructionEvent(in));
            }
        }
        
    }
}