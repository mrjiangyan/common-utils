package com.touchbiz.common.utils.minio.configuration;

import io.minio.MinioClient;

public class CustomMinioClient extends MinioClient {

    public CustomMinioClient(MinioClient client) {
        super(client);
    }


}