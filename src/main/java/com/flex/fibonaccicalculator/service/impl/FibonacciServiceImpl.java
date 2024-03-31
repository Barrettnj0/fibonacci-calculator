package com.flex.fibonaccicalculator.service.impl;

import com.flex.fibonaccicalculator.service.FibonacciService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class FibonacciServiceImpl implements FibonacciService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FibonacciServiceImpl.class); // Add logger

    Integer currentIndex = 0; // Application begins with initial index of 0
    HashMap<Integer, Integer> fibonacciMap = new HashMap<>() {{ // Initialize first two numbers as defined
        put(0, 0);                                                             // by fibonacci sequence in a hashMap to assure no
        put(1, 1);                                                            // duplicate indices
    }};

    @Override
    public String getCurrent() {
        Integer currentFibonacciNum = fibonacciMap.get(currentIndex); // Uses the 'currentIndex' to retrieve the fibonacci number associated with it in the hashMap
        return "Index: " + currentIndex + "\nFibonacci number: " + currentFibonacciNum; // Send current fibonacci number back to user
    }

    @Override
    public String getNext() {
        currentIndex++;
        LOGGER.info("Your index is now " + currentIndex);

        if(currentIndex >= fibonacciMap.size()) { // Check if next index is already in HashMap. If index is created we already calculated the fibonacci number
            LOGGER.info("Your 'next' index is not in the fibonacci map. Generating fibonacci number for index " + currentIndex + "...");
            calcFibonacciNum(); // Use calcFibonacciNum() function. Putting this computation in its own function for tidiness.
        }

        return "Your current fibonacci index is now " + currentIndex; // Send current index info back to user
    }

    @Override
    public String getPrevious() {
        String indexMessage;

        if(currentIndex == 0) { // Check if index is 0
            indexMessage = "Index goes below bounds for normal fibonacci sequence. Your index is still 0."; //
            /* Instead of decrementing index, just store this message about why the index will remain 0 as the fibonacci
            sequence normally pertains to positive integers */
        }
        else {
            currentIndex--;
            indexMessage = "Your current fibonacci index is now " + currentIndex; // Send current index info back to user
        }
        return indexMessage;
    }

    private void calcFibonacciNum() {
        Integer newFibonacciNum = fibonacciMap.get(currentIndex - 2) + fibonacciMap.get(currentIndex - 1);

        fibonacciMap.put(currentIndex, newFibonacciNum);

        LOGGER.info("Adding pair (" + currentIndex + ", " + newFibonacciNum + ") to fibonacci map.");
    }

}
