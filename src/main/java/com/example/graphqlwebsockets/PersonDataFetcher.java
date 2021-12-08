package com.example.graphqlwebsockets;

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
    private final PersonRepository personRepository;

    @DgsQuery
    public Iterable<Person> people() {
        return personRepository.findAll();
    }

    @DgsMutation
    public Person addPerson(@InputArgument("input") PersonInput p) {


        var person = Person.builder().name(p.getName())
                .age(p.getAge())
                .address( new Address("129 Catoctin Cir", "Leesburg", "VA", "20175" ) )
                .build();

        personRepository.save( person );

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
