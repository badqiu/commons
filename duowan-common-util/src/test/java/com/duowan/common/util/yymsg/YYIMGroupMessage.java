package com.duowan.common.util.yymsg;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YYIMGroupMessage {

	static Logger logger = LoggerFactory.getLogger(YYIMGroupMessage.class);

	static final Charset DEFAULT_CHARSET = Charset.forName("UTF8");
	static final int HEADER_BYTES = 10;
	static final int MSG_OTHERS_BYTES = 50;

	/* ++++++++++++ struct Header ++++++++++ */
	int /* uint32_t */length; // 填PSS_MultiRouteGChatMsg结构二进制后的长度
	int /* uint32_t */uri = ((108 << 8) | 57); // 这里为 ((108 << 8) | 57)
	short /* uint16_t */resCode = 200; // 总是填 200
	/* +++++++++++++++++++++++++++++++++++++ */

	/* struct PSS_MultiRouteGChatMsg */
	int /* uint32_t */m_uFlags = 0; // 一般填0
	int /* uint32_t */m_groupId; // 填报警到送达的群内部编号
	int /* uint32_t */m_folderId; // 填报警要送达的组内部编号、一般与群内部编号相同
	int /* uint32_t */m_seqId; // 消息的流水号，短时间内不要重复
	int /* uint32_t */m_imlinkdId; // 服务器填充，不需要填写
	int /* uint32_t */m_senderUid; // 服务器填充，不需要填写
	long /* uint64_t */m_timestamp; // 服务器填充，不需要填写

	/* ++++ GTopicTextChat m_msgText; ++++++ */
	String/* std::string */msgtext; // 报警的内容
	String/* std::string */nickname; // 报警人的昵称
	String/* std::string */fontname = "宋体"; // 字体名，如”宋体”

	int/* uint32_t */effect = 0; // 一般填0
	int/* uint32_t */charset = 134; // 一般填134
	int/* uint32_t */color = 0; // 一般填0
	int/* uint32_t */height = -12; // 一般填 -12
	/* +++++++++++++++++++++++++++++++++++++ */

	byte /* EFrom */m_from; // 不用填写
	byte/* bool */m_bConvertFromOldMsg = 0; // compatible with v3.8 msg

	/* +++++++++++++++++++++++++++++++++++++ */

	/*
	 * 整型分别按实际的字节数以litter_endding的方式放入二进制流； 字符串 先压入2字节长度，然后再压入字符串；
	 */
	ByteBuffer marshal() {
		ByteBuffer msgtextBuf = DEFAULT_CHARSET.encode(msgtext);
		ByteBuffer nicknameBuf = DEFAULT_CHARSET.encode(nickname);
		ByteBuffer fontnameBuf = DEFAULT_CHARSET.encode(fontname);

		length = HEADER_BYTES + 2 + msgtextBuf.limit() + 2 + nicknameBuf.limit() + 2 + fontnameBuf.limit() + MSG_OTHERS_BYTES;

		ByteBuffer result = ByteBuffer.allocate(length);
		/* 设置little endian */
		result.order(ByteOrder.LITTLE_ENDIAN);
		// result.order(ByteOrder.BIG_ENDIAN);
		/* marshal header */
		result.putInt(length);
		result.putInt(uri);
		result.putShort(resCode);
		/* marshal message */
		result.putInt(m_uFlags);
		result.putInt(m_groupId);
		result.putInt(m_folderId);
		result.putInt(m_seqId);
		result.putInt(m_imlinkdId);
		result.putInt(m_senderUid);
		result.putLong(m_timestamp);
		/* GTopicTextChat部分 */
		result.putShort((short) msgtextBuf.limit());
		result.put(msgtextBuf);
		result.putShort((short) nicknameBuf.limit());
		result.put(nicknameBuf);
		result.putShort((short) fontnameBuf.limit());
		result.put(fontnameBuf);
		result.putInt(effect);
		result.putInt(charset);
		result.putInt(color);
		result.putInt(height);
		/*******************/
		result.put(m_from);
		result.put(m_bConvertFromOldMsg);
		result.flip();
		/* return result */
		return result;
	}

	public static void send(String host, int port, int groupId, int folderId, String nickname, String... messages) throws IOException {

		logger.debug(String.format("发送消息内容:{host=%s, port=%s, groupId=%s, folderId=%s, nickname=%s, messages=%s}", host, port, groupId, folderId, nickname, Arrays.toString(messages)));

		if (messages == null || messages.length == 0) {
			return;
		}
		SocketChannel sc = null;
		try {
			sc = SocketChannel.open();
			sc.connect(new InetSocketAddress(host, port));

			YYIMGroupMessage pck = new YYIMGroupMessage();
			pck.m_groupId = groupId;// 小组学习
			pck.m_folderId = folderId;
			pck.nickname = nickname;
			for (int i = 0; i < messages.length; i++) {
				pck.m_seqId = i;
				pck.msgtext = messages[i];
				sc.write(pck.marshal());
			}
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
	}

	/**
	 * 
	 * 接口地址： 请按照协议，将报文发到以下任一地址即可；
	 * 
	 * 电信：
	 * 
	 * 121.14.37.158：54321
	 * 
	 * 113.107.111.18：54321
	 * 
	 * 网通：
	 * 
	 * 112.91.168.18：54321
	 * 
	 * 58.249.119.222：54321
	 * 
	 * @throws IOException
	 */
	public static void sendAndWait(int groupId, int folderId, String... messages) throws IOException {
		String host = "121.14.37.158";
		int port = 54321;
		String nickname = "系统预警";
		send(host, port, groupId, folderId, nickname, messages);
	}

	public static void send(final int groupId, final int folderId, final String... messages) throws IOException {
		new Thread() {
			@Override
			public void run() {
				try {
					sendAndWait(groupId, folderId, messages);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}.start();
	}

	public static void main(String[] args) throws IOException {
		int groupId = 4601261;
		send(groupId, groupId, "来自预警系统的测试消息!");
	}
}
