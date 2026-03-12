package e2e.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:env-dev.properties") // First read all the property file data
public class AppConfiguration {

    @Value("${userName}")
    private String dbUserName;

    @Value("${operation}")
    private String operation;

    public String getDbUserName() {
        return dbUserName;
    }

    public String getOpertation(){
        return operation;
    }
}
