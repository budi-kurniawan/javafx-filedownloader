package com.brainysoftware.downloader;

import java.nio.file.Path;

/**
 * @author Budi Kurniawan (https://brainysoftware.com)
 */
public record DownloadRequest(String uri, Path savePath) {}