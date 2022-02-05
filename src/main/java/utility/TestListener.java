package utility;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestListener implements ITestListener {

    static ReporterClass reporterClass = new ReporterClass();
    public HashMap<String, String> testResults = new HashMap<String, String>();
    PropertyReader propertyReader = new PropertyReader();

    public void onStart(ITestContext context) {
        System.out.println("*** Test Suite " + context.getName() + " started ***");
    }

    public void onFinish(ITestContext context) {
        System.out.println(("*** Test Suite " + context.getName() + " ending ***"));
        reporterClass.endReporter();
        reporterClass.getInstance().flush();
        integrateWithTestrail();
    }

    public void onTestStart(ITestResult result) {
        System.out.println(("*** Running test method " + result.getMethod().getMethodName() + "..."));
        reporterClass.createTest(result.getMethod().getMethodName());
    }

    public void onTestSuccess(ITestResult result) {
        System.out.println("*** Executed " + result.getMethod().getMethodName() + " test successfully...");
        try {
            reporterClass.pass(result.getMethod().getMethodName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        reporterClass.log("Test passed");
        addResultToMap(result.getMethod().getDescription(), "Passed");
    }

    public void onTestFailure(ITestResult result) {
        System.out.println("*** Test execution " + result.getMethod().getMethodName() + " failed...");
        try {
            reporterClass.fail(result.getMethod().getMethodName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        reporterClass.log("Test Failed");
        addResultToMap(result.getMethod().getDescription(), "Failed");
    }

    public void onTestSkipped(ITestResult result) {
        System.out.println("*** Test " + result.getMethod().getMethodName() + " skipped...");
        try {
            reporterClass.skip(result.getMethod().getMethodName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        reporterClass.log("Test Skipped");
        addResultToMap(result.getMethod().getDescription(), "Skipped");
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("*** Test failed but within percentage % " + result.getMethod().getMethodName());
    }

    public void addResultToMap(String arg, String result){
        try {
            if (arg.contains(",")) {
                String st = arg.replace(" ", "");
                String[] arr = st.split(",");
                for (int i = 0; i < arr.length; i++) {
                    testResults.put(arr[i], result);
                    try {
                        propertyReader.writeResults(arr[i], result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                String st = arg.replace(" ", "");
                testResults.put(st, result);
                try {
                    propertyReader.writeResults(st, result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (NullPointerException e){

        }
    }

    private void integrateWithTestrail() {
        String flag = propertyReader.readProperty("testrailIntegration");
        if(flag.equalsIgnoreCase("yes")){
            try{
                APIClient ap = new APIClient("https://arexdata.testrail.io/");
                ap.setUser("agonzalez@arexdata.com");
                ap.setPassword("Arexdata1!");
                Map data = new HashMap();
                for(Map.Entry m:testResults.entrySet()){
                    if(m.getValue().equals("Passed")){
                        data.put("status_id", 1);
                        data.put("comment", "Status updated automatically from Selenium test automation.");
                        ap.sendPost("add_result/"+m.getKey(), data);
                    }
                    else if (m.getValue().equals("Failed")){
                        data.put("status_id", 5);
                        data.put("comment", "Status updated automatically from Selenium test automation.");
                        ap.sendPost("add_result/"+m.getKey(), data);
                    }
                }
            }
            catch (Exception e){

            }
        }
        else if(flag.equalsIgnoreCase("no")){

        }
        else {}

    }
}
