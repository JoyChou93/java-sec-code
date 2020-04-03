package org.joychou.security;

import java.lang.reflect.Method;

public class SocketHookUtils {

    /**
     * @Author liergou
     * @Description 轮询父类查找反射方法
     * @Date 1:43 2020/4/4
     * @Param [inputClazz, findName, args]
     * @return java.lang.reflect.Method
     **/
    public static Method findMethod(Class<?> inputClazz, String findName ,Class<?>[] args){
        Class<?> temp=inputClazz;
        Method tmpMethod = null;
        while(temp!=null){
            try{
                tmpMethod = temp.getDeclaredMethod(findName,args);
                tmpMethod.setAccessible(true);
                return tmpMethod;
            }catch (NoSuchMethodException e){
                temp=temp.getSuperclass();
            }
        }
        return null;
    }

}
