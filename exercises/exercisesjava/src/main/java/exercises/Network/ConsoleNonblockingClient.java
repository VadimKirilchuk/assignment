/**
 * Non-blocking client.
 * We carefully take bytes from the specific file and then send data to server.
 */

package exercises.Network;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class ConsoleNonblockingClient {
    private ByteBuffer buffer;

    public static void main(String[] args) {

        try {
            new ConsoleNonblockingClient().clientStart();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clientStart() throws IOException {
        //open channel and stream
        try (SocketChannel channel = SocketChannel.open(new InetSocketAddress(8188))) {
            //non-blocking mode
            channel.configureBlocking(false);
            byte[] bytes = new byte[36];
            buffer = ByteBuffer.wrap(bytes);
            //if client and server are not  local machine instances
            // then we use selector to finish connection
            if (!channel.isConnected()) {
                finishConnectChannel(channel);
            }
            //write data from stream to channel
            System.out.println("wr");
            writeData(channel);
        }
    }

    public void finishConnectChannel(SocketChannel channel) throws IOException {
        try (Selector selector = Selector.open()) {
            channel.register(selector, SelectionKey.OP_CONNECT);
            //waiting server answer
            selector.select();
            Set<SelectionKey> selectedKey = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKey.iterator();
            //here we finish connection
            if (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isConnectable()) {
                    if (channel.isConnectionPending()) {
                        while (!channel.finishConnect()) ;
                    }
                }
            }
        }
    }

    public void writeData(SocketChannel channel) throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                try (FileInputStream fileStream = new FileInputStream(scanner.nextLine());
                     ReadableByteChannel readableByteChannel = Channels.newChannel(fileStream)) {
                    while (readableByteChannel.read(buffer) != -1) {
                        buffer.flip();
                        channel.write(buffer);
                        buffer.clear();
                    }
                }
            }
        }
    }
}
