package com.vu.subreddit;

import com.vu.subreddit.config.SwaggerConfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class SubredditApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubredditApplication.class, args);
	}

}
