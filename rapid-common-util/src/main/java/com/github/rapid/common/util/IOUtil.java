package com.github.rapid.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class IOUtil {

	public static List<String> readLines(BufferedReader reader ,int size) throws IOException {
		List<String> lines = new ArrayList<String>();
		String line = null;
		int count = 1;
		while((line = reader.readLine()) != null) {
			count++;
			lines.add(line);
			if(count > size) {
				break;
			}
		}
		return lines;
	}
	
	public static void writeWithLength(DataOutputStream dos,byte[] buf) throws IOException {
		dos.writeInt(buf.length);
		dos.write(buf);
	}
	
	public static byte[] readByLength(DataInputStream dis) throws IOException {
		int length = dis.readInt();
		if(length > 0) {
			byte[] buf = new byte[length];
			dis.read(buf);
			return buf;
		}
		return null;
	}
	
}
