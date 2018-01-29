package mi.data.test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Pipe;

public class NIOTest {

    public static void main(String[] args) throws Exception {
        // methodIO();
        // methodNIO();
        pipeChannel();
    }

    public static void pipeChannel() throws Exception {
        Pipe pipe = Pipe.open();
        Pipe.SinkChannel sinkChannel = pipe.sink();
        //
        String newData = "New String to write to file..." + System.currentTimeMillis();
        ByteBuffer buf1 = ByteBuffer.allocate(48);
        buf1.clear();
        buf1.put(newData.getBytes());
        buf1.flip();
        while (buf1.hasRemaining()) {
            sinkChannel.write(buf1);
        }
        Pipe.SourceChannel sourceChannel = pipe.source();
        ByteBuffer buf2 = ByteBuffer.allocate(48);
        int bytesRead = sourceChannel.read(buf2);
        System.out.println(bytesRead);
        //
        buf2.flip();
        while (buf2.hasRemaining()) {
            System.out.print((char)buf2.get());
        }
    }

    // 非阻塞IO
    public static void methodNIO() {
        RandomAccessFile aFile = null;
        try {
            aFile = new RandomAccessFile("src/nio.txt", "rw");
            FileChannel fileChannel = aFile.getChannel();
            ByteBuffer buf = ByteBuffer.allocate(1024);
            //
            int bytesRead = fileChannel.read(buf);
            System.out.println(bytesRead);
            while (bytesRead != -1) {
                buf.flip();
                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get());
                }
                buf.compact();
                bytesRead = fileChannel.read(buf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (aFile != null) {
                    aFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 传统IO
    public static void methodIO() {
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream("src/normal_io.txt"));
            //
            byte[] buf = new byte[1024];
            int bytesRead = in.read(buf);
            while (bytesRead != -1) {
                for (int i = 0; i < bytesRead; i++)
                    System.out.print((char) buf[i]);
                bytesRead = in.read(buf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
