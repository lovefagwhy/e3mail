package com.e3.active.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

public class ActiveTest {
	@Test
	public void producreTest() throws Exception{
		// 1） 创建消息工厂，ActiveMQConnectionFactory，需要传递参数：协议，地址，端口。
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
		"tcp://192.168.199.128:61616");
		// 2） 从工厂中获取连接
		Connection connection = connectionFactory.createConnection();
		// 3） 开启连接
		connection.start();
		// 4） 从连接中获取Session
		//第一个参数：消息事务，如果第一个参数是true，第二个参数将会被忽略
		//第二个参数：消息应答模式，自动应答模式
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5） 从Session中获取消息目的地
		//创建一个消息目的地，给消息目的起一个名称：myqueue
		//相当域在ActiveMQ服务中开辟了名称为myqueue一块空间，消息发送myqueue这块空间中。
		//消息有两种模式：点对点（queue），发布定阅模式：（Topic）
		Queue queue = session.createQueue("myqueue");
		// 6） 创建消息发生者
		MessageProducer producer = session.createProducer(queue);
		// 7） 发送消息
		TextMessage message = new ActiveMQTextMessage();
		message.setText("我们1114都是中国人");
		producer.send(message);
		// 8） 关闭资源
		producer.close();
		session.close();
		connection.close();
		}

	@Test
	public void consumerTest() throws Exception{
		// 1） 创建消息工厂，ActiveMQConnectionFactory，需要传递参数：协议，地址，端口。
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"tcp://192.168.199.128:61616");
		// 2） 从工厂中获取连接
		Connection connection = connectionFactory.createConnection();
		// 3） 开启连接
		connection.start();
		// 4） 从连接中获取Session
		//第一个参数：消息事务，如果第一个参数是true，第二个参数将会被忽略
		//第二个参数：消息应答模式，自动应答模式
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5） 从Session中获取消息目的地
		//创建一个消息目的地，给消息目的起一个名称：myqueue
		//相当域在ActiveMQ服务中开辟了名称为myqueue一块空间，消息发送myqueue这块空间中。
		//消息有两种模式：点对点（queue），发布定阅模式：（Topic）
		Queue queue = session.createQueue("myqueue");
		// 6） 创建消息接收者
		MessageConsumer consumer = session.createConsumer(queue);
		// 7） 接收消息
		//同步接收  一直接收
//		while(true){
//			Message message = consumer.receive(20000);
//			TextMessage textMessage = (TextMessage) message;
//			String text = textMessage.getText();
//			System.out.println(text);
//		}
		//异步监听接收
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				if(message instanceof TextMessage){ 
					TextMessage tm = (TextMessage) message; 
					try { 
						//打印消息
						System.out.println(tm.getText()); 
						} catch (JMSException e) { 
						// TODO Auto-generated catch block 
							e.printStackTrace();
						} 
					}
			}
		});
	}
}
