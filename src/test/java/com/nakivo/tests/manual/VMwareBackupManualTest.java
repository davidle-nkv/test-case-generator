package com.nakivo.tests.manual;

import static com.nakivo.common.Constants.*;
import static com.nakivo.utils.selenium.WaitUtils.*;
import static com.nakivo.utils.extentreports.HtmlLogs.*;
import static com.nakivo.utils.others.DateTimeUtils.getCurrentTimeStamp;

import com.nakivo.anotations.FrameworkAnnotation;
import com.nakivo.drivers.ui.TestData;
import com.nakivo.enums.*;
import com.nakivo.pages.page.backup.vmwarevspherebackup.VMwareBackupPage;
import org.testng.annotations.Test;

public class VMwareBackupManualTest extends VMwareBackupPage {

    @FrameworkAnnotation(
      author = {AuthorType.NKV_USER},
      category = {CategoryType.DEBUG},
      date = "",
      features = {ProductType.VMWARE},
      product = {Product.CORE},
      status = {Status.DONE},
      service = {},
      repository = {Repository.ONBOARD},
      testcaseId = "TC_001")
    @Test(
      groups = {"DIET_DEBUG", "regression"},
      description = "TC_001 - Create and run VMware backup job to local repository successfully")
    public void TC_001() {
        try {
            testData = TestData.getTestData("DIET_DEBUG", "TC_001").get(0);

            reportInfo("Step 1: Create VMware Backup job");
            vmwareBackupPage().createBackupJob(testData);
            reportInfo("Step 2: Run job");
            dashboardPage().runJob(testData);
            reportInfo("Step 3: Wait for job to finish");
            dashboardPage().waitJobRunComplete(testData.getJobName(), DEFAULT_TIMEOUT);
            reportInfo("Step 4: Verify backup object");
            vmwareBackupPage().validateCreatedBackupJob(testData);
            reportInfo("Step 5: Remove the job");
            dashboardPage().removeJob(testData.getJobName(), testData.getDeleteMode());

            reportPassed("Test case passed: Create and run VMware backup job to local repository successfully");
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
      testcaseId = "TC_002")
    @Test(
      groups = {"DIET_DEBUG", "regression"},
      description = "TC_002 - Create and run VMware backup job with LAN mode to local repository successfully")
    public void TC_002() {
        try {
            testData = TestData.getTestData("DIET_DEBUG", "TC_002").get(0);

            reportInfo("Step 1: Create VMware Backup job");
            vmwareBackupPage().createBackupJob(testData);
            reportInfo("Step 2: Run job");
            dashboardPage().runJob(testData);
            reportInfo("Step 3: Wait for job to finish");
            dashboardPage().waitJobRunComplete(testData.getJobName(), DEFAULT_TIMEOUT);
            reportInfo("Step 4: Verify backup object");
            vmwareBackupPage().validateCreatedBackupJob(testData);
            reportInfo("Step 5: Remove the job");
            dashboardPage().removeJob(testData.getJobName(), testData.getDeleteMode());

            reportPassed("Test case passed: Create and run VMware backup job with LAN mode to local repository successfully");
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
      testcaseId = "TC_003")
    @Test(
      groups = {"DIET_DEBUG", "regression"},
      description = "TC_003 - Create and run VMware backup job with HotAdd mode to local repository successfully")
    public void TC_003() {
        try {
            testData = TestData.getTestData("DIET_DEBUG", "TC_003").get(0);

            reportInfo("Step 1: Create VMware Backup job");
            vmwareBackupPage().createBackupJob(testData);
            reportInfo("Step 2: Run job");
            dashboardPage().runJob(testData);
            reportInfo("Step 3: Wait for job to finish");
            dashboardPage().waitJobRunComplete(testData.getJobName(), DEFAULT_TIMEOUT);
            reportInfo("Step 4: Verify backup object");
            vmwareBackupPage().validateCreatedBackupJob(testData);
            reportInfo("Step 5: Remove the job");
            dashboardPage().removeJob(testData.getJobName(), testData.getDeleteMode());

            reportPassed("Test case passed: Create and run VMware backup job with HotAdd mode to local repository successfully");
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
      testcaseId = "TC_004")
    @Test(
      groups = {"DIET_DEBUG", "regression"},
      description = "TC_004 - Create and run VMware backup Mapping job to local repository successfully")
    public void TC_004() {
        try {
            testData = TestData.getTestData("DIET_DEBUG", "TC_004").get(0);

            reportInfo("Step 1:  Remove recovery point from repo");
            vmwareBackupPage().apiRemoveBackupObject(testData.getRepoName());
            reportInfo("Step 2: Create VMware Backup job");
            vmwareBackupPage().createBackupJob(testData);
            reportInfo("Step 3: Run job");
            dashboardPage().runJob(testData);
            reportInfo("Step 4: Wait for job to finish");
            dashboardPage().waitJobRunComplete(testData.getJobName(), DEFAULT_TIMEOUT);
            reportInfo("Step 5: Verify backup object");
            vmwareBackupPage().validateCreatedBackupJob(testData);
            reportInfo("Step 6: Remove backup keep recovery point");
            dashboardPage().removeJob(testData.getJobName(), KEEP_BACKUP);
            reportInfo("Step 7:  create backup to existing backup");
            vmwareBackupPage().createExistBackupJobs(testData);
            reportInfo("Step 8: Run job");
            dashboardPage().runJob(testData);
            reportInfo("Step 9: Wait for job to finish");
            dashboardPage().waitJobRunComplete(testData.getJobName(), DEFAULT_TIMEOUT);
            reportInfo("Step 10: Validate backup exist");
            vmwareBackupPage().validateCreatedExistBackupJob(testData, 1);
            reportInfo("Step 11: Remove the job");
            dashboardPage().removeJob(testData.getJobName(), testData.getDeleteMode());

            reportPassed("Test case passed: Create and run VMware backup Mapping job to local repository successfully");
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
      testcaseId = "TC_009")
    @Test(
      groups = {"DIET_DEBUG", "regression"},
      description = "TC_009 - Create and run VMware Incremental backup job to local repository successfully")
    public void TC_009() {
        try {
            testData = TestData.getTestData("DIET_DEBUG", "TC_009").get(0);

            reportInfo("Step 1: Create VMware Backup job");
            vmwareBackupPage().createBackupJob(testData);
            reportInfo("Step 2: Run job");
            dashboardPage().runJob(testData);
            reportInfo("Step 3: Wait for job to finish");
            dashboardPage().waitJobRunComplete(testData.getJobName(), DEFAULT_TIMEOUT);
            reportInfo("Step 4: Verify backup object");
            vmwareBackupPage().validateCreatedBackupJob(testData);
            reportInfo("Step 5: Run job");
            dashboardPage().runJob(testData);
            reportInfo("Step 6: Wait for job to finish");
            dashboardPage().waitJobRunComplete(testData.getJobName(), DEFAULT_TIMEOUT);
            reportInfo("Step 7: Remove the job");
            dashboardPage().removeJob(testData.getJobName(), testData.getDeleteMode());

            reportPassed("Test case passed: Create and run VMware Incremental backup job to local repository successfully");
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
      testcaseId = "TC_010")
    @Test(
      groups = {"DIET_DEBUG", "regression"},
      description = "TC_010 - Create and run VMware backup job multiple VM to local repository successfully")
    public void TC_010() {
        try {
            testData = TestData.getTestData("DIET_DEBUG", "TC_010").get(0);

            reportInfo("Step 1: Create VMware Backup job");
            vmwareBackupPage().createBackupJob(testData);
            reportInfo("Step 2: Run job");
            dashboardPage().runJob(testData);
            reportInfo("Step 3: Wait for job to finish");
            dashboardPage().waitJobRunComplete(testData.getJobName(), DEFAULT_TIMEOUT);
            reportInfo("Step 4: Verify backup object");
            vmwareBackupPage().validateCreatedBackupJob(testData);
            reportInfo("Step 5: Run job");
            dashboardPage().runJob(testData);
            reportInfo("Step 6: Wait for job to finish");
            dashboardPage().waitJobRunComplete(testData.getJobName(), DEFAULT_TIMEOUT);
            reportInfo("Step 7: Remove the job");
            dashboardPage().removeJob(testData.getJobName(), testData.getDeleteMode());

            reportPassed("Test case passed: Create and run VMware backup job multiple VM to local repository successfully");
        } catch (Exception e) {
            reportFail("Test case failed: " + e.getMessage());
        }
    }

}
