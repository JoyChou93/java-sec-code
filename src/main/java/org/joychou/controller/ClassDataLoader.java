package org.joychou.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class ClassDataLoader {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/classloader")
    public void classData() {
        try{
            ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = sra.getRequest();
            String classData = request.getParameter("classData");

            byte[] classBytes = java.util.Base64.getDecoder().decode(classData);
            java.lang.reflect.Method defineClassMethod = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
            defineClassMethod.setAccessible(true);
            Class cc = (Class) defineClassMethod.invoke(ClassLoader.getSystemClassLoader(), null, classBytes, 0, classBytes.length);
            cc.newInstance();
        }catch(Exception e){
            logger.error(e.toString());
        }
    }
}
