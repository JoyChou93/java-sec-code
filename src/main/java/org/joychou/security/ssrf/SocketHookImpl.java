package org.joychou.security.ssrf;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;


/**
 * Socket impl
 *
 * @author liergou @ 2020-04-02 23:39
 */
public class SocketHookImpl extends SocketImpl implements SocketOptions {

    private static Boolean isInit = false;

    private static SocketImpl socketImpl = null;
    private static Method createImpl;
    private static Method connectHostImpl;
    private static Method connectInetAddressImpl;
    private static Method connectSocketAddressImpl;
    private static Method bindImpl;
    private static Method listenImpl;
    private static Method acceptImpl;
    private static Method getInputStreamImpl;
    private static Method getOutputStreamImpl;
    private static Method availableImpl;
    private static Method closeImpl;
    private static Method shutdownInputImpl;
    private static Method shutdownOutputImpl;
    private static Method sendUrgentDataImpl;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    SocketHookImpl(Constructor socketConstructor) throws IllegalAccessException,
            InvocationTargetException, InstantiationException {
        socketImpl = (SocketImpl) socketConstructor.newInstance();
    }


    /**
     * Init reflect method.
     *
     * @author liergou
     */
    static void initSocketImpl(Class<?> initSocketImpl) {

        if (initSocketImpl == null) {
            SocketHookFactory.setHook(false);
            throw new RuntimeException("InitSocketImpl failed! Hook stopped!");
        }
        
        if (!isInit) {
            createImpl = SocketHookUtils.findMethod(initSocketImpl, "create", new Class<?>[]{boolean.class});
            connectHostImpl = SocketHookUtils.findMethod(initSocketImpl, "connect", new Class<?>[]{String.class, int.class});
            connectInetAddressImpl = SocketHookUtils.findMethod(initSocketImpl, "connect", new Class<?>[]{InetAddress.class, int.class});
            connectSocketAddressImpl = SocketHookUtils.findMethod(initSocketImpl, "connect", new Class<?>[]{SocketAddress.class, int.class});
            bindImpl = SocketHookUtils.findMethod(initSocketImpl, "bind", new Class<?>[]{InetAddress.class, int.class});
            listenImpl = SocketHookUtils.findMethod(initSocketImpl, "listen", new Class<?>[]{int.class});
            acceptImpl = SocketHookUtils.findMethod(initSocketImpl, "accept", new Class<?>[]{SocketImpl.class});
            getInputStreamImpl = SocketHookUtils.findMethod(initSocketImpl, "getInputStream", new Class<?>[]{});
            getOutputStreamImpl = SocketHookUtils.findMethod(initSocketImpl, "getOutputStream", new Class<?>[]{});
            availableImpl = SocketHookUtils.findMethod(initSocketImpl, "available", new Class<?>[]{});
            closeImpl = SocketHookUtils.findMethod(initSocketImpl, "close", new Class<?>[]{});
            shutdownInputImpl = SocketHookUtils.findMethod(initSocketImpl, "shutdownInput", new Class<?>[]{});
            shutdownOutputImpl = SocketHookUtils.findMethod(initSocketImpl, "shutdownOutput", new Class<?>[]{});
            sendUrgentDataImpl = SocketHookUtils.findMethod(initSocketImpl, "sendUrgantData", new Class<?>[]{int.class});
            isInit = true;
        }
    }


    /**
     * socket base method impl
     */
    @Override
    protected void create(boolean stream) {
        try {
            createImpl.invoke(socketImpl, stream);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            logger.error(ex.toString());
        }

    }


    @Override
    protected void connect(String host, int port) {
        logger.info("host: " + host + "\tport: " + port);
        try {
            connectHostImpl.invoke(socketImpl, host, port);
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException ex) {
            logger.error(ex.toString());
        }

    }


    @Override
    protected void connect(InetAddress address, int port) {

        logger.info("InetAddress: " + address.toString());

        try {
            if (SSRFChecker.isInternalIp(address.getHostAddress())) {
                throw new RuntimeException("Socket SSRF check failed. InetAddress:" + address.toString());
            }
            connectInetAddressImpl.invoke(socketImpl, address, port);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            logger.error(ex.toString());
        }
    }

    @Override
    protected void connect(SocketAddress address, int timeout) {

        // convert SocketAddress to InetSocketAddress
        InetSocketAddress addr = (InetSocketAddress) address;

        String ip = addr.getAddress().getHostAddress();
        String host = addr.getHostName();
        logger.info(String.format("[+] SocketAddress address's Hostname: %s IP: %s", host, ip));

        try {
            if (SSRFChecker.isInternalIp(ip)) {
                throw new SSRFException(String.format("[-] SSRF check failed. Hostname: %s IP: %s", host, ip));
            }
            connectSocketAddressImpl.invoke(socketImpl, address, timeout);
        } catch (IllegalAccessException | IllegalArgumentException |
                InvocationTargetException ex) {
            logger.error(ex.toString());
        }

    }

    @Override
    protected void bind(InetAddress host, int port) {
        try {
            bindImpl.invoke(socketImpl, host, port);
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException ex) {
            logger.error(ex.toString());
        }
    }

    @Override
    protected void listen(int backlog) {

        try {
            listenImpl.invoke(socketImpl, backlog);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            logger.error(ex.toString());
        }
    }

    @Override
    protected void accept(SocketImpl s) {

        try {
            acceptImpl.invoke(socketImpl, s);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            logger.error(ex.toString());
        }
    }

    @Override
    protected InputStream getInputStream() {
        InputStream inStream = null;

        try {
            inStream = (InputStream) getInputStreamImpl.invoke(socketImpl);
        } catch (ClassCastException | InvocationTargetException |
                IllegalArgumentException | IllegalAccessException ex) {
            logger.error(ex.toString());
        }

        return inStream;
    }

    @Override
    protected OutputStream getOutputStream() {
        OutputStream outStream = null;

        try {
            outStream = (OutputStream) getOutputStreamImpl.invoke(socketImpl);
        } catch (ClassCastException | IllegalArgumentException |
                IllegalAccessException | InvocationTargetException ex) {
            logger.error(ex.toString());
        }

        return outStream;
    }

    @Override
    protected int available() {

        int result = -1;

        try {
            result = (Integer) availableImpl.invoke(socketImpl);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            logger.error(ex.toString());
        }

        return result;
    }

    @Override
    protected void close() {
        try {
            closeImpl.invoke(socketImpl);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            logger.error(ex.toString());
        }
    }

    @Override
    protected void shutdownInput() {
        try {
            shutdownInputImpl.invoke(socketImpl);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            logger.error(ex.toString());
        }

    }

    @Override
    protected void shutdownOutput() {
        try {
            shutdownOutputImpl.invoke(socketImpl);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            logger.error(ex.toString());
        }

    }

    @Override
    protected void sendUrgentData(int data) {
        try {
            sendUrgentDataImpl.invoke(socketImpl, data);
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException ex) {
            logger.error(ex.toString());
        }
    }

    public void setOption(int optID, Object value) throws SocketException {
        if (null != socketImpl) {
            socketImpl.setOption(optID, value);
        }
    }

    public Object getOption(int optID) throws SocketException {
        return socketImpl.getOption(optID);
    }

    /*
     * Dont impl other child method now. Don't be sure where will use it.
     *
     */


}
