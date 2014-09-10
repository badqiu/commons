package com.duowan.common.rpc.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.springframework.oxm.castor.CastorMappingException;
import org.springframework.oxm.castor.CastorMarshaller;

import com.duowan.common.rpc.fortest.api.BlogInfoService;
import com.duowan.common.rpc.fortest.api.BlogInfoServiceImpl;

public class XmlSerDeTest {
	
	@Test
	public void test() throws CastorMappingException, IOException {
		BlogInfoService service = new BlogInfoServiceImpl();
//		marshalAndUnmarshal(service.findSingleBlog("", ""));
//		marshalAndUnmarshal(service.findBlogCollection("key"));
	}

	private void marshalAndUnmarshal(Object result) throws IOException {
		System.out.println("\n\n----------------------------------------------");
		System.out.println("marshalAndUnmarshal():"+result);
		System.out.println("----------------------------------------------");
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		CastorMarshaller marshaller = new org.springframework.oxm.castor.CastorMarshaller();
		marshaller.afterPropertiesSet();
		
//		marshaller.marshal(result, new StreamResult(new TeeOutputStream(System.out,output)));
		marshaller.unmarshal(new StreamSource(new ByteArrayInputStream(output.toByteArray())));
		
	}
}
