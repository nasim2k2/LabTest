package utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public abstract class  DriverPage extends DriverTestCase {

	protected WebDriver driver;

	public DriverPage(WebDriver webdriver) {
		driver = webdriver;
	}

	/**
	 * @desc this method will verify that element is present or not
	 */
	public Boolean isElementPresent(String locator)
	{
		Boolean result = false;
		try
		{

			getWebDriver().findElement(By.xpath(locator));
			result = true;
		}
		catch (Exception ex) {
			System.out.println(ex);
		}
		return result;
	}

	/**
	 * @desc this method is used to perform drag and drop
	 */
	public void dragAndDrop(String loc_SourceElement, String loc_TargetElement)
	{
		Actions act = new Actions(getWebDriver());
		WebElement SourceElement = getWebDriver().findElement(By.xpath(loc_SourceElement));
		WebElement TargetElement = getWebDriver().findElement(By.xpath(loc_TargetElement));
		act.clickAndHold(SourceElement).moveToElement(TargetElement).pause(2000).release(TargetElement).build().perform();
	}

	/**
	 * @desc verify element is present with given time
	 */
	public boolean waitForElementPresent(String locator, int timeout) {
		boolean flag = false;
		for (int i = 0; i < timeout; i++) {
			if (isElementPresent(locator)) {
				flag = true;
				break;
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	/**
	 * @desc verify element is enabled with given time
	 */
	public void WaitForElementEnabled(String locator, int timeout) {

		for (int i = 0; i < timeout; i++) {
			if (isElementPresent(locator)) {
				if (getWebDriver().findElement(By.xpath(locator)).isEnabled()) {
					break;
				}
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @desc verify element is not enabled for the given time
	 */
	public void WaitForElementNotEnabled(String locator, int timeout) {

		for (int i = 0; i < timeout; i++) {
			if (isElementPresent(locator)) {
				if (!getWebDriver().findElement(By.xpath(locator)).isEnabled()) {
					break;
				}
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @desc verify that element is visible in given time
	 */
	public void WaitForElementVisible(String locator, int timeout) {

		for (int i = 0; i < timeout; i++) {
			if (isElementPresent(locator)) {
				if (getWebDriver().findElement(By.xpath(locator)).isDisplayed()) {
					break;
				}
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * @desc performs the mouse over on given element
	 */
	public void mouseOver(String locator){
		this.waitForElementPresent(locator, 20);
		// find Assignments menu
		WebElement el = getWebDriver().findElement(By.xpath(locator));

		//build and perform the mouseOver with Advanced User Interactions API
		Actions builder = new Actions(getWebDriver());
		builder.moveToElement(el).pause(5000).build().perform();
	}

	/**
	 * @desc perform click operation on given element
	 */
	public void clickOn(String locator)
	{
		this.waitForElementPresent(locator, 20);
		getWebDriver().findElement(By.xpath(locator)).click();
	}

	/**
	 * @desc perform double click on given element
	 */
	public void doubleClick(String locator)
	{
		this.waitForElementPresent(locator, 20);
		Assert.assertTrue(isElementPresent(locator));
		WebElement el = getWebDriver().findElement(By.xpath(locator));
		Actions action = new Actions(driver);
		action.doubleClick(el).perform();
	}

	/**
	 * @desc perform right click on given element
	 */
	public void rightClick(String locator)
	{

		WebElement el = getWebDriver().findElement(By.xpath(locator));
		Actions action = new Actions(driver);
		action.contextClick(el).build().perform();
	}

	/**
	 * @desc enters input on given element
	 */
	public void sendKeys(String locator, String value){
		this.waitForElementPresent(locator, 20);
		Assert.assertTrue(isElementPresent(locator));
		WebElement el = getWebDriver().findElement(By.xpath(locator));
		el.clear();
		putWait(ConstantsUtil.oneMinWait);
		el.sendKeys(value);
	}

	/**
	 * @desc selects frame by locator
	 */
	public void selectFrame(String locator){

		this.waitForElementPresent(locator, 20);
		Assert.assertTrue(isElementPresent(locator));
		getWebDriver().switchTo().frame(locator);

	}

	/**
	 * @desc selects frame by frame index
	 */
	public void selectFrame(int index){
		getWebDriver().switchTo().frame(index);
	}

	/**
	 * @desc selects value from drop down by visible text
	 */
	public void selectDropDownByVisibleText(String locator, String targetValue){
		Assert.assertTrue(isElementPresent(locator));
		this.waitForElementPresent(locator, 20);
		new Select(getWebDriver().findElement(By.xpath(locator))).selectByVisibleText(targetValue);

	}

	/**
	 * @desc selects value from drop down by value
	 */
	public void selectDropDownByValue(String locator, String value){
		Assert.assertTrue(isElementPresent(locator));
		this.waitForElementPresent(locator, 20);
		new Select(getWebDriver().findElement(By.xpath(locator))).selectByValue(value);

	}

	/**
	 * @desc selects value from drop down by index
	 */
	public void selectDropDownByIndex(WebElement locator, int value){
		new Select(locator).selectByIndex(value);;

	}

	/**
	 * @desc verify given text is present at given locators and returns true or falsed
	 */
	public boolean isTextPresent(String locator, String str){
		String message = getWebDriver().findElement(By.xpath(locator)).getText();
		if(message.contains(str)){return true;}
		else {	return false; }
	}

	/**
	 * @desc returns text for the given element
	 */
	public String getText(String locator){
		String text = "";
		try {
			waitForElementPresent(locator, 5);
			text = getWebDriver().findElement(By.xpath(locator)).getText();
		}
		catch (Exception e){}

		return text;
	}

	/**
	 * @desc scrolls up to the given element
	 */
	public void scrollIntoView(String locator){

		WebElement elem = getWebDriver().findElement(By.xpath(locator));
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", new Object[] { elem });
	}

	/**
	 * @desc generating random string of given length with alphanumeric type
	 */
	public static String generateRandomString(int length) throws Exception {

		StringBuffer buffer = new StringBuffer();
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		int charactersLength = characters.length();

		for (int i = 0; i < length; i++) {
			double index = Math.random() * charactersLength;
			buffer.append(characters.charAt((int) index));
		}
		return buffer.toString();
	}

	/**
	 * @desc return  random number
	 */
	public int getRandomNumber(){
		Random random = new Random();
		return random.nextInt(100000);
	}

	/**
	 @desc put a hard coded wait for the given value in seconds
	 */
	public void putWait(int sec){
		try{
			Thread.sleep(sec);
		}
		catch (Exception e){}
	}

	/**
	 @desc return date for the given format
	 */
	public String getDate(String format){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now.plusDays(1));
	}

	/**
	 * @desc verify that document state is ready by executing js code
	 */
	public void verifyDocumentIsReady(){
		boolean flag = false;
		int i = 0;
		while(true){
			if(i==60)
				break;
			boolean val = ((JavascriptExecutor)getWebDriver()).executeScript("return document.readyState").equals("complete");
			if(val == true){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
			i++;
		}



	}

	/**
	 * @desc this method will switch window only when there are only two windows
	 */
	public void switchWindow(){
		String parent = getWebDriver().getWindowHandle();
		Set<String> allWin = getWebDriver().getWindowHandles();
		for(String s : allWin){
			if(!s.equals(parent))
				getWebDriver().switchTo().window(s);
		}
	}

	/**
	 * @desc this method will be used to check element is clickable or not
	 * @param loc
	 * @return
	 */
	public WebElement waitForElementToBeClickable(By loc){
		WebDriverWait wait = new WebDriverWait(getWebDriver(), 60);
		WebElement ele = wait.until(ExpectedConditions.elementToBeClickable(loc));
		return ele;
	}

	/**
	 * @desc this method checks given element is selected or not
	 * @param loc
	 * @return
	 */
	public boolean isElementSelected(String loc){
		return getWebDriver().findElement(By.xpath(loc)).isSelected();
	}

	/**
	 * @desc this method is used to check given element got disappeared or not
	 * @param loc
	 * @return
	 */
	public boolean waitForLoaderToDisappear(String loc, int timeout){
		boolean flag = false;
		for(int i=0;i<timeout;i++){
			try{
				getWebDriver().findElement(By.xpath(loc));
				Thread.sleep(1000);
			}
			catch (Exception e){
				flag = true;
				break;
			}
			if(i==timeout){
				break;
			}
		}
		return flag;
	}

	public void selectMainNavigationMenu(String menuName){
		putWait(ConstantsUtil.mediumWait);
		clickOn("//trq-icon[contais(@class, 'trq-icon-menu')]");
		WebElement navigationMenu= (new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@title='"+ menuName + "']"))));
		navigationMenu.click();
		putWait(ConstantsUtil.mediumWait);
	}
	
}