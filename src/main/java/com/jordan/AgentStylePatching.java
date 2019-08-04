/*
 * Author: Jordan Sheinfeld - Taboola
 */

package com.jordan;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

public class AgentStylePatching implements ClassFileTransformer {
    public static void premain(final String agentArgument, final Instrumentation instrumentation) {
        System.err.println("CapsLockDelayAgent");

        instrumentation.addTransformer(new AgentStylePatching());
    }

    public byte[] transform(final ClassLoader loader, final String className, final Class clazz,
                            final java.security.ProtectionDomain domain, final byte[] bytes) {

        if (className.endsWith("XKeysym")) {
            return doClass(className, clazz, bytes);
        } else {
            return bytes;
        }
    }

    private byte[] doClass(final String name, final Class clazz, byte[] b) {
        ClassPool cp = ClassPool.getDefault();
        CtClass xkeysymClass = null;

        try {
            System.out.println("Patching sun.awt.X11.XKeysym ...");
            xkeysymClass = cp.makeClass(new java.io.ByteArrayInputStream(b));
            CtMethod getUppercaseAlphabetic = xkeysymClass.getDeclaredMethod("getUppercaseAlphabetic");
            getUppercaseAlphabetic.insertBefore("{ return $1; }");
            b = xkeysymClass.toBytecode();
        } catch (Exception | Error e) {
            e.printStackTrace(System.err);
        } finally {
            if (xkeysymClass != null) {
                xkeysymClass.detach();
            }
        }

        return b;
    }
}
