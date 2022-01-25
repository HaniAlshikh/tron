package de.alshikh.haw.tron;

import java.util.Objects;

// TODO: ConfigManager
public class Config {
    public final static boolean DISTRIBUTED = true;

    public static final int UPDATE_MAX_RETRIES = 50; // no update -> wait NUMBER_OF_RETRIES / FBS seconds
    public static final int FRAMES_PER_SECOND = 13;

    public static final int WIDTH = 750;
    public static final int HEIGHT = 600;
    public static final int ROWS = 40;
    public static final int COLUMNS = 50;


    public static final String MENU_CSS = "menu.css";
    public static final String VIEW_PROP = getAbsolutePath("view.properties");
    public static final String MANAGER_PROP = getAbsolutePath("manager.properties");


    public static final int RPC_CALLBACK_TIMEOUT = 3; // in seconds


    public static String getAbsolutePath(String resourcesFile) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return Objects.requireNonNull(classloader.getResource(resourcesFile)).getPath();
    }
}
