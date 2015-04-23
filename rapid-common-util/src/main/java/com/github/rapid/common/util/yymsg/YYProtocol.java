package com.github.rapid.common.util.yymsg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.Assert;

 
public class YYProtocol {

	/**
	struct Header
	{
	    uint32_t length;  // 填PSS_MultiRouteGChatMsg结构二进制后的长度
	    uint32_t uri;    // 这里为 ((108 << 8) | 57)
	    uint16_t resCode;  // 总是填 200
	};
		 *
		 */
	public static class Header {
		public static int HEADER_LENGTH = 10;
		private int length; // 填PSS_MultiRouteGChatMsg结构二进制后的长度
		private int uri; // 这里为 ((108 << 8) | 57)
		private short resCode = (short) 200; // 总是填 200

		public Header() {
		}
		
		public Header(int uri,int length) {
			super();
			this.length = length;
			this.uri = uri;
		}
		
		public void setLength(int length) {
			this.length = length;
		}

		public void setUri(int uri) {
			this.uri = uri;
		}

		public void marshal(LEDataOutputStream output) throws IOException {
			Assert.isTrue(uri>0,"Header.uri>0");
			Assert.isTrue(length>0,"Header.length>0");
			
			output.writeInt(length + HEADER_LENGTH);
			output.writeInt(uri);
			output.writeShort(resCode);
			output.flush();
		}
		
		public byte[] toBytes() {
			try {
				ByteArrayOutputStream headerBytes = new ByteArrayOutputStream();
				marshal(new LEDataOutputStream(headerBytes));
				Assert.isTrue(headerBytes.toByteArray().length == 10,"expected header length is 10,actual:"+headerBytes.toByteArray().length );
				return headerBytes.toByteArray();
			}catch(IOException e) {
				throw new RuntimeException("toBytes error",e);
			}
		}
	}

		/**
		struct PSS_MultiRouteGChatMsg : public sox::Marshallable
		        {
		            enum {uri = (108 << 8) | 57};

		            uint32_t     m_uFlags;   // 一般填0
		            uint32_t     m_groupId;  // 填报警到送达的群内部编号
		            uint32_t     m_folderId; // 填报警要送达的组内部编号、一般与群内部编号相同
		            uint32_t     m_seqId;    // 消息的流水号，短时间内不要重复
		            uint32_t     m_imlinkdId; // 服务器填充，不需要填写
		            uint32_t     m_senderUid; // 服务器填充，不需要填写
		            uint64_t     m_timestamp; // 服务器填充，不需要填写
		            GTopicTextChat  m_msgText; 
		            EFrom m_from;  // 不用填写

		            bool m_bConvertFromOldMsg; // compatible with v3.8 msg,为false

		            virtual void marshal(sox::Pack &p) const
		            {
		                p << m_uFlags;
		                p << m_groupId << m_folderId << m_seqId << m_imlinkdId << m_senderUid << m_timestamp;
		                m_msgText.marshal(p);
		                p.push_uint8(m_from);
		                p.push_uint8(m_bConvertFromOldMsg);
		            }
		        };

		 */
		public static class MultiRouteGChatMsg implements Cloneable {
			static int uri = (108 << 8) | 57;
			
			private int flags = 0; // 一般填0
			private int groupId; // 填报警到送达的群内部编号
			private int folderId; // 填报警要送达的组内部编号、一般与群内部编号相同
			private int seqId = (int)(System.currentTimeMillis() % Integer.MAX_VALUE); // 消息的流水号，短时间内不要重复
			private int imlinkdId; // 服务器填充，不需要填写
			private int senderUid; // 服务器填充，不需要填写
			private long timestamp; // 服务器填充，不需要填写
			private GTopicTextChat msgText;
			
			private byte from = 1; // 不用填写
			private byte bConvertFromOldMsg = 0; // compatible with v3.8 msg
			
			public MultiRouteGChatMsg() {
			}
			
			public MultiRouteGChatMsg(int groupId, GTopicTextChat msgText) {
				super();
				setGroupId(groupId);
				setMsgText(msgText);
			}

			public void setMsgText(GTopicTextChat m_msgText) {
				this.msgText = m_msgText;
			}

			public void setGroupId(int groupId) {
				this.groupId = groupId;
				if(folderId == 0) {
					folderId = groupId;
				}
			}

			public void setFolderId(int folderId) {
				this.folderId = folderId;
			}

			public int getGroupId() {
				return groupId;
			}

			public int getFolderId() {
				return folderId;
			}

			public GTopicTextChat getMsgText() {
				return msgText;
			}

