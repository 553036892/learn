package com.learn.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.json.JSONObject;

public class FilesReducer extends Reducer<Text, MyText, NullWritable, Text> {

	@Override
	protected void reduce(Text key, Iterable<MyText> values, Reducer<Text, MyText, NullWritable, Text>.Context context)
			throws IOException, InterruptedException {
		System.out.println("reducer");
		System.out.println("key:"+key);
		String content = "";
		String lineSeparator = System.lineSeparator();
		for (MyText myText : values) {
			content = lineSeparator + myText.getValue() + content;
		}
		content = content.substring(lineSeparator.length() + 1,content.length());
		JSONObject json = new JSONObject();
		String path = key.toString();
		String type = path.substring(path.lastIndexOf(".") + 1, path.length());
		json.put("type", type);
		System.out.println("content:"+content);
		String parse = ParseText.parse(content.getBytes("UTF-8"), type);
		System.out.println("parse:"+parse);
		json.put("content", parse);
		json.put("path", path);
		System.out.println("json:" + json.toString());
//		context.write(NullWritable.get(), new Text(json.toString()));
	}
}
