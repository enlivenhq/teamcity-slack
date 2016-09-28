package com.enlivenhq.teamcity;

import com.enlivenhq.slack.SlackWrapper;
import jetbrains.buildServer.Build;
import jetbrains.buildServer.notification.Notificator;
import jetbrains.buildServer.notification.NotificatorRegistry;
import jetbrains.buildServer.responsibility.ResponsibilityEntry;
import jetbrains.buildServer.responsibility.TestNameResponsibilityEntry;
import jetbrains.buildServer.serverSide.Branch;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SProject;
import jetbrains.buildServer.serverSide.SRunningBuild;
import jetbrains.buildServer.serverSide.STest;
import jetbrains.buildServer.serverSide.UserForm;
import jetbrains.buildServer.serverSide.UserPropertyInfo;
import jetbrains.buildServer.serverSide.UserPropertyValidator;
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
import java.util.HashSet;
import java.util.Set;

public class SlackNotificator implements Notificator {

    private static final Logger log = Logger.getLogger(SlackNotificator.class);

    private static final String type = "SlackNotificator";

    private static final String slackChannelKey = "slack.Channel";
    private static final String slackUsernameKey = "slack.Username";
    private static final String slackUrlKey = "slack.Url";
    private static final String slackVerboseKey = "slack.Verbose";

    private static final PropertyKey slackChannel = new NotificatorPropertyKey(type, slackChannelKey);
    private static final PropertyKey slackUsername = new NotificatorPropertyKey(type, slackUsernameKey);
    private static final PropertyKey slackUrl = new NotificatorPropertyKey(type, slackUrlKey);
    private static final PropertyKey slackVerbose = new NotificatorPropertyKey(type, slackVerboseKey);

    private SBuildServer myServer;

    public SlackNotificator(NotificatorRegistry notificatorRegistry, SBuildServer server) {
        registerNotificatorAndUserProperties(notificatorRegistry);
        myServer = server;
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
         sendNotification(sRunningBuild.getFullName(), sRunningBuild.getBuildNumber(), "failed: " + sRunningBuild.getStatusDescriptor().getText(), "danger", users, sRunningBuild);
    }

    public void notifyBuildFailedToStart(@NotNull SRunningBuild sRunningBuild, @NotNull Set<SUser> users) {
        sendNotification(sRunningBuild.getFullName(), sRunningBuild.getBuildNumber(), "failed to start", "danger", users, sRunningBuild);
    }

    public void notifyBuildSuccessful(@NotNull SRunningBuild sRunningBuild, @NotNull Set<SUser> users) {
        sendNotification(sRunningBuild.getFullName(), sRunningBuild.getBuildNumber(), "built successfully", "good", users, sRunningBuild);
    }

    public void notifyLabelingFailed(@NotNull Build build, @NotNull VcsRoot vcsRoot, @NotNull Throwable throwable, @NotNull Set<SUser> sUsers) {
        sendNotification(build.getFullName(), build.getBuildNumber(), "labeling failed", "danger", sUsers, build);
    }

    public void notifyBuildFailing(@NotNull SRunningBuild sRunningBuild, @NotNull Set<SUser> sUsers) {
        sendNotification(sRunningBuild.getFullName(), sRunningBuild.getBuildNumber(), "failing", "danger", sUsers, sRunningBuild);
    }

    public void notifyBuildProbablyHanging(@NotNull SRunningBuild sRunningBuild, @NotNull Set<SUser> sUsers) {
        sendNotification(sRunningBuild.getFullName(), sRunningBuild.getBuildNumber(), "probably hanging", "warning", sUsers, sRunningBuild);
    }

    public void notifyBuildStarted(@NotNull SRunningBuild sRunningBuild, @NotNull Set<SUser> sUsers) {
        sendNotification(sRunningBuild.getFullName(), sRunningBuild.getBuildNumber(), "started", "warning", sUsers, sRunningBuild);
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

    private void registerNotificatorAndUserProperties(NotificatorRegistry notificatorRegistry) {
        ArrayList<UserPropertyInfo> userPropertyInfos = getUserPropertyInfosList();
        notificatorRegistry.register(this, userPropertyInfos);
    }

    private ArrayList<UserPropertyInfo> getUserPropertyInfosList() {
        ArrayList<UserPropertyInfo> userPropertyInfos = new ArrayList<>();

        UserPropertyValidator verboseValidator = new UserPropertyValidator() {
            @Nullable
            public String validate(@NotNull String s, @Nullable SUser sUser, @NotNull UserForm userForm) {
                String sUpper = s.toUpperCase();
                Set<String> validValues = new HashSet<>();
                validValues.add("TRUE");
                validValues.add("FALSE");
                validValues.add("YES");
                validValues.add("NO");
                if (validValues.contains(sUpper)) {
                    return null;
                } else {
                    return "Please use True/False or Yes/No to declare if you want Verbose Slack Messages.";
                }
            }
        };

        userPropertyInfos.add(new UserPropertyInfo(slackChannelKey, "#channel or @name"));
        userPropertyInfos.add(new UserPropertyInfo(slackUsernameKey, "Bot name"));
        userPropertyInfos.add(new UserPropertyInfo(slackUrlKey, "Webhook URL"));
        userPropertyInfos.add(new UserPropertyInfo(slackVerboseKey, "Verbose Messages", "True or False", verboseValidator));

        return userPropertyInfos;
    }

    private void sendNotification(String project, String build, String statusText, String statusColor, Set<SUser> users, Build bt) {
        for (SUser user : users) {
            SlackWrapper slackWrapper = getSlackWrapperWithUser(user);
            slackWrapper.send(project, build, getBranch((SBuild)bt), statusText, statusColor, bt);
        }
    }

    private SlackWrapper getSlackWrapperWithUser(SUser user) {
        String channel = user.getPropertyValue(slackChannel);
        String username = user.getPropertyValue(slackUsername);
        String url = user.getPropertyValue(slackUrl);
        String verbose = user.getPropertyValue(slackVerbose);

        if (slackConfigurationIsInvalid(channel, username, url, verbose)) {
            log.error("Could not send Slack notification. The Slack channel, username, or URL was null. " +
                      "Double check your Notification settings");

            return new SlackWrapper();
        }

        boolean useAttachements = convertToBoolean(verbose);
        return constructSlackWrapper(channel, username, url, useAttachements);
    }

    private boolean slackConfigurationIsInvalid(String channel, String username, String url, String verbose) {
        return channel == null || username == null || url == null || verbose == null;
    }

    private SlackWrapper constructSlackWrapper(String channel, String username, String url, boolean useAttachements) {
        SlackWrapper slackWrapper = new SlackWrapper(useAttachements);

        slackWrapper.setChannel(channel);
        slackWrapper.setUsername(username);
        slackWrapper.setSlackUrl(url);
        slackWrapper.setServerUrl(myServer.getRootUrl());

        return slackWrapper;
    }

    private String getBranch(SBuild build) {
        Branch branch = build.getBranch();
        if (branch != null && !branch.getName().equals("<default>")) {
            return branch.getDisplayName();
        } else {
            return "";
        }
    }

    private boolean convertToBoolean(String value) {
        String upper = value.toUpperCase();
        return "TRUE".equals(upper) || "YES".equals(upper);
    }
}
