package com.brainysoftware.downloader.event;

import java.util.EventObject;

/**
 * 
 * @author Budi Kurniawan (https://brainysoftware.com)
 * 
 */
public class DownloadProgressEvent extends EventObject {
    private static final long serialVersionUID = 1123887L;
    private int index;
    private long bytesDownloaded;
    private long contentLength;
    public DownloadProgressEvent(Object source, int index, long bytesDownloaded,
            long contentLength) {
        super(source);
        this.index = index;
        this.bytesDownloaded = bytesDownloaded;
        this.contentLength = contentLength;
    }
    
    public int getIndex() {
        return index;
    }
    
    public long getBytesDownloaded() {
    	return bytesDownloaded;
    }
    
    public long getContentLength() {
        return contentLength;
    }
}
