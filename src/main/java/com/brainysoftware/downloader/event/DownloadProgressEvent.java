package com.brainysoftware.downloader.event;

import java.util.EventObject;

/**
 * 
 * @author Budi Kurniawan (http://brainysoftware.com)
 * 
 */
public class DownloadProgressEvent extends EventObject {
    private static final long serialVersionUID = 1123887L;
    private long bytesDownloaded;
    private long contentLength;
    public DownloadProgressEvent(Object source, long bytesDownloaded,
            long contentLength) {
        super(source);
        this.bytesDownloaded = bytesDownloaded;
        this.contentLength = contentLength;
    }
    
    public long getBytesDownloaded() {
    	return bytesDownloaded;
    }
    
    public long getContentLength() {
        return contentLength;
    }
}
