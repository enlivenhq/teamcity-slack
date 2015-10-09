package com.enlivenhq.slack;

import jetbrains.buildServer.Build;
import jetbrains.buildServer.serverSide.TeamCityProperties;
import jetbrains.buildServer.web.util.WebUtil;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

public class SlackWrapper
{
    protected String slackUrl;

    protected String username;

    protected String channel;

    protected String serverUrl;

    public String send(String project, String build, String statusText, String statusColor, Build bt) throws IOException
    {
        String formattedPayload = getFormattedPayload(project, build, statusText, statusColor, bt.getBuildTypeExternalId(), bt.getBuildId());

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
    public String getFormattedPayload(String project, String build, String statusText, String statusColor, String btId, long buildId) {

        project = WebUtil.escapeForJavaScript(project, false, false);
        build = WebUtil.escapeForJavaScript(build, false, false);
        statusText = "<" + WebUtil.escapeUrlForQuotes(getServerUrl()) + "/viewLog.html?buildId=" + buildId + "&buildTypeId=" + btId + "|" + statusText + ">";
        String payloadText = project + " #" + build + " " + statusText;
        String attachmentProject = "{\"title\":\"Project\",\"value\":\"" + project + "\",\"short\": false}";
        String attachmentBuild = "{\"title\":\"Build\",\"value\":\"" + build + "\",\"short\": true}";
        String attachmentStatus = "{\"title\":\"Status\",\"value\":\"" + statusText + "\",\"short\": false}";
        String attachment = "";

        if (TeamCityProperties.getBooleanOrTrue("teamcity.notification.slack.useAttachment")) {
            attachment = "\"attachments\": [" + "{" +
                "\"fallback\":\"" + payloadText + "\"," +
                "\"pretext\":\"Build Status\"," +
                "\"color\":\"" + statusColor + "\"," +
                "\"fields\": [" +
                    attachmentProject + "," +
                    attachmentBuild + "," +
                    attachmentStatus +
                "]" +
            "}" + "],";
        }

        return "{" +
            "\"text\":\"" + payloadText + "\"," +
            attachment +
            "\"channel\":\"" + this.getChannel() + "\"," +
            "\"username\":\"" + this.getUsername() + "\"" +
        "}";
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
