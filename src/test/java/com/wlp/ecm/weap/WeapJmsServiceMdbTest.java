package com.wlp.ecm.weap;

import javax.jms.JMSException;

import org.junit.Test;

import com.wlp.ecm.weap.mock.MockBytesMessage;

public class WeapJmsServiceMdbTest {
	
	@Test
	public void testOnMessage() {
		//MockBytesMessage bm = new MockBytesMessage("c:/work/temp2/D34D134B.msg");
		//MockBytesMessage bm = new MockBytesMessage("c:/work/temp2/F2821D6Y.msg");
		//MockBytesMessage bm = new MockBytesMessage("c:/work/temp2/F5569W7W.msg");
		//MockBytesMessage bm = new MockBytesMessage("c:/work/temp2/N8699K0F.msg");
		MockBytesMessage bm = new MockBytesMessage("c:/work/temp2/B70F414O.msgdat");		
		try {
			bm.reset();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		WeapJmsServiceMdb mdb = new WeapJmsServiceMdb();
		mdb.onMessage(bm);
	}
	
}
