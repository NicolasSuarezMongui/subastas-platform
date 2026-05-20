package com.subasta.subasta_streamer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class SubastaStreamerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubastaStreamerApplication.class, args);
	}

}
