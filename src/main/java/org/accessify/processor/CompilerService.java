package org.accessify.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by edouard on 14/06/16.
 */
public class CompilerService {

    static final Locale DEFAULT_LOCALE = Locale.getDefault();
    static final Logger LOG = LoggerFactory.getLogger(CompilerService.class);


    static final String GENERATED_CLASS_FILES_FOLDER = "target/classes";

    public static Boolean compileGeneratedSourceFiles(List<File> files) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(new DiagnosticLoggingListener("FILE_MANAGER"), DEFAULT_LOCALE, Charset.defaultCharset());
        Iterable<? extends JavaFileObject> javaFiles = fileManager.getJavaFileObjectsFromFiles(files);
        Boolean success = compiler.getTask(null, fileManager, new DiagnosticLoggingListener("COMPILER"), Arrays.asList("-d", GENERATED_CLASS_FILES_FOLDER), null, javaFiles).call();
        fileManager.close();
        return success;
    }

    public static Boolean compileGeneratedSourceFiles(File... files) throws IOException {
        return compileGeneratedSourceFiles(Arrays.asList(files));
    }

    static class DiagnosticLoggingListener implements DiagnosticListener<JavaFileObject> {

        final String label;

        DiagnosticLoggingListener(String label) {
            this.label = label;
        }

        @Override
        public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
            switch (diagnostic.getKind()) {
                case ERROR:
                    LOG.error("[{}] {} {} at {}:{}", label, diagnostic.getCode(), diagnostic.getMessage(DEFAULT_LOCALE), diagnostic.getSource().getName(), diagnostic.getLineNumber());
                    break;
                case WARNING:
                case MANDATORY_WARNING:
                    LOG.warn("[{}] {} {} at {}:{}", label, diagnostic.getCode(), diagnostic.getMessage(DEFAULT_LOCALE), diagnostic.getSource().getName(), diagnostic.getLineNumber());
                    break;
                case NOTE:
                    LOG.info("[{}] {} {} at {}:{}", label, diagnostic.getCode(), diagnostic.getMessage(DEFAULT_LOCALE), diagnostic.getSource().getName(), diagnostic.getLineNumber());
                    break;
                case OTHER:
                    LOG.debug("[{}] {} {} at {}:{}", label, diagnostic.getCode(), diagnostic.getMessage(DEFAULT_LOCALE), diagnostic.getSource().getName(), diagnostic.getLineNumber());
                    break;
            }
        }
    }

}
