package com.vrivoire.projectyt.consumer.jms;

import com.vrivoire.projectyt.Config;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JmsConsumer {

    private static final Logger LOG = LogManager.getLogger(JmsConsumer.class);
    private static final ConnectionFactory CONNECTION_FACTORY = new ActiveMQConnectionFactory(Config.BROKER_TCP_ADDRESS.getString());

    public JmsConsumer() {
    }

    public String readMessage(String queueName) throws Exception {
        Connection connection = null;

        try {
            connection = CONNECTION_FACTORY.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Queue queue = session.createQueue(queueName);
            MessageConsumer consumer = session.createConsumer(queue);
            connection.start();
            // blocking
            LOG.info("Wailting for data from the queue.");
            TextMessage textMsg = (TextMessage) consumer.receive();
            LOG.debug("Received: \n" + textMsg.getText());

            session.close();
            return textMsg.getText();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
