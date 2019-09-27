package com.learn;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SelectorPractice {
	public static void main(String[] args) throws IOException {
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.socket().bind(new InetSocketAddress("localhost", 8080));
		ssc.configureBlocking(false);
		Selector selector = Selector.open();
		ssc.register(selector, SelectionKey.OP_ACCEPT);

		while (true) {
			int readyNum = selector.select();
			if (readyNum == 0) {
				continue;
			}

			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> it = selectedKeys.iterator();

			while (it.hasNext()) {
				SelectionKey key = it.next();

				if (key.isAcceptable()) {
					// 接受连接
				} else if (key.isReadable()) {
					// 通道可读
				} else if (key.isWritable()) {
					// 通道可写
				}

				it.remove();
			}
		}
	}

}
