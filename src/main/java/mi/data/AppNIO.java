package mi.data;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Hello world!
 */
public class AppNIO {

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    /**
     * capacity	缓冲区数组的总长度
     * position	下一个要操作的数据元素的位置
     * limit 缓冲区数组中不可操作的下一个元素的位置：limit<=capacity
     * mark	用于记录当前position的前一个位置或者默认是0
     */
    public static void method1() {
        RandomAccessFile aFile = null;
        ByteBuffer buf = ByteBuffer.allocate(1024);
        try {
            aFile = new RandomAccessFile("src/nio.txt", "rw");
            FileChannel fileChannel = aFile.getChannel();
            int bytesRead = fileChannel.read(buf);
            System.out.println(bytesRead);
            while (bytesRead != -1) {
                buf.flip(); // 设置limit为position, 然后再设置position为起始位置
                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get());
                }
                buf.compact(); // 将所有未读的数据拷贝到Buffer的起始初, limit属性设置为capacity
                bytesRead = fileChannel.read(buf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                buf.clear(); // position将被设置为0, limit设置为capacity, 即buffer被清空
                if (aFile != null) {
                    aFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
