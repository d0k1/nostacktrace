package com.focusit.nostacktrace;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;

import javassist.*;

/**
 * Created by doki on 09.11.16.
 */
public class AgentMain implements ClassFileTransformer {
    private ClassPool pool;

    public AgentMain() {
        pool = ClassPool.getDefault();
    }

    /**
     * add agent
     */
    public static void premain(final String agentArgument, final Instrumentation instrumentation)
            throws UnmodifiableClassException {
        System.out.println("Loading agent");
        instrumentation.addTransformer(new AgentMain(), true);
        instrumentation.retransformClasses(new Class[]{Throwable.class});
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.toLowerCase().endsWith("throwable")) {
            System.err.println("Throwable without stacktraces " + className);
            pool.insertClassPath(new ByteArrayClassPath(className, classfileBuffer));
            CtClass cclass = null;
            try {
                cclass = pool.get(className.replaceAll("/", "."));
                if (!cclass.isFrozen()) {
                    CtMethod ctMethod = cclass.getDeclaredMethod("fillInStackTrace");
                    ctMethod.setBody("{return this;}");
                }
                System.out.println("Done. Throwable without stacktraces");
                return cclass.toBytecode();
            } catch (NotFoundException e) {
                e.printStackTrace(System.err);
            } catch (CannotCompileException e) {
                e.printStackTrace(System.err);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
        return classfileBuffer;
    }
}
