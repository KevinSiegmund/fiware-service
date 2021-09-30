package de.fraunhofer.iosb.ast.fiware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * @author Kevin Siegmund
 *
 */
@SpringBootApplication
public class Application {
	
	/**
	 * Methode, welche den Webservice startet
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}