package core;

import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import utils.common.ExtentReporter;
import utils.http.RestClient;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A class to hold all setup, teardown, or test code common between all tests.
 * This base class is to be extended by every other test class.
 */
public abstract class BaseTest extends ExtentReporter
{
    public static String geoName;
    public static String hostName;
    public static String env;
    public String suitExecutionTime;
    public int totalScripts = 0;

    @BeforeMethod(alwaysRun = true)
    public void commonSetup()
    {
        new RestClient();
        env = System.getProperty("envname");
        geoName = System.getProperty("geoname");
        hostName = System.getProperty("hostname");
    }

    @AfterMethod(alwaysRun = true)
    public void getResult(ITestResult result)
    {
        endExtentReport();
    }

    @AfterSuite(alwaysRun = true)
    public void commonTearDown(ITestContext context)
    {
        long millis = 0;
        Map<String, ISuiteResult> suiteResults = context.getSuite().getResults();
        for(ISuiteResult sr : suiteResults.values())
        {
            context = sr.getTestContext();
            millis = ((context.getEndDate().getTime()) - (context.getStartDate().getTime()));
            totalScripts = totalScripts + ((context.getPassedTests().size()) + (context.getFailedTests().size()) + (context.getSkippedTests().size()));
        }
        suitExecutionTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        extent.setSystemInfo("Total_Scripts", +totalScripts + "");
        extent.setSystemInfo("Execution_Time", suitExecutionTime);
        extent.flush();
    }
}