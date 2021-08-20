package com.test.example.fibonacci.impl;

import com.test.example.exceptions.ConcurrencyException;
import com.test.example.exceptions.InvalidSeed;
import com.test.example.exceptions.SeedTooLarge;
import com.test.example.fibonacci.FibonacciResult;
import com.test.example.fibonacci.FibonacciService;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service Implementation. For all implementation below please see in main folder of the project
 * Postman collection and image of the Intellij Run/Debug configuration.
 */
@Slf4j
@Service
public class FibonacciServiceImpl implements FibonacciService {
    private Map<Integer, BigInteger> cache;

    @Value( "${classic_max_seed_value}" )
    private Integer classic_max_seed_value;

    @Value( "${cache_max_seed_value}" )
    private Integer cache_max_seed_value;

    @Value( "${max_seed_value}" )
    private Integer max_seed_value;

    /**
     * This classical recursive version works up to seed values of 40-42 in a decent amount of time.
     */
    @Override
    public FibonacciResult calculateFibonacci(String n) throws InvalidSeed, SeedTooLarge {

        long startTime = System.currentTimeMillis();
        BigInteger result;
        try {
            int seed = Integer.parseInt(n);
            if (seed > classic_max_seed_value) {
                throw new SeedTooLarge("Value " + seed + " is too large for this method, try alternatives");
            }
            result = fibonacci(seed);
        } catch(NumberFormatException nfe) {
            //should never be thrown due to validation in the controller
            throw  new InvalidSeed(nfe.getMessage());
        }
        long stopTime = System.currentTimeMillis();
        return new FibonacciResult(result.toString(), stopTime - startTime);
    }

    /**
     * This cached recursive version works very well up to just over 100000 as a seed value. With following tuning
     * values for JVM: -Xms512m -Xmx2g -Xss512m
     */
    @Override
    public FibonacciResult calculateFibonacciWithCache(String n) throws InvalidSeed, OutOfMemoryError, SeedTooLarge {
        cache = new HashMap<>();
        cache.put(0, BigInteger.ZERO);
        cache.put(1, BigInteger.ONE);
        cache.put(2, BigInteger.ONE);
        cache.put(3, BigInteger.valueOf(2));
        cache.put(4, BigInteger.valueOf(3));
        cache.put(5, BigInteger.valueOf(5));
        cache.put(6, BigInteger.valueOf(8));
        cache.put(7, BigInteger.valueOf(13));
        cache.put(8, BigInteger.valueOf(21));

        long startTime = System.currentTimeMillis();
        BigInteger result;
        try {
            int seed = Integer.parseInt(n);
            if (seed > cache_max_seed_value) {
                throw new SeedTooLarge("Value " + seed + " is too large for this method, try alternatives");
            }
            result = fibonacciWithCache(seed);
        } catch(NumberFormatException nfe) {
            //should never be thrown due to validation in the controller
            throw  new InvalidSeed(nfe.getMessage());
        } catch(StackOverflowError soe) {
            String  message = soe.getMessage();
            if (message == null) message = "Out of memory";
            throw new OutOfMemoryError(message);
        }
        long stopTime = System.currentTimeMillis();
        return new FibonacciResult(result.toString(), stopTime - startTime);
    }

    /**
     * Works faster and without any OOM exceptions. Tried until a seed value of 500000 which took about 4 seconds.
     * @param n seed value
     * @return Fibonacci value
     * @throws InvalidSeed should not be thrown
     */
    @Override
    public FibonacciResult calculateFibonacciWithoutRecursion(String n) throws InvalidSeed, SeedTooLarge {
        long startTime = System.currentTimeMillis();
        BigInteger result;
        try {
            int seed = Integer.parseInt(n);
            if (seed > max_seed_value) {
                throw new SeedTooLarge("Value " + seed + " is too large than maximum accepted");
            }
            result = fibonacciWithoutRecursion(seed);
        } catch(NumberFormatException nfe) {
            //should never be thrown due to validation in the controller
            throw  new InvalidSeed(nfe.getMessage());
        }
        long stopTime = System.currentTimeMillis();
        return new FibonacciResult(result.toString(), stopTime - startTime);
    }

    /**
     * Works even faster without any OOM exceptions. Tried until a seed value of 500000 which took about 2.6 seconds.
     * @param n seed value
     * @return Fibonacci number
     * @throws InvalidSeed should not be thrown
     */
    @Override
    public FibonacciResult calculateFibonacciWithoutRecursionWithoutBigInteger(String n) throws InvalidSeed, SeedTooLarge {
        long startTime = System.currentTimeMillis();
        String result;
        try {
            int seed = Integer.parseInt(n);
            if (seed > max_seed_value) {
                throw new SeedTooLarge("Value " + seed + " is too large than maximum accepted");
            }
            result = fibonacciWithoutRecursionWithoutBigInteger(seed);
        } catch(NumberFormatException nfe) {
            //should never be thrown due to validation in the controller
            throw  new InvalidSeed(nfe.getMessage());
        }
        long stopTime = System.currentTimeMillis();
        return new FibonacciResult(result, stopTime - startTime);
    }

