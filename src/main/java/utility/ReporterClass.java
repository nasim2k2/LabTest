package utility;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ReporterClass extends DriverTestCase{

    private static ExtentReports extent;
    private static ExtentTest test;
    private static ExtentHtmlReporter htmlReporter;
    private static String path = System.getProperty("user.dir");
    private static String reportPath = path + "/test-output/extent-report.html";
    public static PropertyReader propertyReader;

        ReporterClass() {
            if (extent == null) {
                propertyReader = new PropertyReader();
                htmlReporter = new ExtentHtmlReporter(reportPath);
                extent = new ExtentReports();
                extent.attachReporter(htmlReporter);
                extent.setSystemInfo("OS", propertyReader.readProperty("os"));
                extent.setSystemInfo("Browser", propertyReader.readProperty("browser"));
                extent.setSystemInfo("Environment", propertyReader.readProperty("env"));

                htmlReporter.config().setChartVisibilityOnOpen(true);
                htmlReporter.config().setDocumentTitle("AutomationTesting.in Demo Report");
                htmlReporter.config().setReportName("My Own Report");
                htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
                htmlReporter.config().setTheme(Theme.DARK);
            }
    }

    public static void createTest(String testcasename) {
        test = extent.createTest(testcasename);
    }

    //this method will mark test case as pass
    public void pass(String testcasename) throws IOException {
        test.pass(testcasename, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot(testcasename)).build());
//        Assert.assertTrue(true);
    }

    //this method will mark test case as fail
    public void fail(String testcasename) throws IOException {

        test.fail(testcasename, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot(testcasename)).build());
//        Assert.assertTrue(false);
    }

    //this method will mark test case as fail
    public void skip(String testcasename) throws IOException {

        test.skip(testcasename, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot(testcasename)).build());
    }

    public static void log(String msg) {
        test.log(Status.INFO, msg);
    }

    public static void endReporter() {
        extent.flush();
    }

    public static ExtentReports getInstance() {
        return extent;
    }

    //Creating file name
    public static String getFileName(String file) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String fileName = file + dateFormat.format(cal.getTime());
        return fileName;
    }

    public String Screenshot(String fileName) {
        String path = "";
        try {
            path = System.getProperty("user.dir");
            String screenshotName = getFileName(fileName);
            path = path + "//Screenshots//" + screenshotName + ".png";
            FileOutputStream out = new FileOutputStream(path);
            out.write(((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES));
            out.close();
        } catch (Exception e) {

        }
        return path;

    }
}
