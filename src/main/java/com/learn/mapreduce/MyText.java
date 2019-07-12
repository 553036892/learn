package com.learn.mapreduce;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class MyText implements WritableComparable<MyText> {

	public MyText() {
	}

	public MyText(long key, String path, byte[] value) {
		this.key = key;
		this.path = path;
		this.value = value;
		if (this.value != null)
			this.length = this.value.length;
	}

	private long key;

	private int length;

	private byte[] value;

	private String path;

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
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

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public static class Comparator extends WritableComparator {
		public Comparator() {
			super(MyText.class);
		}

		@Override
		public int compare(Object a, Object b) {
			MyText am = (MyText) a;
			MyText bm = (MyText) b;
			long l = am.key - bm.key;
			return l == 0 ? 0 : (l > 0 ? -1 : 1);
		}

	}

	static { // register this comparator
		WritableComparator.define(MyText.class, new Comparator());
	}

	@Override
	public int compareTo(MyText o) {
		long l = this.key - o.key;
		return (int) l == 0 ? 0 : (l > 0 ? -1 : 1);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeLong(key);
		out.writeUTF(path);
		out.writeInt(length);
		out.write(value);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.key = in.readLong();
		this.path = in.readUTF();
		this.length = in.readInt();
		this.value = new byte[this.length];
		in.readFully(this.value, 0, this.length);
	}

}
