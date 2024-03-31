package com.flex.fibonaccicalculator.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Uses random port for test
public class FibonacciControllerTest {

    private String currentUrl;
    private String nextUrl;
    private String previousUrl;

    @LocalServerPort
    private int port; // Stores random port in 'port' variable

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        currentUrl = "http://localhost:" + port + "/current"; // Gets url for current based on port used
        nextUrl = "http://localhost:" + port + "/next"; // Gets url for next based on port used
        previousUrl = "http://localhost:" + port + "/previous"; // Gets url for previous based on port used
    }

    @Test
    public void testCurrentOnStart() { // Test initial response of /current
        String currentResponse = restTemplate.getForEntity(currentUrl, String.class).getBody(); // Calls /current API and stores body in variable

        Assertions.assertEquals("Index: 0\nFibonacci number: 0", currentResponse);
        /* Compares the response that we should get vs what the API returns
        If API returns something different from expected, test fails. */
    }

    @Test
    public void testNextAndGeneration() {

        /* For this test, we will test if /next works as expected twice. The first will use
        the already provided map as a reference. The second one must generate a new fibonacci
        number to store in the map. We will check current for each one to assure that they are
        getting the correct fibonacci numbers. */

        String firstNextResponse = restTemplate.getForEntity(nextUrl, String.class).getBody(); // Calls /next API and stores body in variable
        String firstCurrentResponse = restTemplate.getForEntity(currentUrl, String.class).getBody(); // Calls /current for firstNextResponse
        String secondNextResponse = restTemplate.getForEntity(nextUrl, String.class).getBody(); // Calls /next again, stores new body separately
        String secondCurrentResponse = restTemplate.getForEntity(currentUrl, String.class).getBody(); // Calls /current for secondNextResponse

        Assertions.assertEquals("Your current fibonacci index is now 1", firstNextResponse);
        Assertions.assertEquals("Index: 1\nFibonacci number: 1", firstCurrentResponse);
        Assertions.assertEquals("Your current fibonacci index is now 2", secondNextResponse);
        Assertions.assertEquals("Index: 2\nFibonacci number: 1", secondCurrentResponse);
    }

    @Test
    public void testPrevious() {

        /* For this test, we will first check if /previous blocks iterating down from an index of 0 as it should. Then, we will iterate
        arbitrarily up to index 4 and see the /previous will give us the proper response here for index 3 and check the /current response
        to make sure that it successfully calculated the correct fibonacci number associated with 3 when iterating down. */

        String firstPreviousResponse = restTemplate.getForEntity(previousUrl, String.class).getBody(); // Calls /previous API and stores body in variable

        for (int index = 0; index < 4; index++) { // Loop that increases index to 4
            restTemplate.getForEntity(nextUrl, String.class); // Hits /next endpoint to iterate up index. No need to store this for this case.
        }

        String secondPreviousResponse = restTemplate.getForEntity(previousUrl, String.class).getBody(); // Calls /previous API and stores body in variable
        String currentResponse = restTemplate.getForEntity(currentUrl, String.class).getBody(); // Calls /current API and stores body in variable

        Assertions.assertEquals("Index goes below bounds for normal fibonacci sequence. Your index is still 0.", firstPreviousResponse);
        Assertions.assertEquals("Your current fibonacci index is now 3", secondPreviousResponse);
        Assertions.assertEquals("Index: 3\nFibonacci number: 2", currentResponse);
    }

}
