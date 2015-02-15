/**
 *Non-blocking server-side
 */

package exercises;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NonblockingServer {
    private ByteBuffer buffer;

    public static void main(String[] args) {
        try {
            new NonblockingServer().serverStart();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serverStart() throws IOException {
        //make server channel
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        //put channel to non-blocking mode
        serverChannel.configureBlocking(false);
        //bound to port 8188
        serverChannel.socket().bind(new InetSocketAddress(8188));
        //registration server channel at selector, so we will know if client will want to make connection
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        firststep:
        while (true) {
            // waiting for clients
            System.out.println(selector.select());

            //get a set of keys which want to make any action
            Set<SelectionKey> set = selector.selectedKeys();
            Iterator<SelectionKey> iterator = set.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    SocketChannel socketChannel = serverChannel.accept();
                    socketChannel.configureBlocking(false);
                    //registration channel to be able to know  what time to read data
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    continue;
                }
                buffer = ByteBuffer.allocate(36);
                if (key.isReadable()) {
                    System.out.println("read");
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    try (FileOutputStream outputStream = new FileOutputStream("e:\\outTxt.txt", true)) {
                        //working till EOS
                        while (socketChannel.read(buffer) != -1) {
                            buffer.flip();
                            outputStream.write(buffer.slice().array());
                            buffer.clear();
                        }
                        //if we will't close a channel than get EOS loop
                        //socketChannel.close();
                        //outputStream.close();
                        continue;
                    }
                }
            }
        }
    }
}
