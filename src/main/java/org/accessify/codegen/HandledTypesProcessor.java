package org.accessify.codegen;

import org.accessify.utils.FilesUtils;
import org.apache.commons.lang.NotImplementedException;
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

    /**
     * Create code and compile it from the information provided by the handledTypes
     *
     * @param handledTypes
     * @param codeGenDirectory
     * @param compileClassDir
     * @return class names of the compile object handlers (do not return the property handlers)
     */
    public static List<String> generateCodeAndCompile(List<Class> handledTypes,
        String codeGenDirectory, String compileClassDir)
        throws IntrospectionException, IOException {
        CodeGenService codeGenService = new CodeGenService();
        File sourceDirectory = FilesUtils.makeDirIfNotExists(codeGenDirectory);


        List<VelocityContext> propertyHandlersContexts;
        VelocityContext objectHandlerContext;
        ArrayList<String> files = new ArrayList<>();
        ArrayList<String> typeHandlerClasses = new ArrayList<>();
        //TODO: Get rid of this ugly code!!!!
        for (Class handledType : handledTypes) {
            propertyHandlersContexts = generatePropertyHandlersContexts(handledType);
            objectHandlerContext = generateObjectHandlerContext(handledType,
                propertyHandlersContexts);

            String fileName;
            String className;
            for (VelocityContext phc : propertyHandlersContexts) {
                className = (String) phc.get(PropertyTemplateFields.HANDLER_TYPE);
                fileName = className + ".java";
                codeGenService.writePropertyHandler(phc, new FileWriter(
                    new File(sourceDirectory,
                        fileName)));
                files.add(fileName);
            }
            className =
                (String) objectHandlerContext.get(ObjectHandlerTemplateFields.HANDLER_CLASS_NAME);
            fileName = className + ".java";
            codeGenService.writeObjectHandler(objectHandlerContext, new FileWriter(
                new File(sourceDirectory,
                    fileName)));
            files.add(fileName);
            typeHandlerClasses.add(className);
        }

        //TODO
        throw new NotImplementedException();
    }


}
