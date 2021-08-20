package com.test.example.fibonacci;

import com.test.example.exceptions.InvalidSeed;

/**
 * Main service entry point
 */
public interface FibonacciService {
    /**
     * Classical calculation via Recursion
     * @param n seed value
     * @return Fibonacci value
     * @throws InvalidSeed Should not be thrown due to controller validations
     */
    FibonacciResult calculateFibonacci(String n) throws InvalidSeed;

    /**
     * Classical calculation via Recursion but with cache to avoid recalculation of values
     * @param n seed value
     * @return Fibonacci value
     * @throws InvalidSeed Should not be thrown due to controller validations
     * @throws OutOfMemoryError if seed too high
     */
    FibonacciResult calculateFibonacciWithCache(String n) throws InvalidSeed, OutOfMemoryError;

    /**
     * Iterative calculation
     * @param n seed value
     * @return Fibonacci value
     * @throws InvalidSeed Should not be thrown due to controller validations
     */
    FibonacciResult calculateFibonacciWithoutRecursion(String n) throws InvalidSeed;

    /**
     * Iterative calculation with strings (no special types like BigInteger).
     * @param n seed value
     * @return Fibonacci
     * @throws InvalidSeed Should not be thrown due to controller validations
     */
    FibonacciResult calculateFibonacciWithoutRecursionWithoutBigInteger(String n) throws InvalidSeed;

    /**
     * Concurrent calculation via Futures and Callables.
     * @param n seed value
     * @return Fibonacci value
     * @throws InvalidSeed Should not be thrown due to controller validations
     */
    FibonacciResult calculateFibonacciConcurrently(String n) throws InvalidSeed;
}
