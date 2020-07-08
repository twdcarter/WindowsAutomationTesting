import cucumber.api.CucumberOptions;
import cucumber.api.Plugin;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "html:target\\reports"}, tags = {"~@disabled"})
public class RunCucumberTest {
}
