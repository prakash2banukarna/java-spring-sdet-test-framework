package e2e.stepDefs;

import e2e.config.AppConfiguration;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;

public class CalculatorSteps {

    @Autowired
    private AppConfiguration appConfiguration;

    @And("I calculate two numbers which is {} and {}")
    public void iCalculateTwoNumbers(int a,int b){
        int c=0;
        System.out.println("User Name from property file: " + appConfiguration.getDbUserName());
        switch (appConfiguration.getOpertation()){
            case "Addition":
                c= a+b;
                break;
            case "Subtraction":
                c=a-b;
                break;
            default:
                break;
        }
        System.out.println("This is my answer: " +c);
    }
}
