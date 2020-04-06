package org.joychou.security.ssrf;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;


/**
 * Socket Hook switch
 *
 * @author liergou @ 2020-04-04 02:12
 */
public class SocketHook {

    public static void startHook() throws IOException {
        SocketHookFactory.initSocket();
        SocketHookFactory.setHook(true);
        try{
            Socket.setSocketImplFactory(new SocketHookFactory());
        }catch (SocketException ignored){
        }
    }

    public static void stopHook(){
        SocketHookFactory.setHook(false);
    }
}