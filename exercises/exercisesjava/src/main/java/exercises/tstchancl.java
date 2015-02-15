/**
 * Non-blocking client.
 * We carefully take bytes from the specific file and then send data to server.
 */

package exercises;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class tstchancl {
    private ByteBuffer buffer;

    public static void main(String[] args) {

        try {
            new tstchancl().clientStart();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clientStart() throws IOException {
        //open channel and stream
        try (SocketChannel channel = SocketChannel.open(new InetSocketAddress(8188))) {
            //non-blocking mode
            channel.configureBlocking(false);

            final byte[] bytes = new byte[36];
            buffer = ByteBuffer.wrap(bytes);

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {

                //if client and server are not  local machine instances then we use selector to finish connection
                if (!channel.isConnected()) {
                    try (Selector selector = Selector.open()) {
                        channel.register(selector, SelectionKey.OP_CONNECT);
                        //waiting server answer
                        int d = selector.select();
                        Set<SelectionKey> selectedKey = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectedKey.iterator();
                        //here we finish connection
                        if (iterator.hasNext()) {
                            SelectionKey key = iterator.next();
                            iterator.remove();
                            if (key.isConnectable()) {
                                key.cancel();
                                if (channel.isConnectionPending()) {
                                    channel.finishConnect();
                                }
                            }
                        }
                    }
                }
               try(FileInputStream output=new FileInputStream(scanner.nextLine())) {
                   //write data from stream to channel
                   int count;
                   while ((count = output.read(bytes)) != -1)

                   {
                       buffer.limit(count);
                       System.out.println(count);
                       channel.write(buffer);
                       buffer.clear();
                   }
               }
            }
        }
    }
}

