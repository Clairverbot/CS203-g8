package com.G2T8.CS203WebApp;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
            return application.sources(Cs203WebAppApplication.class); // Replace DemoApplication with your main class
    }

}