package com.enlivenhq.slack;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SlackWrapperTest {

    @Test
    public void testSlackWrapperUsesAttachmentByDefault() throws Exception {
        SlackWrapper slack = new SlackWrapper();
        assertTrue(slack.useAttachment);
    }

    @org.testng.annotations.Test
    public void testSlackWrapperDoesNotUseAttachmentWhenToldSo() throws Exception {
        SlackWrapper slack = new SlackWrapper(false);
        assertFalse(slack.useAttachment);
    }

}