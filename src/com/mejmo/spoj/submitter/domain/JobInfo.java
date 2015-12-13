package com.mejmo.spoj.submitter.domain;

/**
 * Class holding information about the currently submitted job
 *
 * @author Martin Formanko 2015
 */
public class JobInfo {
    private String username;
    private String password;
    private String solution;
    private ProblemInfo problem;
    private LanguageInfo language;

    public JobInfo() {

    }

    public JobInfo(String username, String password, String solution, ProblemInfo problem, LanguageInfo language) {
        this.username = username;
        this.password = password;
        this.solution = solution;
        this.problem = problem;
        this.language = language;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public ProblemInfo getProblem() {
        return problem;
    }

    public void setProblem(ProblemInfo problem) {
        this.problem = problem;
    }

    public LanguageInfo getLanguage() {
        return language;
    }

    public void setLanguage(LanguageInfo language) {
        this.language = language;
    }
}
