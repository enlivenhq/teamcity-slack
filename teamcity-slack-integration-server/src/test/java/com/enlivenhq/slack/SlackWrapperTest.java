package com.enlivenhq.slack;

import com.google.gson.Gson;

import java.util.List;

import static org.testng.Assert.*;

public class SlackWrapperTest {

    class Payload {
        public String text;
        public String channel;
        public String username;
        public List<Attachment> attachments;
    }

    class Attachment {
        public String fallback;
        public String pretext;
        public String color;
        public List<AttachmentField> fields;
    }

    class AttachmentField {
        public String title;
        public String value;
        public boolean isShort;
    }

    @org.testng.annotations.Test
    public void testGetFormattedPayload() throws Exception {
        String project = "project";
        String build = "build";
        String statusText = "status";
        String statusColor = "color";
        String btId = "btId";
        long buildId = 0;
        SlackWrapper slack = new SlackWrapper();;

        String payload;

        payload = slack.getFormattedPayload(project, build, statusText, statusColor, btId, buildId);

        assertTrue(checkPayload(payload), "Payload is valid JSON");
    }

    @org.testng.annotations.Test
    public void testPayloadForEmptyInput() throws Exception {
        SlackWrapper slack = new SlackWrapper();
        assertTrue(checkPayload(slack.getFormattedPayload("", "", "", "", "", 0)));
    }

    @org.testng.annotations.Test
    public void testPayloadForQuotesInInput() throws Exception {
        SlackWrapper slack = new SlackWrapper();
        assertTrue(!checkPayload(slack.getFormattedPayload("'\"\\", "'\"\\", "'\"\\", "'\"\\", "'\"\\", 0)));
    }

    private boolean checkPayload(String payload) {
        Gson gson = new Gson();
        try {
            gson.fromJson(payload, Payload.class);
            return true;
        } catch(com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }
}