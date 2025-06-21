package com.brainysoftware.downloader.ui.event;

/**
 * 
 * @author Budi Kurniawan (http://brainysoftware.com)
 * 
 */
public class EventManager {
    private static EventManager instance;
    
    private EventManager() {
    }
    
    public static EventManager getInstance() {
        if (instance == null) {
            synchronized(EventManager.class) {
                if (instance == null) {
                    instance = new EventManager();
                }
            }
        }
        return instance;
    }
}