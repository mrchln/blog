package BlockingQueue.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class testss {
	public static void main(String[] args) throws InterruptedException {
		// 声明一个容量为10的缓存队列
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10);

		Producer producer1 = new Producer(queue);
		Producer producer2 = new Producer(queue);
		Producer producer3 = new Producer(queue);
		Consumer consumer = new Consumer(queue);

		// 借助Executors
		ExecutorService service = Executors.newCachedThreadPool();
		service.execute(consumer);
		// 启动线程
		service.execute(producer1);
		service.execute(producer2);
		service.execute(producer3);

		for (int i = 0; i < 999999999; i++) {
//			service.execute(new  Producer(queue));
		}


	}
}
