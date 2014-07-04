package org.accessify.codegen;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by edouard on 14/06/16.
 */
class CompilerService {

    static final Locale DEFAULT_LOCALE = Locale.getDefault();
    static final Logger LOG = LoggerFactory.getLogger(CompilerService.class);
    static final String GENERATED_CLASS_FILES_FOLDER = "target/classes";

    final String classFilesFolderName;
    final JavaCompiler compiler;
    StandardJavaFileManager fileManager;

    public CompilerService(String classFilesFolderName) {
        this.classFilesFolderName = classFilesFolderName;
        this.compiler = ToolProvider.getSystemJavaCompiler();
        initFileManager();
    }

    public CompilerService() {
        this(GENERATED_CLASS_FILES_FOLDER);
    }

    private void initFileManager() {
        this.fileManager = compiler
            .getStandardFileManager(new DiagnosticLoggingListener("FILE_MANAGER"), DEFAULT_LOCALE,
                Charset.defaultCharset());
    }

    /**
     * Compiles provided Java source files in the order provided (hence the use of list)
     *
     * @param files
     * @return true if all files compiled successfully
     * @throws IOException
     */
    public Boolean compileGeneratedSourceFiles(List<String> classesNames, List<File> files)
        throws IOException {

        Iterable<? extends JavaFileObject> javaFiles =
            fileManager.getJavaFileObjectsFromFiles(files);
        Boolean success = compiler.getTask(
            new OutputStreamWriter(System.out),
            fileManager,
            new DiagnosticLoggingListener("COMPILER"),
            Arrays.asList("-d", classFilesFolderName),
            classesNames,
            javaFiles
        ).call();
//        fileManager.close();
//        initFileManager();
        return success;
    }

    public ClassLoader classLoader() {
        return ToolProvider.getSystemToolClassLoader();
    }

    public Boolean compileGeneratedSourceFiles(List<String> classesNames, File... files)
        throws IOException {
        return compileGeneratedSourceFiles(classesNames, Arrays.asList(files));
    }

    public Boolean compileGeneratedSourceFiles(List<File> files) throws IOException {
        return compileGeneratedSourceFiles(null, files);
    }

    public Boolean compileGeneratedSourceFiles(File... files) throws IOException {
        return compileGeneratedSourceFiles(null, files);
    }

    static class DiagnosticLoggingListener implements DiagnosticListener<JavaFileObject> {

        final String label;

        DiagnosticLoggingListener(String label) {
            this.label = (stringWithDefault(label, "COMPILING"));
        }

        @Override
        public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
            if (LOG != null) {
                switch (diagnostic.getKind()) {
                    case ERROR:
                        LOG.error(
                            "[{}] {} {} at {}:{}",
                            this.label,
                            stringWithDefault(diagnostic.getCode(), "NO_CODE"),
                            stringWithDefault(diagnostic.getMessage(DEFAULT_LOCALE), "NO_MESSAGE"),
                            stringWithDefault((diagnostic.getSource() != null ?
                                diagnostic.getSource().getName() :
                                ""), "NO_SOURCE"),
                            stringWithDefault(Long.toString(diagnostic.getLineNumber()),
                                "NO_LINE"));
                        break;
                    case WARNING:
                    case MANDATORY_WARNING:
                        LOG.warn("[{}] {} {} at {}:{}", label, diagnostic.getCode(),
                            diagnostic.getMessage(DEFAULT_LOCALE), diagnostic.getSource().getName(),
                            diagnostic.getLineNumber());
                        break;
                    case NOTE:
                        LOG.info("[{}] {} {} at {}:{}", label, diagnostic.getCode(),
                            diagnostic.getMessage(DEFAULT_LOCALE), diagnostic.getSource().getName(),
                            diagnostic.getLineNumber());
                        break;
                    case OTHER:
                        LOG.debug("[{}] {} {} at {}:{}", label, diagnostic.getCode(),
                            diagnostic.getMessage(DEFAULT_LOCALE), diagnostic.getSource().getName(),
                            diagnostic.getLineNumber());
                        break;
                }
            }
        }

        private String stringWithDefault(String code, String d) {
            return StringUtils.isNotBlank(code) ? code : d;
        }
    }

}
