package org.joychou.security;


import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.SocketImpl;
import java.net.SocketImplFactory;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @Author liergou
 * @Description socket factory impl
 * @Date 23:41 2020/4/3
 * @Param
 * @return
 **/
public class SocketHookFactory implements SocketImplFactory
    {
        private static SocketImpl   clazz;
        private static Boolean isHook = false;

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
        public static synchronized void initSocket() throws NoSuchFieldException {
            if ( clazz != null ) { return; }

            Socket  socket = new Socket();
            try{
                Field implField = Socket.class.getDeclaredField("impl");
                implField.setAccessible( true );
                clazz = (SocketImpl) implField.get(socket);
            }catch (NoSuchFieldException | IllegalAccessException e){
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
                        return new SocketHookImpl(clazz);
                    } catch (Exception e) {
                        Logger.getLogger(SocketHookFactory.class.getName()).log(Level.WARNING, "hook 失败  请检查" );
                        return clazz;
                    }
            }else{
                return clazz;
            }
        }
    }
