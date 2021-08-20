package com.test.example.fibonacci;


import com.test.example.validation.ValuesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/api")
public class FibonacciController {

    @Autowired
    FibonacciService fibService;


    @GetMapping(value="/fibonacci", produces = MediaType.APPLICATION_JSON_VALUE)
    public FibonacciResult getFibonacciNumber(@Valid
                                              @NotNull(message = "Number missing")
                                              @NotEmpty(message = "Number cannot be empty")
                                              @Pattern(regexp="^[1-9][0-9]*$",
                                                       message = "Number must be formed only from digits and cannot start with zero")
                                              @RequestParam(value="seed")  String seed,
                                              @RequestParam(value="version")
                                              @ValuesAllowed(values = {
                                                      "classic",
                                                      "cache",
                                                      "iterative",
                                                      "iterative-string",
                                                      "concurrent"
                                              })
                                                      String version) {
        if ("cache".equals(version)) return fibService.calculateFibonacciWithCache(seed);
        else if ("iterative".equals(version)) return fibService.calculateFibonacciWithoutRecursion(seed);
        else if ("iterative-string".equals(version)) return fibService.calculateFibonacciWithoutRecursionWithoutBigInteger(seed);
        else if ("concurrent".equals(version)) return fibService.calculateFibonacciConcurrently(seed);
        return fibService.calculateFibonacci(seed);
    }
}
