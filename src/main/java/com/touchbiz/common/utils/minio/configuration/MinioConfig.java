package com.touchbiz.common.utils.minio.configuration;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@ConfigurationProperties(prefix ="minio")
public class MinioConfig implements Serializable {

    private Long expire;

    private String url;

    private String accessKey;

    private String secretKey;

    private String externalUrl;

    private String region;

}

