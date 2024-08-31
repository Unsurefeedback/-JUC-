package cn.itcast.test;

/**
 * @author WeiHanQiang
 * @date 2024/08/17 22:04
 **/
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class FileResource {
    private FileWriter writer;

    public FileResource(String filePath) throws IOException {
        this.writer = new FileWriter(new File(filePath));
        System.out.println("FileResource created for file: " + filePath);
    }

    public void writeData(String data) throws IOException {
        writer.write(data);
    }

    public void close() throws IOException {
        if (writer != null) {
            writer.close();
            System.out.println("FileResource closed.");
        }
    }
}

class FileResourcePhantomReference extends PhantomReference<FileResource> {
    private String filePath;

    public FileResourcePhantomReference(FileResource referent, ReferenceQueue<? super FileResource> queue, String filePath) {
        super(referent, queue);
        this.filePath = filePath;
    }

    public void cleanup() {
        // 在对象被回收后执行的清理操作
        System.out.println("Cleaning up resources for file: " + filePath);
    }
}
// 展示虚引用的作用
public class ResourceManager {
    private static ReferenceQueue<FileResource> queue = new ReferenceQueue<>();

    public static void main(String[] args) throws IOException {
        // 创建一个FileResource对象
        FileResource resource = new FileResource("example.txt");

        // 创建虚引用并与引用队列关联
        FileResourcePhantomReference ref = new FileResourcePhantomReference(resource, queue, "example.txt");

        // 模拟写入数据
        resource.writeData("Hello, World!");

        // 释放强引用，让FileResource成为虚引用
        resource = null;

        // 强制执行垃圾回收
        System.gc();

        // 检查引用队列中是否有被回收的虚引用
        try {
            FileResourcePhantomReference polledRef = (FileResourcePhantomReference) queue.remove();
            if (polledRef != null) {
                // 执行清理操作
                polledRef.cleanup();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

