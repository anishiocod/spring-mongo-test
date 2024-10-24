package com.iocod.spring_mongo_test.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "receipts")
public class Receipt {
    @Id
    private String apiVersion;
    private String messageStatus;
    private String smsSid;
    private String to;
    private String from;
    private String accountSid;
     // Getter methods
     public String getApiVersion() {
        return apiVersion;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public String getSmsSid() {
        return smsSid;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getAccountSid() {
        return accountSid;
    }

    // Setter methods
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public void setSmsSid(String smsSid) {
        this.smsSid = smsSid;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }
    @Override
    public String toString() {
        return "Receipt{" +
                "apiVersion='" + apiVersion + '\'' +
                ", messageStatus='" + messageStatus + '\'' +
                ", smsSid='" + smsSid + '\'' +
                ", to='" + to + '\'' +
                ", from='" + from + '\'' +
                ", accountSid='" + accountSid + '\'' +
                '}';
    }
}
