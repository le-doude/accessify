package base;

import org.accessify.annotations.Property;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * Created by edouard on 14/06/11.
 */
public class TemplatingTest {

    static final String CLASS_NAME_FORMAT = "%s%sHandler";
    static final String FILE_NAME_FORMAT = "%s.java";
    static final String GENERATED_CODE_FOLDER = "src/test/java";
    static final String GENERATED_CLASS_FILES_FOLDER = "target/classes";

    @Test
    public void testName() throws Exception {



        Method[] methods = TestEntity.class.getDeclaredMethods();
        Map<String, String> settersNAme = new HashMap<>();
        for (Method setter : methods) {
            if (setter.isAnnotationPresent(Property.Setter.class)) {
                settersNAme.put(setter.getAnnotation(Property.Setter.class).name(), setter.getName());
            }
        }
        ArrayList<PropertyInfo> infos = new ArrayList<>();
        for (Method getter : methods) {
            if (getter.isAnnotationPresent(Property.Getter.class)) {
                PropertyInfo temp = new PropertyInfo();
                temp.entity = getter.getDeclaringClass().getSimpleName();
                String c = getter.getDeclaringClass().getCanonicalName();
                temp.pkg = c.substring(0, c.length() - temp.entity.length() - 1);
                temp.property = getter.getAnnotation(Property.Getter.class).name();
                temp.getter = getter.getName();
                temp.value = getter.getReturnType().getName();
                temp.setter = settersNAme.get(temp.property);
                infos.add(temp);
            }
        }

        Properties properties = new Properties();
        URL url = this.getClass().getClassLoader().getResource("velocity.properties");
        properties.load(url.openStream());
        VelocityEngine engine = new VelocityEngine(properties);
        engine.init();
        Template template = engine.getTemplate("property_handler.vtl");

        VelocityContext context;
        FileWriter writer;
        String filename;
        ArrayList<File> files = new ArrayList<>();

        File codeGenDir = new File(GENERATED_CODE_FOLDER);
        if (!codeGenDir.exists()) {
            try {
                codeGenDir.mkdirs();
            } catch (Exception ignored) {
            }
        }

        File file;
        for (PropertyInfo info : infos) {
            info.handler_type = String.format(CLASS_NAME_FORMAT, info.property, info.entity);
            context = info.toContext();
            filename = String.format(FILE_NAME_FORMAT, info.handler_type);
            file = new File(codeGenDir, filename);
            writer = new FileWriter(file);
            template.merge(context, writer);
            writer.flush();
            writer.close();
            files.add(file);
        }

        System.out.println(files);


        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> javaFiles = fileManager.getJavaFileObjectsFromFiles(files);
        compiler.getTask(null, fileManager, null, Arrays.asList("-d", GENERATED_CLASS_FILES_FOLDER), null, javaFiles).call();
        fileManager.close();

        //NOW we test the generated property handlers

        TestEntity instance = new TestEntity();
        instance.setBool(true);
        instance.setInteger(15);
        instance.setString("The quick fox jumps over the lazy dog.");

//        base.boolTestEntityHandler handler1 = new base.boolTestEntityHandler();
//
//        assertTrue(handler1.get(instance));
//        handler1.set(instance, false);
//        assertFalse(handler1.get(instance));
//
//        base.integerTestEntityHandler handler2 = new base.integerTestEntityHandler();
//
//        assertEquals(15, handler2.get(instance).intValue());
//        handler2.set(instance, 150);
//        assertEquals(150, handler2.get(instance).intValue());
//
//        base.stringTestEntityHandler handler3 = new base.stringTestEntityHandler();
//        assertEquals("The quick fox jumps over the lazy dog.", handler3.get(instance));
//        handler3.set(instance, "I shot a man in Reno and I can't tell you why.");
//        assertEquals("I shot a man in Reno and I can't tell you why.", handler3.get(instance));
    }


    static class PropertyInfo {
        String pkg;
        String handler_type;
        String entity;
        String value;
        String setter;
        String getter;
        String property;

        VelocityContext toContext() {
            VelocityContext context = new VelocityContext();
            context.put("package", pkg);
            context.put("handler_type", handler_type);
            context.put("entity", entity);
            context.put("value", value);
            context.put("setter", setter);
            context.put("getter", getter);
            context.put("property", property);
            return context;
        }
    }
}
