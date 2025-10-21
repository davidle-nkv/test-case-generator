package com.nakivo.testgen.utils;

/**
 * Description.
 *
 * Author: David Le
 * Date: 10/21/2025
 * Time: 11:18 AM
 */
public class GitHubUtil {

    public static String getGitHubPAT() {
        String pat = System.getProperty("GITHUB_PAT"); // via -D flag
        if (pat == null || pat.isEmpty()) {
            pat = System.getenv("GITHUB_PAT"); // fallback to environment variable
        }
        if (pat == null || pat.isEmpty()) {
            throw new IllegalStateException("GitHub PAT not provided!");
        }
        return pat;
    }


}
