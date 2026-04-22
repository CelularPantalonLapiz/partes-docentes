package unpsjb.labprog.backend.steps.divisiones;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/divisiones", glue = {
                "unpsjb.labprog.backend.steps.divisiones", "unpsjb.labprog.backend.config" }, plugin = { "pretty" })
public class RunDivisionTest {
}