package tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.HomePage;
import utility.DriverTestCase;
import utility.ExcelUtils;
import utility.ExecutionLog;
import utility.PropertyReader;

public class LABSupportTest extends DriverTestCase {

	private static HomePage homePage;
	private static ExcelUtils sheet;

	PropertyReader propertyReader = new PropertyReader();
//	String userName = propertyReader.readProperty("username");
//	String password = propertyReader.readProperty("password");

	@BeforeMethod(alwaysRun = true)
	public void initForAlerts() throws Exception {
		setup();
		sheet = new ExcelUtils();
		homePage = new HomePage(getWebDriver());
	}

	@Test
	public void testUserIsAbleToNavigateToLABSupportPage() throws Exception {

		String navigationName = sheet.getSingleCellData("NavigationName", 4, "HomePage");
		try {
            homePage.selectMainNavigationOption(navigationName);

		} catch (Error e) {
			getScreenshot("testUserIsAbleToNavigateToLABSupportPage");
			ExecutionLog.logErrorMessage(e);
			throw e;
		} catch (Exception e) {
			getScreenshot("testUserIsAbleToNavigateToLABSupportPage");
			ExecutionLog.logExceptionMessage(e);
			throw e;
		} finally {
			try {

                 // Write Tier down Codes here

			} catch (Error e) {
				ExecutionLog.logErrorMessage(e);
				throw e;
			} catch (Exception e) {
				ExecutionLog.logExceptionMessage(e);
				throw e;
			}
		}
	}
	
	@AfterMethod(alwaysRun = true)
	public void closeBrowser() throws Exception {
		destroyBrowser();
	}

}
