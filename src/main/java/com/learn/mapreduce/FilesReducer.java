package com.learn.mapreduce;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.json.JSONObject;

public class FilesReducer extends Reducer<Text, MyText, NullWritable, Text> {

	@Override
	protected void reduce(Text key, Iterable<MyText> values, Reducer<Text, MyText, NullWritable, Text>.Context context)
			throws IOException, InterruptedException {
		System.out.println("reducer");
		System.out.println("key:" + key);
		try(ByteArrayOutputStream stm = new ByteArrayOutputStream()){
			ArrayList<byte[]> list = new ArrayList<>();
			for (MyText myText : values) {
				list.add(myText.getValue());
			}
			Collections.reverse(list);
			byte[] last = list.remove(list.size() - 1);
			for (byte[] value : list) {
				stm.write(value);
				stm.write(System.lineSeparator().getBytes());
			}
			stm.write(last);
			JSONObject json = new JSONObject();
			String path = key.toString();
			String type = path.substring(path.lastIndexOf(".") + 1, path.length());
			json.put("type", type);
			byte[] byteArray = stm.toByteArray();
			stm.close();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
			String parse = ParseText.parse(byteArrayInputStream, type);
			System.out.println("parse:" + parse);
			json.put("path", path);
			json.put("content", parse);
			System.out.println("path:"+path);
			//		context.write(NullWritable.get(), new Text(json.toString()));
		}
	}
}
