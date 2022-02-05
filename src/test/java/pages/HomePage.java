package pages;

import locators.HomePageLocators;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import utility.ConstantsUtil;
import utility.DriverPage;
import utility.ExecutionLog;
import utility.PropertyReader;

public class HomePage extends DriverPage {


    PropertyReader prop = new PropertyReader();

    public HomePage(WebDriver webdriver){
        super(webdriver);
    }
    
    public void selectMainNavigationOption(String navigationName){
    	 selectMainNavigationMenu(navigationName);
    	 putWait(ConstantsUtil.minWait);
    }
        
}
