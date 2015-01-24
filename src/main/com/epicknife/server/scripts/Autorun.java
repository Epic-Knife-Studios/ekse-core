package com.epicknife.server.scripts;

import com.epicknife.server.Server;
import com.epicknife.server.util.io.FileIO;
import com.epicknife.server.plugin.JavaPlugin;
import com.google.gson.Gson;
import groovy.lang.GroovyShell;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

public class Autorun
{
    
    private URLClassLoader classes;
    private final ArrayList<URL> urls;
    private final Gson gson;
    public ArrayList<JavaPlugin> plugins;
    public GroovyShell groovy;
    public Globals lua;
    private final HashMap<String, String> clientPack;
    public ArrayList<String> clientList;
    
    public static void addPath(String s) throws Exception
    {
        File f = new File(s);
        URI u = f.toURI();
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<URLClassLoader> urlClass = URLClassLoader.class;
        Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
        method.setAccessible(true);
        method.invoke(urlClassLoader, new Object[]{u.toURL()});
    }
    
    public static void ensureDefaults()
    {
        if(!FileIO.fileExistsDirectory("autorun"))
        {
            FileIO.mkdirRecursive("autorun");
        }
        if(!FileIO.fileExistsDirectory("scripts"))
        {
            FileIO.mkdirRecursive("scripts");
        }
        if(!FileIO.fileExistsDirectory("plugins"))
        {
            FileIO.mkdirRecursive("plugins");
        }
        if(!FileIO.fileExistsDirectory("log"))
        {
            FileIO.mkdirRecursive("log");
        }
        if(!FileIO.fileExistsDirectory("conf"))
        {
            FileIO.mkdirRecursive("conf");
        }
        if(!FileIO.fileExistsBasic("autorun/autorun.lua"))
        {
            try
            {
                BufferedWriter out = new BufferedWriter(new FileWriter("autorun/autorun.lua"));
                out.write("plugins.load(\"com.epicknife.modules.builtin.TcpListener\");\n");
                out.close();
            }
            catch(Exception e)
            {
                
            }
        }
    }
    
    public Autorun()
    {
        classes = URLClassLoader.newInstance(new URL[0]);
        urls = new ArrayList<>();
        gson = new Gson();
        plugins = new ArrayList<>();
        
        groovy = new GroovyShell();
        
        lua = JsePlatform.standardGlobals();
        
        LuaTable clibrary = new LuaTable();
        clibrary.set("exists", new libraryExists());
        clibrary.set("load", new libraryLoad());

        LuaTable cplugins = new LuaTable();
        cplugins.set("SUCCESS", 0);
        cplugins.set("NO_SUCH_FILE", 1);
        cplugins.set("INVALID_FORMAT", 2);
        cplugins.set("start", new start());
        cplugins.set("stop", new stop());
        cplugins.set("reload", new reload());
        cplugins.set("exists", new pluginExists());
        cplugins.set("load", new pluginLoad());
        cplugins.set("class", new pluginClass());

        LuaTable cutils = new LuaTable();
        cutils.set("log", new log());

        LuaTable cscripts = new LuaTable();
        cscripts.set("load", new scriptLoad());

        LuaTable cautorun = new LuaTable();
        cautorun.set("load", new autorunLoad());

        LuaTable cclient = new LuaTable();
        cclient.set("add", new clientAdd());
        cclient.set("pack", new clientPack());

        lua.set("library", clibrary);
        lua.set("plugins", cplugins);
        lua.set("utils", cutils);
        lua.set("scripts", cscripts);
        lua.set("autorun", cautorun);
        lua.set("client", cclient);
        
        clientPack = new HashMap<>();
        clientList = new ArrayList<String>();
    }
    
    public void run(String str)
    {
        LuaValue chunk = lua.load(str);
        chunk.call();
    }
        
    public class log extends OneArgFunction
    {
        @Override
        public LuaValue call(LuaValue lv)
        {
            System.out.println("[Autorun] " + lv.checkjstring());
            return CoerceJavaToLua.coerce(null);
        }
    }

