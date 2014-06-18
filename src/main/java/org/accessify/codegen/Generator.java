package org.accessify.codegen;

import org.apache.commons.lang.NotImplementedException;
import org.apache.velocity.VelocityContext;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.List;

/**
 * Created by edouard on 14/06/17.
 */
public class Generator {

    /**
     * Create code and compile it from the information provided by the handledTypes
     *
     * @param handledTypes
     * @param codeGenDirectory
     * @param compileClassDir
     * @return true if compilation completed successfully
     */
    public static boolean generateCodeAndCompile(List<Class> handledTypes, String codeGenDirectory, String compileClassDir) throws IntrospectionException, IOException {

        List<VelocityContext> propertyHandlersContexts;
        VelocityContext objectHandlerContext;
        for (Class handledType : handledTypes) {
            propertyHandlersContexts = TemplatingContextsGenerator.generatePropertyHandlersContexts(handledType);
            objectHandlerContext = TemplatingContextsGenerator.generateObjectHandlerContext(handledType, propertyHandlersContexts);
        }

        //TODO
        throw new NotImplementedException();
    }


}
