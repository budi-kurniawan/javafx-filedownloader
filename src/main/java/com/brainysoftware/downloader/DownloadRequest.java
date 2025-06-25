package com.brainysoftware.downloader;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

public record DownloadRequest(int index, String uri, Path savePath, 
        AtomicBoolean cancelled) {}