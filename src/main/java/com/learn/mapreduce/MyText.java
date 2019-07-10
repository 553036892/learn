package com.learn.mapreduce;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class MyText implements WritableComparable<MyText> {

	public MyText() {
	}

	public MyText(long key, String path, String value) {
		this.key = key;
		this.path = path;
		this.value = value;
	}

	private long key;

	private String value;

	private String path;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public int compareTo(MyText o) {
		long l = this.key - o.key;
		return (int) l == 0 ? 0 : (l > 0 ? 1 : -1);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeLong(key);
		out.writeUTF(path);
		out.writeUTF(value);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.key = in.readLong();
		this.path = in.readUTF();
		this.value = in.readUTF();
	}

}
