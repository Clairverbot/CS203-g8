package com.G2T8.CS203WebApp.model;

import java.util.Map;

/**
 * Java class for emailing
 */
public class Email {
    /**
     * Email address of sender
     */
    private String from;

    /**
     * Email address of receiver
     */
    private String mailTo;

    /**
     * Email subject
     */
    private String subject;

    /**
     * Content of the email
     */
    private Map<String, Object> props;

    /**
     * Getters and setters for the variables of the email
     */

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Map<String, Object> getProps() {
        return props;
    }

    public void setProps(Map<String, Object> props) {
        this.props = props;
    }

}
