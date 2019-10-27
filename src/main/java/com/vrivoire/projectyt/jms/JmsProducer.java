package com.vrivoire.projectyt.jms;

public interface JmsProducer {

    void sendMessage(String queueName, String message) throws Exception;

}