    public class start extends ZeroArgFunction
    {
        @Override
        public LuaValue call()
        {
            for(int i = 0; i < plugins.size(); i++)
            {
                JavaPlugin p = plugins.get(i);
                System.out.println("[Autorun] Starting plugin \"" +
                    p.getFancyName() +
                    "\" v" +
                    p.getVersion()
                );
                p.onStart();
            }
            return CoerceJavaToLua.coerce(null);
        }
    }

    public class stop extends ZeroArgFunction
    {
        @Override
        public LuaValue call()
        {
            for(int i = 0; i < plugins.size(); i++)
            {
                JavaPlugin p = plugins.get(i);
                System.out.println("[Autorun] Stopping plugin \"" +
                    p.getFancyName() +
                    "\" v" +
                    p.getVersion()
                );
                p.onStop();
            }
            return CoerceJavaToLua.coerce(null);
        }
    }

    public class reload extends ZeroArgFunction
    {
        @Override
        public LuaValue call()
        {
            for(int i = 0; i < plugins.size(); i++)
            {
                JavaPlugin p = plugins.get(i);
                System.out.println("[Autorun] Stopping plugin \"" +
                    p.getFancyName() +
                    "\" v" +
                    p.getVersion()
                );
                p.onStop();
            }
            for(int i = 0; i < plugins.size(); i++)
            {
                JavaPlugin p = plugins.get(i);
                System.out.println("[Autorun] Starting plugin \"" +
                    p.getFancyName() +
                    "\" v" +
                    p.getVersion()
                );
                p.onStart();
            }
            return CoerceJavaToLua.coerce(null);
        }
    }

    public class libraryExists extends OneArgFunction
    {
        @Override
        public LuaValue call(LuaValue lv)
        {
            String cleaned = (lv.checkjstring().replace("/", ""));
            cleaned = (cleaned.replace("\\", ""));
            String sname = "plugins/" + cleaned;
            boolean jar = FileIO.fileExistsBasic(sname + ".jar");
            boolean zip = FileIO.fileExistsBasic(sname + ".zip");
            return CoerceJavaToLua.coerce(jar || zip);
        }
    }

    public class pluginExists extends OneArgFunction
    {
        @Override
        public LuaValue call(LuaValue lv)
        {
            String cleaned = (lv.checkjstring().replace("/", ""));
            cleaned = (cleaned.replace("\\", ""));
            String sname = "plugins/" + cleaned;
            return CoerceJavaToLua.coerce(FileIO.fileExistsBasic(sname + ".plugin"));
        }
    }

    public class libraryLoad extends OneArgFunction
    {
        @Override
        public LuaValue call(LuaValue lv)
        {
            String cleaned = (lv.checkjstring().replace("/", ""));
            cleaned = (cleaned.replace("\\", ""));
            String sname = "plugins/" + cleaned;
            boolean jar = FileIO.fileExistsBasic(sname + ".jar");
            boolean zip = FileIO.fileExistsBasic(sname + ".zip");

            if(jar)
            {
                try
                {
                    urls.add(new File(sname + ".jar").toURI().toURL());
                    Autorun.addPath(sname + ".jar");
                    return CoerceJavaToLua.coerce(true);
                }
                catch(Exception e)
                {
                    return CoerceJavaToLua.coerce(false);
                }
            }
            else if (zip)
            {
                try
                {
                    urls.add(new File(sname + ".zip").toURI().toURL());
                    Autorun.addPath(sname + ".zip");
                    return CoerceJavaToLua.coerce(true);
                }
                catch(Exception e)
                {
                    return CoerceJavaToLua.coerce(false);
                }
            }
            else
            {
                return CoerceJavaToLua.coerce(false);
            }
        }
    }

    public class pluginLoad extends OneArgFunction
    {
        @Override
        public LuaValue call(LuaValue lv)
        {
            String classname = (lv.checkjstring());

            URL[] curls = new URL[urls.size()];
            for(int i = 0; i < urls.size(); i ++)
            {
                curls[i] = urls.get(i);
            }
            classes = URLClassLoader.newInstance(curls);

            try
            {
                Class<?> clazz = Class.forName(classname, true, classes);
                Class<? extends JavaPlugin> runClass = clazz.asSubclass(JavaPlugin.class);
                Constructor<? extends JavaPlugin> ctor = runClass.getConstructor();
                JavaPlugin plugin = (JavaPlugin)ctor.newInstance();
                plugins.add(plugin);
                Server.log.logInfo("Autorun", "Loading \"" + plugin.getFancyName() + "\" v" + plugin.getVersion());
                return CoerceJavaToLua.coerce(plugin.getInternalName());
            }
            catch(Exception e)
            {
                return CoerceJavaToLua.coerce("");
            }
        }
    }
    
