package org.accessify.codegen;

import org.accessify.codegen.data.PropertyHandlerContext;
import org.accessify.codegen.fields.ObjectHandlerFields;
import org.accessify.codegen.fields.PropertyTemplateFields;
import org.accessify.handlers.HandlingFactory;
import org.accessify.handlers.ObjectHandler;
import org.accessify.utils.ConfigurationUtils;
import org.accessify.utils.FilesUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.velocity.VelocityContext;

import javax.tools.ToolProvider;
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

    private static CompilerService service = new CompilerService();

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


        List<PropertyHandlerContext> propertyHandlersContexts;
        VelocityContext objectHandlerContext;
        ArrayList<File> propertyHandlersFiles = new ArrayList<>();
        ArrayList<File> typeHandlersFiles = new ArrayList<>();
        ArrayList<String> propertyHandlersClasses = new ArrayList<>();
        ArrayList<String> typeHandlersClasses = new ArrayList<>();
        //TODO: Get rid of this ugly code!!!!
        for (Class handledType : handledTypes) {
            propertyHandlersContexts = generatePropertyHandlersContexts(handledType);
            objectHandlerContext = generateObjectHandlerContext(handledType);

            String fileName;
            String className;
            File temp;
            for (PropertyHandlerContext phc : propertyHandlersContexts) {
                className = (String) phc.getContext().get(PropertyTemplateFields.HANDLER_TYPE.getElement());
                fileName = className + ".java";
                temp = new File(sourceDirectory, fileName);
                try (FileWriter writer = new FileWriter(temp)) {
                    codeGenService.writePropertyHandler(phc, writer);
                    writer.flush();
                }
                propertyHandlersFiles.add(temp);
                propertyHandlersClasses.add(className);
            }

            className = objectHandlerContext
                .get(ObjectHandlerFields.PACKAGE.getElement())
                + "." + objectHandlerContext
                .get(ObjectHandlerFields.HANDLER_CLASS_NAME.getElement());

            fileName = objectHandlerContext
                .get(ObjectHandlerFields.HANDLER_CLASS_NAME.getElement()) + ".java";

            temp = new File(sourceDirectory, fileName);

            codeGenService.writeObjectHandler(objectHandlerContext, new FileWriter(
                temp));
            typeHandlersFiles.add(temp);
            typeHandlersClasses.add(className);
        }


        ArrayList<Class> clazzes = new ArrayList<>();
        //if compiles are successfull
        if (service.compileGeneratedSourceFiles(propertyHandlersFiles) && service
            .compileGeneratedSourceFiles(typeHandlersFiles)) {

            ClassLoader classLoader = ToolProvider.getSystemToolClassLoader();
            //Load all ObjectHandler classes compiled
            for (String name : typeHandlersClasses) {
                try {
                    clazzes.add(classLoader.loadClass(name));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        return clazzes; //return only objectHandler classes.
    }

    /**
     * This is the main function of this project.
     * Get all handled types and generate, compile, and register handlers for them.
     *
     * @param allHandledTypes
     * @return true if all went well
     * @throws IntrospectionException
     * @throws IOException
     */
    public static boolean fullyProcessHandledTypes(List<Class> allHandledTypes)
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
