package com.proxym.sonarteamsnotifier.extension;

import com.proxym.sonarteamsnotifier.constants.Constants;

import java.util.Map;
import java.util.Optional;

import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.ce.posttask.QualityGate;
import org.sonar.api.config.Configuration;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * Post Project Analysis Task that sends the WebEx Teams notification message.
 */
public class TeamsPostProjectAnalysisTask implements PostProjectAnalysisTask {

    /**
     * Logger.
     */
    private static final Logger LOG = Loggers.get(TeamsPostProjectAnalysisTask.class);

    /**
     * SonarQube settings.
     */
    private final Configuration settings;

    /**
     * Constructor.
     *
     * @param settings The SonarQube Configuration settings.
     */
    public TeamsPostProjectAnalysisTask(Configuration settings) {
        this.settings = settings;
    }

    /**
     * Post analysis task.
     */
    @Override
    public void finished(final Context context) {
        boolean showAuthor = settings.getBoolean(Constants.SHOW_AUTHOR).orElse(false);
        if (!isPluginEnabled()) {
            LOG.info("Teams Notifier Plugin disabled.");
            return;
        }
        if (!validPostCondition(context)) {
            LOG.info("Teams notifier post conditions does not match");
            return;
        }
        Map<String, String> properties = context.getProjectAnalysis().getScannerContext().getProperties();
        if (!properties.containsKey(Constants.HOOK)) {
            LOG.info("No hook URL found for Teams Notifier Plugin.");
            return;
        }
        if (showAuthor && !properties.containsKey(Constants.SHOW_AUTHOR)) {
            LOG.info("No author were provided by scanner side");
            return;
        }
        if (!properties.containsKey(Constants.TOKEN)) {
            LOG.info("No token found for Teams Notifier Plugin");
            return;
        }
        if (!properties.containsKey(Constants.PROJECT_ID)) {
            LOG.info("No project id found for teams notifier plugin");
            return;
        }
        if (!properties.containsKey(Constants.SERVER_URL)) {
            LOG.info("No server url found for teams notifier plugin");
            return;
        }
        Optional<String> authorName = showAuthor ? settings.get(Constants.SHOW_AUTHOR) : Optional.empty();
        String projectUrl = properties.get(Constants.SERVER_URL);
        String hook = properties.get(Constants.HOOK);
        String token = properties.get(Constants.TOKEN);
        String projectId = properties.get(Constants.PROJECT_ID);
        LOG.debug("Analysis ScannerContext: [{}]", properties);
        LOG.debug("Teams notification URL: " + hook);
        LOG.debug("Teams notification analysis: " + context);
        sendNotification(hook, context, token, projectId, projectUrl,authorName);
    }

    /**
     * Checks if the quality gate status is set and is OK.
     *
     * @return True if quality gate is set and is OK. False if not.
     */
    private boolean qualityGateOk(Context context) {
        QualityGate qualityGate = context.getProjectAnalysis().getQualityGate();
        return (qualityGate != null && QualityGate.Status.OK.equals(qualityGate.getStatus()));
    }

    /**
     * Sends the WebEx teams notification.
     *
     * @param hook The hook URL.
     */
    private void sendNotification(String hook, Context context, String token, String projectId, String serverUrl,Optional<String> authorName) {
        try {
            ProjectAnalysis analysis = context.getProjectAnalysis();
            TeamsHttpClient httpClient = TeamsHttpClient
                    .of(hook, PayloadBuilder.of(analysis,
                                    serverUrl, qualityGateOk(context),
                                    token, projectId, settings.getStringArray(Constants.REPORTS_METRICS),
                                    authorName)
                            .build())
                    .build();
            if (httpClient.post()) {
                LOG.info("Teams message posted");
            } else {
                LOG.error("Teams message failed");
            }
        } catch (Exception e) {
            LOG.error("Failed to send teams message", e);
        }
    }


    private boolean isPluginEnabled() {
        return settings.getBoolean(Constants.ENABLED).orElse(false);
    }

    private boolean validPostCondition(Context context) {
        String postWhen = settings.get(Constants.POST_CONDITIONS).orElse(Constants.ANYWAYS);
        switch (postWhen) {
            case Constants.BAD_QUALITY_GATEWAY:
                return !qualityGateOk(context);
            case Constants.GOOD_QUALITY_GATEWAY:
                return qualityGateOk(context);
            default:
                return true;
        }
    }

}
