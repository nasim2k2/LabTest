package utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Reporter;


public abstract class DriverTestCase {

    enum WebBrowser{
        Firefox, Chrome, Edge, Safari
    }

    public static WebDriver driver;
    public ExecutionLog log = new ExecutionLog();
    public static PropertyReader propertyReader;

    public void setup() throws Exception{
        String driverType, url, os, mode;
        propertyReader = new PropertyReader();
        driverType = propertyReader.readProperty("browser");
        url = propertyReader.readProperty("url");
        mode = propertyReader.readProperty("headless");
        String osVal = propertyReader.readProperty("os");

        if(osVal.equalsIgnoreCase("linux")){
            System.setProperty("webdriver.chrome.driver", getPath() + "//drivers//chromedriver_linux");
        }
        else if (osVal.equalsIgnoreCase("Win")){
            System.setProperty("webdriver.chrome.driver", getPath() + "//drivers//chromedriver.exe");
        }
        else if (osVal.equalsIgnoreCase("mac")){
            System.setProperty("webdriver.chrome.driver", getPath() + "//drivers//chromedriver_mac");
        }
        // checking if browser is closed then initializing again
        if(!isAlive()) {
            //Check if desired browser is Firefox
            if (WebBrowser.Firefox.toString().equalsIgnoreCase(driverType)) {
                DesiredCapabilities dc = new DesiredCapabilities();
                dc.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
                driver = new FirefoxDriver(dc);
            }

            //Check if desired browser is Chrome
            else if (WebBrowser.Chrome.toString().equalsIgnoreCase(driverType)) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox");
                options.addArguments("--headless");
                options.addArguments("--disable-gpu");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--window-size=1325x744");
                if(osVal.equalsIgnoreCase("Linux"))
                    driver = new ChromeDriver(options);
                else{
                    if(mode.equalsIgnoreCase("no"))
                        driver = new ChromeDriver();
                    else
                        driver = new ChromeDriver(options);

                }

            }

            //Check if desired browser is Safari
            else if (WebBrowser.Safari.toString().equalsIgnoreCase(driverType)) {
                driver = new SafariDriver();

            }

            //Check if desired browser is Edge
            else if (WebBrowser.Edge.toString().equalsIgnoreCase(driverType)) {
                EdgeOptions edgeOptions = new EdgeOptions();

                driver = new EdgeDriver();
            }

            //If browser type is not matched, exit from the system
            else {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox");
                options.addArguments("--headless");
                options.addArguments("--disable-gpu");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--window-size=1325x744");
                if(osVal.equalsIgnoreCase("linux"))
                    driver = new ChromeDriver(options);
                else{
                    if(mode.equalsIgnoreCase("no"))
                        driver = new ChromeDriver();
                    else
                        driver = new ChromeDriver(options);
                }

            }

            String url1 = System.getProperty("app.url");
            if(url1 != null)
                url = url1;

            //Delete cookies
            driver.manage().deleteAllCookies();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.navigate().to(url);
        }
    }

    /*
        exiting all windows opened by webdriver
     */
    public void exit(){
        driver.quit();
    }

    /*
        return web driver instance
     */
    public WebDriver getWebDriver(){
        return driver;
    }

    /*
        this will return execution log class object
     */
    public Object log(){
        return log;
    }

    //Get absolute path
    public String getPath()
    {
        String path ="";
        File file = new File("");
        String absolutePathOfFirstFile = file.getAbsolutePath();
        path = absolutePathOfFirstFile.replaceAll("\\\\+", "/");
        return path;
    }

    /*
        passing the scenario as file name
     */
    public void getScreenshot(String str) throws IOException {
        TakesScreenshot scrShot =((TakesScreenshot)getWebDriver());
        File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
        File file=new File(getPath()+"/Screenshots/"+str);
//        FileUtils.copyFile(SrcFile, file);
    }
    
    
    
    public void captureScreenshot(String fileName) {
		try {
			String screenshotName = ReporterClass.getFileName(fileName);
			FileOutputStream out = new FileOutputStream("screenshots//" + screenshotName + ".jpg");
			out.write(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
			out.close();
			if(driver.getPageSource().contains("An error has occurred processing your request."))
			{
				ExecutionLog.log("****** An Exception occurred in the application ************");
			}
			String path = getPath();
			String screen = "file://" + path + "/screenshots/" + screenshotName + ".jpg";
			Reporter.log("<a href= '" + screen + "'target='_blank' >" + screenshotName + "</a>");
		} 
		catch (Exception e) {
			ExecutionLog.log(e.getMessage());
		}
	}



    public Boolean isAlive(){
        try {
            driver.getCurrentUrl();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    public boolean destroyBrowser() throws Exception {
		boolean bClosed=false;
		String sPlatform = System.getProperty("os.name");
		ExecutionLog.log("Closing driver on platform: " + sPlatform);
		
		try {
			if(driver != null)
				driver.quit();
			
			bClosed=true;
			driver=null;
		}
		catch (Exception e) {
			ExecutionLog.log("Exception in DriverTestCase::closeBrowser() and browser not closed and associated processes potentially not killed.");
		}
		return(bClosed);
	}
  }

