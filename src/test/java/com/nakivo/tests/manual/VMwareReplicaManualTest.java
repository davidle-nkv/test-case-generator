package com.nakivo.tests.manual;

import static com.nakivo.common.Constants.*;
import static com.nakivo.utils.selenium.WaitUtils.*;
import static com.nakivo.utils.extentreports.HtmlLogs.*;
import static com.nakivo.utils.others.DateTimeUtils.getCurrentTimeStamp;

import com.nakivo.anotations.FrameworkAnnotation;
import com.nakivo.drivers.ui.TestData;
import com.nakivo.enums.*;
import com.nakivo.pages.page.backup.vmwarevspherebackup.VMwareReplicaPage;
import org.testng.annotations.Test;

public class VMwareReplicaManualTest extends VMwareReplicaPage {

    @FrameworkAnnotation(
      author = {AuthorType.NKV_USER},
      category = {CategoryType.DEBUG},
      date = "",
      features = {ProductType.VMWARE},
      product = {Product.CORE},
      status = {Status.DONE},
      service = {},
      repository = {Repository.ONBOARD},
      testcaseId = "TC_005")
    @Test(
      groups = {"DIET_DEBUG", "regression"},
      description = "TC_005 - Create and run VMware replica job to the same host successfully")
    public void TC_005() {
        try {
            testData = TestData.getTestData("DIET_DEBUG", "TC_005").get(0);

            reportInfo("Step 1: Create VMware Replica job");
            vmwareReplicationPage.createReplicationJobs(testData);
            reportInfo("Step 2: Run job");
            dashboardPage().runJob(testData);
            reportInfo("Step 3: Wait for job to finish");
            dashboardPage().waitJobRunComplete(testData.getJobName(), DEFAULT_TIMEOUT);
            reportInfo("Step 4: verify replication job");
            vmwareReplicationPage.validateReplicationJobs();
            reportInfo("Step 5: Remove the job");
            dashboardPage().removeJob(testData.getJobName(), testData.getDeleteMode());

            reportPassed("Test case passed: Create and run VMware replica job to the same host successfully");
        } catch (Exception e) {
            reportFail("Test case failed: " + e.getMessage());
        }
    }


@FrameworkAnnotation(
      author = {AuthorType.NKV_USER},
      category = {CategoryType.DEBUG},
      date = "",
      features = {ProductType.VMWARE},
      product = {Product.CORE},
      status = {Status.DONE},
      service = {},
      repository = {Repository.ONBOARD},
      testcaseId = "TC_006")
    @Test(
      groups = {"DIET_DEBUG", "regression"},
      description = "TC_006 - Create and run VMware replica job to the different host successfully")
    public void TC_006() {
        try {
            testData = TestData.getTestData("DIET_DEBUG", "TC_006").get(0);

            reportInfo("Step 1: Create VMware Replica job");
            vmwareReplicationPage.createReplicationJobs(testData);
            reportInfo("Step 2: Run job");
            dashboardPage().runJob(testData);
            reportInfo("Step 3: Wait for job to finish");
            dashboardPage().waitJobRunComplete(testData.getJobName(), DEFAULT_TIMEOUT);
            reportInfo("Step 4: Verify replica job");
            vmwareReplicationPage.validateReplicationJobs();
            reportInfo("Step 5: Remove the job");
            dashboardPage().removeJob(testData.getJobName(), testData.getDeleteMode());

            reportPassed("Test case passed: Create and run VMware replica job to the different host successfully");
        } catch (Exception e) {
            reportFail("Test case failed: " + e.getMessage());
        }
    }

}
