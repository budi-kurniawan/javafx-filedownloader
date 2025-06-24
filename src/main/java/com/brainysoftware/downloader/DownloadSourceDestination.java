package com.brainysoftware.downloader;

import java.nio.file.Path;

public record DownloadSourceDestination(String uri, Path savePath) {}