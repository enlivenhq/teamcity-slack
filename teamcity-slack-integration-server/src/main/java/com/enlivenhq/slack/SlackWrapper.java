package com.enlivenhq.slack;

import com.enlivenhq.teamcity.SlackNotificator;
import com.enlivenhq.teamcity.SlackPayload;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jetbrains.buildServer.Build;
import jetbrains.buildServer.serverSide.TeamCityProperties;
import jetbrains.buildServer.web.util.WebUtil;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

public class SlackWrapper
{
    public static final GsonBuilder GSON_BUILDER = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
    private static final Logger LOG = Logger.getLogger(SlackNotificator.class);
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

    public String send(String project, String build, String branch, String statusText, String statusColor, Build bt) throws IOException
    {
        String formattedPayload = getFormattedPayload(project, build, branch, statusText, statusColor, bt.getBuildTypeExternalId(), bt.getBuildId());
        LOG.debug(formattedPayload);

        URL url = new URL(this.getSlackUrl());
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

        httpsURLConnection.setRequestMethod("POST");
        httpsURLConnection.setRequestProperty("User-Agent", "Enliven");
        httpsURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        httpsURLConnection.setDoOutput(true);

        DataOutputStream dataOutputStream = new DataOutputStream(
            httpsURLConnection.getOutputStream()
        );

        dataOutputStream.writeBytes(formattedPayload);
        dataOutputStream.flush();
        dataOutputStream.close();

        InputStream inputStream;
        String responseBody = "";

        try {
            inputStream = httpsURLConnection.getInputStream();
        }
        catch (IOException e) {
            responseBody = e.getMessage();
            inputStream = httpsURLConnection.getErrorStream();
            if (inputStream != null) {
                responseBody += ": ";
                responseBody = getResponseBody(inputStream, responseBody);
            }
            throw new IOException(responseBody);
        }

        return getResponseBody(inputStream, responseBody);
    }

    @NotNull
    public String getFormattedPayload(String project, String build, String branch, String statusText, String statusColor, String btId, long buildId) {
        Gson gson = GSON_BUILDER.create();

        SlackPayload slackPayload = new SlackPayload(project, build, branch, statusText, statusColor, btId, buildId, WebUtil.escapeUrlForQuotes(getServerUrl()));
        slackPayload.setChannel(getChannel());
        slackPayload.setUsername(getUsername());
        slackPayload.setUseAttachments(this.useAttachment);

        return gson.toJson(slackPayload);
    }

    private String getResponseBody(InputStream inputStream, String responseBody) throws IOException {
        String line;

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream)
        );

        while ((line = bufferedReader.readLine()) != null) {
            responseBody += line + "\n";
        }

        bufferedReader.close();
        return responseBody;
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
