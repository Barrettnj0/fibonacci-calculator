package com.flex.fibonaccicalculator.controller;

import com.flex.fibonaccicalculator.service.FibonacciService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FibonacciController {

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

    @GetMapping("/jump/{index}") // Jump API which allows user to pick any index they want to jump to
    public String getJump(@PathVariable("index") String index) {
        return fibonacciService.getJump(index);
    }

}
