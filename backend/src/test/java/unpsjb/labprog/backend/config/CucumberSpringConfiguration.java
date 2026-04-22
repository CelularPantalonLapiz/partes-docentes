package unpsjb.labprog.backend.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import unpsjb.labprog.backend.BackendApplication;

@CucumberContextConfiguration
@SpringBootTest(classes = BackendApplication.class)
public class CucumberSpringConfiguration {
    // Esta clase queda acá solita y sirve para TODO el proyecto
}