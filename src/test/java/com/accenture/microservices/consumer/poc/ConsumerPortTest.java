package com.accenture.microservices.consumer.poc;

import static junit.framework.TestCase.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;

public class ConsumerPortTest {

    @Rule
    public PactProviderRule rule = new PactProviderRule("Foo_Provider", this);

    @Pact(provider="Foo_Provider", consumer="Foo_Consumer")
    public PactFragment createFragment(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");

        return builder.uponReceiving("a request for Foos")
                .path("/foos")
                .method("GET")

                .willRespondWith()
                .headers(headers)
                .status(200)
                .body("[{\"value\":13}, {\"value\":50}]").toFragment();
    }

    @Test
    @PactVerification("Foo_Provider")
    public void runTest() {
    	System.out.println("Producer URL: "+rule.getConfig().url());
    	System.out.println("Return :"+ new ConsumerPort(rule.getConfig().url()).foos().toString()+"   "+Arrays.asList(new Foo(13), new Foo(50)));
        assertEquals(new ConsumerPort(rule.getConfig().url()).foos(), Arrays.asList(new Foo(13), new Foo(50)));
    }
}
