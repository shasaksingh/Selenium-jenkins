package com.guru.script;
import static com.guru.script.DriverScript.APP_LOGS;
import static com.guru.script.DriverScript.CONFIG;
import static com.guru.script.DriverScript.OR;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.ini4j.Config;
import org.openqa.selenium.By;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


import com.thoughtworks.selenium.Wait;



public class Keywords {
	public int count =1;
	String screenshotName="";
	public WebDriver driver;
	public String locatorResult="";
	public String writeResult;
	public String part="";
	public String imageFormat = ".jpg";
	public String ScrResult="";


	public String verifyElements(String object, String data) throws IOException, NoSuchElementException, InterruptedException,InvocationTargetException{
		APP_LOGS.debug("Verifying the expected elements");
		try{
			String element = OR.getProperty(object);
			System.out.println(element);	 
			String locator[] = element.split(",,");
			for (String part : locator) {
				//System.out.println("String part is--->" +part);
				verifyLocarorsInPage(part, data);
				writeResult = locatorResult;
			}
			locatorResult="";
		}catch(Exception e){
			System.out.println("Exception in verifyElements--->" +e);
		}
		return writeResult;
	}



	public  String verifyLocarorsInPage (String part, String data) throws IOException{
		try{
			Thread.sleep(5000);
			if(!driver.findElement(By.xpath(part)).isDisplayed())
			{
				System.out.println("Element not displayed.");
			}
		}catch(Exception e){
			System.out.println("Exception in verifyLocarorsInPage--> Element is not found");
			ScrResult = Constants.KEYWORD_FAIL;
			captureScreenshot(imageFormat, ScrResult);
			locatorResult+=Constants.KEYWORD_FAIL+screenshotName+", ";
			return locatorResult;
		}
		locatorResult+=Constants.KEYWORD_PASS+", ";
		return locatorResult;
	}

	public String captureScreenshot(String Format, String ScrResult) throws IOException{
		// take screen shots
		//if(CONFIG.getProperty("screenshot_everystep").equals("Y")){
		// capturescreen

		//File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		//FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") +"//screenshots//"+filename+".jpg"));

		//}else 
		if (ScrResult.startsWith("FAIL,") && CONFIG.getProperty("screenshot_error").equals("Y") ){
			// capture screenshot
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			String screenshotName = System.getProperty("user.dir") +"/screenshots//"+count + ".jpg";
			FileUtils.copyFile(scrFile, new File(screenshotName));

			HSSFHyperlink url_link=new HSSFHyperlink(HSSFHyperlink.LINK_URL);
			screenshotName=screenshotName.replace('\\', '/');

			url_link.setAddress(screenshotName);
			count++;



		}
		return screenshotName;
	}


	public String openBrowser(String object,String data) throws InterruptedException{		
		APP_LOGS.debug("Opening browser");
		if(data.equals("Mozilla")){
			driver=new FirefoxDriver();
			driver.manage().window().maximize();
//			Set<Cookie> allCookies = driver.manage().getCookies();
//			for(Cookie cookie : allCookies)
//			{
//			    driver.manage().addCookie(cookie);
//			}
			//driver.quit();
			
		}

		else if(data.equals("IE")){

			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			//capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability("ignoreZoomSetting", true);
			driver = new InternetExplorerDriver(capabilities);

			//			System.setProperty("webdriver.ie.driver", "D:/Selenium Stuffs/Selenium Driver/IEDriverServer.exe");
			System.setProperty("webdriver.ie.driver", "D:/Selenium Stuffs/Selenium Driver/IEDriverServer.exe");


			//driver=new InternetExplorerDriver();
			//driver.get("https://xsy.detail.com/main.aspx");

			driver.get("https://www.site.com");


			//driver.execute_script("document.getElementById('overridelink').click()");


			//			WebDriverWait wait =new WebDriverWait(driver, 10);
			//			WebElement ele = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.linkText("Continue to this website (not recommended)."))));
			//		 ele.click();


			//driver.get("javascript:document.getElementById('overridelink').click()");
			//
			//driver.navigate().to("javascript:document.getElementById('overridelink').click()");
			//driver.quit();
		}
		else if(data.equals("Chrome")){
			System.setProperty("webdriver.chrome.driver", "D:/Selenium Stuffs/Selenium Drivers/chromedriver.exe");
			driver=new ChromeDriver();
		}
		long implicitWaitTime=Long.parseLong(CONFIG.getProperty("implicitwait"));
		driver.manage().timeouts().implicitlyWait(implicitWaitTime, TimeUnit.SECONDS);
		return Constants.KEYWORD_PASS;
	}

