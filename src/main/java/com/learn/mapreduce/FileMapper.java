package com.learn.mapreduce;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class FileMapper extends Mapper<Text, File, Text, BytesWritable> {

	@Override
	protected void map(Text key, File value,
			Mapper<Text, File, Text, BytesWritable>.Context context) throws IOException, InterruptedException {
		System.out.println("s:" + key + ",\nvalue:" + value.toString());
	}

}
