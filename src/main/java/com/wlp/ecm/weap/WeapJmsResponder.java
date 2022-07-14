package com.wlp.ecm.weap;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;

import com.ibm.mq.jms.MQDestination;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.wlp.ecm.weap.common.LoggingUtil;
import com.wlp.ecm.weap.data.WeapMetadata;
import com.wlp.ecm.weap.jaxb.ResponseMessageXml;

public class WeapJmsResponder 
{
	private Queue responseQueue;
	private QueueConnectionFactory qcf;
	
	public Queue getResponseQueue() 
	{
		return responseQueue;
	}

	public void setResponseQueue(Queue respQueue) {
		this.responseQueue = respQueue;
	}

	public QueueConnectionFactory getQueueConnectionFactory() {
		return qcf;
	}

	public void setQueueConnectionFactory(QueueConnectionFactory qcf) {
		this.qcf = qcf;
	}

	public String SendSuccessResponse(WeapMetadata metadata) throws JMSException, JAXBException {
		LoggingUtil.LogTraceStartMsg();

		String message = ResponseMessageXml.createSuccessResponse(metadata);
		String sentTo = SendResponse(message);

		LoggingUtil.LogTraceEndMsg();
		return sentTo;
	}
	
	public String SendFailureResponse(WeapMetadata metadata, String error) throws JMSException, JAXBException {
		LoggingUtil.LogTraceStartMsg();

		String message = ResponseMessageXml.createFailedResponse(metadata, error);
		String sentTo = SendResponse(message);

		LoggingUtil.LogTraceEndMsg();
		return sentTo;
	}

	public String SendFailureResponse(WeapMetadata metadata, String[] errors) throws JMSException, JAXBException {
		LoggingUtil.LogTraceStartMsg();

		String message = ResponseMessageXml.createFailedResponse(metadata, errors);
		String sentTo = SendResponse(message);

		LoggingUtil.LogTraceEndMsg();
		return sentTo;
	}

	/* only used for testing */
	public void WriteDebugResponse(WeapMetadata metadata, String directory) throws JMSException, JAXBException {
		LoggingUtil.LogTraceStartMsg();

		String message = ResponseMessageXml.createSuccessResponse(metadata);
		FileOutputStream fos;
		try {
			String filePath = directory + metadata.getDcn() + ".xml" ; 
			fos = new FileOutputStream(filePath);
			fos.write(message.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			LoggingUtil.LogErrorMsg(e);
			// we dont want this to stop
		} catch (IOException e) {
			LoggingUtil.LogErrorMsg(e);
		}

		LoggingUtil.LogTraceEndMsg();
	}

	protected String SendResponse(String message) throws JMSException {
		LoggingUtil.LogTraceStartMsg();

		QueueConnection queueConn = null;
		QueueSession session = null;
		MessageProducer producer = null;
        try {
	        queueConn = qcf.createQueueConnection();
			session = queueConn.createQueueSession(true, Session.AUTO_ACKNOWLEDGE);
			producer = session.createProducer(responseQueue);
			
			TextMessage responseMsg = session.createTextMessage();
			responseMsg.setText(message);
			producer.send(responseMsg);
        } finally {
        	if (producer != null) {
        		producer.close();
        	}
        	if (session != null) {
        		session.close();
        	}
        	if (queueConn != null) {
        		queueConn.close();
        	}
        }
        
        String sentTo = "";
        if (qcf instanceof MQQueueConnectionFactory) {
        	MQQueueConnectionFactory mqQcf = (MQQueueConnectionFactory)qcf;
        	MQDestination mqDestination = (MQDestination)producer.getDestination();
           	sentTo = ((Queue) mqDestination).getQueueName() + " on " + mqQcf.getQueueManager() + " (" + mqQcf.getHostName() + ")";
        }
        
		LoggingUtil.LogTraceEndMsg();
		return sentTo;
	}
}
