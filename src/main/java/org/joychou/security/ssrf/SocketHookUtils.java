package org.joychou.security.ssrf;

import java.lang.reflect.Method;

class SocketHookUtils {

    /**
     * Poll the parent class to find the reflection method.
     *
     * @author liergou @2020-04-04 01:43
     */
    static Method findMethod(Class<?> inputClazz, String findName, Class<?>[] args) {

        Class<?> temp = inputClazz;

        while (temp != null) {
            try {
                Method tmpMethod = temp.getDeclaredMethod(findName, args);
                tmpMethod.setAccessible(true);
                return tmpMethod;
            } catch (NoSuchMethodException e) {
                temp = temp.getSuperclass();
            }
        }
        return null;
    }

}
