package com.yordan.karabelyov.shop.dto.subscriber;
public class SubscriberReadDTO {

    private Long id;
    private String firstName;
    private String lastName;

    public SubscriberReadDTO() {
    }

    public SubscriberReadDTO(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
