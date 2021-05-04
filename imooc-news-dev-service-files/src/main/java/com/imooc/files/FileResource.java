package com.imooc.files;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:file-dev.properties")
@ConfigurationProperties(prefix = "file")
public class FileResource {

}
