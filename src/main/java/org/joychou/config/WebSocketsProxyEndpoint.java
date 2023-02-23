package org.joychou.config;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class WebSocketsProxyEndpoint extends Endpoint {
    long i = 0;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    HashMap<String, AsynchronousSocketChannel> map = new HashMap<String, AsynchronousSocketChannel>();

    static class Attach {
        public AsynchronousSocketChannel client;
        public Session channel;
    }

    void readFromServer(Session channel, AsynchronousSocketChannel client) {
        final ByteBuffer buffer = ByteBuffer.allocate(50000);
        Attach attach = new Attach();
        attach.client = client;
        attach.channel = channel;
        client.read(buffer, attach, new CompletionHandler<Integer, Attach>() {
            @Override
            public void completed(Integer result, final Attach scAttachment) {
                buffer.clear();
                try {
                    if (buffer.hasRemaining() && result >= 0) {
                        byte[] arr = new byte[result];
                        ByteBuffer b = buffer.get(arr, 0, result);
                        baos.write(arr, 0, result);
                        ByteBuffer q = ByteBuffer.wrap(baos.toByteArray());
                        if (scAttachment.channel.isOpen()) {
                            scAttachment.channel.getBasicRemote().sendBinary(q);
                        }
                        baos = new ByteArrayOutputStream();
                        readFromServer(scAttachment.channel, scAttachment.client);
                    } else {
                        if (result > 0) {
                            byte[] arr = new byte[result];
                            ByteBuffer b = buffer.get(arr, 0, result);
                            baos.write(arr, 0, result);
                            readFromServer(scAttachment.channel, scAttachment.client);
                        }
                    }
                } catch (Exception ignored) {
                }
            }

            @Override
            public void failed(Throwable t, Attach scAttachment) {
                t.printStackTrace();
            }
        });
    }

    void process(ByteBuffer z, Session channel) {
        try {
            if (i > 1) {
                AsynchronousSocketChannel client = map.get(channel.getId());
                client.write(z).get();
                z.flip();
                z.clear();
            } else if (i == 1) {
                String values = new String(z.array());
                String[] array = values.split(" ");
                String[] addrarray = array[1].split(":");
                AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
                int po = Integer.parseInt(addrarray[1]);
                InetSocketAddress hostAddress = new InetSocketAddress(addrarray[0], po);
                Future<Void> future = client.connect(hostAddress);
                try {
                    future.get(10, TimeUnit.SECONDS);
                } catch (Exception ignored) {
                    channel.getBasicRemote().sendText("HTTP/1.1 503 Service Unavailable\r\n\r\n");
                    return;
                }
                map.put(channel.getId(), client);
                readFromServer(channel, client);
                channel.getBasicRemote().sendText("HTTP/1.1 200 Connection Established\r\n\r\n");
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onOpen(final Session session, EndpointConfig config) {
        i = 0;
        session.setMaxBinaryMessageBufferSize(1024 * 1024 * 20);
        session.setMaxTextMessageBufferSize(1024 * 1024 * 20);
        session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {
            @Override
            public void onMessage(ByteBuffer message) {
                try {
                    message.clear();
                    i++;
                    process(message, session);
                } catch (Exception ignored) {
                }
            }
        });
    }
}