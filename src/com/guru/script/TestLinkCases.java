package com.guru.script;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;

public class TestLinkCases {

	private WebDriver driver;

	// Substitute your Dev Key Here
	public final String DEV_KEY = "0bb6a9b805a2ac5c4632f10ae97819a5";

	// Substitute your Server URL Here
	public final String SERVER_URL = "http://qa.kelltontech.net/testlink/lib/api/xmlrpc/v1/xmlrpc.php";

	// Substitute your project name Here
	public final String PROJECT_NAME = "Venu-IQ-Platform";

	// Substitute your test plan Here
	public final String PLAN_NAME = "Venu-IQ-Platform-Test Plan";

	// Substitute your build name
	public final String BUILD_NAME = "Build 1.0";

	@BeforeSuite
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
	}

	@Test
	public void testSearchCountry() throws Exception {
		String result = "";
		String exception = null;
		try {
			driver.navigate().to("http://www.wikipedia.org/wiki/Main_Page");
			result = TestLinkAPIResults.TEST_PASSED;
			updateTestLinkResult("VIQ-18", null, result);
		} catch (Exception ex) {
			result = TestLinkAPIResults.TEST_FAILED;
			exception = ex.getMessage();
			updateTestLinkResult("VIQ-18", exception, result);
		}
//		try {
//			driver.findElement(By.id("searchInput")).clear();
//			driver.findElement(By.id("searchInput")).sendKeys("India");
//			result = TestLinkAPIResults.TEST_PASSED;
//			updateTestLinkResult("TC001-2", null, result);
//		} catch (Exception ex) {
//			result = TestLinkAPIResults.TEST_FAILED;
//			exception = ex.getMessage();
//			updateTestLinkResult("TC001-2", exception, result);
//		}
//		try {
//			driver.findElement(By.id("searchButton")).click();
//			result = TestLinkAPIResults.TEST_PASSED;
//			exception = null;
//			updateTestLinkResult("TC001-3", null, result);
//		} catch (Exception ex) {
//			result = TestLinkAPIResults.TEST_FAILED;
//			exception = ex.getMessage();
//			updateTestLinkResult("TC001-3", exception, result);
//		}
//		String str = driver.findElement(
//				By.xpath("//h1[@id='firstHeading']/span")).getText();
//		Assert.assertTrue(str.contains("India"));
}

	@AfterSuite
	public void tearDown() throws Exception {
		driver.quit();
	}

	public void updateTestLinkResult(String testCase, String exception,
			String result) throws TestLinkAPIException {
		TestLinkAPIClient testlinkAPIClient = new TestLinkAPIClient(DEV_KEY,
				SERVER_URL);
		testlinkAPIClient.reportTestCaseResult(PROJECT_NAME, PLAN_NAME,
				testCase, BUILD_NAME, exception, result);
	}
}
