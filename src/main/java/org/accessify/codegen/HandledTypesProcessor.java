package org.accessify.codegen;

import org.accessify.handlers.HandlingFactory;
import org.accessify.handlers.ObjectHandler;
import org.accessify.utils.ConfigurationUtils;
import org.accessify.utils.FilesUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.velocity.VelocityContext;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.accessify.codegen.TemplatingContextsGenerator.generateObjectHandlerContext;
import static org.accessify.codegen.TemplatingContextsGenerator.generatePropertyHandlersContexts;

/**
 * Created by edouard on 14/06/17.
 */
public class HandledTypesProcessor {

    private static CompilerService service;

    public HandledTypesProcessor() {
        service = new CompilerService();
    }

    /**
     * Create code and compile it from the information provided by the handledTypes
     *
     * @param handledTypes
     * @param codeGenDirectory
     * @param compileClassDir
     * @return class names of the compile object handlers (do not return the property handlers)
     */
    public static ArrayList<Class> generateCodeAndCompile(List<Class> handledTypes,
        String codeGenDirectory, String compileClassDir)
        throws IntrospectionException, IOException {
        CodeGenService codeGenService = new CodeGenService();
        File sourceDirectory = FilesUtils.makeDirIfNotExists(codeGenDirectory);


        List<VelocityContext> propertyHandlersContexts;
        VelocityContext objectHandlerContext;
        ArrayList<File> files = new ArrayList<>();
        ArrayList<String> classes = new ArrayList<>();
        ArrayList<String> typeHandlersClasses = new ArrayList<>();
        //TODO: Get rid of this ugly code!!!!
        for (Class handledType : handledTypes) {
            propertyHandlersContexts = generatePropertyHandlersContexts(handledType);
            objectHandlerContext = generateObjectHandlerContext(handledType,
                propertyHandlersContexts);

            String fileName;
            String className;
            File temp;
            for (VelocityContext phc : propertyHandlersContexts) {
                className = (String) phc.get(PropertyTemplateFields.HANDLER_TYPE);
                fileName = className + ".java";
                temp = new File(sourceDirectory,
                    fileName);
                codeGenService.writePropertyHandler(phc, new FileWriter(
                    temp));
                files.add(temp);
                classes.add(className);
            }
            className =
                (String) objectHandlerContext.get(ObjectHandlerTemplateFields.HANDLER_CLASS_NAME);
            fileName = className + ".java";
            temp = new File(sourceDirectory, fileName);
            codeGenService.writeObjectHandler(objectHandlerContext, new FileWriter(
                temp));
            files.add(temp);
            classes.add(className);
            typeHandlersClasses.add(className);
        }
        ArrayList<Class> clazzes = new ArrayList<>();
        if (service.compileGeneratedSourceFiles(classes, files)) {
            ClassLoader classLoader = service.classLoader();
            for (String thc : typeHandlersClasses) {
                try {
                    clazzes.add(classLoader.loadClass(thc));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        return clazzes;
    }

    /**
     * This is the main function of this project.
     * Get all handled types and generate, compile, and register handlers for them.
     * @param allHandledTypes
     * @return  true if all went well
     * @throws IntrospectionException
     * @throws IOException
     */
    public static boolean fullyProcessHandledTypes(ArrayList<Class> allHandledTypes)
        throws IntrospectionException, IOException {
        ArrayList<Class> clazzs = generateCodeAndCompile(
                allHandledTypes,
                ConfigurationUtils.CODE_GEN_DIR,
                ConfigurationUtils.CLASS_FILE_DIR
            );

        ArrayList<ObjectHandler> handlers = new ArrayList<>(clazzs.size());
        for (Class clazz : clazzs) {
            try {
                handlers.add((ObjectHandler) clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        HandlingFactory.create(ConfigurationUtils.FACTORY_INSTANCE_NAME,
            handlers.toArray(new ObjectHandler[handlers.size()]));

        return CollectionUtils.isNotEmpty(handlers);
    }
}
