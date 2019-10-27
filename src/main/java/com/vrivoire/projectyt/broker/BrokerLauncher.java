package com.vrivoire.projectyt.broker;

import com.vrivoire.projectyt.Config;

import org.apache.activemq.broker.BrokerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BrokerLauncher {

    private static final Logger LOG = LogManager.getLogger(BrokerLauncher.class);

    public static void main(String[] args) {
        try {
            LOG.info("JMS broker starting...");
            BrokerService broker = new BrokerService();
            broker.addConnector(Config.BROKER_TCP_ADDRESS.getString());
            broker.start();
        } catch (Exception ex) {
            LOG.fatal(ex.getMessage(), ex);
            System.exit(-1);
        }
    }

}
