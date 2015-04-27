package com.enlivenhq.slack;

import jetbrains.buildServer.web.util.WebUtil;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

public class SlackWrapper
{
    protected String slackUrl;

    protected String username;

    protected String channel;

    public String send(String project, String build, String statusText, String statusColor) throws IOException
    {
        project = WebUtil.encode(project);
        build = WebUtil.encode(build);
        statusText = WebUtil.encode(statusText);
        String payloadText = project + " #" + build + " " + statusText;
        String attachmentProject = "{\"title\":\"Project\",\"value\":\"" + project + "\",\"short\": false}";
        String attachmentBuild = "{\"title\":\"Build\",\"value\":\"" + build + "\",\"short\": true}";
        String attachmentStatus = "{\"title\":\"Status\",\"value\":\"" + statusText + "\",\"short\": false}";

        String formattedPayload = "payload={" +
            "\"text\":\"" + payloadText + "\"," +
            "\"attachments\": [{" +
                "\"fallback\":\"" + payloadText + "\"," +
                "\"pretext\":\"Build Status\"," +
                "\"color\":\"" + statusColor + "\"," +
                "\"fields\": [" +
                    attachmentProject + "," +
                    attachmentBuild + "," +
                    attachmentStatus +
                "]" +
            "}]," +
            "\"channel\":\"" + this.getChannel() + "\"," +
            "\"username\":\"" + this.getUsername() + "\"" +
        "}";

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
            responseBody = e.getMessage() + ": ";
            inputStream = httpsURLConnection.getErrorStream();
            throw new IOException(getResponseBody(inputStream, responseBody));
        }

        return getResponseBody(inputStream, responseBody);
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
}
