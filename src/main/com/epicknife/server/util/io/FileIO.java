package com.epicknife.server.util.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class FileIO
{

    public static boolean fileExistsBasic(String file)
    {
        File f = new File(file);
        boolean e = f.exists();
        f = null;
        return e;
    }
    public static boolean fileExistsFile(String file)
    {
        File f = new File(file);
        boolean e = (f.exists() && f.isFile());
        f = null;
        return e;
    }
    
    public static boolean fileExistsUsable(String file)
    {
        File f = new File(file);
        boolean e = (f.exists() && f.canRead() && f.canWrite());
        f = null;
        return e;
    }
    
    public static boolean fileExistsDirectory(String file)
    {
        File f = new File(file);
        boolean e = (f.exists() && f.isDirectory());
        f = null;
        return e;
    }
    
    public static void mkdirRecursive(String name)
    {
        File f = new File(name);
        if(!f.exists())
        {
            try
            {
                f.mkdirs();
            }
            catch(Exception e)
            {
                f = null;
            }
        }
        f = null;
    }
    
    public static void createFileRecursive(String name)
    {
        File f = new File(name);
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
                f = null;
            }
        }
        f = null;
    }
    
    public static String[] listFilesBasic(String name)
    {
        if(!FileIO.fileExistsDirectory(name))
        {
            return null;
        }
        ArrayList<String> list = new ArrayList<String>();
        File f = new File(name);
        File[] lst = f.listFiles();
        for(int i = 0; i < lst.length; i++)
        {
            if(lst[i].isFile())
            {
                list.add(lst[i].getAbsolutePath());
            }
        }
        String[] tr = new String[list.size()];
        for(int i = 0; i < tr.length; i++)
        {
            tr[i] = list.get(i);
        }
        return tr;
    }
    
    public static String[] listFilesRecursive(String name)
    {
        if(!FileIO.fileExistsDirectory(name))
        {
            return null;
        }
        ArrayList<String> list = new ArrayList<String>();
        File f = new File(name);
        File[] lst = f.listFiles();
        for(int i = 0; i < lst.length; i++)
        {
            if(lst[i].isFile())
            {
                list.add(lst[i].getAbsolutePath());
            }
            else
            {
                String[] arr = FileIO.listFilesRecursive(lst[i].getAbsolutePath());
                for(int x = 0; x < arr.length; x++)
                {
                    list.add(arr[x]);
                }
            }
        }
        String[] tr = new String[list.size()];
        for(int i = 0; i < tr.length; i++)
        {
            tr[i] = list.get(i);
        }
        return tr;
    }
    
    public static boolean archiveContains(String zip, String file)
    {
        try
        {
            ZipFile zzip = new ZipFile(zip);
            if(zzip.getEntry(file) != null)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch(Exception e)
        {
            return false;
        }
    }
    
    public static String readFromArchive(String zip, String file)
    {
        try
        {
            ZipFile zzip = new ZipFile(zip);
            ZipEntry zap = zzip.getEntry(file);
            if(zap != null)
            {
                InputStream is = zzip.getInputStream(zap);
                ArrayList<Byte> bytes = new ArrayList<Byte>();
                byte b;
                int i = 0;
                while((b = bytes.get(i)) != -1)
                {
                    bytes.add(b);
                    i++;
                }
                byte[] bite = new byte[bytes.size()];
                for(i = 0; i < bytes.size(); i++)
                {
                    bite[i] = bytes.get(i);
                }
                return new String(bite, 0, bite.length);
            }
            else
            {
                return null;
            }
        }
        catch(Exception e)
        {
            return null;
        }
    }
    
    public static String readAllText(String file)
    {
        if(!FileIO.fileExistsUsable(file))
        {
            return null;
        }
        BufferedReader br = null;
        try
        {
            String tr = "";
            br = new BufferedReader(new FileReader(file));
            String line;
            while((line = br.readLine()) != null)
            {
                tr = tr + line + "\n";
            }
            br.close();
            return tr;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            if(br != null)
            {
                try
                {
                    br.close();
                }
                catch(Exception ee) { ee.printStackTrace(); }
            }
            return null;
        }
    }

}