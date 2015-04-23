package com.github.rapid.common.util;

import java.io.IOException;
import java.util.List;

import org.xerial.snappy.Snappy;

public final class ByteCodec {

	static ByteCodec singleton = new ByteCodec();

	public static ByteCodec getInstance() {
		return singleton;
	}

	public byte[] encode(List<byte[]> datas) throws IOException {
		int bytes = 0;
		for (byte[] data : datas) {
			bytes += data.length;
		}
		byte[] tmp = new byte[bytes];
		bytes = 0;
		for (byte[] data : datas) {
			System.arraycopy(data, 0, tmp, bytes, data.length);
			bytes += data.length;
		}
		return Snappy.compress(tmp);
	}

	public byte[] decode(byte[] value) throws IOException {
		return Snappy.uncompress(value);
	}

}