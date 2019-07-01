package com.learn.mapreduce;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.elasticsearch.hadoop.mr.EsOutputFormat;

public class FileToEsJob {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		// ElasticSearch Server nodes to point to
		conf.set("es.nodes", "172.21.11.7:9200");
		// ElasticSearch index and type name in {indexName}/{typeName} format
		conf.set("es.resource", "eshadoop/word");

		// Create Job instance
		Job job = Job.getInstance(conf, "file2es");
		// set Driver class
		job.setJarByClass(FileToEsJob.class);
		job.setMapperClass(FileMapper.class);
		//    job.setReducerClass(WordsReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(File.class);

		//ES-Hadoop库提供了单独的EslnputFormat和EsOutputFormat
		//它们扮演了Elasticsearch需要的JSON格式之间的适配器角色。
		//默认情况下, MapWritable对象被写入context对象。然后EsOutputFormat将MapWritable对象转换为JSON格式
		// set OutputFormat to EsOutputFormat provided by ElasticSearch-Hadoop jar
		job.setOutputFormatClass(EsOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
