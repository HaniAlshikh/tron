package de.alshikh.haw.tron;

// TODO: ConfigManager
public class Config {
    public final static boolean DISTRIBUTED = true;

    public static final int UPDATE_MAX_RETRIES = 50; // no update -> wait NUMBER_OF_RETRIES / FBS seconds
    public static final int FRAMES_PER_SECOND = 13;

    public static final int WIDTH = 750;
    public static final int HEIGHT = 600;
    public static final int ROWS = 40;
    public static final int COLUMNS = 50;
}
