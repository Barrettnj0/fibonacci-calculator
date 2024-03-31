package com.flex.fibonaccicalculator.service.impl;

import com.flex.fibonaccicalculator.service.FibonacciService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FibonacciServiceImpl implements FibonacciService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FibonacciServiceImpl.class); // Add logger

    @Override
    public Integer getCurrent() {
        return 0;
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
