package unpsjb.labprog.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.business.PersonaRepository;

@SpringBootApplication
@RestController
public class BackendApplication {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Object> home() {
        return Response.response(
                HttpStatus.OK,
                "El servidor esta andando",
                "Hola partes-docentes!");
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(PersonaRepository repo) {
        return args -> {

        };
    }
}