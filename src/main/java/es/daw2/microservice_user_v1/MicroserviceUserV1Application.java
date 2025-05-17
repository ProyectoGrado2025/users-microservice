package es.daw2.microservice_user_v1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MicroserviceUserV1Application {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceUserV1Application.class, args);
	};

	@Bean
	public CommandLineRunner createPasswordsCommand(PasswordEncoder passwordEncoder){
		return args -> {
			System.out.println(passwordEncoder.encode("clave123"));
			System.out.println(passwordEncoder.encode("clave456"));
			System.out.println(passwordEncoder.encode("clave789"));
		};
	}
}
