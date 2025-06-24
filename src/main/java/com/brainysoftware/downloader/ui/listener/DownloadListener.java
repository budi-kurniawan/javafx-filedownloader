package com.brainysoftware.downloader.ui.listener;

import java.nio.file.Path;

public interface DownloadListener {
    void onProgress(long bytesDownloaded);
    void onComplete(Path path);
    void onError(Throwable error);
}
