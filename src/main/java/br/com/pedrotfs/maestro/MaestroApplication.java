package br.com.pedrotfs.maestro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class MaestroApplication {
	public static void main(String[] args) {
		SpringApplication.run(MaestroApplication.class, args);
	}
}
