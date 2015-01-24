package com.epicknife.server.util.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class Logger
{

    public ArrayList<PrintStream> outputs;
    public enum LogType { INFO, WARNING, SEVERE, FATAL }
    
    public boolean addOutput(PrintStream pw)
    {
        if(outputs == null)
        {
            outputs = new ArrayList<PrintStream>();
        }
        
        if(pw == null)
        {
            return false;
        }
        outputs.add(pw);
        return true;
    }
    
    public boolean addFile(File f)
    {
        if(f == null)
        {
            return false;
        }
        if(!f.exists())
        {
            try
            {
                f.mkdirs();
                f.delete();
                f.createNewFile();
            }
            catch(Exception e)
            {
                return false;
            }
        }
        
        try
        {
            outputs.add(new PrintStream(new FileOutputStream(f)));
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    
    public void log(LogType type, String sender, String message)
    {
        switch(type)
        {
        case FATAL:
            logFatal(sender, message);
            break;
        case INFO:
            logInfo(sender, message);
            break;
        case SEVERE:
            logSevere(sender, message);
            break;
        case WARNING:
            logWarning(sender, message);
            break;
        default:
            logInfo(sender, message);
            break;
        }
    }
    
    public void logInfo(String sender, String message)
    {
        if(outputs == null)
        {
            outputs = new ArrayList<PrintStream>();
            return;
        }
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        for(int i = 0; i < outputs.size(); i++)
        {
            try
            {
                outputs.get(i).println("[" + time + "] [" + sender + "] [info] " + message);
            }
            catch(Exception e)
            {
                outputs.remove(i);
                i--;
            }
        }
    }
    
    public void logWarning(String sender, String message)
    {
        if(outputs == null)
        {
            outputs = new ArrayList<PrintStream>();
            return;
        }
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        for(int i = 0; i < outputs.size(); i++)
        {
            try
            {
                outputs.get(i).println("[" + time + "] [" + sender + "] [WARNING] " + message);
            }
            catch(Exception e)
            {
                outputs.remove(i);
                i--;
            }
        }
    }
    
    public void logSevere(String sender, String message)
    {
        if(outputs == null)
        {
            outputs = new ArrayList<PrintStream>();
            return;
        }
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        for(int i = 0; i < outputs.size(); i++)
        {
            try
            {
                outputs.get(i).println("[" + time + "] [" + sender + "] [SEVERE-WARNING] " + message);
            }
            catch(Exception e)
            {
                outputs.remove(i);
                i--;
            }
        }
    }
    
    public void logFatal(String sender, String message)
    {
        if(outputs == null)
        {
            outputs = new ArrayList<PrintStream>();
            return;
        }
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        for(int i = 0; i < outputs.size(); i++)
        {
            try
            {
                outputs.get(i).println("[" + time + "] [" + sender + "] [FATAL-ERROR] " + message);
            }
            catch(Exception e)
            {
                outputs.remove(i);
                i--;
            }
        }
    }

}