package com.nakivo.tests.manual;

import static com.nakivo.common.Constants.*;
import static com.nakivo.utils.selenium.WaitUtils.*;
import static com.nakivo.utils.extentreports.HtmlLogs.*;
import static com.nakivo.utils.others.DateTimeUtils.getCurrentTimeStamp;

import com.nakivo.anotations.FrameworkAnnotation;
import com.nakivo.drivers.ui.TestData;
import com.nakivo.enums.*;
import com.nakivo.pages.page.backup.vmwarevspherebackup.VMwareRecoveryPage;
import org.testng.annotations.Test;

public class VMwareRecoveryManualTest extends VMwareRecoveryPage {

    @FrameworkAnnotation(
      author = {AuthorType.NKV_USER},
      category = {CategoryType.DEBUG},
      date = "",
      features = {ProductType.VMWARE},
      product = {Product.CORE},
      status = {Status.DONE},
      service = {},
      repository = {Repository.ONBOARD},
      testcaseId = "TC_007")
    @Test(
      groups = {"DIET_DEBUG", "regression"},
      description = "TC_007 - Create and run VMware recovery job to the same host successfully")
    public void TC_007() {
        try {
            testData = TestData.getTestData("DIET_DEBUG", "TC_007").get(0);

            reportInfo("Step 1: Create VMware Backup job");
            vmwareBackupPage().createBackupJob(testData);
            reportInfo("Step 2: Run job");
            dashboardPage().runJob(testData);
            reportInfo("Step 3: Wait for job to finish");
            dashboardPage().waitJobRunComplete(testData.getJobName(), DEFAULT_TIMEOUT);
            reportInfo("Step 4: Verify backup object");
            vmwareBackupPage().validateCreatedBackupJob(testData);
            reportInfo("Step 5: Create recovery job");
            vmwareRecoveryPage.createRecoveryJobs(testData, platform);
            reportInfo("Step 6: Run recovery job");
            dashboardPage().runJob(testData.getRecoverJobName(), testData.getJobScope());
            reportInfo("Step 7: Remove the job");
            dashboardPage().removeJob(testData.getJobName(), testData.getDeleteMode());

            reportPassed("Test case passed: Create and run VMware recovery job to the same host successfully");
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
      testcaseId = "TC_008")
    @Test(
      groups = {"DIET_DEBUG", "regression"},
      description = "TC_008 - Create and run VMware recovery job to different host successfully")
    public void TC_008() {
        try {
            testData = TestData.getTestData("DIET_DEBUG", "TC_008").get(0);

            reportInfo("Step 1: Create VMware Backup job");
            vmwareBackupPage().createBackupJob(testData);
            reportInfo("Step 2: Run job");
            dashboardPage().runJob(testData);
            reportInfo("Step 3: Wait for job to finish");
            dashboardPage().waitJobRunComplete(testData.getJobName(), DEFAULT_TIMEOUT);
            reportInfo("Step 4: Verify backup object");
            vmwareBackupPage().validateCreatedBackupJob(testData);
            reportInfo("Step 5: Create recovery job");
            vmwareRecoveryPage.createRecoveryJobs(testData, platform);
            reportInfo("Step 6: Run recovery job");
            dashboardPage().runJob(testData.getRecoverJobName(), testData.getJobScope());
            reportInfo("Step 7: Remove the job");
            dashboardPage().removeJob(testData.getJobName(), testData.getDeleteMode());

            reportPassed("Test case passed: Create and run VMware recovery job to different host successfully");
        } catch (Exception e) {
            reportFail("Test case failed: " + e.getMessage());
        }
    }

}
