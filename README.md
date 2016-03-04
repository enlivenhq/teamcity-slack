# teamcity-slack
[![Build Status](https://teamcity.jetbrains.com/app/rest/builds/buildType:(id:TeamCityThirdPartyPlugins_TeamcitySlackNotifications_Build)/statusIcon.svg)](https://teamcity.jetbrains.com/viewType.html?buildTypeId=TeamCityThirdPartyPlugins_TeamcitySlackNotifications_Build)

A configurable TeamCity plugin that notifies your [Slack](https://slack.com) channel.
Because it is a [TeamCity Custom Notifier](http://confluence.jetbrains.com/display/TCD8/Custom+Notifier) plugin, it extends the existing user interface and allows for easy configuration directly within your TeamCity server. Once installed, you can configure the plugin for multiple TeamCity projects and multiple build conditions (i.e. Build failures, successes, hangs, etc.)

## Installation
Download the [plugin zip package](/target/teamcity-slack-integration.zip).

Follow the TeamCity [plugin installation directions](http://confluence.jetbrains.com/display/TCD8/Installing+Additional+Plugins).

## Configuration

Create an [incoming webook](https://my.slack.com/services/new/incoming-webhook) in Slack.

Pick a Slack channel to notify on build events.

Pick a Slack username to use for automated notifications.

Copy the URL for the webhook.

As an admin, Navigate to your TeamCity profile page ("My Settings & Tools") and click "Edit".

Enter the channel name, username, and full webhook URL in the Notification settings as seen below.

Add notification rules as appropriate.

## Configuration Example

![Configuration Settings](/configuration%20example.png)

## Warning

Tested mostly with TeamCity version 8.1.1 but we've definitely seen lots of folks using it with newer version as well.

## Contributors

[Jesse Dunlap](https://twitter.com/jessedunlap)

[Andrew Clark](https://twitter.com/andrew_jclark)

[cy6erskunk] (https://github.com/cy6erskunk)

[Ian Robinson](https://twitter.com/irobinson)

## License
MIT