    @Override
    public FibonacciResult calculateFibonacciConcurrently(String n) throws InvalidSeed, ConcurrencyException, SeedTooLarge {
        long startTime = System.currentTimeMillis();
        BigInteger result;
        try {
            int seed = Integer.parseInt(n);
            if (seed > max_seed_value) {
                throw new SeedTooLarge("Value " + seed + " is too large than maximum accepted");
            }
            result = fibonacciAsync(seed);
        } catch(NumberFormatException nfe) {
            //should never be thrown due to validation in the controller
            throw  new InvalidSeed(nfe.getMessage());
        }
        long stopTime = System.currentTimeMillis();
        return new FibonacciResult(result.toString(), stopTime - startTime);
    }

    /**
     * Classical recursive implementation
     * @param n seed value
     * @return Fibonacci number
     */
    private BigInteger fibonacci(int n) {
        if (n == 0) return BigInteger.ZERO;
        if (n == 1 || n == 2) {
            return BigInteger.ONE;
        }

        return fibonacci(n-2).add(fibonacci(n-1));
    }

    /**
     * Recursive but avoids recalculation of lower values
     * @param n seed number
     * @return Fibonacci number
     */
    private BigInteger fibonacciWithCache(int n) throws StackOverflowError {
        if (n == 0) return BigInteger.ZERO;
        if (n == 1 || n == 2) {
            return BigInteger.ONE;
        }

        Integer first = n - 1;
        Integer second = n - 2;
        if (cache.get(first) == null) cache.put(first, fibonacciWithCache(n-1));
        if (cache.get(second) == null) cache.put(second, fibonacciWithCache(n-2));

        return cache.get(first).add(cache.get(second));
    }

    /**
     * Variant without recursion.
     * @param n seed value
     * @return Fibonacci number
     */
    private BigInteger fibonacciWithoutRecursion(int n) {
        if ( n == 0 ) return BigInteger.ZERO;
        if ( n == 1) return BigInteger.ONE;

        BigInteger first = BigInteger.ZERO;
        BigInteger second = BigInteger.ONE;
         BigInteger tempValue = BigInteger.ONE;
        for(int i = 2; i <= n; i++) {
            tempValue =  first.add(second);
            first = second;
            second = tempValue;
        }

        return tempValue;
    }

    /**
     * Variant without recursion and without special types like BigInteger
     * @param n seed value
     * @return Fibonacci number
     */
    private String fibonacciWithoutRecursionWithoutBigInteger(int n) {
        if ( n == 0 ) return "0";
        if ( n == 1) return "1";

        String first = "0";
        String second = "1";
        String tempValue = "1";
        for(int i = 2; i <= n; i++) {
            tempValue =  calculateSumOfStringRepresentedNumbers(first, second);
            first = second;
            second = tempValue;
        }

        return tempValue;
    }

    /**
     * Adds two positive integers represented as strings.
     * @param str1 first number
     * @param str2 second number
     * @return sum of ttwo numbers
     */
    private String calculateSumOfStringRepresentedNumbers(String str1, String str2) {
        // Before proceeding further, make sure length of str2 is larger.
        if (str1.length() > str2.length()) {
            String t = str1;
            str1 = str2;
            str2 = t;
        }

        // Take an empty String for storing result
        StringBuilder str = new StringBuilder();

        // Calculate length of both String
        int n1 = str1.length(), n2 = str2.length();

        // Reverse both of Strings
        str1 = new StringBuilder(str1).reverse().toString();
        str2 = new StringBuilder(str2).reverse().toString();

        int carry = 0;
        for (int i = 0; i < n1; i++) {
            // compute sum of current digits and carry
            int sum = ((str1.charAt(i) - '0') + (str2.charAt(i) - '0') + carry);
            str.append((char) (sum % 10 + '0'));

            // Calculate carry for next step
            carry = sum / 10;
        }

        // Add remaining digits of larger number
        for (int i = n1; i < n2; i++) {
            int sum = (str2.charAt(i) - '0') + carry;
            str.append((char) (sum % 10 + '0'));
            carry = sum / 10;
        }

        // Add remaining carry
        if (carry > 0) {
            str.append((char) (carry + '0'));
        }

        // reverse result String
        str = new StringBuilder(new StringBuilder(str.toString()).reverse().toString());

        return str.toString();
    }

