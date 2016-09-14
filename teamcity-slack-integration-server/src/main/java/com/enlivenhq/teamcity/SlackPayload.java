package com.enlivenhq.teamcity;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class SlackPayload {
    @Expose
    protected String text;
    @Expose
    protected String channel;
    @Expose
    protected String username;
    @Expose
    protected List<Attachment> attachments;
    private List<Attachment> _attachments;

    public String getText() {
        return text;
    }

    private class Attachment {
        @Expose
        protected String fallback;
        @Expose
        protected String pretext;
        @Expose
        protected String color;
        @Expose
        protected List<AttachmentField> fields;
    }

    private class AttachmentField {
        public AttachmentField (String name, String val, boolean isShort) {
            title = name;
            value = val;
            this.isShort = isShort;
        }
        @Expose
        protected String title;
        @Expose
        protected String value;
        @Expose
        protected boolean isShort;
    }

    private boolean useAttachments = true;

    private String escape(String s) {
        return s
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    private String escapeNewline(String s) {
        return s.replace("\n", "\\n");
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUseAttachments (boolean useAttachments) {
        this.useAttachments = useAttachments;
        if (!useAttachments) {
            _attachments = attachments;
            attachments = null;
        } else {
            attachments = _attachments;
        }
    }

    public boolean hasAttachments () {
        return attachments != null && attachments.size() > 0;
    }

    public SlackPayload(String project, String build, String branch, String statusText, String statusColor, String btId, long buildId, String serverUrl) {
        project = escape(project);
        build = escape(build);
        branch = escape(branch);
        statusText = escape(escapeNewline(statusText));
        btId = escape(btId);

        String escapedBranch = branch.length() > 0 ? " [" + branch + "]" : "";

        statusText = "<" + serverUrl + "/viewLog.html?buildId=" + buildId + "&buildTypeId=" + btId + "|" + statusText + ">";

        String statusEmoji = statusColor.equals("danger") ? ":x: " : statusColor.equals("warning") ? "" : ":white_check_mark: ";

        String payloadText = statusEmoji + project + escapedBranch + " #" + build + " " + statusText;
        this.text = payloadText;

        Attachment attachment = new Attachment();
        attachment.color = statusColor;
        attachment.pretext = "Build Status";
        attachment.fallback = payloadText;
        attachment.fields = new ArrayList<AttachmentField>();

        AttachmentField attachmentProject = new AttachmentField("Project", project, false);
        AttachmentField attachmentBuild = new AttachmentField("Build", build, true);
        AttachmentField attachmentStatus = new AttachmentField("Status", statusText, false);
        AttachmentField attachmentBranch;

        attachment.fields.add(attachmentProject);
        attachment.fields.add(attachmentBuild);
        if (branch.length() > 0) {
            attachmentBranch = new AttachmentField("Branch", branch, false);
            attachment.fields.add(attachmentBranch);
        }
        attachment.fields.add(attachmentStatus);

        this._attachments = new ArrayList<Attachment>();
        this._attachments.add(0, attachment);

        if (this.useAttachments) {
            attachments = _attachments;
        }
    }
}