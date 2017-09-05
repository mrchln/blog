package BlockingQueue.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class test {
public static void main(String[] args) {
	// 声明一个容量为10的缓存队列
			BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10);

			Producer producer1 = new Producer(queue);
			// 借助Executors
			ExecutorService service = Executors.newCachedThreadPool();
			// 启动线程
			service.execute(producer1);
}
}
