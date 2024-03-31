package com.flex.fibonaccicalculator.controller;

import com.flex.fibonaccicalculator.service.FibonacciService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FibonacciController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FibonacciController.class); // Add logger

    @Autowired
    private FibonacciService fibonacciService; // Allow Connection between controller and service layer

    @GetMapping("/current") // Current API endpoint
    public String getCurrent() {
        return fibonacciService.getCurrent();
    }

    @GetMapping("/next") // Next API endpoint
    public String getNext() {
        return fibonacciService.getNext();
    }

    @GetMapping("/previous") // Previous API endpoint
    public String getPrevious() {
        return fibonacciService.getPrevious();
    }

}
