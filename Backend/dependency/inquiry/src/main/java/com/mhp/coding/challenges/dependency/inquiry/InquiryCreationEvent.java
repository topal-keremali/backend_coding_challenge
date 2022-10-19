package com.mhp.coding.challenges.dependency.inquiry;

import org.springframework.context.ApplicationEvent;

/**
 * Object of Inquiry Creation Event
 */
public class InquiryCreationEvent extends ApplicationEvent {

    private final Inquiry inquiry;

    public InquiryCreationEvent(Object source, Inquiry inquiry) {
        super(source);
        this.inquiry = inquiry;
    }

    public Inquiry getInquiry() {
        return inquiry;
    }
}
