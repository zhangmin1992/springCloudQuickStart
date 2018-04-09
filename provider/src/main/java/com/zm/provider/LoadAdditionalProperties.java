package com.zm.provider;

import java.io.IOException;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.env.PropertySourcesLoader;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class LoadAdditionalProperties implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private ResourceLoader loader = new DefaultResourceLoader();

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent) {
        try {
            Resource resource = loader.getResource("classpath:rabbitmq.properties");
            PropertySource<?> propertySource = new PropertySourcesLoader().load(resource);
            applicationEnvironmentPreparedEvent.getEnvironment().getPropertySources().addLast(propertySource);
        }catch(IOException e){
            throw new IllegalStateException(e);
        }
    }
}