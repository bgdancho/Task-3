package com.yordan.karabelyov.shop.dto.subscriber;

import com.yordan.karabelyov.shop.util.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class SubscriberDTO {

    @NotNull
    @Size(min = Constants.SUBSCRIBER_MIN_NAME_LENGTH, max = Constants.SUBSCRIBER_MAX_NAME_LENGTH)
    private String firstName;

    @NotNull
    @Size(min = Constants.SUBSCRIBER_MIN_NAME_LENGTH, max = Constants.SUBSCRIBER_MAX_NAME_LENGTH)
    private String lastName;

    public SubscriberDTO() {
    }

    public SubscriberDTO(String firstName, String lastName, List<Long> productsIds) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
