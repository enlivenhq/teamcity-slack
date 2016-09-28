package com.enlivenhq.slack;

import com.enlivenhq.teamcity.MessageFactory;
import com.enlivenhq.teamcity.SlackNotificator;
import jetbrains.buildServer.Build;
import jetbrains.buildServer.serverSide.TeamCityProperties;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;
import org.apache.log4j.Logger;

public class SlackWrapper {
    private static final Logger LOG = Logger.getLogger(SlackNotificator.class);

    private String slackUrl;

    private String username;

    private String channel;

    private String serverUrl;

    Boolean useAttachment;

    public SlackWrapper () {
        this.useAttachment  = TeamCityProperties.getBooleanOrTrue("teamcity.notification.slack.useAttachment");
    }

    public SlackWrapper (Boolean useAttachment) {
        this.useAttachment = useAttachment;
    }

    public void send(String project, String build, String branch, String statusText, String statusColor, Build bt) {
        SlackMessage message = MessageFactory.createBuildStatusMessage(
                getChannel(), getUsername(), project, build, branch, statusText, statusColor,
                bt.getBuildTypeExternalId(), bt.getBuildId(), getServerUrl(), useAttachment);
        LOG.debug(message.toString());

        SlackApi slackClient = new SlackApi(getSlackUrl());
        slackClient.call(message);
    }

    public void setSlackUrl(String slackUrl) {
        this.slackUrl = slackUrl;
    }

    private String getSlackUrl() {
        return this.slackUrl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String getUsername() {
        return this.username;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    private String getChannel() {
        return this.channel;
    }

    private String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
