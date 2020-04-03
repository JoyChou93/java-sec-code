package org.joychou.security;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * @Author liergou
 * @Description Socket hook开关自如
 * @Date 2:12 2020/4/4
 **/
class SocketHook {
    static void startHook() throws NoSuchFieldException, IOException {
        SocketHookFactory.initSocket();
        SocketHookFactory.setHook(true);
        try{
            Socket.setSocketImplFactory(new SocketHookFactory());
        }catch (SocketException ignored){
        }
    }

    static void stopHook(){
        SocketHookFactory.setHook(false);
    }
}