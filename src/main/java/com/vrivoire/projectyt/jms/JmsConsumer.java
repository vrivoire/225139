package com.vrivoire.projectyt.jms;

public interface JmsConsumer {

    String readMessage(String queueName) throws Exception;

}
