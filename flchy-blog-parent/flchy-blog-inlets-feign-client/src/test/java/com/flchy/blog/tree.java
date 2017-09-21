package com.flchy.blog;

public class tree {
	public String ss;

	public String getSs() {
		return ss;
	}

	public void setSs(String ss) {
		this.ss = ss;
	}

	static class Thread1 implements Runnable {
		@Override
		public void run() {
			System.out.println("Thread1 is ready :currenttime-->" + System.currentTimeMillis());
			// 因为sleep不会释放资源，所以在主线程sleep结束前，是不能取得资源的锁，而是在等待
			tree t=new tree();
			synchronized (tree.class) {
				System.out.println("Thread1 is running :currenttime-->" + System.currentTimeMillis());
				System.out.println("Thread1 wait :currenttime-->" + System.currentTimeMillis());
				try {
					
					tree.class.wait(10000);
					t.setSs("ss");
					System.out.println();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Thread1 is over ");
			}
		}

	}

	static class Thread2 implements Runnable {
		@Override
		public void run() {
			System.out.println("Thread2 is ready :currenttime-->" + System.currentTimeMillis());
			synchronized (tree.class) {
				System.out.println("Thread2 is running :currenttime-->" + System.currentTimeMillis());
				System.out.println("Thread2 notify :currenttime-->" + System.currentTimeMillis());
				tree.class.notify();
				System.out.println("Thread2 is over");

			}
		}

	}

	public static void main(String[] args) throws InterruptedException {

		new Thread(new Thread1()).start();

//		synchronized (tree.class) {
//			System.out.println("Main Thread go to sleep  : currenttime-->" + System.currentTimeMillis());
//			// sleep过程不会释放资源
//			Thread.sleep(10000);
//		}
//		System.out.println("Main Thread get up : currenttime-->" + System.currentTimeMillis());
//		new Thread(new Thread2()).start();
//		System.out.println("Main Thread over");
	}
}
