package com.cloudstorage.cloudstrorage.dto;

public record FileDownload(
        String originalName,
        String storageKey
) { }