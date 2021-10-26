package com.example.xml_parser_first_server;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class XmlParserFirstServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(XmlParserFirstServerApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
