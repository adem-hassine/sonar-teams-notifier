package com.proxym.sonarteamsnotifier.constants;


/**
 * Constants to be used across the plugin.
 */
public class Constants {

    /**
     * The plugin's category.
     */
    public static final String CATEGORY = "Teams";
    /**
     * The plugin's metric list.
     */
    public static final String METRICS = "Metrics";
    /**
     * The name of the Post Condition property.
     */
    public static final String POST_CONDITIONS = "POST_CONDITIONS";
    /**
     * The name of the Show author property.
     */
    public static final String SHOW_AUTHOR = "show.author";
    /**
     * The plugin's sub category.
     */
    public static final String SUBCATEGORY = "Teams Notifier Plugin";
    /**
     * GET_MEASURE endpoint pointing to properties file.
     */
    public static final String MEASURES_ENDPOINT = "GET_MESURES";

    /**
     * The name of the enabled property.
     */
    public static final String ENABLED = "sonar.teams.enabled";
    /**
     * Metrics pointing to  properties file.
     */
    public static final String REPORTS_METRICS = "REPORTS_METRICS";
    /**
     * Separator.
     */
    public static final String COMMA = ",";

    /**
     * The name of the webhook property, supplied to sonar-scanner.
     */
    public static final String HOOK = "sonar.teams.hook";
    /**
     * The token value for sonar.
     */
    public static final String TOKEN = "sonar.login";
    /**
     * The projectId supplied to sonar-scanner.
     */
    public static final String PROJECT_ID = "sonar.analysis.projectId";
    /**
     * The server url supplied to sonar-scanner.
     */
    public static final String SERVER_URL = "sonar.host.url";
    /**
     * Bad quality gateway administration config choice .
     */
    public static final String BAD_QUALITY_GATEWAY = "Bad Quality Gateway";
    /**
     * Good quality gateway administration config choice .
     */
    public static final String GOOD_QUALITY_GATEWAY = "Good Quality Gateway";
    /**
     * Administration config for sending notification in both case .
     */
    public static final String ANYWAYS = "Both";
    public static final String GREEN_COLOR="008000";
    public static final String RED_COLOR="bc4749";


    private Constants() {
    }
}
