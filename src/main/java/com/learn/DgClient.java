package com.learn;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DgClient {
	public static void main(String[] args) throws IOException {
		client();
	}

	private static void client() throws IOException {

		DatagramChannel channel = DatagramChannel.open();
		channel.configureBlocking(false);
		Scanner sc = new Scanner(System.in);
		while(sc.hasNextLine()) {
			String nextLine = sc.nextLine();
			if(null != nextLine) {
				if("end".equals(nextLine)) {
					break;
				}
				channel.send(ByteBuffer.wrap(nextLine.getBytes("UTF-8")), new InetSocketAddress("localhost", 8080));
			}
		}
	
		
	

	}
}
