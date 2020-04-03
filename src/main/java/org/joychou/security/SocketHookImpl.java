package org.joychou.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Author liergou
 * @Description socket impl
 * @Date 23:39 2020/4/2
 * @Param
 * @return
 **/
public class SocketHookImpl extends SocketImpl implements SocketOptions
{

    private SocketImpl socketImpl = null;
    private Method createImpl;
    private Method connectHostImpl;
    private Method connectInetAddressImpl;
    private Method connectSocketAddressIMPL;
    private Method bindImpl;
    private Method listenImpl;
    private Method acceptImpl;
    private Method getInputStreamImpl;
    private Method getOutputStreamImpl;
    private Method availableImpl;
    private Method closeImpl;
    private Method shutdownInputImpl;
    private Method shutdownOutputImpl;
    private Method sendUrgentDataImpl;


    /**
     * @Author liergou
     * @Description 初始化反射方法
     * @Date 23:40 2020/4/2
     * @Param [initSocketImpl]
     * @return
     **/
    public SocketHookImpl(SocketImpl initSocketImpl) {

        if ( initSocketImpl == null){
            throw new RuntimeException("");
            //TODO close hook
        }

        this.socketImpl = initSocketImpl;
        final Class<?>  clazz = this.socketImpl.getClass();
        Method[] allMethod = clazz.getDeclaredMethods();
        createImpl = SocketHookUtils.findMethod( clazz,"create", new Class<?>[]{ boolean.class } );
        connectHostImpl = SocketHookUtils.findMethod( clazz, "connect", new Class<?>[]{ String.class, int.class } );
        connectInetAddressImpl = SocketHookUtils.findMethod( clazz, "connect", new Class<?>[]{ InetAddress.class, int.class } );
        connectSocketAddressIMPL = SocketHookUtils.findMethod( clazz, "connect", new Class<?>[]{ SocketAddress.class, int.class } );
        bindImpl = SocketHookUtils.findMethod( clazz, "bind", new Class<?>[]{ InetAddress.class, int.class } );
        listenImpl = SocketHookUtils.findMethod( clazz, "listen", new Class<?>[]{ int.class } );
        acceptImpl = SocketHookUtils.findMethod( clazz, "accept", new Class<?>[]{ SocketImpl.class } );
        getInputStreamImpl = SocketHookUtils.findMethod( clazz, "getInputStream", new Class<?>[]{  } );
        getOutputStreamImpl = SocketHookUtils.findMethod( clazz, "getOutputStream", new Class<?>[]{  } );
        availableImpl = SocketHookUtils.findMethod( clazz, "available", new Class<?>[]{ } );
        closeImpl = SocketHookUtils.findMethod( clazz, "close", new Class<?>[]{ } );
        shutdownInputImpl = SocketHookUtils.findMethod( clazz, "shutdownInput", new Class<?>[]{ } );
        shutdownOutputImpl = SocketHookUtils.findMethod( clazz, "shutdownOutput", new Class<?>[]{ } );
        sendUrgentDataImpl = SocketHookUtils.findMethod( clazz, "sendUrgantData", new Class<?>[]{ int.class } );
    }


    /**
     * socket base method impl
     */
    @Override
    protected void create(boolean stream) throws IOException {
            try
            {
                this.createImpl.invoke( this.socketImpl, stream);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
            {
                Logger.getLogger(SocketHookImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

    }

    @Override
    protected void connect(String host, int port) throws IOException {
        Logger.getLogger(SocketHookImpl.class.getName()).log(Level.INFO, "host=" + host + ",port=" + port );

            try
            {
                this.connectHostImpl.invoke( this.socketImpl, host, port);
            }
            catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException ex)
            {
                Logger.getLogger(SocketHookImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

    }


    @Override
    protected void connect(InetAddress address, int port) throws IOException {
            Logger.getLogger(SocketHookImpl.class.getName()).log(Level.INFO, "InetAddress=" + address.toString());

            //start check SSRF
            if(SSRFChecker.isInnerIp(SSRFChecker.getIpFromStr(address.toString()))){
                throw new RuntimeException("Socket SSRF check failed. InetAddress:"+address.toString());
            }
            try
            {
                this.connectInetAddressImpl.invoke( this.socketImpl, address, port);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
            {
                Logger.getLogger(SocketHookImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    @Override
    protected void connect(SocketAddress address, int timeout) throws IOException {
            Logger.getLogger(SocketHookImpl.class.getName()).log(Level.INFO, "SocketAddress=" + address.toString());
            //start check SSRF
            if(SSRFChecker.isInnerIp(SSRFChecker.getIpFromStr(address.toString()))){
                throw new RuntimeException("Socket SSRF check failed. SocketAddress:"+address.toString());
            }

            try
            {
                this.connectSocketAddressIMPL.invoke( this.socketImpl, address, timeout);
            }
            catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException ex)
            {
                Logger.getLogger(SocketHookImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    @Override
    protected void bind(InetAddress host, int port) throws IOException {
            try
            {
                this.bindImpl.invoke( this.socketImpl, host, port);
            }
            catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException ex)
            {
                Logger.getLogger(SocketHookImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    @Override
    protected void listen(int backlog) throws IOException {

            try
            {
                this.listenImpl.invoke( this.socketImpl, backlog);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
            {
                Logger.getLogger(SocketHookImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    @Override
    protected void accept(SocketImpl s) throws IOException {

            try
            {
                this.acceptImpl.invoke( this.socketImpl, s);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
            {
                Logger.getLogger(SocketHookImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    @Override
    protected InputStream getInputStream() throws IOException {
        InputStream inStream = null;

            try
            {
                inStream = (InputStream)this.getInputStreamImpl.invoke( this.socketImpl);
            }
            catch ( ClassCastException | InvocationTargetException | IllegalArgumentException | IllegalAccessException ex )
            {
                Logger.getLogger(SocketHookImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        return inStream;
    }

    @Override
    protected OutputStream getOutputStream() throws IOException {
        OutputStream outStream = null;

            try
            {
                outStream = (OutputStream)this.getOutputStreamImpl.invoke( this.socketImpl);
            }
            catch ( ClassCastException | IllegalArgumentException | IllegalAccessException | InvocationTargetException ex )
            {
                Logger.getLogger(SocketHookImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        return outStream;
    }

    @Override
    protected int available() throws IOException {
        int result = -1;

            try
            {
                result = (Integer)this.availableImpl.invoke( this.socketImpl);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
            {
                Logger.getLogger(SocketHookImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        return result;
    }

    @Override
    protected void close() throws IOException {
            try
            {
                this.closeImpl.invoke( this.socketImpl);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
            {
                Logger.getLogger(SocketHookImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    @Override
    protected void shutdownInput() throws IOException {
        try
        {
            this.shutdownInputImpl.invoke( this.socketImpl);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
            Logger.getLogger(SocketHookImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void shutdownOutput() throws IOException {
        try
        {
            this.shutdownOutputImpl.invoke( this.socketImpl);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
            Logger.getLogger(SocketHookImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void sendUrgentData(int data) throws IOException {
            try
            {
                this.sendUrgentDataImpl.invoke( this.socketImpl, data);
            }
            catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException ex)
            {
                Logger.getLogger(SocketHookImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    public void setOption(int optID, Object value) throws SocketException {
        if ( null != this.socketImpl )
        {
            this.socketImpl.setOption( optID, value );
        }
    }

    public Object getOption(int optID) throws SocketException {
        return this.socketImpl.getOption( optID );
    }

    /**
     * dont impl other child method now
     * dont sure where will use it
     **/


}
