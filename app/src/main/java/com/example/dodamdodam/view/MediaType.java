package com.example.dodamdodam.view;

import java.util.Arrays;
import java.util.Optional;

public enum MediaType {
    IMAGE_GIF("image/gif"),
    IMAGE_PNG("image/png"),
    IMAGE_JPG("image/jpeg"),
    VIDEO_WEBM("video/webm"),
    VIDEO_MP4("video/mp4");

    private final String mediaType;

    MediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaType() {
        return mediaType;
    }

    public static Optional<MediaType> mediaFind(String type) throws IllegalArgumentException {
        return Arrays.stream(values()).filter(mediaType -> mediaType.mediaType.equals(type)).findFirst();
    }
}