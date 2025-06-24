package com.brainysoftware.downloader.listener;

import java.nio.file.Path;

import com.brainysoftware.downloader.event.DownloadProgressEvent;

public class SimpleDownloadListener implements DownloadListener {

    @Override
    public void onProgress(DownloadProgressEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onComplete(Path path) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onError(Throwable error) {
        // TODO Auto-generated method stub
        
    }

}
