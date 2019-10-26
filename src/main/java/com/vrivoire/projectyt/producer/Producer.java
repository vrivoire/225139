package com.vrivoire.projectyt.producer;

import com.vrivoire.projectyt.Config;
import com.vrivoire.projectyt.producer.jms.JmsProducer;
import com.vrivoire.projectyt.youtube.SearchYt;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.api.services.youtube.model.SearchResult;

public class Producer {

    private static final Logger LOG = LogManager.getLogger(Producer.class);
    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private static final ObjectMapper OBJECT_MAPPER = new XmlMapper();

    public static void main(String[] args) {
        try {
            Producer producer = new Producer();
        } catch (Exception t) {
            LOG.fatal(t);
            System.exit(-1);
        }
    }

    public Producer() throws Exception {
        List<SearchResult> searchResults = queryYt();

        JmsProducer jmsProducer = new JmsProducer();
        for (SearchResult cleanedResult : searchResults) {
            cleanedResult.getId().put("URL", BASE_YOUTUBE_URL + cleanedResult.getId().getVideoId());
            String xml = getXML(cleanedResult);
            jmsProducer.sendMessage(Config.YOU_TUBE_QUEUE_A.getString(), xml);
        }

        LOG.info("Number of SearchResult sent: " + searchResults.size());
    }

    private String getXML(SearchResult searchResult) throws JsonProcessingException {
        OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        String xml = OBJECT_MAPPER.writeValueAsString(searchResult);
        LOG.debug(xml);
        return xml;
    }

    private List<SearchResult> queryYt() throws Exception {
        SearchYt searchYt = new SearchYt();
        LOG.info(Config.SHOW_RESULTS.getBoolean());
        return searchYt.queryYT(Config.DEFAULT_QUERY_STRING.getString(), Config.SHOW_RESULTS.getBoolean());
    }

}
