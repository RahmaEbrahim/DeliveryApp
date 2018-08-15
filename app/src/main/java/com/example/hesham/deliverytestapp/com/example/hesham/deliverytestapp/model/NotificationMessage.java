package com.example.hesham.deliverytestapp.com.example.hesham.deliverytestapp.model;

public class NotificationMessage {
    private String customerNumber;
    private String timeBalance;
    private String messageBody;
    private String topicName;
    private String messageType;

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getTimeBalance() {
        return timeBalance;
    }

    public void setTimeBalance(String timeBalance) {
        this.timeBalance = timeBalance;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
