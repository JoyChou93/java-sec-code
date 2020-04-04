package org.joychou.security;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.SocketImpl;
import java.net.SocketImplFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Author liergou
 * @Description socket factory impl
 * @Date 23:41 2020/4/3
 * @Param
 * @return
 **/
public class SocketHookFactory implements SocketImplFactory
{
    private static Logger logger = LoggerFactory.getLogger(SocketHookFactory.class);

    private static Boolean isHook = false;
    private static Constructor socketConstructor = null;


    /**
     * @Author liergou
     * @Description switch hook
     * @Date 23:42 2020/4/2
     * @Param [set]
     * @return void
     **/
    public static void setHook(Boolean set){
        isHook = set;
    }

    /**
     * @Author liergou
     * @Description 初始化
     * @Date 23:42 2020/4/2
     * @Param []
     * @return void
     **/
    public static void initSocket() {
        if ( socketConstructor != null ) { return; }

        Socket  socket = new Socket();
        try{
            Field implField = Socket.class.getDeclaredField("impl");
            implField.setAccessible( true );
            Class<?> clazz = implField.get(socket).getClass();
            SocketHookImpl.initSocketImpl(clazz);
            socketConstructor = clazz.getDeclaredConstructor();
            socketConstructor.setAccessible(true);
        }catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException e){
            throw new RuntimeException("SocketHookFactory init failed!");
        }

        try {
            socket.close();
        }
        catch ( IOException ignored)
        {

        }
    }

    public SocketImpl createSocketImpl() {

        if(isHook) {
            try {
                return new SocketHookImpl(socketConstructor);
            } catch (Exception e) {
                logger.error( "hook 失败  请检查" );
                try {
                    return (SocketImpl) socketConstructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                    ex.printStackTrace();
                }
            }
        }else{
            try {
                return (SocketImpl) socketConstructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
