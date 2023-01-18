package com.depcue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.depcue.model.Usuario;
import com.depcue.repository.IUsuarioRepository;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import java.util.Optional;
import java.util.TimeZone;

@SpringBootApplication
public class ClubDepCueApplication {

    private static Logger LOG = LoggerFactory.getLogger(ClubDepCueApplication.class);


    @PostConstruct
    public void init() {
        // Setting Spring Boot SetTimeZone
        TimeZone.setDefault(TimeZone.getTimeZone("America/Guayaquil"));
    }

    public static void main(String[] args) {
        SpringApplication.run(ClubDepCueApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
// add cors para peticiones de mismo origen a todos los servicios
                registry.addMapping("/**").allowedMethods("PUT", "GET", "DELETE", "OPTIONS", "PATCH", "POST").allowedHeaders("*");
                ;
            }
        };
    }


/*	@Override
	public void run(String... args) throws Exception {
		LOG.info("############# Servicio Iniciado ############# ");

	}*/

	/*@Bean
	CommandLineRunner initDatabase(IUsuarioRepository repousu, BCryptPasswordEncoder encoder) {
		Optional<Usuario> useradmin = repousu.findByEstadoAndUsername("A", "admin");
		if (!useradmin.isPresent()) {
			Usuario admin = new Usuario();
			admin.setUsername("admin");
			admin.setPassword("$2a$10$vZ6f5vCSDcAs1LMMhE5yru4awYHyG6F2dJ2Li.4AS7VFQWtbIwkYO");
			admin.setRole("ADMIN");
			LOG.info(" User Admin create successfull! ");
			
			Usuario palco = new Usuario();
			palco.setUsername("palco");
			palco.setPassword("$2a$10$wwK8LscM4HZR0bX7lg71b.z/rsak82Itpug.wJrB2d3TOfVZNU61m");
			palco.setRole("USER");
			LOG.info(" User palco create successfull! ");
			
			
			Usuario general = new Usuario();
			general.setUsername("general");
			general.setPassword("$2a$10$/MhXpEGnIA2TLL9/.m3Cneoy5/3CyTIjke0qhpBt8RhnIRkepe2hm");
			general.setRole("USER");
			LOG.info(" User general create successfull! ");
			
			Usuario tribuna = new Usuario();
			tribuna.setUsername("tribuna");
			tribuna.setPassword("$2a$10$SMIU2lVs4IQ4RxTj9mcPA.kZLoQ2mXFXNvNpUPj2Rdug2dOrmJWIq");
			tribuna.setRole("USER");
			LOG.info(" User tribuna create successfull! ");
			
			Usuario preferencia = new Usuario();
			preferencia.setUsername("preferencia");
			preferencia.setPassword("$2a$10$P6mwL5/KrPWKcjlzwa81F.1dOmvX7WC8Ty/Z0skD7RckCtbeJIE3O");
			preferencia.setRole("USER");
			LOG.info(" User preferencia create successfull! ");
			
			
			
			return args -> {
				repousu.save(admin);
				repousu.save(palco);
				repousu.save(general);
				repousu.save(tribuna);
				repousu.save(preferencia);
			};
		} else {
			return args -> {
			};
		}

	}*/

}
