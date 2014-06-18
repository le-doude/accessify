package org.accessify.processor;

import org.accessify.annotations.HandledType;
import org.accessify.codegen.HandledTypesProcessor;
import org.accessify.utils.ConfigurationUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
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

    private boolean processImpl(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws ClassNotFoundException, IntrospectionException, IOException {
        return HandledTypesProcessor.generateCodeAndCompile(getAllHandledTypes(roundEnv), ConfigurationUtils.CODE_GEN_DIR, ConfigurationUtils.CLASS_FILE_DIR);
    }

    private ArrayList<Class> getAllHandledTypes(RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(HandledType.class);
        ArrayList<Class> handledTypes = new ArrayList<>(elements.size());
        Elements elementUtils = processingEnv.getElementUtils();
        String className;
        for (Element element : elements) {
            if (element.getKind().isClass()) {
                className = elementUtils.getBinaryName((TypeElement) element).toString();
                Class<?> clazz = null;
                try {
                    clazz = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    clazz = null;
                }
                if (clazz != null) {
                    handledTypes.add(clazz);
                }
            }
        }
        return handledTypes;
    }
}
