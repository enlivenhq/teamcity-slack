package com.enlivenhq.teamcity;

import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;

/**
 * Generates {@link net.gpedro.integrations.slack.SlackMessage} instances.
 * <p>
 * The messages are appropriate for the given TeamCity data.
 */
public class MessageFactory {

    public static SlackMessage createBuildStatusMessage(
            String channel, String username, String project, String build, String branch, String statusText,
            String statusColor, String btId, long buildId, String serverUrl, boolean useAttachments) {
        project = escape(project);
        build = escape(build);
        branch = escape(branch);
        statusText = escape(escapeNewline(statusText));
        btId = escape(btId);

        String escapedBranch = branch.length() > 0 ? " [" + branch + "]" : "";

        statusText = "<" + serverUrl + "/viewLog.html?buildId=" + buildId + "&buildTypeId=" + btId + "|" + statusText + ">";

        String statusEmoji = statusColor.equals("danger") ? ":x: " : statusColor.equals("warning") ? "" : ":white_check_mark: ";

        String payloadText = statusEmoji + project + escapedBranch + " #" + build + " " + statusText;

        SlackMessage message = new SlackMessage(channel, username, payloadText);

        if (useAttachments) {
            SlackAttachment attachment = new SlackAttachment();
            attachment.setColor(statusColor);
            attachment.setPretext("Build Status");
            attachment.setFallback(payloadText);


            SlackField attachmentProject = new SlackField().setTitle("Project").setValue(project).setShorten(false);
            SlackField attachmentBuild = new SlackField().setTitle("Build").setValue(build).setShorten(true);
            SlackField attachmentStatus = new SlackField().setTitle("Status").setValue(statusText).setShorten(false);

            attachment.addFields(attachmentProject);
            attachment.addFields(attachmentBuild);
            attachment.addFields(attachmentStatus);

            SlackField attachmentBranch;
            if (branch.length() > 0) {
                attachmentBranch = new SlackField().setTitle("Branch").setValue(branch).setShorten(false);
                attachment.addFields(attachmentBranch);
            }

            message.addAttachments(attachment);
        }
        return message;
    }

    private static String escape(String s) {
        return s.replace("<", "&lt;").replace(">", "&gt;");
    }

    private static String escapeNewline(String s) {
        return s.replace("\n", "\\n");
    }
}