			public void marshal(LEDataOutputStream output) throws IOException {
				Assert.notNull(msgText,"yy msgText not null");
				Assert.isTrue(groupId>0,"yy groupId>0");
				Assert.isTrue(folderId>0,"yy folderId>0");
				Assert.isTrue(seqId>0,"yy seqId>0");
				/*
				 * p << m_uFlags; 
				 * p << m_groupId << m_folderId << m_seqId << m_imlinkdId << m_senderUid << m_timestamp; 
				 * m_msgText.marshal(p);
				 * p.push_uint8(m_from); 
				 * p.push_uint8(m_bConvertFromOldMsg);
				 */
				output.writeInt(flags);
				output.writeInt(groupId);
				output.writeInt(folderId);
				output.writeInt(seqId);
				output.writeInt(imlinkdId);
				output.writeInt(senderUid);
				output.writeLong(timestamp);
				
				msgText.marshal(output);
				
				output.writeByte(from);
				output.writeByte(bConvertFromOldMsg);
				
				output.flush();
			}
			
			public byte[] toBytes() {
				try {
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					marshal(new LEDataOutputStream(output));
					return output.toByteArray();
				}catch(IOException e) {
					throw new RuntimeException("toBytes() IOException",e);
				}
			}
			
			public MultiRouteGChatMsg clone()  {
				try {
					return (MultiRouteGChatMsg)super.clone();
				} catch (CloneNotSupportedException e) {
					throw new RuntimeException("CloneNotSupportedException",e);
				}
			}
		}

		
		/**
		 * 
		       struct  GTopicTextChat : public sox::Marshallable  
		{
		            std::string msgtext;    // 报警的内容
		            std::string nickname;   // 报警人的昵称
		            std::string fontname;   // 字体名，如”宋体”
		            
		            uint32_t effect;   // 一般填0
		            uint32_t charset;  // 一般填134
		            uint32_t color;    // 一般填0
		            uint32_t height;   // 一般填 -12
		
		            virtual void marshal(sox::Pack &pak) const
		            {
		                pak << msgtext << nickname << fontname << effect << charset << color << height;
		            }
		        };
		
		 *
		 */
		public static class GTopicTextChat implements Cloneable{
			private String msgtext; // 报警的内容
			private String nickname; // 报警人的昵称
			private String fontname = "宋体"; // 字体名，如”宋体”

			private int effect = 0; // 一般填0
			private int charset = 134; // 一般填134
			private int color = 0x000000FF; // 一般填0
			private int height = -16; // 一般填 -12

			public GTopicTextChat() {
			}
			
			public GTopicTextChat(String nickname,String msgtext) {
				super();
				this.msgtext = msgtext;
				this.nickname = nickname;
			}
			
			public String getMsgtext() {
				return msgtext;
			}

			public void setMsgtext(String msgtext) {
				this.msgtext = msgtext;
			}

			public String getNickname() {
				return nickname;
			}

			public void setNickname(String nickname) {
				this.nickname = nickname;
			}

			public String getFontname() {
				return fontname;
			}

			public void setFontname(String fontname) {
				this.fontname = fontname;
			}

			public int getEffect() {
				return effect;
			}

			public void setEffect(int effect) {
				this.effect = effect;
			}

			public int getCharset() {
				return charset;
			}

			public void setCharset(int charset) {
				this.charset = charset;
			}

			public int getColor() {
				return color;
			}

			public void setColor(int color) {
				this.color = color;
			}

			public int getHeight() {
				return height;
			}

			public void setHeight(int height) {
				this.height = height;
			}

			public void marshal(LEDataOutputStream output) throws IOException {
				Assert.hasText(msgtext,"msgtext hasText");
				Assert.hasText(nickname,"nickname hasText");
				Assert.hasText(fontname,"fontname hasText");
				
				writeString(output,msgtext);
				writeString(output,nickname);
				writeString(output,fontname);
				output.writeInt(effect);
				output.writeInt(charset);
				output.writeInt(color);
				output.writeInt(height);
				output.flush();
			}
			
			public GTopicTextChat clone()  {
				try {
					return (GTopicTextChat)super.clone();
				} catch (CloneNotSupportedException e) {
					throw new RuntimeException("CloneNotSupportedException",e);
				}
			}

		}
		
		//字符串 先压入2字节长度，然后再压入字符串；
		private static void writeString(LEDataOutputStream output,String str) throws IOException {
			output.writeShort(str.getBytes().length);
			output.write(str.getBytes("UTF-8"));
		}	
		
		
}
