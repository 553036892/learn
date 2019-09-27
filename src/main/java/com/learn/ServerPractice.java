package com.learn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServerPractice {

	/**
	 * 聊天室
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		datagram();
	}

	private static void datagram() throws IOException {
		DatagramChannel channel = DatagramChannel.open();
		channel.bind(new InetSocketAddress("localhost", 8080));
		channel.configureBlocking(false);
		Selector selector = Selector.open();
		channel.register(selector, SelectionKey.OP_READ);
		while (true) {
			int readyNum;
			try {
				readyNum = selector.select();
				if (readyNum == 0) {
					continue;
				}
				
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectedKeys.iterator();
				
				while (it.hasNext()) {
					SelectionKey key = it.next();
					if (key.isAcceptable()) {
						System.out.println("来了");
//						try {
//							OutputStream outputStream = socket.getOutputStream();
//							InputStream inputStream = accept.getInputStream();
//							PrintWriter pw = new PrintWriter(outputStream, true);
//							Scanner sc = new Scanner(inputStream);
//							pw.println("Welcome, old brother!Please input your name:");
//							String clientName = null;
//							while (sc.hasNext()) {
//								String next = sc.nextLine();
//								if (next != null && !"".equals(next.trim())) {
//									clientName = next.replaceAll("\n\r", "").replaceAll("\r", "");
//									System.out.println("--------Welcome " + clientName + "!--------");
//									break;
//								} else {
//									pw.print("Welcome, old brother!Please input your name:");
//								}
//							}
//							pw.println("Welcome, " + clientName + "!If need, input 'giao' to Exit!");
//							while (sc.hasNextLine()) {
//								String nextLine = sc.nextLine();
//								System.out.println(Calendar.getInstance().getTime() + " " + clientName + ":");
//								System.out.println(nextLine);
//								if (null != nextLine) {
//									if ("giao".equals(nextLine.trim())) {
//										System.out.println("--------" + clientName + " exit!--------");
//										break;
//									}
//								}
//							}
//							accept.close();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
					} else if (key.isReadable()) {
						// 通道可读
						ByteBuffer buffer = ByteBuffer.allocate(2);
						SocketAddress receive = channel.receive(buffer);
						System.out.println(new String(buffer.array(),"UTF-8"));
						buffer.clear();
					} else if (key.isWritable()) {
						System.out.println("来了");
						// 通道可写
					}
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);
//		for (int i = 0; i < 10; i++) {
//			newFixedThreadPool.submit(new Runnable() {
//				@Override
//				public void run() {
//					ByteBuffer buffer = ByteBuffer.allocate(1024);
//					
//				}
//			});
//		}
	
		
	}

	private static void socket() throws IOException {
		ServerSocket ss = new ServerSocket(8189);
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);
		for (int i = 0; i < 10; i++) {
			newFixedThreadPool.submit(new Runnable() {
				@Override
				public void run() {
					while (true) {
						Socket accept;
						try {
							accept = ss.accept();
							OutputStream outputStream = accept.getOutputStream();
							InputStream inputStream = accept.getInputStream();
							PrintWriter pw = new PrintWriter(outputStream, true);
							Scanner sc = new Scanner(inputStream);
							pw.println("Welcome, old brother!Please input your name:");
							String clientName = null;
							while (sc.hasNext()) {
								String next = sc.nextLine();
								if (next != null && !"".equals(next.trim())) {
									clientName = next.replaceAll("\n\r", "").replaceAll("\r", "");
									System.out.println("--------Welcome " + clientName + "!--------");
									break;
								} else {
									pw.print("Welcome, old brother!Please input your name:");
								}
							}
							pw.println("Welcome, " + clientName + "!If need, input 'giao' to Exit!");
							while (sc.hasNextLine()) {
								String nextLine = sc.nextLine();
								System.out.println(Calendar.getInstance().getTime() + " " + clientName + ":");
								System.out.println(nextLine);
								if (null != nextLine) {
									if ("giao".equals(nextLine.trim())) {
										System.out.println("--------" + clientName + " exit!--------");
										break;
									}
								}
							}
							accept.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}
			});
		}
	}
	public void client() throws IOException {
		DatagramChannel da = DatagramChannel.open();
		Thread t = new Thread();
	}
}
