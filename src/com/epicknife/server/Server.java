package com.epicknife.server;

import com.epicknife.server.util.io.FileIO;
import com.epicknife.server.event.events.ShutdownEvent;
import com.epicknife.server.event.events.StartEvent;
import com.epicknife.server.util.io.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.HashMap;
import com.epicknife.server.etc.Config;
import com.epicknife.server.event.EventManager;
import com.epicknife.server.event.events.ConsoleEvent;
import com.epicknife.server.instructions.InstructionThread;
import com.epicknife.server.instructions.Instruction;
import com.epicknife.server.network.ConnectionReceiver;
import com.epicknife.server.scripts.Autorun;
import java.util.ArrayList;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class Server
{
    public static ServerSocket server;
    private static boolean running = false;
    public static Autorun autorun;
    public static Logger log;
    public static Config svars;
    public static ArrayList<ConnectionReceiver> listeners;
    public static EventManager events;
    
    public static void main(String[] args)
    {
        start();
        run();
    }
    
    public static void addReceiver(ConnectionReceiver receiver)
    {
        listeners.add(receiver);
    }
    
    public static ConnectionReceiver getReceiver(String id)
    {
        for(int i = 0; i < listeners.size(); i++)
        {
            if(listeners.get(i).id.equals(id))
            {
                return listeners.get(i);
            }
        }
        return null;
    }
    
    public static void start()
    {
        log = new Logger();
        log.addOutput(System.out);
        log.addFile(new File("log/server.log"));

        log.logInfo("Main", "EpicKnife Server Starting...");

        log.logInfo("Main", "Initializing data structures...");
        InstructionThread.addInstruction(new Instruction("Null"));
        new InstructionThread().start();
        autorun = new Autorun();
        Autorun.ensureDefaults();
        HashMap<String, String> defaults = new HashMap<>();
        listeners = new ArrayList<>();

        svars = new Config(defaults);
        svars.ensureDefaults();
        
        events = new EventManager();

        log.logInfo("Main", "Data structures ready!");

        log.logInfo("Main", "Loading server vars...");

        if(FileIO.fileExistsBasic("conf/svars.json"))
        {
            svars.readCfg("svars");
            svars.writeCfg("svars");
        }
        else
        {
            svars.writeCfg("svars");
        }

        log.logInfo("Main", "Server vars loaded!.");

        log.logInfo("Main", "Loading autorun...");

        autorun.run(FileIO.readAllText("autorun/autorun.lua"));

        log.logInfo("Main", "Autorun loaded!");

        running = true;
        
        for(int i = 0; i < autorun.plugins.size(); i++)
        {
            autorun.plugins.get(i).onStart();
        }
        events.raiseEvent(new StartEvent());
        
        log.logInfo("Main", "Intializing listeners...");
        for (ConnectionReceiver listener : listeners)
        {
            listener.onStart();
        }
        log.logInfo("Main", "Listeners running");
        
        log.logInfo("Main", "Done!  Accepting connections!");
        InstructionThread.addInstruction(new Instruction("Null"));
    }

    public static void run()
    {
        String line;
        BufferedReader br = null;

        try
        {
            System.out.print(">");
            br = new BufferedReader(
                new InputStreamReader(System.in)
            );
            while((line = br.readLine()) != null && running)
            {
                if(line.replace("\n", "").equalsIgnoreCase("@stop"))
                {
                    stop();
                    System.exit(0);
                }
                else if(line.replace("\n", "").equalsIgnoreCase("@restart"))
                {
                    stop();
                    start();
                    run();
                }
                else
                {
                    events.raiseEvent(new ConsoleEvent(line.replace("\n", "")));
                }
                System.out.print(">");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
        finally
        {
            try
            {
                if(br != null)
                {
                    br.close();
                }
            }
            catch (Exception e) {}
            running = false;
            stop();
        }
    }

    public static void stop()
    {
        for(int i = 0; i < autorun.plugins.size(); i++)
        {
            autorun.plugins.get(i).onStop();
        }
        events.raiseEvent(new ShutdownEvent());
        events.reset();
        log.logInfo("Main", "Epic Knife Server Stopping...");
        
        log.logInfo("Main", "Halting all connections...");
        for(int i = 0; i < listeners.size(); i++)
        {
            log.logInfo("Main", "Stopping Listener \"" + listeners.get(i).id + "\"");
            listeners.get(i).onStop();
        }
        log.logInfo("Main", "Connections halted!");
        log.logInfo("Main", "Saving config...");
        svars.ensureDefaults();
        svars.writeCfg("svars");
        log.logInfo("Main", "Config Saved!");
        
        log.logInfo("Main", "Done!");
    }
    
    public static boolean isRunning()
    {
        return running;
    }
}