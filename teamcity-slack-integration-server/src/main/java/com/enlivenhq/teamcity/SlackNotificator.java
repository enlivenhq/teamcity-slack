package com.enlivenhq.teamcity;

import com.enlivenhq.slack.SlackWrapper;
import jetbrains.buildServer.Build;
import jetbrains.buildServer.BuildProblemData;
import jetbrains.buildServer.notification.Notificator;
import jetbrains.buildServer.notification.NotificatorRegistry;
import jetbrains.buildServer.responsibility.ResponsibilityEntry;
import jetbrains.buildServer.responsibility.TestNameResponsibilityEntry;
import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.serverSide.mute.MuteInfo;
import jetbrains.buildServer.serverSide.problems.BuildProblemInfo;
import jetbrains.buildServer.tests.TestName;
import jetbrains.buildServer.users.NotificatorPropertyKey;
import jetbrains.buildServer.users.PropertyKey;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.vcs.VcsRoot;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class SlackNotificator implements Notificator {

    private static final Logger log = Logger.getLogger(SlackNotificator.class);

    private static final String type = "SlackNotificator";

    private static final String slackChannelKey = "slack.Channel";
    private static final String slackUsernameKey = "slack.Username";
    private static final String slackUrlKey = "slack.Url";

    private static final PropertyKey slackChannel = new NotificatorPropertyKey(type, slackChannelKey);
    private static final PropertyKey slackUsername = new NotificatorPropertyKey(type, slackUsernameKey);
    private static final PropertyKey slackUrl = new NotificatorPropertyKey(type, slackUrlKey);

    public SlackNotificator(NotificatorRegistry notificatorRegistry) {
        log.info("Registering user properties.");
        registerNotificatorAndUserProperties(notificatorRegistry);
    }

    @NotNull
    public String getNotificatorType() {
        return type;
    }

    @NotNull
    public String getDisplayName() {
        return "Slack Notifier";
    }

    public void notifyBuildFailed(@NotNull SRunningBuild sRunningBuild, @NotNull Set<SUser> users) {
        log.info("Notify build failed.");

        String concatenatedReasons = getConcatenatedFailureReasons(sRunningBuild.getFailureReasons());

        sendNotification("Build " + sRunningBuild.getFullName() + " failed to build #" +
                            sRunningBuild.getBuildNumber() + " for the following reason(s):\n" +
                            concatenatedReasons, users);
    }

    public void notifyBuildFailedToStart(@NotNull SRunningBuild sRunningBuild, @NotNull Set<SUser> users) {
        log.info("Notify build failed to start.");

        String concatenatedReasons = getConcatenatedFailureReasons(sRunningBuild.getFailureReasons());

        sendNotification("Build " + sRunningBuild.getFullName() + " failed to start building #" +
                sRunningBuild.getBuildNumber() + " for the following reason(s):\n" +
                concatenatedReasons, users);
    }

    public void notifyBuildSuccessful(@NotNull SRunningBuild sRunningBuild, @NotNull Set<SUser> users) {
        log.info("Notify build successful.");

        sendNotification("Build " + sRunningBuild.getFullName() + " built #" +
                sRunningBuild.getBuildNumber() + " successfully!", users);
    }

    public void notifyLabelingFailed(@NotNull Build build, @NotNull VcsRoot vcsRoot, @NotNull Throwable throwable, @NotNull Set<SUser> sUsers) {
        log.info("Notify build labeling failed.");

        sendNotification("Build " + build.getFullName() + " labeling failed for #" +
                build.getBuildNumber(), sUsers);
    }

    public void notifyBuildFailing(@NotNull SRunningBuild sRunningBuild, @NotNull Set<SUser> sUsers) {
        log.info("Notify build failing.");

        String concatenatedReasons = getConcatenatedFailureReasons(sRunningBuild.getFailureReasons());

        sendNotification("Build " + sRunningBuild.getFullName() + " failing #" +
                sRunningBuild.getBuildNumber() + " for the following reason(s):\n" +
                concatenatedReasons, sUsers);
    }

    public void notifyBuildProbablyHanging(@NotNull SRunningBuild sRunningBuild, @NotNull Set<SUser> sUsers) {
        log.info("Notify build probably hanging.");

        String concatenatedReasons = getConcatenatedFailureReasons(sRunningBuild.getFailureReasons());

        sendNotification("Build " + sRunningBuild.getFullName() + " is probably hanging on #" +
                sRunningBuild.getBuildNumber() + " for the following reason(s):\n" +
                concatenatedReasons, sUsers);
    }

    public void notifyBuildStarted(@NotNull SRunningBuild sRunningBuild, @NotNull Set<SUser> sUsers) {
        log.info("Notify build started.");

        sendNotification("Build " + sRunningBuild.getFullName() + " started #" +
                sRunningBuild.getBuildNumber(), sUsers);
    }

    public void notifyResponsibleChanged(@NotNull SBuildType sBuildType, @NotNull Set<SUser> sUsers) {

    }

    public void notifyResponsibleAssigned(@NotNull SBuildType sBuildType, @NotNull Set<SUser> sUsers) {

    }

    public void notifyResponsibleChanged(@Nullable TestNameResponsibilityEntry testNameResponsibilityEntry, @NotNull TestNameResponsibilityEntry testNameResponsibilityEntry2, @NotNull SProject sProject, @NotNull Set<SUser> sUsers) {

    }

    public void notifyResponsibleAssigned(@Nullable TestNameResponsibilityEntry testNameResponsibilityEntry, @NotNull TestNameResponsibilityEntry testNameResponsibilityEntry2, @NotNull SProject sProject, @NotNull Set<SUser> sUsers) {

    }

    public void notifyResponsibleChanged(@NotNull Collection<TestName> testNames, @NotNull ResponsibilityEntry responsibilityEntry, @NotNull SProject sProject, @NotNull Set<SUser> sUsers) {

    }

    public void notifyResponsibleAssigned(@NotNull Collection<TestName> testNames, @NotNull ResponsibilityEntry responsibilityEntry, @NotNull SProject sProject, @NotNull Set<SUser> sUsers) {

    }

    public void notifyBuildProblemResponsibleAssigned(@NotNull Collection<BuildProblemInfo> buildProblemInfos, @NotNull ResponsibilityEntry responsibilityEntry, @NotNull SProject sProject, @NotNull Set<SUser> sUsers) {

    }

    public void notifyBuildProblemResponsibleChanged(@NotNull Collection<BuildProblemInfo> buildProblemInfos, @NotNull ResponsibilityEntry responsibilityEntry, @NotNull SProject sProject, @NotNull Set<SUser> sUsers) {

    }

    public void notifyTestsMuted(@NotNull Collection<STest> sTests, @NotNull MuteInfo muteInfo, @NotNull Set<SUser> sUsers) {

    }

    public void notifyTestsUnmuted(@NotNull Collection<STest> sTests, @NotNull MuteInfo muteInfo, @Nullable SUser sUser, @NotNull Set<SUser> sUsers) {

    }

    public void notifyBuildProblemsMuted(@NotNull Collection<BuildProblemInfo> buildProblemInfos, @NotNull MuteInfo muteInfo, @NotNull Set<SUser> sUsers) {

    }

    public void notifyBuildProblemsUnmuted(@NotNull Collection<BuildProblemInfo> buildProblemInfos, @NotNull MuteInfo muteInfo, @Nullable SUser sUser, @NotNull Set<SUser> sUsers) {

    }

    private String getConcatenatedFailureReasons(List<BuildProblemData> failureReasons) {
        String concatenated = "";

        for (BuildProblemData buildProblemData : failureReasons) {
            concatenated += buildProblemData.getDescription() + "\n";
        }

        return concatenated.trim();
    }

    private void registerNotificatorAndUserProperties(NotificatorRegistry notificatorRegistry) {
        ArrayList<UserPropertyInfo> userPropertyInfos = getUserPropertyInfosList();
        notificatorRegistry.register(this, userPropertyInfos);
    }

    private ArrayList<UserPropertyInfo> getUserPropertyInfosList() {
        ArrayList<UserPropertyInfo> userPropertyInfos = new ArrayList<UserPropertyInfo>();

        userPropertyInfos.add(new UserPropertyInfo(slackChannelKey, "Slack Channel"));
        userPropertyInfos.add(new UserPropertyInfo(slackUsernameKey, "Slack Username"));
        userPropertyInfos.add(new UserPropertyInfo(slackUrlKey, "Slack Instance URL"));

        return userPropertyInfos;
    }

    private void sendNotification(String message, Set<SUser> users) {
        log.info("Sending notification: " + message);

        for (SUser user : users) {
            SlackWrapper slackWrapper = getSlackWrapperWithUser(user);
            slackWrapper.send(message);
        }
    }

    private SlackWrapper getSlackWrapperWithUser(SUser user) {
        String channel = user.getPropertyValue(slackChannel);
        String username = user.getPropertyValue(slackUsername);
        String url = user.getPropertyValue(slackUrl);

        if (channel == null || username == null || url == null) {
            log.error("Could not send Slack notification. The Slack channel, username, or URL was null. " +
                      "Double check your Notification settings");

            return new SlackWrapper();
        }

        SlackWrapper slackWrapper = new SlackWrapper();

        slackWrapper.setChannel(channel);
        slackWrapper.setUsername(username);
        slackWrapper.setSlackUrl(url);

        return slackWrapper;
    }
}
