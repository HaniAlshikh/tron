package de.alshikh.haw.tron.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class Config { // TD09
    public static boolean DISTRIBUTED = true;

    public static int FRAMES_PER_SECOND = 13;
    public static int UPDATE_MAX_RETRIES = 50; // wait max UPDATE_MAX_RETRIES/FRAMES_PER_SECOND seconds for an update

    public static int WIDTH = 750;
    public static int HEIGHT = 600;
    public static int ROWS = 40;
    public static int COLUMNS = 50;


    public static String MENU_CSS = "menu.css";
    public static String VIEW_PROP = "system.properties";
    public static String MANAGER_PROP = getAbsolutePath("manager.properties");


    public static String DISCOVERY_GROUP = "235.0.0.0";
    public static int DISCOVERY_PORT = 4446;
    public static int DISCOVERY_PERIOD = 2; // each DISCOVERY_PERIOD in seconds a multicast message is send

    public static int RPC_CALLBACK_TIMEOUT = 3; // in seconds
    // a typical update message is around 254 - 256 bytes
    public static int MAX_PACKET_SIZE = 512;

    private static final Logger log = LoggerFactory.getLogger(Config.class.getSimpleName());
    static {
        try {
            loadConfig();
        } catch (Exception e) {
            log.warn("failed to load system properties: " + e.getMessage());
            log.debug("system properties exception: " + e);
        }
    }

    private static void loadConfig() throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream("system.properties"));

        DISTRIBUTED = Integer.parseInt(prop.getProperty("distributed", DISTRIBUTED ? "1":"0")) == 1;

        FRAMES_PER_SECOND = Integer.parseInt(prop.getProperty("frames_per_second", ""+FRAMES_PER_SECOND));
        UPDATE_MAX_RETRIES = Integer.parseInt(prop.getProperty("update_max_retries", ""+UPDATE_MAX_RETRIES));

        WIDTH = Integer.parseInt(prop.getProperty("width", ""+WIDTH));
        HEIGHT = Integer.parseInt(prop.getProperty("height", ""+HEIGHT));
        ROWS = Integer.parseInt(prop.getProperty("rows", ""+ROWS));
        COLUMNS = Integer.parseInt(prop.getProperty("columns", ""+COLUMNS));

        DISCOVERY_GROUP = prop.getProperty("discovery_group", DISCOVERY_GROUP);
        DISCOVERY_PORT = Integer.parseInt(prop.getProperty("discovery_port", ""+DISCOVERY_PORT));
        DISCOVERY_PERIOD = Integer.parseInt(prop.getProperty("discovery_period", ""+DISCOVERY_PERIOD));

        RPC_CALLBACK_TIMEOUT = Integer.parseInt(prop.getProperty("rpc_callback_timeout", ""+RPC_CALLBACK_TIMEOUT));
        MAX_PACKET_SIZE = Integer.parseInt(prop.getProperty("max_udp_packet_size", ""+MAX_PACKET_SIZE));
    }

    public static String getAbsolutePath(String resourcesFile) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return Objects.requireNonNull(classloader.getResource(resourcesFile)).getPath();
    }
}
