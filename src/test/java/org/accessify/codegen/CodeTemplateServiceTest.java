package org.accessify.codegen;

import base.DummyHandledType;
import org.accessify.utils.FilesUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class CodeTemplateServiceTest {

    static final String GENERATED_CODE_FOLDER = "generated/src/test/java";
    private static final Logger LOG = LoggerFactory.getLogger(CodeGenService.class);

    @Test
    public void testOne() throws Exception {
        List<VelocityContext> contexts =
            TemplatingContextsGenerator.generatePropertyHandlersContexts(DummyHandledType.class);
        VelocityContext context = contexts.get(0);

        StringWriter writer = new StringWriter();
        CodeGenService service = new CodeGenService();
        service.writePropertyHandler(context, writer);
        writer.close();
        String code = writer.toString();

        assertTrue(StringUtils.isNotBlank(code));
        assertTrue(!code.contains("$")); //assert that no template code is left
        LOG.debug(code);
    }

    @Test
    public void testWithCompile() throws Exception {
        File dir = FilesUtils.makeDirIfNotExists(GENERATED_CODE_FOLDER);
        List<VelocityContext> contexts =
            TemplatingContextsGenerator.generatePropertyHandlersContexts(DummyHandledType.class);
        VelocityContext context = contexts.get(0);
        File tempFile =
            new File(dir, context.internalGet(PropertyTemplateFields.HANDLER_TYPE) + ".java");
        FileWriter writer = new FileWriter(tempFile);
        CodeGenService service = new CodeGenService();
        service.writePropertyHandler(context, writer);
        writer.close();
        assertTrue(CompilerService.compileGeneratedSourceFiles(tempFile));
        //        tempFile.delete();
        //        dir.delete();
    }
}
