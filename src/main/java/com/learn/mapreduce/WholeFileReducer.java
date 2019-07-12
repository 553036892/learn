package com.learn.mapreduce;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.json.JSONObject;

public class WholeFileReducer extends Reducer<Text, BytesWritable, NullWritable, Text> {

	@Override
	protected void reduce(Text key, Iterable<BytesWritable> values,
			Reducer<Text, BytesWritable, NullWritable, Text>.Context context) throws IOException, InterruptedException {
		BytesWritable next = values.iterator().next();
		JSONObject json = new JSONObject();
		String path = key.toString();
		String type = path.substring(path.lastIndexOf(".") + 1, path.length());
		json.put("type", type);
		System.out.println("length:" + next.getLength());
		byte[] byteArray = next.copyBytes();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
		String parse = ParseText.parse(byteArrayInputStream, type);
		System.out.println("parse:" + parse);
		json.put("path", path);
		json.put("content", parse);
		System.out.println("path:" + path);
	}

}
