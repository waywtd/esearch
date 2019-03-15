package com.globalegrow.esearch.realtime.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <pre>
 * 
 *  File: ClassUtils.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年5月5日				chengmo				Initial.
 *
 * </pre>
 */
public final class ClassUtils implements Serializable
{
    private static Logger logger = LogManager.getLogger(ClassUtils.class);
    
    /**
     * Load class by given name and return corresponding Class object.
     *
     * This method will return null in case that the class is not found, no
     * exception will be rised.
     *
     * @param className Name of class
     * @return Class instance or NULL
     */
    public static Class<?> loadClass(String className)
    {
        if (className == null)
        {
            return null;
        }

        Class<?> klass = null;
        try
        {
            klass = Class.forName(className);
        }
        catch (ClassNotFoundException ex)
        {
            logger.debug("Exception while loading class: " + className, ex);
        }
        return klass;
    }
    
    /**
     * Get default ClassLoader
     *
     * @return
     */
    public static ClassLoader getDefaultClassLoader()
    {
        ClassLoader cl = null;
        try
        {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex)
        {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null)
        {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
            if (cl == null)
            {
                // getClassLoader() returning null indicates the bootstrap
                // ClassLoader
                try
                {
                    cl = ClassLoader.getSystemClassLoader();
                }
                catch (Throwable ex)
                {
                    // Cannot access system ClassLoader - oh well, maybe the
                    // caller can live with null...
                }
            }
        }
        return cl;
    }
    
    public static String getPackageName(Class<?> clazz) {
        
        if (clazz == null)
        {
            return null;
        }
        String fqClassName = clazz.getName();
        int lastDotIndex = clazz.getName().lastIndexOf(".");
        return (lastDotIndex != -1 ? fqClassName.substring(0, lastDotIndex) : "");
    }

    /**
     * Get all classes of package
     * 
     * @return
     */
    public static List<Class<?>> getClasses(String packageName)
    {
        return getClasses(packageName, "");
    }

    /**
     * Get all classes of package
     * 
     * @return
     */
    public static List<Class<?>> getClasses(String packageName, String clazzSuffix)
    {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        boolean recursive = true;
        String packageDirName = packageName.replace('.', '/');
        String clazzFilter = clazzSuffix + ".class";
        Enumeration<URL> dirs;
        try
        {
            dirs = getDefaultClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements())
            {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol))
                {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findAndAddClassesInPackageByFile(packageName, filePath, clazzFilter, recursive, classes);
                }
                else if ("jar".equals(protocol))
                {
                    JarFile jar;
                    try
                    {
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements())
                        {
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if (name.charAt(0) == '/')
                            {
                                name = name.substring(1);
                            }

                            if (name.startsWith(packageDirName))
                            {
                                int idx = name.lastIndexOf('/');
                                if (idx != -1)
                                {
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }

                                if ((idx != -1) || recursive)
                                {
                                    if (name.endsWith(clazzFilter) && !entry.isDirectory())
                                    {
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try
                                        {
                                            classes.add(Class.forName(packageName + '.' + className));
                                        }
                                        catch (ClassNotFoundException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return classes;
    }

    private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final String clazzFilter,
            final boolean recursive, List<Class<?>> classes)
    {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory())
        {
            return;
        }
        File[] dirfiles = dir.listFiles(new FileFilter()
        {
            public boolean accept(File file)
            {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(clazzFilter));
            }
        });

        for (File file : dirfiles)
        {
            if (file.isDirectory())
            {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(),
                        clazzFilter, recursive, classes);
            }
            else
            {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try
                {
                    classes.add(Class.forName(packageName + '.' + className));
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Return jar path for given class.
     *
     * @param className Class name
     * @return Path on local filesystem to jar where given jar is present
     */
    public static String jarForClass(String className)
    {
        Class<?> klass = loadClass(className);
        return jarForClass(klass);
    }

    /**
     * Return jar path for given class.
     *
     * @param klass Class object
     * @return Path on local filesystem to jar where given jar is present
     */
    public static String jarForClass(Class<?> klass)
    {
        return klass.getProtectionDomain().getCodeSource().getLocation().toString();
    }

    private ClassUtils()
    {
        // Disable explicit object creation
    }
    
    public static void main(String[] args)
    {
        String str = ClassUtils.jarForClass(ClassUtils.class);
        System.out.println(str);
        System.out.println(ClassUtils.class.getResource("/"));
        System.out.println(System.getProperty("user.dir"));
    }
}
