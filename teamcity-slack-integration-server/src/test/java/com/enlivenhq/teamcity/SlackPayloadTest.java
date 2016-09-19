package com.enlivenhq.teamcity;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SlackPayloadTest {

    String project = "project<http://example.com|lol>";
    String build = "build<http://example.com|lol>";
    String branch = "<http://example.com|lol>";
    String statusText = "<status>";
    String statusTextMultiline = "<status\n>";
    String statusColor = "color";
    String btId = "btId<http://example.com|lol>";
    long buildId = 0;
    String serverUrl = "localhost";
    String channel = "#channel";
    String username = "bot";
    SlackPayload slackPayload;

    private String escape(String s) {
        return s
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    @AfterMethod
    public void tearDown() throws Exception {
        slackPayload = null;
    }

    @Test
    public void testSlackPayloadDoesNotRequiresUserAndChannel() {
        slackPayload = new SlackPayload(project, build, branch, statusText, statusColor, btId, buildId, serverUrl);
        assertFalse(slackPayload == null);
    }

    @Test
    public void testSlackPayloadWithoutAttachment() {
        slackPayload = new SlackPayload(project, build, branch, statusText, statusColor, btId, buildId, serverUrl);
        slackPayload.setUseAttachments(false);
        assertFalse(slackPayload.hasAttachments());
    }

    @Test
    public void testSlackPayloadUsesAttachmentByDefault() {
        slackPayload = new SlackPayload(project, build, branch, statusText, statusColor, btId, buildId, serverUrl);
        assertTrue(slackPayload.hasAttachments());
    }

    @Test
    public void testSlackPayloadIsUpdatedWithUsername() {
        slackPayload = new SlackPayload(project, build, branch, statusText, statusColor, btId, buildId, serverUrl);
        slackPayload.setUseAttachments(false);
        slackPayload.setUsername(username);
        assertTrue(slackPayload.getUsername().equals(username));
    }

    @org.testng.annotations.Test
    public void testSlackPayloadIsUpdatedWithChannel() {
        slackPayload = new SlackPayload(project, build, branch, statusText, statusColor, btId, buildId, serverUrl);
        slackPayload.setUseAttachments(false);
        slackPayload.setChannel(channel);
        assertTrue(slackPayload.getChannel().equals(channel));
    }

    @Test
    public void testSlackPayloadProjectEscapeLtGt() {
        slackPayload = new SlackPayload(project, build, branch, statusText, statusColor, btId, buildId, serverUrl);
        assertFalse(slackPayload.getText().contains(project));
        assertTrue(slackPayload.getText().contains(escape(project)));
    }

    @Test
    public void testSlackPayloadBuildEscapeLtGt() {
        slackPayload = new SlackPayload(project, build, branch, statusText, statusColor, btId, buildId, serverUrl);
        assertFalse(slackPayload.getText().contains(build));
        assertTrue(slackPayload.getText().contains(escape(build)));
    }

    @Test
    public void testSlackPayloadBranchEscapeLtGt() {
        slackPayload = new SlackPayload(project, build, branch, statusText, statusColor, btId, buildId, serverUrl);
        assertFalse(slackPayload.getText().contains(branch));
        assertTrue(slackPayload.getText().contains(escape(branch)));
    }

    @Test
    public void testSlackPayloadStatusTextEscapeLtGt() {
        slackPayload = new SlackPayload(project, build, branch, statusText, statusColor, btId, buildId, serverUrl);
        assertFalse(slackPayload.getText().contains(statusText));
        assertTrue(slackPayload.getText().contains(escape(statusText)));
    }

    @Test
    public void testSlackPayloadBtIdEscapeLtGt() {
        slackPayload = new SlackPayload(project, build, branch, statusText, statusColor, btId, buildId, serverUrl);
        assertFalse(slackPayload.getText().contains(btId));
        assertTrue(slackPayload.getText().contains(escape(btId)));
    }

    @Test
    public void testSlackPayloadStatusEscapeNewline() {
        slackPayload = new SlackPayload(project, build, branch, statusTextMultiline, statusColor, btId, buildId, serverUrl);
        assertFalse(slackPayload.getText().contains("\n"));
        assertTrue(slackPayload.getText().contains("\\n"));
    }

}