package com.batch.camel;

import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.dataformat.bindy.annotation.FixedLengthRecord;

@FixedLengthRecord
public class FormattedCustomerRecord {

    @DataField(pos = 1, length = 10)
    private String id;

    @DataField(pos = 11, length = 20)
    private String firstName;

    @DataField(pos = 31, length = 20)
    private String lastName;

    @DataField(pos = 51, length = 30)
    private String email;

    @DataField(pos = 81, length = 15)
    private String phoneNumber;

    // Default constructor required by Bindy
    public FormattedCustomerRecord() {
    }
    
    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}