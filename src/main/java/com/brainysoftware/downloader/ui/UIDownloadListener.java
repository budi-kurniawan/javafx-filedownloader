package com.brainysoftware.downloader.ui;

import java.nio.file.Path;

import com.brainysoftware.downloader.event.DownloadProgressEvent;
import com.brainysoftware.downloader.listener.DownloadListener;

/**
 * @author Budi Kurniawan (https://brainysoftware.com)
 */
public class UIDownloadListener implements DownloadListener {
    private ConcurrentDownloadDialog dialog;
    public UIDownloadListener(ConcurrentDownloadDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void onProgress(DownloadProgressEvent event) {
        double progress = (double) event.getBytesDownloaded() / event.getContentLength();
        dialog.setProgress(event.getIndex(), progress);
    }

    @Override
    public void onComplete(int index, Path path) {
        System.out.println("complete:" + path.toString());
        this.dialog.setComplete(index);
    }

    @Override
    public void onError(Throwable error) {
        System.out.println("onError:" + error.toString());
    }
}
