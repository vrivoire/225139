package com.vrivoire.projectyt;

import com.vrivoire.projectyt.jms.JmsConsumer;
import com.vrivoire.projectyt.jms.JmsProducer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Consumer {

    private static final Logger LOG = LogManager.getLogger(Consumer.class);

    public static void main(String[] args) {
        Consumer consumer = new Consumer();
    }

    public Consumer() {

        JmsConsumer jmsConsumer = new JmsConsumer();
        JmsProducer jmsProducer = new JmsProducer();
        try {
            int count = 0;
            while (true) {
                String xml = jmsConsumer.readMessage(Config.YOU_TUBE_QUEUE_A.getString());
                count++;
                LOG.info("Number of SearchResult recieved: " + count + " for " + Config.YOU_TUBE_QUEUE_A.getString());

                String newXml = change(xml, "(?i)telecom", "telco");
                if (newXml != null && newXml.length() != 0) {
                    jmsProducer.sendMessage(Config.YOU_TUBE_QUEUE_B.getString(), newXml);
                    LOG.info("Message sent to " + Config.YOU_TUBE_QUEUE_B.getString());
                }
            }
        } catch (Exception e) {
            LOG.fatal(e.getMessage(), e);
            System.exit(-1);
        }
    }

    // je n<ai pas utilis/ Jackson car il n<arrive pas a deserialiser un xml en SearchResult
    private String change(String xmmlString, String before, String after) {

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new ByteArrayInputStream(xmmlString.getBytes()));

            NodeList elementsByTagName = doc.getElementsByTagName("snippet");
            Node item = elementsByTagName.item(0);
            NodeList childNodes = item.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node child = childNodes.item(i);
                if (child.getNodeName().equals("title")) {
                    String textContent = child.getTextContent();
                    String replaced = textContent.replaceAll(before, after);
                    child.setTextContent(replaced);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(byteArrayOutputStream);
            transformer.transform(source, result);

            String modifiedXml = byteArrayOutputStream.toString();
            LOG.trace("Modified XML:" + modifiedXml);
            return modifiedXml;
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException ex) {
            LOG.error("Error while changing the title: " + ex.getMessage(), ex);
            LOG.error("Input de l'érreur: \n" + xmmlString);
        }
        return xmmlString;
    }
}
