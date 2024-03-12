package com.springboot.reactive.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class MyController {

    @GetMapping
    public Mono<String> getString(){
        return getGreeting().zipWith(getName())
                .map(objects -> objects.getT1()+objects.getT2())
                .zipWith(getTerminal())
                .map(objects -> objects.getT1()+objects.getT2());
    }

    public Mono<String> getGreeting(){
        return Mono.just("Hello ").delayElement(Duration.ofSeconds(5));
    }

    public Mono<String> getName(){
        return Mono.just("John ").delayElement(Duration.ofSeconds(10));
    }

    public Mono<String> getTerminal(){
        return Mono.just("...! ").delayElement(Duration.ofSeconds(15));
    }
}
