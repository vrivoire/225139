package com.vrivoire.projectyt;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public enum Config {

    BROKER_TCP_ADDRESS,
    DEFAULT_QUERY_STRING,
    REPLACEMENT_STRING,
    NUMBER_OF_VIDEOS_RETURNED,
    YOUTUBE_PROPERTIES_FILENAME,
    YOU_TUBE_QUEUE_A,
    YOU_TUBE_QUEUE_B;

    private static final Logger LOG = LogManager.getLogger(Config.class);
    private static Map<String, String> map = new HashMap<>();
    private static final String CONFIG_FILE_NAME = "config.json";

    static {
        try {
            File file = new File(CONFIG_FILE_NAME);
            if (!file.exists()) {
                LOG.fatal("Config file not found in the path: " + file.getAbsolutePath());
                file = new File("../" + CONFIG_FILE_NAME);
                if (!file.exists()) {
                    LOG.fatal("Config file not found in the path: " + file.getAbsolutePath());
                    System.exit(-1);
                }
            }
            LOG.debug("Config file found in: " + file.getAbsolutePath());

            ObjectMapper om = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            map = om.readValue(file, new TypeReference<Map<String, String>>() {
            });

            LOG.debug("Configuration:\n" + om.writeValueAsString(map));
        } catch (IOException ex) {
            LOG.fatal(ex);
            System.exit(-1);
        }
    }

    public String getString() {
        return map.get(name());
    }

    public Integer getInteger() {
        return Integer.parseInt(map.get(name()));
    }

    public Long getLong() {
        return Long.parseLong(map.get(name()));
    }

    public Float getFloat() {
        return Float.parseFloat(map.get(name()));
    }

    public Boolean getBoolean() {
        return Boolean.parseBoolean(map.get(name()));
    }

    public Object get() {
        return map.get(name());
    }

}
