package com.example.graphqlwebsockets;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder(toBuilder = true)
@RedisHash("person")
@AllArgsConstructor
public class Person {

    @Id
    String id;
    String name;
    int age;
    Address address;
}
