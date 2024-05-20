package utils.common;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.BeforeSuite;
import utils.http.IRestResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExtentReporter {

	protected static ExtentTest test;
	protected static ExtentReports extent;
	private static DateFormat dateFormat;
	private static Date date;
	private static String dt;
	private static String buildnumber;
	private static String jenkinsjobname;
	public static String extentReportPath;
	private static String extentReport, reportFolder, title;
	private static HashMap<Object, Object> testStatusDetail;
	static Map<Object, Object> extentTestMap = new HashMap<>();
	static Map<Object, Object> testMap = new HashMap<>();

	@BeforeSuite(alwaysRun = true)
	public void extentsetUp(ITestContext xmlSuite)
	{
		String suitName = xmlSuite.getSuite().getName();
		startExtentReport(suitName);
	}

	public synchronized static ExtentReports startExtentReport(String suiteName)
	{
		try
		{
			dateFormat = new SimpleDateFormat("dd_MMMM_yyyy_hh_mm_ss");
			date = new Date();
			dt = dateFormat.format(date);
			reportFolder = suiteName + "_" + dt;

			extentReportPath = System.getProperty("user.dir") + "/test-output/";
			buildnumber = System.getProperty("jenkinsbuild");
			jenkinsjobname = System.getProperty("jobname");
			if(buildnumber != null)
			{
				/*
				 * commenting for moving the testreport to sharedrive access reportFolder =
				 * buildnumber; extentReport = buildnumber + "/" + jenkinsjobname + ".html";
				 * title = jenkinsjobname;
				 */
				extentReport = reportFolder + "/" + suiteName + "_Report_" + dt + ".html";
				title = suiteName;
			}
			else
			{
				extentReport = reportFolder + "/" + suiteName + "_Report_" + dt + ".html";
				title = suiteName;
			}

			/* NEW CHANGES FOR V5.0.8 */
			extent = new ExtentReports();
			ExtentSparkReporter spark = new ExtentSparkReporter(
					extentReportPath + "/reports/extent" + "/" + extentReport).viewConfigurer().viewOrder()
							.as(new ViewName[] { ViewName.DASHBOARD, ViewName.TEST, ViewName.CATEGORY, ViewName.AUTHOR,
									ViewName.DEVICE, ViewName.EXCEPTION, ViewName.LOG })
							.apply();
			extent.attachReporter(spark);
			extent.setSystemInfo("Environment", "QVC");
			extent.setSystemInfo("OS Version", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("User Name", System.getProperty("user.name"));
			if(buildnumber != null)
			{
				extent.setSystemInfo("Build Number", buildnumber);
				extent.setSystemInfo("Title", jenkinsjobname);
			}

			testStatusDetail = new HashMap<>();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return extent;
	}

	public synchronized ExtentTest startTestCase(String testName)
	{
		test = getReporter().createTest(testName, testName);
		return test;
	}

	public synchronized static void reportStep(String desc)
	{
		try
		{
			getTest().log(Status.INFO, desc);
			testStatusDetail.put(getTestName(), "-");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public synchronized static void reportStep(String desc, String status)
	{
		try
		{

			if(status.toUpperCase().equals("PASS"))
			{
				getTest().log(Status.PASS, desc);
				testStatusDetail.put(getTestName(), "-");
			}
			else if(status.toUpperCase().equals("FAIL"))
			{
				getTest().log(Status.FAIL, desc);
				testStatusDetail.put(getTestName(), "-");
				Assert.assertTrue(Boolean.parseBoolean(status));
			}
			else if(status.toUpperCase().equals("INFO"))
			{
				getTest().log(Status.INFO, desc);
				testStatusDetail.put(getTestName(), "-");
			}
			else if(status.toUpperCase().equals("WARNING"))
			{
				getTest().log(Status.WARNING, desc);
				testStatusDetail.put(getTestName(), "-");
			}
		}
		catch (Exception e)
		{
			try
			{
				throw (e);
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
	}

	protected synchronized void endExtentReport(String status, ITestResult result)
	{
		if(status == "Failed")
		{
			ExtentReporter.getTest().log(Status.FAIL, result.getThrowable());
		}
		else if(status == "Skipped")
		{
			ExtentReporter.getTest().log(Status.SKIP, "Test skipped " + result.getThrowable());
		}
		else
		{
			ExtentReporter.getTest().log(Status.PASS, "Test passed");
		}
		getReporter().flush();
	}

	protected synchronized void endExtentReport(String scenario, String status)
	{
		if(status == "Failed")
		{
			ExtentReporter.getTest().log(Status.FAIL, "Scenario \"" + scenario + "\" Failed.");
		}
		else if(status == "Skipped")
		{
			ExtentReporter.getTest().log(Status.SKIP, "Scenario \"" + scenario + "\" Skipped.");
		}
		else
		{
			ExtentReporter.getTest().log(Status.PASS, "Scenario \"" + scenario + "\" Passed.");
		}
		getReporter().flush();
	}

	public synchronized static void endExtentReport()
	{
		getReporter().flush();
	}

	private synchronized static ExtentReports getReporter()
	{
		if(extent == null)
			extent = startExtentReport("Non Transactional Automation");
		return extent;
	}

	private static synchronized ExtentTest getTest()
	{
		return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
	}

	private static synchronized String getTestName()
	{
		String testName = (String) testMap.get((int) (long) (Thread.currentThread().getId()));
		return testName;
	}

	@SuppressWarnings("unused")
	private synchronized ExtentTest startExtentTestReport(String testName)
	{
		return startExtentTestReport(testName, "");
	}

	public synchronized static ExtentTest startExtentTestReport(String testName, String desc)
	{
		test = getReporter().createTest(testName, desc).assignCategory(testName.substring(0, testName.lastIndexOf(":")));
		extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
		testMap.put((int) (long) (Thread.currentThread().getId()), testName);
		return test;
	}
	
	/**
	 * @Method_Name: InfoStep
	 * @Functionality: This method is used to print code blocks in the report e.g. request body, response body etc.
	 * @param: String codeBlockTitle,String codeBlock
	 * @return: None
	 * @author: Kunal Kaviraj
	 */
	public synchronized static void InfoStep(String codeBlockTitle,String codeBlock)
	{
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(codeBlock, Object.class);
			codeBlock = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
			getTest().info(MarkupHelper.createCodeBlock(codeBlockTitle + " ->", codeBlock));
		}
		catch (Exception e)
		{
			getTest().info(MarkupHelper.createCodeBlock(codeBlockTitle + " ->", codeBlock));
		}
	}

	public synchronized static void InfoStep(String codeBlockTitle, IRestResponse<?> response)
	{
		try
		{
			ObjectMapper mapper = null;

			if (response.getResponse().contentType().contains("json"))
				mapper = new ObjectMapper();

			else if (response.getResponse().contentType().contains("xml"))
				mapper = new XmlMapper();

			Object obj = mapper.readValue(response.getContent(), Object.class);
			String codeBlock  = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
			getTest().info(MarkupHelper.createCodeBlock(codeBlockTitle + " ->", codeBlock));
		}
		catch (Exception e)
		{
			getTest().info(MarkupHelper.createCodeBlock(codeBlockTitle + " ->", response.getContent()));
		}
	}
	
	public synchronized static void InfoStep(String codeBlockTitle, Object T)
	{
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(((String[])T)[0], Object.class);
		 	String codeBlock = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
			getTest().info(MarkupHelper.createCodeBlock(codeBlockTitle + " ->", codeBlock));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	

}