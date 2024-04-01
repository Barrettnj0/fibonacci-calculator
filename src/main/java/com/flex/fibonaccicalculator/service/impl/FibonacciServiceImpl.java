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

        if(!fibonacciMap.containsKey(currentIndex)) { // Check if next index is already in HashMap. If index is created we already calculated the fibonacci number
            calcNextFibonacciNum(currentIndex); // Use calcFibonacciNum() function. Putting this computation in its own function for tidiness.
        }

        return "Your current fibonacci index is now " + currentIndex; // Send current index info back to user
    }

    @Override
    public String getPrevious() {
        String returnMessage;

        try {
            if(currentIndex == 0) { // Check if index is 0
                throw new IndexOutOfBoundsException();
            }

            currentIndex--;
            if (!fibonacciMap.containsKey(currentIndex)) { // Check if next index is already in HashMap. This is only necessary if /jump endpoint is used
                calcPreviousFibonacciNum(currentIndex); // Use calcFibonacciNum() function. Putting this computation in its own function for tidiness.
            }
            returnMessage = "Your current fibonacci index is now " + currentIndex;
        } catch (IndexOutOfBoundsException e) {
            LOGGER.error("User attempted to go below index 0.\nMessage: " + e);
            returnMessage = "Index goes below bounds for normal fibonacci sequence. Your index is still 0.";
        }
        return returnMessage; // Send current index info back to user
    }

    @Override
    public String getJump(String index) {
        String returnMessage;

        try {
            Integer parsedIndex = Integer.parseInt(index);

            if(parsedIndex < 0) {
                throw new IndexOutOfBoundsException();
            }

            currentIndex = parsedIndex;
            if (!fibonacciMap.containsKey(currentIndex)) { // Check if new index is already in fibonacci map
                calcFibonacciExpr(currentIndex);
            }
            if (!fibonacciMap.containsKey(currentIndex - 1) && currentIndex > 0) { // Check if previous index is already in fibonacci map
                calcFibonacciExpr(currentIndex - 1);
            }
            if (!fibonacciMap.containsKey(currentIndex + 1)) { // Check if previous index is already in fibonacci map
                calcNextFibonacciNum(currentIndex + 1); // We can save on some computing by using the simple addition computation
            }
            returnMessage = "Your current fibonacci index is now " + currentIndex; // Send current index info back to user;
        } catch (NumberFormatException e){
            LOGGER.error("Input format is not allowed for input: " + index + ". Index input can only be integers 0 or greater.\nMessage: " + e);
            returnMessage = "You can only input integers 0 or greater to jump to. Your index is still " + currentIndex + ".";
        } catch (IndexOutOfBoundsException e) {
            returnMessage = "Index goes below bounds for normal fibonacci sequence. Your index is still " + currentIndex + ".";
        }
        return returnMessage;
    }

    private void calcNextFibonacciNum(Integer index) { // Adds next fibonacci number in line and maps it to its index
        LOGGER.info("Your index is not in the fibonacci map. Generating fibonacci number for index " + index + " via 'next' method...");

        Integer newFibonacciNum = fibonacciMap.get(index - 2) + fibonacciMap.get(index - 1);
        fibonacciMap.put(index, newFibonacciNum);

        LOGGER.info("Adding pair (" + index + ", " + newFibonacciNum + ") to fibonacci map.");
    }

    private void calcPreviousFibonacciNum(Integer index) { // Gets previous fibonacci number in line and maps it to its index
        LOGGER.info("Your index is not in the fibonacci map. Generating fibonacci number for index " + index + " via 'previous' method...");

        Integer newFibonacciNum = fibonacciMap.get(index + 2) - fibonacciMap.get(index + 1);
        fibonacciMap.put(index, newFibonacciNum);

        LOGGER.info("Adding pair (" + index + ", " + newFibonacciNum + ") to fibonacci map.");
    }

    private void calcFibonacciExpr(Integer index) { // Uses a closed form expression to calculate the fibonacci number and map it to its index
        LOGGER.info("Calculating fibonacci number for index " + index + " using closed form expression...");
        double GOLDEN_RATIO = ( 1 + Math.sqrt(5) ) / 2;
        double GOLDEN_CONJUGATE = 1 - GOLDEN_RATIO;

        Integer fibonacciNum = (int) ( // Closed form fibonacci expression math
                ( Math.pow(GOLDEN_RATIO, index) - Math.pow(GOLDEN_CONJUGATE, index) )
                        / ( GOLDEN_RATIO - GOLDEN_CONJUGATE )
        );

        fibonacciMap.put(index, fibonacciNum);

        LOGGER.info("Adding pair (" + index + ", " + fibonacciNum + ") to fibonacci map.");
    }

}
