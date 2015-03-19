/**
 *Non-blocking server-side
 */

package exercises.Network;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
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
        configurate(serverChannel, selector);
        while (true) {
            // waiting for clients
            selector.select();
            //get a set of keys which want to make any action
            Set<SelectionKey> set = selector.selectedKeys();
            Iterator<SelectionKey> iterator = set.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    accept(key, selector);
                    continue;
                }
                if (key.isReadable()) {
                    read(key);
                    System.out.println("read");
                    continue;
                }
            }
        }
    }

    public void configurate(ServerSocketChannel serverChannel,
                            Selector selector) throws IOException {
        //put channel to non-blocking mode
        serverChannel.configureBlocking(false);
        //bound to port 8188
        serverChannel.socket().bind(new InetSocketAddress(8188));
        //registration server channel at selector, so we will know
        // if client will want to make connection
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void accept(SelectionKey key,
                       Selector selector) throws IOException {
        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
        socketChannel.configureBlocking(false);
        //registration channel to be able to know  what time to read data
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    public void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        buffer = ByteBuffer.allocate(36);
        int read;
        try {
            read = socketChannel.read(buffer);
        } catch (IOException e) {
            socketChannel.close();
            return;
        }
        if (read == -1) {
            socketChannel.close();
            return;
        }
        try {
            buffer.flip();
            writeToFile(buffer);
            buffer.clear();
        } catch (IOException e) {
            socketChannel.close();
            return;
        }
    }

    public void writeToFile(ByteBuffer buffer) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream("e:\\outTxt.txt", true);
             WritableByteChannel channelForWriting = Channels.newChannel(outputStream)) {
            channelForWriting.write(buffer);
        }
    }
}

