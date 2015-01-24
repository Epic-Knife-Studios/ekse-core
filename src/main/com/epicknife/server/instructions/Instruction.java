package com.epicknife.server.instructions;

import com.epicknife.server.util.io.Logger.LogType;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class Instruction
{
    public String type;
    public LogType logType = LogType.INFO;
    
    public Instruction(String type)
    {
        this.type = type;
    }
    
    public Instruction(String type, LogType logType)
    {
        this.type = type;
        this.logType = logType;
    }
}