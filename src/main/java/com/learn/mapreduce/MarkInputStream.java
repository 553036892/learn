package com.learn.mapreduce;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.utils.InputStreamStatistics;
import org.elasticsearch.hadoop.util.IOUtils;

public class MarkInputStream extends InputStream implements InputStreamStatistics{

	byte[] bytes;

	public MarkInputStream(InputStream in) throws IOException {
		this.bytes = IOUtils.asBytes(in).bytes();
	}

	private int mark = 0;

	@Override
	public int read() throws IOException {
		if (mark < 0 || mark > bytes.length) {
			return -1;
		} else {
			return bytes[mark];
		}
	}

	@Override
	public synchronized void mark(int readlimit) {
		this.mark = readlimit;
	}

	@Override
	public synchronized void reset() throws IOException {
		this.mark = 0;
	}

	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public long getCompressedCount() {
		return 0;
	}

	@Override
	public long getUncompressedCount() {
		return bytes.length;
	}
	
}
