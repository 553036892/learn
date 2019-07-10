package com.learn.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class FileMapper extends Mapper<Object, Text, NullWritable, Text> {

	@Override
	protected void map(Object key, Text value,
			Mapper<Object, Text, NullWritable, Text>.Context context) throws IOException, InterruptedException {
		context.write(NullWritable.get(), value);
	}

}
