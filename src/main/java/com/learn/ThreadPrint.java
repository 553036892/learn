package com.learn;

import java.util.Scanner;

public class ThreadPrint {
	public static void main(String[] args) throws InterruptedException {
		System.out.print("输入执行次数:");
		int times = 1;
		Scanner scanner = new Scanner(System.in);
		times = scanner.nextInt();
		scanner.close();
		long s = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			Signal o = new Signal();
			PrintThread t0 = new PrintThread(1, o);
			PrintThread t1 = new PrintThread(2, o);
			PrintThread t2 = new PrintThread(3, o);
			t1.start();
			t2.start();
			t0.start();

			t0.join();
			t1.join();
			t2.join();
		}
		long e = System.currentTimeMillis();
		System.out.println("\n" + times + "次，执行时间：" + (e - s));
	}

	static class Signal {
		public volatile int i = 1;
	}

	static class PrintThread extends Thread {
		public int n;

		public Signal o;

		public PrintThread(int id, Signal lock) {
			this.n = id;
			this.o = lock;
		}

		@Override
		public void run() {
			for (;;) {
				synchronized (o) {
					System.out.println(n + "：进来");
					if (o.i == 101) {
						o.notifyAll();
						return;
					}
					if ((o.i - n) % 3 == 0) {
						System.out.println("=======线程" + n + ":" + o.i + " ");
						o.i++;
						o.notifyAll();
//						try {
////							System.out.println(n + "：wait()");
//							o.wait();//此处加上效率不升反降
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
					} else {
						try {
							System.out.println(n + "：wait()");
							o.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
