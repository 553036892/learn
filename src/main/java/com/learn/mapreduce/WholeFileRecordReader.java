package com.learn.mapreduce;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.poi.util.IOUtils;

public class WholeFileRecordReader extends RecordReader<Text, BytesWritable> {

	private Text currentKey = new Text();

	private BytesWritable currentValue = new BytesWritable();

	private int finish = 0;

	public WholeFileRecordReader() {

	}

	@Override
	public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
		FileSplit file = (FileSplit) split;
		Path path = file.getPath();
		FileSystem fileSystem = path.getFileSystem(context.getConfiguration());
		FSDataInputStream open = fileSystem.open(path);
		String pathStr = path.toString();
		String type = pathStr.substring(pathStr.lastIndexOf(".") + 1, pathStr.length());
//		File createTempFile = File.createTempFile(UUID.randomUUID().toString(), type);
//		FileOutputStream fileOutputStream = new FileOutputStream(createTempFile);
//		fileOutputStream.write(IOUtils.toByteArray(open));
//		fileOutputStream.flush();
//		fileOutputStream.close();
		byte[] byteArray = IOUtils.toByteArray(open);
		System.out.println("open:\n"+new String(byteArray));
		currentKey.set(pathStr);
		String parse = ParseText.parse(new ZipArchiveInputStream(new ByteArrayInputStream(byteArray)) , type);
		System.out.println(
				"wr:\n" + parse);
		byte[] bytes = parse.getBytes();
		currentValue.set(bytes, 0, bytes.length);
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		return finish == 0;
	}

	@Override
	public Text getCurrentKey() throws IOException, InterruptedException {
		return currentKey;
	}

	@Override
	public BytesWritable getCurrentValue() throws IOException, InterruptedException {
		finish = 1;
		return currentValue;
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		return finish;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

}
