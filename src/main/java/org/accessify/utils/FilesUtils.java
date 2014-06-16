package org.accessify.utils;

import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by edouard on 14/06/16.
 */
public class FilesUtils {

    protected static final String JAVA_FILE_EXTANSION = ".java";
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(FilesUtils.class);

    public static File makeDirIfNotExists(String dir) {
        File codeGenDir = new File(dir);
        if (!codeGenDir.exists()) {
            try {
                if (!codeGenDir.mkdirs()) {
                    throw new RuntimeException("Could not create " + dir + "directory");
                }
            } catch (SecurityException e) {
                LOG.error("Could not create directory.", e);
            }
        }
        return codeGenDir;
    }

    public static File makeSourceFileForClassName(String className) {
        return makeSourceFileForClassName(className, (File) null);
    }

    public static File makeSourceFileForClassName(String className, String directory) {
        return makeSourceFileForClassName(className, makeDirIfNotExists(directory));
    }

    public static File makeSourceFileForClassName(String className, File directory) {
        if (directory != null) {
            return new File(directory, className + JAVA_FILE_EXTANSION);
        } else {
            return new File(className + JAVA_FILE_EXTANSION);
        }
    }

}
