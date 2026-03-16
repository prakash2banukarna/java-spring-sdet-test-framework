package e2e.hooks;

import e2e.config.AppConfiguration;
import e2e.kafka.consumer.KafkaConsumerService;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@ContextConfiguration(classes = AppConfiguration.class)
public class Hooks {

    @Autowired
    private KafkaConsumerService kafkaConsumerService;

    @Before
    public void beforeScenario(Scenario scenario) {
        System.out.println("\n========== Starting: " + scenario.getName() + " ==========");
        kafkaConsumerService.reset();           // clears list + opens the gate
    }

    @After
    public void afterScenario(Scenario scenario) {
        kafkaConsumerService.stopAccepting();   // closes gate — no bleed into next scenario
        System.out.println("========== Finished: " + scenario.getName()
                + " | Status: " + scenario.getStatus() + " ==========\n");
    }
}