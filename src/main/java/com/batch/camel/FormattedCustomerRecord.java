package com.batch.camel;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.dataformat.bindy.annotation.FixedLengthRecord;

@Setter
@Getter
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

}