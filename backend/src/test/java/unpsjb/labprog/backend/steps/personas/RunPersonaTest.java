package unpsjb.labprog.backend.steps.personas;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/personas", glue = { "unpsjb.labprog.backend.steps.personas",
                "unpsjb.labprog.backend.config" }, plugin = { "pretty" })
public class RunPersonaTest {
}
