package com.epicknife.server.event.events;

import com.epicknife.server.instructions.Instruction;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class InstructionEvent extends ServerEvent
{
    private final Instruction instruction;
    
    public InstructionEvent(Instruction instruction)
    {
        this.instruction = instruction;
    }
    
    public Instruction getInstruction()
    {
        return this.instruction;
    }
}
