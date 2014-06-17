package org.accessify.processor;

import org.apache.commons.lang.NotImplementedException;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.io.PrintStream;
import java.util.Set;

/**
 * Created by edouard on 14/06/17.
 */
public class MavenAnnotationProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            return processImpl(annotations, roundEnv);
        } catch (Throwable t) {
            PrintStream err = System.err;
            err.println(t.getMessage());
            t.printStackTrace(err);
            return false;
        }
    }

    private boolean processImpl(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //TODO:IMplement me!
        throw new NotImplementedException();
    }
}
