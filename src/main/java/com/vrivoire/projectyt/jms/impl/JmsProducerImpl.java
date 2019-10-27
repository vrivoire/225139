package com.vrivoire.projectyt.jms.impl;

import com.vrivoire.projectyt.Config;
import com.vrivoire.projectyt.jms.JmsProducer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JmsProducerImpl implements JmsProducer {

    private static final Logger LOG = LogManager.getLogger(JmsProducerImpl.class);
    private static final ConnectionFactory CONNECTION_FACTORY = new ActiveMQConnectionFactory(Config.BROKER_TCP_ADDRESS.getString());

    public JmsProducerImpl() {
    }

    @Override
    public void sendMessage(String queueName, String message) throws Exception {
        Connection connection = null;
        Session session = null;
        try {
            connection = CONNECTION_FACTORY.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(queue);

            TextMessage msg = session.createTextMessage(message);
            producer.send(msg);

            LOG.debug("Sent: \n" + msg.getText());
        } catch (JMSException ex) {
            LOG.error(ex.getErrorCode() + " " + ex.getMessage(), ex);
        } finally {
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

}
