package e2e.hooks;

import e2e.config.AppConfiguration;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@ContextConfiguration(classes = AppConfiguration.class)
public class Hooks {
}