    public class scriptLoad extends OneArgFunction
    {
        @Override
        public LuaValue call(LuaValue lv)
        {
            String cleaned = lv.checkjstring();
            String sname = "scripts/" + cleaned;

            if(FileIO.fileExistsBasic(sname))
            {
                if (sname.endsWith(".lua"))
                {
                    lua.loadfile(sname);
                    return CoerceJavaToLua.coerce(true);
                }
                else if (sname.endsWith(".groovy"))
                {
                    groovy.evaluate(FileIO.readAllText(sname));
                    return CoerceJavaToLua.coerce(true);
                }
                else
                {
                    System.out.println("[Autorun] No executor for \"" + sname + "\"!");
                    return CoerceJavaToLua.coerce(false);
                }
            }
            else
            {
                System.out.println("[Autorun] No such script \"" + sname + "\"!");
                return CoerceJavaToLua.coerce(false);
            }
        }
    }
    
    public class autorunLoad extends OneArgFunction
    {
        @Override
        public LuaValue call(LuaValue lv)
        {
            String cleaned = (lv.checkjstring().replace("/", ""));
            cleaned = (cleaned.replace("\\", ""));
            String sname = "autorun/" + cleaned;
            
            if(FileIO.fileExistsBasic(sname + ".lua"))
            {
                LuaValue chunk = lua.loadfile(sname + ".lua");
                chunk.call();
                return CoerceJavaToLua.coerce(true);
            }
            else
            {
                System.out.println("[Autorun] No such script \"" + sname + "\"!");
                return CoerceJavaToLua.coerce(false);
            }
        }
    }
    
    private class clientAdd extends TwoArgFunction
    {
        @Override
        public LuaValue call(LuaValue orig, LuaValue ldest)
        {
            String cleaned = orig.checkjstring();
            String sname = "client/" + cleaned;
            
            String dest = ldest.checkjstring();
            
            try
            {
                if(FileIO.fileExistsBasic(sname))
                {
                    clientList.add(dest);
                    clientPack.put(dest, sname);
                }
            }
            catch(Exception e)
            {
                
            }
            
            return CoerceJavaToLua.coerce(null);
        }
    }
    
    private class clientPack extends ZeroArgFunction
    {
        @Override
        public LuaValue call()
        {
            ZipOutputStream zap = null;
            try
            {
                zap = new ZipOutputStream(
                    new FileOutputStream("client.zip", true)
                );
                for(int i = 0; i < clientList.size(); i++)
                {
                    String name = clientList.get(i);
                    ZipEntry ze = new ZipEntry(name);
                    FileReader fr = new FileReader(clientPack.get(name));
                    zap.putNextEntry(ze);
                    int b;
                    while((b = fr.read()) != -1)
                    {
                        zap.write(b);
                    }
                    zap.closeEntry();
                }
                zap.close();
            }
            catch(Exception e)
            {
                
            }
            return CoerceJavaToLua.coerce(null);
        }
    }
    
    public class pluginClass extends OneArgFunction
    {
        @Override
        public LuaValue call(LuaValue lv)
        {
            URL[] curls = new URL[urls.size()];
            for(int i = 0; i < urls.size(); i ++)
            {
                curls[i] = urls.get(i);
            }
            try
            {
                Class<?> clazz = Class.forName(lv.checkjstring(), true, classes);
                Class<? extends JavaPlugin> runClass = clazz.asSubclass(JavaPlugin.class);
                Constructor<? extends JavaPlugin> ctor = runClass.getConstructor();
                JavaPlugin plugin = (JavaPlugin)ctor.newInstance();
                plugins.add(plugin);
                return CoerceJavaToLua.coerce(plugin.getInternalName());
            }
            catch(Exception e)
            {
                return CoerceJavaToLua.coerce(null);
            }
        }
    }

}
