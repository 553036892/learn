package com.learn;

import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

import org.elasticsearch.common.io.PathUtils;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.node.Node;

public class Sccc {
public static void main(String[] args) throws Exception {
		
		Settings settings = Settings.EMPTY;
		URI path = new URI("http://172.21.10.3:9200");
		Environment sss = new Environment(settings,PathUtils.get(path));
		Node node = new Node(sss);
		node.start();
	}
}
