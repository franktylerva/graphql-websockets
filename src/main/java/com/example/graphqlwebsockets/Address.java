package com.example.graphqlwebsockets;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Address {

    private String street;
    private String city;
    private String state;
    private String zip;
}