	public String navigate(String object,String data){		
		APP_LOGS.debug("Navigating to URL");
		try{
			driver.navigate().to(data);
			//driver.quit();
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Not able to navigate";
		}
		return Constants.KEYWORD_PASS;
	}

	public  String clickButton(By object,String data) throws InterruptedException{
		APP_LOGS.debug("Clicking on Button");
		try{
			
			System.out.println("Clicking on the login");
			driver.findElement(object).click();
			Thread.sleep(5000);
			System.out.println("clicked");
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Unable to click on button"+e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}

	public String waitForElement(String object, String data) {
		try{
			WebDriverWait wait = new WebDriverWait(driver,30);

			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(OR.getProperty(object))));
			System.out.println("Element is present");
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- button is not present";
		}
		return Constants.KEYWORD_PASS;
	}


	public String dropDownByIndex (String object, String data) throws InterruptedException{
		//Thread.sleep(3000);
		try{
			WebElement dropDown = driver.findElement(By.xpath(OR.getProperty(object)));
			Select select = new Select(dropDown);
			int i =select.getOptions().size();
			ArrayList<String>  valuesInList = new ArrayList<String>();
			for(WebElement element:select.getOptions())
			{
				valuesInList.add(element.getText());

				System.out.println("list elements are"+valuesInList);
			}
			if(valuesInList.contains(data))
			{
				int index=valuesInList.indexOf(data);
				select.selectByIndex(index);
				System.out.println("Selected value is "+index);
			}
		}
		catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Not able to click on Button"+e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}


	public String dropDownByText (String object,String data) throws InterruptedException{
		//Thread.sleep(1000);
		try{
			WebElement dropDown = driver.findElement(By.xpath(OR.getProperty(object)));
			Select select = new Select(dropDown);
			int i =select.getOptions().size();
			ArrayList<String>  valuesInList = new ArrayList<String>();
			for(WebElement element:select.getOptions())
			{
				valuesInList.add(element.getText());
			}

			select.selectByVisibleText(data);
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Not able to click on Button"+e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}
	public  String uploadFile (String object,String data) throws InterruptedException{
		Thread.sleep(3000);
		try{
			driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
			//String baseWindow = driver.getWindowHandle();
			//driver.switchTo().window(baseWindow);
			System.out.println("file is uploaded");
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Unable to upload the file"+e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}


	public  String selectList(String object, String data){
		APP_LOGS.debug("Selecting from list");
		try{
			if(!data.equals(Constants.RANDOM_VALUE)){
				driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
			}else{
				// logic to find a random value in list
				WebElement droplist= driver.findElement(By.xpath(OR.getProperty(object))); 
				List<WebElement> droplist_cotents = droplist.findElements(By.tagName("option"));
				Random num = new Random();
				int index=num.nextInt(droplist_cotents.size());
				String selectedVal=droplist_cotents.get(index).getText();
				driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(selectedVal);
			}
		}catch(Exception e){
			return Constants.KEYWORD_FAIL +" - Could not select from list. "+ e.getMessage();	
		}
		return Constants.KEYWORD_PASS;
	}


	public String verifyText(String object, String data){
		APP_LOGS.debug("Verifying the text");
		try{


			String actual=driver.findElement(By.xpath(OR.getProperty(object))).getText();
			String expected=data;
			//String confirmation=driver.findElement(By.xpath(OR.getProperty("confirmation"))).getText();
			if(actual.equals(expected))
				return Constants.KEYWORD_PASS;
			else
				return Constants.KEYWORD_FAIL+" -- text not verified "+actual+" -- "+expected;
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" Object not found "+e.getMessage();
		}
	}

	public  String enterText(By object,String data){
		APP_LOGS.debug("Writing in text box");
		try{
			
			System.out.println("enetr text is"+data);
			//driver.findElement(object).sendKeys(data);
			//driver.findElement(By.id("login-email")).sendKeys(data);

			driver.findElement(object).sendKeys(data);
			System.out.println("TEXT ENTERED");
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" Unable to write "+e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}

	public  String enterTextt(By object,String data){
		APP_LOGS.debug("Writing in text box");
		try{
			driver.findElement(object).sendKeys(data);
			//driver.findElement(By.xpath(OR.getProperty(object))).clear();
			//driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" Unable to write "+e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}


	public  String writeIntInput(String object,String data){
		APP_LOGS.debug("Writing in integer value in text box");
		System.out.println("Exceuting writeIntInput Keyword............");
		int integetData = Integer.parseInt(data); 
		try{
			driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(""+integetData);
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" Unable to write "+e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}


	public  String verifyLinkText(String object,String data){
		APP_LOGS.debug("Verifying link Text");
		try{
			String actual=driver.findElement(By.xpath(OR.getProperty(object))).getText();
			String expected=data;
			String confirmation=driver.findElement(By.xpath(OR.getProperty("confirmation"))).getText();
			if(actual.equals(expected))
				return Constants.KEYWORD_PASS+"--"+confirmation;
			else
				return Constants.KEYWORD_FAIL+" -- Link text not verified";
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Link text not verified"+e.getMessage();
		}
	}


	public String clickLink(String object,String data){
		APP_LOGS.debug("Clicking on link ");
		try{
			driver.findElement(By.xpath(OR.getProperty(object))).click();
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Not able to click on link"+e.getMessage();
		}

		return Constants.KEYWORD_PASS;
	}
	public String datePicker(String object,String data){
		APP_LOGS.debug("Clicking on link ");
		try{

			WebElement dateWidget = driver.findElement(By.xpath(OR.getProperty(object)));
			List<WebElement> rows = dateWidget.findElements(By.tagName("tr"));  
			List<WebElement> columns = dateWidget.findElements(By.tagName("td"));
			//int columnsSize =columns.size();
			// System.out.println("columns are--->" +columnsSize);
			for (WebElement cell: columns){
				if (cell.getText().equals(data)){
					cell.findElement(By.linkText(data)).click();
					break; 
				}
			}
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Not able to click on link"+e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}

	public String waitAutoPopup(String object,String data){
		APP_LOGS.debug("waiting for popup closer");
		try{

			WebDriverWait wait = new WebDriverWait(driver,20);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(OR.getProperty(object))));
			driver.findElement(By.xpath(OR.getProperty(object))).click();
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- popup not closed within time"+e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}


	public String dragAndDrop(String object,String data){
		APP_LOGS.debug("waiting for popup closer");
		try{
			driver.switchTo().frame("iframeResult");
			WebElement element = driver.findElement(By.xpath(".//*[@id='drag1']"));
			WebElement target = driver.findElement(By.xpath(".//*[@id='div1']"));
			(new Actions(driver)).dragAndDrop(element, target).build().perform();
			Thread.sleep(3000);
			//(new Actions(driver)).dragAndDropBy(element, 0, 100).build().perform();
			(new Actions(driver)).clickAndHold(element).moveToElement(target).build().perform();
			System.out.println("Exceuting writeIntInput Keyword............");
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- popup not closed within time"+e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}

	public String mouseHover(By object,String data){
		APP_LOGS.debug("waiting for popup closer");
		try{

			Actions action= new Actions(driver);
			WebElement hoverElement = driver.findElement(object);
			action.moveToElement(hoverElement).build().perform();
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- popup not closed within time"+e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}
	public String scrollWindow(By object,String data){
		APP_LOGS.debug("waiting for popup closer");
		try{

			WebElement element =driver.findElement(object);

					((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", element);
					
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- popup not closed within time"+e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}



	//**************************************************************************

	public String test(String object,String data){
		APP_LOGS.debug("waiting for popup closer");
		try{
			System.out.println("inside test");


			//Thread.sleep(10000);
			//String S = driver.findElement(By.name("Consignor.Address.CountryId")).getText();
			driver.get("http://www.flipkart.com/");
			driver.manage().window().maximize();
			Actions action = new Actions(driver);



			WebElement mousehover = driver.findElement(By.xpath(".//*[@id='fk-mainhead-id']/div[2]/div/div/ul/li[2]/a/span"));
			action.moveToElement(mousehover).build().perform();
			driver.findElement(By.xpath(".//*[@id='menu-men-tab-0-content']/ul[1]/li[3]/a")).click();
			//((JavascriptExecutor) driver).executeScript("scroll(0,550);");

			WebElement scrollTo = driver.findElement(By.xpath(".//*[@id='facetList']/div[3]/div[2]/div/div[1]/div/h2"));
			((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", scrollTo);
			driver.findElement(By.xpath(".//*[@id='price_range']/li[4]/a/input")).click();
			String pricerange = driver.findElement(By.xpath(".//*[@id='price_range']/li[4]/a/span")).getText();
			int spricerange= pricerange.indexOf("-");
			String sspricerange= pricerange.substring(spricerange+1);
			String ssspricerange= pricerange.substring(spricerange);

			System.out.println("Item price is----->" +ssspricerange);

			System.out.println("Item price is----->" +sspricerange);


			WebDriverWait wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//*[@id='sticky-referrer']/div[6]/div/div[1]/div[2]/img")));

			//WebElement Srollup = driver.findElement(By.xpath(".//*[@id='products']/div[1]/div[1]/div[1]/div"));
			//((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", Srollup);
			//driver.findElement(By.xpath(".//*[@id='products']/div[1]/div[1]/div[1]/div")).click();

			String price = driver.findElement(By.xpath(".//*[@id='products']/div[1]/div[1]/div[1]/div/div[2]/div[3]/div[1]/span")).getText();
			//if(pricerange.equals(price))




			System.out.println("Item price is----->" +price);

			Thread.sleep(2000);
			driver.findElement(By.xpath(".//*[@id='products']/div/div[3]/div[1]/div/div[1]/a[1]/img")).click();
			WebElement scrollleft = driver.findElement(By.xpath(".//*[@id='fk-mainbody-id']/div/div[7]/div/div[2]/div/div/div[1]/div[1]/div/img[1]"));
			action.moveToElement(scrollleft, 0, 20).build().perform();
			Screenshot();

			Thread.sleep(5000);
			WebElement scrollto = driver.findElement(By.xpath(".//*[@id='fk-mainbody-id']/div/div[7]/div/div[2]/div/div/div[1]/div[1]/div/img[1]"));
			action.moveToElement(scrollto, 20, 60).build().perform();
			Screenshot();



			//action.moveToElement(toElement, xOffset, yOffset)
			//WebElement scrollup = driver.findElement(By.xpath(".//*[@id='products']/div/div[1]/div[1]/div/div[2]/div[1]/a"));
			//((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", scrollup);
			//driver.findElement(By.xpath(".//*[@id='products']/div[1]/div[3]/div[1]/div/div[1]/a[1]")).click();
			//driver.findElement(By.xpath(".//*[@id='products']/div[1]/div[1]/div[1]/div")).click();
			//driver.findElement(By.xpath(".//*[@id='login']")).sendKeys("si");
			//driver.findElement(By.xpath("//*[@id='f']/table/tbody/tr[1]/td[3]/input")).click();
			//driver.findElement(By.xpath("//a[@class='butcpt']")).click();
			//driver.findElement(By.linkText("Clear list ")).click();
			//driver.switchTo().frame("ifinbox");// Actually the inbox mails are showing in an iFrame so first we have to switch to that iFrame to perform the further action.
			//driver.findElement(By.xpath(".//*[@id='e0']")).click();// click on the select mail icon.
			//driver.findElement(By.xpath("html/body/div[1]/table/tbody/tr/td[2]/a")).click();// click on the delete
			//driver.findElement(By.linkText("Empty Inbox")).click();// click on the Empty Inbox.



		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- popup not closed within time"+e.getMessage();

		}

		return Constants.KEYWORD_PASS;
	}


	public String testFlip(String object,String data){
		APP_LOGS.debug("waiting for popup closer");
		try{

			Thread.sleep(3000);
			//(new Actions(driver)).dragAndDropBy(element, 0, 100).build().perform();

			System.out.println("Exceuting writeIntInput Keyword............");
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- popup not closed within time"+e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}


	public String Screenshot() throws IOException{
		// take screen shots

		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String screenshotName = System.getProperty("user.dir") +"/screenshots//"+count + ".jpg";
		FileUtils.copyFile(scrFile, new File(screenshotName));
		count++;
		return screenshotName;


	}


}
