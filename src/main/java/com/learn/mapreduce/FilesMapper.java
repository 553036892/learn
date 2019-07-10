package com.learn.mapreduce;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class FilesMapper extends Mapper<LongWritable, Text, Text, MyText> {

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, MyText>.Context context)
			throws IOException, InterruptedException {
		Path path = ((FileSplit) context.getInputSplit()).getPath();
		String pathstr = path.toString();
		if (pathstr.indexOf(".") > 0
				&& ParseText.hastType(pathstr.substring(pathstr.lastIndexOf(".") + 1, pathstr.length()))) {
			MyText myValue = new MyText(key.get(), pathstr, value.toString());
			System.out.println("mapper");
			System.out.println("key:" + myValue.getKey());
			System.out.println("value:" + value);
			context.write(new Text(pathstr), myValue);
		}
	}

}
