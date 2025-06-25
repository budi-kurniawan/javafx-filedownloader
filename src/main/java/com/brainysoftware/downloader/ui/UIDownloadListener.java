package com.brainysoftware.downloader.ui;

import java.nio.file.Path;

import com.brainysoftware.downloader.event.DownloadProgressEvent;
import com.brainysoftware.downloader.listener.DownloadListener;

public class UIDownloadListener implements DownloadListener {
    private MultiProgressDialog dialog;
    public UIDownloadListener(MultiProgressDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void onProgress(DownloadProgressEvent event) {
        // TODO Auto-generated method stub
        double progress = (double) event.getBytesDownloaded() / event.getContentLength();
        //System.out.println("progress:" + progress);
        dialog.setProgress(0, progress);
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
