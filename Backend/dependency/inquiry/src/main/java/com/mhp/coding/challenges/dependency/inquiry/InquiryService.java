package com.mhp.coding.challenges.dependency.inquiry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InquiryService {

    private static final Logger LOG = LoggerFactory.getLogger(InquiryService.class);

    //publisher vor the creation event
    private final InquiryCreationEventPublisher eventPublisher;

    public InquiryService(InquiryCreationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void create(final Inquiry inquiry) {
        eventPublisher.publishCustomEvent(inquiry);
        LOG.info("User sent inquiry: {}", inquiry);
    }

}
