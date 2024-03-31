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
    public Integer getCurrent() {
        return fibonacciMap.get(currentIndex); // Uses the 'currentIndex' to retrieve the fibonacci number associated with it in the hashMap
    }

    @Override
    public Integer getNext() {
        return 0;
    }

    @Override
    public Integer getPrevious() {
        return 0;
    }
}
