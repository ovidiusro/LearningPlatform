package com.hozan.platform.configuration;

import com.hozan.platform.service.DateService;
import com.hozan.platform.service.JodaDateServiceImpl;
import org.joda.time.DateTimeZone;
import org.modelmapper.ModelMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.content.fs.config.EnableFilesystemStores;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@EnableCaching
@EnableTransactionManagement
@EnableFilesystemStores
public class SpringConfig {

    @Bean
    public ModelMapper modelMapper(){return new ModelMapper();}

    @Bean
    public CacheManager cacheManager(){
        return new ConcurrentMapCacheManager("accountCache","groupCache");
    }

    @Bean
    public SimpleMailMessage templateSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText( "This is the test email template for your email:\n%s\n");
        return message;
    }

    @Bean
    DateService dateService() {
        return new JodaDateServiceImpl(defaultTimeZone());
    }

    @Bean
    DateTimeZone defaultTimeZone() {
        return DateTimeZone.UTC;
    }

    @Bean
    File filesystemRoot() {
        try {
            return Files.createTempDirectory("").toFile();
        } catch (IOException ignored) {}
        return null;
    }
}
