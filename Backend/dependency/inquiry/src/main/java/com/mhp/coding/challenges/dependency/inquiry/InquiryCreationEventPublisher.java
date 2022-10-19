package com.mhp.coding.challenges.dependency.inquiry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Event for creating an inquiry
 */
@Component
public class InquiryCreationEventPublisher {

    private static final Logger LOG = LoggerFactory.getLogger(InquiryCreationEventPublisher.class);
    private final ApplicationEventPublisher applicationEventPublisher;

    public InquiryCreationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    //published creation event for listeners to listen to
    public void publishCustomEvent(final Inquiry inquiry) {
        LOG.info("Publishing inquiry creation event. ");
        InquiryCreationEvent inquiryCreationEvent = new InquiryCreationEvent(this, inquiry);
        applicationEventPublisher.publishEvent(inquiryCreationEvent);
    }
}
