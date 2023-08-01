package com.tenpo.tenpochallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableAsync
@EnableRedisRepositories
public class TenpoChallengeApplication {
    public static void main(String[] args) {
        SpringApplication.run(TenpoChallengeApplication.class, args);
    }
}
