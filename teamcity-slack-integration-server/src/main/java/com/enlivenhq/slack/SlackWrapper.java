package com.enlivenhq.slack;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class SlackWrapper
{
    protected String slackUrl;

    protected String username;

    protected String channel;

    public String send(String message)
    {
        String formattedPayload =
                "payload={ \"text\": \"" + message +
                "\", \"channel\": \"" + this.getChannel() +
                "\", \"username\": \"" + this.getUsername() + "\" }";


        try {
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

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(httpsURLConnection.getInputStream())
            );

            String line;
            String responseBody = "";

            while ((line = bufferedReader.readLine()) != null) {
                responseBody += line + "\n";
            }

            bufferedReader.close();
            return responseBody;
        }

        catch (Exception e) {
            return e.getMessage();
        }
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
