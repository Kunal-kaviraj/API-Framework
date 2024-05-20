package utils.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.xml.XmlSuite;
import utils.common.ExtentReporter;

import java.util.List;

/**
 * @Class_Name: CustomReporter
 * @Functionality: It provides complete reporting of the suite execution (It
 *                 will be updated in future)
 */

public class CustomReporter implements IReporter , ITestListener , IExecutionListener
{

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomReporter.class);

	@Override
	public void onStart(ITestContext testContext)
	{
	}

	@Override
	public void onFinish(ITestContext testContext)
	{

	}

	@Override
	public void onTestFailure(ITestResult testResult)
	{
		LOGGER.error("Test Case Failed: {}", testResult.getMethod().getMethodName());
		LOGGER.error("Failure Reason : ", testResult.getThrowable());
		ExtentReporter.reportStep("Exception Occurred :" + testResult.getThrowable(), "fail");

		onTestCompletion(testResult, "FAILED");
	}

	@Override
	public void onTestSkipped(ITestResult testResult)
	{
		LOGGER.info("Test Case Skipped: {}", testResult.getMethod().getMethodName());
		onTestCompletion(testResult, "SKIPPED");

	}

	@Override
	public void onTestStart(ITestResult testResult)
	{
		LOGGER.info("######################################## TEST CASE STARTED :  [{}] ###################################################################", testResult.getMethod().getMethodName());
		LOGGER.info("Execution Started For Test: {}", testResult.getMethod().getMethodName());
	}

	@Override
	public void onTestSuccess(ITestResult testResult)
	{
		LOGGER.info("Test Case Passed : {}", testResult.getMethod().getMethodName());
		onTestCompletion(testResult, "PASSED");
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult testResult)
	{

	}

	@Override
	public void onExecutionStart()
	{
		LOGGER.info("                            ##########################                                 ");
		LOGGER.info("############################ SUITE EXECUTION STARTED ################################");
		LOGGER.info("                            ##########################                                 ");
	}

	@Override
	public void onExecutionFinish()
	{
		LOGGER.info("                            ##########################                                 ");
		LOGGER.info("############################ SUITE EXECUTION FINISHED ################################");
		LOGGER.info("                            ##########################                                 ");
	}

	@Override
	public void generateReport(List<XmlSuite> arg0, List<ISuite> arg1, String arg2)
	{

	}

	/**
	 * It will be used to perform the after test steps if any.
	 * 
	 * @param testResult
	 */
	private void onTestCompletion(ITestResult testResult, String testStatus)
	{
		String testCaseName = testResult.getMethod().getMethodName();
		LOGGER.info("#### EXECUTED TEST [{}] : {} ##########", testCaseName, testStatus);
		LOGGER.info("");
	}
}
