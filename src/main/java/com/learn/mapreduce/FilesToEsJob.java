package com.learn.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.elasticsearch.hadoop.mr.EsOutputFormat;

public class FilesToEsJob {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		conf.set("es.nodes", "172.21.11.7:9200");
		conf.set("es.resource", "eshadoop/word");
//		conf.set("es.input.json", "yes");
		conf.set("es.net.http.auth.user", "elastic");
		conf.set("es.net.http.auth.pass", "khGUMJ5lq2kgIHCOzSvb");
		//
		conf.setBoolean("mapred.map.tasks.speculative.execution", false);
		conf.setBoolean("mapred.reduce.tasks.speculative.execution", false);

		// Create Job instance
		Job job = Job.getInstance(conf, "files2es");
		// set Driver class
		job.setJarByClass(FilesToEsJob.class);
		job.setMapperClass(FilesMapper.class);
		job.setReducerClass(FilesReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(MyText.class);
		//ES-Hadoop库提供了单独的EslnputFormat和EsOutputFormat
		//它们扮演了Elasticsearch需要的JSON格式之间的适配器角色。
		//默认情况下, MapWritable对象被写入context对象。然后EsOutputFormat将MapWritable对象转换为JSON格式
		// set OutputFormat to EsOutputFormat provided by ElasticSearch-Hadoop jar
		job.setOutputFormatClass(EsOutputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}

}
