package com.mhp.coding.challenges.dependency.notifications;

import com.mhp.coding.challenges.dependency.inquiry.Inquiry;
import com.mhp.coding.challenges.dependency.inquiry.InquiryCreationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class EmailHandler implements ApplicationListener<InquiryCreationEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(EmailHandler.class);

    @Override
    public void onApplicationEvent(InquiryCreationEvent event) {
        LOG.info("Received spring custom event - " + event.getInquiry());
        this.sendEmail(event.getInquiry());
    }

    public void sendEmail(final Inquiry inquiry) {
        LOG.info("Sending email for: {}", inquiry);
    }

}
