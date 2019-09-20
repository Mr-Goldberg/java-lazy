package com.goldberg.lazy;

import org.junit.Test;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import static com.goldberg.lazy.Lazy.lazy;
import static com.google.common.truth.Truth.assertThat;

public class LazyTest
{
    private static final int THREADS = 8;
    private static final int LOOPS = 600;

    private int funcValue = 0; // Value is reset to 0 for each @Test function.

    private int func()
    {
        try
        {
            // Simulate work will cause threads to wait on synchronization object

            Thread.sleep(20);
        }
        catch (InterruptedException ex)
        {
            throw new RuntimeException(ex);
        }

        // Real work

        return funcValue++;
    }

    @Test
    public void lazyLinear()
    {
        Supplier<Integer> lazy = lazy(() -> func());
        final int initialValue = lazy.get();
        for (int i = 0; i < 10; ++i)
        {
            assertThat(lazy.get()).isEqualTo(initialValue);
        }

        // Make sure function was called only once

        assertThat(funcValue).isEqualTo(1);
    }

    @Test
    public void lazyMultithreaded() throws InterruptedException, ExecutionException
    {
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(Executors.newFixedThreadPool(THREADS));
        int executions = 0;
        for (int loop = 0; loop < LOOPS; ++loop)
        {
            Supplier<Integer> lazy = lazy(() -> func());
            for (int task = 0; task < THREADS; ++task)
            {
                completionService.submit(() -> lazy.get());
            }

            Integer initialResult = null;
            for (int task = 0; task < THREADS; ++task)
            {
                Integer result = completionService.take().get();
                if (initialResult == null)
                {
                    initialResult = result;
                }

                assertThat(result).isNotNull();
                assertThat(result).isEqualTo(initialResult);

                ++executions;
            }
        }

        assertThat(funcValue).isEqualTo(LOOPS);
        assertThat(executions).isEqualTo(THREADS * LOOPS);
    }
}
