package com.brainysoftware.downloader;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

import com.brainysoftware.downloader.listener.DownloadListener;

public record DownloadRequest(String uri, Path savePath, 
        AtomicBoolean cancelled, DownloadListener listener) {}