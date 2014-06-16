package org.accessify.utils;

import org.apache.velocity.VelocityContext;

import java.util.Arrays;

/**
 * Created by edouard on 14/06/16.
 */
public class ContextDebugUtil {

    public static String toString(VelocityContext context) {
        if (context != null) {
            StringBuilder builder = new StringBuilder("VelocityContext={");
            Object[] objects = context.internalGetKeys();
            builder.append(objects[0]).append(":").append(context.get((String) objects[0]));
            for (Object o : Arrays.copyOfRange(objects, 1, objects.length)) {

                builder.append(", ").append(o).append(":").append(context.get((String) o));
            }
            return builder.toString();
        } else {
            return "VelocityContext=null";
        }
    }

}
