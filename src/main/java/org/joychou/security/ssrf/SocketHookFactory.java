package org.joychou.security.ssrf;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.SocketImpl;
import java.net.SocketImplFactory;


/**
 * socket factory impl
 *
 * @author liergou @ 2020-04-03 23:41
 */
public class SocketHookFactory implements SocketImplFactory {


    private static Boolean isHook = false;
    private static Constructor socketConstructor = null;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * @param set hook switch
     */
    static void setHook(Boolean set) {
        isHook = set;
    }


    static void initSocket() {

        if (socketConstructor != null) {
            return;
        }

        Socket socket = new Socket();
        try {
            // get impl field in Socket class
            Field implField = Socket.class.getDeclaredField("impl");
            implField.setAccessible(true);
            Class<?> clazz = implField.get(socket).getClass();

            SocketHookImpl.initSocketImpl(clazz);
            socketConstructor = clazz.getDeclaredConstructor();
            socketConstructor.setAccessible(true);

        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException e) {
            throw new SSRFException("SocketHookFactory init failed!");
        }

        try {
            socket.close();
        } catch (IOException ignored) {

        }
    }


    public SocketImpl createSocketImpl() {

        if (isHook) {
            try {
                return new SocketHookImpl(socketConstructor);
            } catch (Exception e) {
                logger.error("Socket hook failed!");
                try {
                    return (SocketImpl) socketConstructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                    logger.error(ex.toString());
                }
            }
        } else {
            try {
                return (SocketImpl) socketConstructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                logger.error(e.toString());
            }
        }

        return null;
    }
}
