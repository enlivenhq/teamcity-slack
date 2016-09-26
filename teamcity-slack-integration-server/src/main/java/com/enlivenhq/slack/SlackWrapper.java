package com.enlivenhq.slack;

import com.enlivenhq.teamcity.SlackNotificator;
import com.enlivenhq.teamcity.SlackPayload;
import jetbrains.buildServer.Build;
import jetbrains.buildServer.serverSide.TeamCityProperties;
import jetbrains.buildServer.web.util.WebUtil;
import net.gpedro.integrations.slack.SlackApi;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class SlackWrapper
{
    private static final Logger LOG = Logger.getLogger(SlackNotificator.class);

    private SlackApi slackClient;

    protected String slackUrl;

    protected String username;

    protected String channel;

    protected String serverUrl;

    protected Boolean useAttachment;

    public SlackWrapper () {
        this.useAttachment  = TeamCityProperties.getBooleanOrTrue("teamcity.notification.slack.useAttachment");
    }

    public SlackWrapper (Boolean useAttachment) {
        this.useAttachment = useAttachment;
    }

    public void send(String project, String build, String branch, String statusText, String statusColor, Build bt) {
        SlackPayload payload = getPayload(project, build, branch, statusText, statusColor, bt.getBuildTypeExternalId(), bt.getBuildId());
        LOG.debug(payload.toString());

        slackClient = new SlackApi(this.getSlackUrl());
        slackClient.call(payload.asMessage());
    }

    @NotNull
    public SlackPayload getPayload(String project, String build, String branch, String statusText, String statusColor, String btId, long buildId) {
        SlackPayload payload = new SlackPayload(project, build, branch, statusText, statusColor, btId, buildId, WebUtil.escapeUrlForQuotes(getServerUrl()));
        payload.setChannel(getChannel());
        payload.setUsername(getUsername());
        payload.setUseAttachments(this.useAttachment);

        return payload;
    }

    public void setSlackUrl(String slackUrl)
    {
        this.slackUrl = slackUrl;
    }

    public String getSlackUrl()
    {
        return this.slackUrl;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUsername()
    {
        return this.username;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    public String getChannel()
    {
        return this.channel;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