    /**
     * Calculate Fibonacci with formula Fn = 4 * Fn-3 + Fn-6
     * @param first Fibonacci Fn-3
     * @param second Fibonacci Fn-6
     * @return Fibonacci value
     */
    private static Pair<Integer, BigInteger> calculateSpecificFibonacci (Pair<Integer, BigInteger> first, Pair<Integer, BigInteger> second) {
            BigInteger fibonacci  = first.getValue1().multiply(BigInteger.valueOf(4)).add(second.getValue1());
            return Pair.with(first.getValue0() + 3, fibonacci);
    }

    /**
     * Executes a specific callable
     * @param first Fibonacci Fn-3
     * @param second Fibonacci Fn-6
     * @param executorService service executor
     * @return Future of that execution
     */
    private CompletableFuture<Pair<Integer, BigInteger>> calculateSpecificFibonacciAsync (Pair<Integer, BigInteger>  first,
                                                                                          Pair<Integer, BigInteger>  second,
                                                                                          ExecutorService executorService) {
        return CompletableFuture.supplyAsync(() -> calculateSpecificFibonacci(first, second), executorService);
    }

    /**
     * Uses a parallelization based on Project Euler #2
     * https://codereview.stackexchange.com/questions/136304/project-euler-2-even-fibonacci-numbers/136327#136327
     * and caching.
     * Basically one can observe that Fibonacci can be split into three "independent" sequences starting with
     * Fibonacci 8
     *          𝐹8=4⋅𝐹5+𝐹2
     *          𝐹9=4⋅𝐹6+𝐹3
     *          𝐹10=4⋅𝐹7+𝐹4
     *  and this relation continues. Another observation, is that Fibonacci Fn does not depend on Fn-1 and Fn-2, so if
     *  seed is not divisible by 3 than there is no point to calculate remaining Fn-1 and Fn-2.
     * @param n seed value
     * @return Fibonacci value
     */
    private BigInteger fibonacciAsync(int n) throws ConcurrencyException {
        BigInteger result = BigInteger.ZERO;

        // prepare cache
        cache = new HashMap<>();
        cache.put(0, BigInteger.ZERO);
        cache.put(1, BigInteger.ONE);
        cache.put(2, BigInteger.ONE);
        cache.put(3, BigInteger.valueOf(2));
        cache.put(4, BigInteger.valueOf(3));
        cache.put(5, BigInteger.valueOf(5));
        cache.put(6, BigInteger.valueOf(8));
        cache.put(7, BigInteger.valueOf(13));

        // for n < 8 respond immediately
        if (cache.containsKey(n)) return cache.get(n);

        // for  larger values
        ExecutorService executorService = Executors.newFixedThreadPool(9);
        int counter = 8;

        // calculate concurrently in groups of three threads until find result or closing on result
        // each time the cache is updated
        while (counter + 3 <= n) {
            CompletableFuture<Pair<Integer, BigInteger>> Fn_2 =
                    calculateSpecificFibonacciAsync(Pair.with(counter - 3, cache.get(counter - 3)),
                                                    Pair.with(counter - 6, cache.get(counter - 6)), executorService);
            CompletableFuture<Pair<Integer, BigInteger>> Fn_1 =
                    calculateSpecificFibonacciAsync(Pair.with(counter - 2, cache.get(counter - 2)),
                            Pair.with(counter - 5, cache.get(counter - 5)), executorService);
            CompletableFuture<Pair<Integer, BigInteger>> Fn =
                    calculateSpecificFibonacciAsync(Pair.with(counter - 1, cache.get(counter - 1)),
                            Pair.with(counter - 4, cache.get(counter - 4)), executorService);
            CompletableFuture.allOf(Fn_2, Fn_1, Fn).join();
            try {
                cache.put(Fn_2.get().getValue0(), Fn_2.get().getValue1());
                cache.put(Fn_1.get().getValue0(), Fn_1.get().getValue1());
                cache.put(Fn.get().getValue0(), Fn.get().getValue1());
                //save intermediary result - could be final one
                result = Fn.get().getValue1();
            } catch (InterruptedException | ExecutionException ie) {
                throw new ConcurrencyException(ie.getMessage());
            }
            counter += 3;
        }
        //we ignore calculation of Fn-1 and Fn-2 (or just Fn-1)
        if ((counter < n) || (counter == n && cache.get(n) == null)) {
            CompletableFuture<Pair<Integer, BigInteger>> Fn =
                    calculateSpecificFibonacciAsync(Pair.with(n - 3, cache.get(n - 3)),
                            Pair.with(n - 6, cache.get(n - 6)), executorService)
                            .whenComplete((p, error) -> {
                                //put logging here
                                if (error != null) log.info("Completion error: " + error);
                            });
            try {
                result = Fn.get().getValue1();
            } catch (InterruptedException | ExecutionException ie) {
                throw new ConcurrencyException(ie.getMessage());
            }
        }

        return result;

    }
}
