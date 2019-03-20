/*
 * Author: Jordan Sheinfeld - jordan.sheinfeld@gmail.com
 */

package com.jordan;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.File;
import java.nio.file.Files;

public class PatchCapsDelay {
    public static void main(String[] args) throws Exception {
        System.out.println("Patch utility to patch Java rt.jar to handle CapsLock delay linux issues, v0.91 (Jordan.sheinfeld@gmail.com)");
        System.out.println("------------------------------------------------------------------------------------------------------------");
        if (args.length==0) {
            System.out.println("Usage: patch.sh [JRE_PATH]");
            System.exit(1);
        }

        String jdkPath = args[0];
        String rtPath = jdkPath+"/lib/rt.jar";
        String rtPathBackup = rtPath + ".backup";

        if (!new File (rtPath).exists()) {
            System.err.println(jdkPath+ " is no a valid JRE path !");
            System.exit(1);
        }

        if (!new File(rtPathBackup).exists()) {
            System.out.println("Creating backup to: "+rtPathBackup);
            Files.copy(new File(rtPath).toPath(), new File(rtPathBackup).toPath());
        } else {
            System.out.println("Backup already exists on: "+rtPathBackup);
        }

        try {
            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(rtPath);
            CtClass xkeysymClass = pool.get("sun.awt.X11.XKeysym");
            CtMethod getUppercaseAlphabetic = xkeysymClass.getDeclaredMethod("getUppercaseAlphabetic");
            getUppercaseAlphabetic.insertBefore("{ return $1; }");
            xkeysymClass.toClass();

            JarHandler jarHandler = new JarHandler();
            jarHandler.replaceJarFile(rtPath, xkeysymClass.toBytecode(),"sun/awt/X11/XKeysym.class");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}


