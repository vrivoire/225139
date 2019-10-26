package com.vrivoire.projectyt.broker;

import java.net.URISyntaxException;

import org.apache.activemq.broker.BrokerService;

public class BrokerLauncher {

    public static void main(String[] args) throws URISyntaxException, Exception {
        BrokerService broker = new BrokerService();
        broker.addConnector("tcp://127.0.0.1:11616");
        broker.start();
    }

}
