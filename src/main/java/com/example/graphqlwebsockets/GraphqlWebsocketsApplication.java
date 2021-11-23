package com.example.graphqlwebsockets;

import com.netflix.dgs.codgen.generated.types.Person;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@SpringBootApplication
public class GraphqlWebsocketsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraphqlWebsocketsApplication.class, args);
	}

	@Bean
	public ReactiveRedisOperations<String, Person> showTemplate(LettuceConnectionFactory lettuceConnectionFactory){
		RedisSerializationContext<String, Person> serializationContext =
				RedisSerializationContext.<String, Person>newSerializationContext(RedisSerializer.string())
						.value( new Jackson2JsonRedisSerializer<>(Person.class) )
						.build();
		return new ReactiveRedisTemplate<String, Person>(lettuceConnectionFactory, serializationContext);
	}
}
