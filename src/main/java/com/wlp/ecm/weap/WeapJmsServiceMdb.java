package com.wlp.ecm.weap;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.BytesMessage;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ibm.mq.jms.MQQueue;
import com.wlp.ecm.weap.common.LoggingUtil;
import com.wlp.ecm.weap.common.WriteLog;
import com.wlp.ecm.weap.config.AppConfiguration;
import com.wlp.ecm.weap.config.WeapConfigSettings;
import com.wlp.ecm.weap.exception.WeapException;

/**
 * Message-Driven Bean implementation class for: WeapJmsServiveMdb
 *
 */
@MessageDriven(
		name="WeapMessageDrivenBean",
		activationConfig = {
			@ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/WeapRequestQueue"),
			@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
		}, 
		description="Wellpoint Enrollment Application Processor (WEAP) message driven bean"
	)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class WeapJmsServiceMdb implements MessageListener 
{

	@Resource
	MessageDrivenContext mdc;

	@Resource(name = "jms/WeapResponseQueue", type=Queue.class )
	private Queue responseQueue;

	@Resource(name = "jms/WeapResponseQCF1", type=QueueConnectionFactory.class)
	private QueueConnectionFactory responseQcf1;

	@Resource(name = "jms/WeapResponseQCF2", type=QueueConnectionFactory.class)
	private QueueConnectionFactory responseQcf2;
	
	/**
	 * Default constructor. 
	 */
	public WeapJmsServiceMdb() 
	{
		
	}
	
	/**
	 * @see MessageListener#onMessage(Message)
	 */
	public void onMessage(Message msg) 
	{
		LoggingUtil.LogTraceStartMsg();
		
		WriteLog.WriteToLog("New Message received...");
		
		try 
		{
			if (msg instanceof TextMessage) 
			{
				throw new WeapException("E1018");
			} 
			else if (msg instanceof BytesMessage) 
			{
				AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
				ctx.register(AppConfiguration.class);
				ctx.refresh();
				WeapMessageProcessor messageProcessor = ctx.getBean(WeapMessageProcessor.class);
				WeapConfigSettings settings = ctx.getBean(WeapConfigSettings.class);

				QueueConnectionFactory responseQcf = null;
				MQQueue replyDestination = (MQQueue)msg.getJMSReplyTo();
				
				if (replyDestination != null) 
				{
					//String correlationID = msg.getJMSCorrelationID();
					String queueManager = replyDestination.getBaseQueueManagerName();
	
					String qmNumber = settings.getPropertyString("queue.manager.number." + queueManager);
					
					if (qmNumber != null) 
					{
						if (qmNumber.trim().equals("1")) 
						{
							responseQcf = responseQcf1;
						}
						else if (qmNumber.trim().equals("2")) 
						{
							responseQcf = responseQcf2;
						}
						else 
						{
							throw new WeapException("1025", queueManager);
						}
					} 
					else 
					{
						throw new WeapException("1026", queueManager);
					}
				}
				
				messageProcessor.processMessage((BytesMessage)msg, responseQueue, responseQcf, true);
				
			} 
			else if (msg instanceof StreamMessage) 
			{
				throw new WeapException("E1020");
			} 
			else if (msg instanceof ObjectMessage) 
			{
				throw new WeapException("E1021");
			} 
			else if (msg instanceof MapMessage) 
			{
				throw new WeapException("E1022");
			}
			else 
			{
				throw new WeapException("E1023", msg.getClass().getSimpleName());
			}
		} 
		catch(javax.jms.JMSException e) 
		{
			LoggingUtil.LogErrorMsg(e);
		}
		catch (WeapException e) 
		{
			LoggingUtil.LogErrorMsg(e);
		} 
		catch (Exception e) 
		{
			LoggingUtil.LogErrorMsg(e);
		}
		
		LoggingUtil.LogTraceEndMsg();
	}
}
