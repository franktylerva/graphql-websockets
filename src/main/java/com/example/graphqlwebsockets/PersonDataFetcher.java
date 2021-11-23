package com.example.graphqlwebsockets;

import com.netflix.dgs.codgen.generated.types.Address;
import com.netflix.dgs.codgen.generated.types.Person;
import com.netflix.dgs.codgen.generated.types.PersonInput;
import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.data.redis.core.ReactiveRedisOperations;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@DgsComponent
@Slf4j
@RequiredArgsConstructor
public class PersonDataFetcher {

    private final ReactiveRedisOperations<String, Person> redisTemplate;

    private List<Person> people = new ArrayList<>();

    @PostConstruct
    public void loadPeople() {
        people.add( Person.newBuilder().name("John Doe").age(24)
                .address( Address.newBuilder()
                        .street("123 First Street")
                        .city("New York")
                        .state("NY")
                        .zip("12345")
                        .build()).build());

        people.add( Person.newBuilder().name("Jane Doe").age(24)
                .address( Address.newBuilder()
                        .street("123 First Street")
                        .city("New York")
                        .state("NY")
                        .zip("12345")
                        .build()).build());
    }

    @DgsQuery
    public List<Person> people() {
        return people;
    }

    @DgsMutation
    public Person addPerson(@InputArgument("input") PersonInput p) {

        var person = Person.newBuilder()
                .name(p.getName())
                .age(p.getAge())
                .build();
        people.add( person );

        // Publish person
        redisTemplate.convertAndSend("peopleUpdates", person ).subscribe();
        return person;
    }

    @DgsSubscription
    public Publisher<Person> peopleUpdates() {
        // Hook this Publisher up to the peopleUpdates topic
        return redisTemplate.listenToChannel("peopleUpdates").map(c -> {
            return c.getMessage();
        });
    }

}
