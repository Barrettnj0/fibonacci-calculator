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
    private String jumpUrl;

    @LocalServerPort
    private int port; // Stores random port in 'port' variable

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        currentUrl = "http://localhost:" + port + "/current"; // Gets url for current based on port used
        nextUrl = "http://localhost:" + port + "/next"; // Gets url for next based on port used
        previousUrl = "http://localhost:" + port + "/previous"; // Gets url for previous based on port used
        jumpUrl = "http://localhost:" + port + "/jump/"; // Gets url for jump based on port used
    }

    @Test
    public void testCurrent() { // Test initial response of /current
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

    @Test
    public void testJump() {

        /* For this test, we check the possible errors that should be expected with /jump. Then we check jump itself as well as making sure
        it calculated +1 or -1 correctly. Finally, we check a /previous condition that can only be achieved through jumping */

        String stringJumpResponse = restTemplate.getForEntity(jumpUrl + "a", String.class).getBody();
        String nonIntegerJumpResponse = restTemplate.getForEntity(jumpUrl + "3.5", String.class).getBody();
        String negativeJumpResponse = restTemplate.getForEntity(jumpUrl + "-1", String.class).getBody();

        String goodJumpResponse = restTemplate.getForEntity(jumpUrl + "8", String.class).getBody();
        String goodCurrentResponse = restTemplate.getForEntity(currentUrl, String.class).getBody();

        String nextResponse = restTemplate.getForEntity(nextUrl, String.class).getBody();
        String nextNumResponse = restTemplate.getForEntity(currentUrl, String.class).getBody();

        restTemplate.getForEntity(previousUrl, String.class).getBody(); // Go back to orignal jump index (8)

        String firstPreviousResponse = restTemplate.getForEntity(previousUrl, String.class).getBody();
        String firstPreviousNumResponse = restTemplate.getForEntity(currentUrl, String.class).getBody();
        String secondPreviousResponse = restTemplate.getForEntity(previousUrl, String.class).getBody();
        String secondPreviousNumResponse = restTemplate.getForEntity(currentUrl, String.class).getBody();

        Assertions.assertEquals("You can only input integers 0 or greater to jump to. Your index is still 0.", stringJumpResponse);
        // Checks if non-numbers will get an expected error message in response
        Assertions.assertEquals("You can only input integers 0 or greater to jump to. Your index is still 0.", nonIntegerJumpResponse);
        // Checks if non-integers will get an expected error message in response
        Assertions.assertEquals("Index goes below bounds for normal fibonacci sequence. Your index is still 0.", negativeJumpResponse);
        // Checks if negative integers will get an expected error message in response

        Assertions.assertEquals("Your current fibonacci index is now 8", goodJumpResponse);
        // Checks if /jump responds as expected for non-negative integers
        Assertions.assertEquals("Index: 8\nFibonacci number: 21", goodCurrentResponse);
        // Checks if corresponding jump gets correct fibonacci number

        Assertions.assertEquals("Your current fibonacci index is now 9", nextResponse);
        // Checks if fibonacci map received the correct index from \jump
        Assertions.assertEquals("Index: 9\nFibonacci number: 34", nextNumResponse);
        // Checks if corresponding number is correct from calculations of jump

        Assertions.assertEquals("Your current fibonacci index is now 7", firstPreviousResponse);
        // Checks if fibonacci map received the correct index from \jump
        Assertions.assertEquals("Index: 7\nFibonacci number: 13", firstPreviousNumResponse);
        // Checks if corresponding number is correct from calculations of jump
        Assertions.assertEquals("Your current fibonacci index is now 6", secondPreviousResponse);
        // Checks that decrement of index works properly after \jump
        Assertions.assertEquals("Index: 6\nFibonacci number: 8", secondPreviousNumResponse);
        // Checks that previous calculations work for fibonacci number after jumps beyond the current population

    }

}
