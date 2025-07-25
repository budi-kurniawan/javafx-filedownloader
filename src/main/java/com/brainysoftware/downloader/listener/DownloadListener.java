package com.brainysoftware.downloader.listener;

import java.nio.file.Path;

import com.brainysoftware.downloader.event.DownloadProgressEvent;

public interface DownloadListener {
    void onProgress(DownloadProgressEvent event);
    void onComplete(int index, Path path);
    void onError(Throwable error);
}
