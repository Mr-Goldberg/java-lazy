package com.goldberg.lazy;

import java.util.function.Supplier;

/**
 * Thread-safe lazy implementation.
 *
 * Inspired by Kotlin 'lazy' and:
 * https://stackoverflow.com/questions/35331327/does-java-8-have-cached-support-for-suppliers
 * \@Holder answer: https://stackoverflow.com/a/35335467/5035991
 */
public final class Lazy
{
    public static <T> Supplier<T> lazy(final Supplier<T> valueSupplier)
    {
        // Generate new implementation of the Supplier class per each call to lazy(...)

        return new Supplier<T>()
        {
            private Supplier<T> delegate = this::initialize;
            private boolean isInitialized;

            /**
             * First call will execute {@link #initialize()}, which is synchronized.
             * Subsequent calls will just return the value.
             * It is safe in multithreaded environment, because reference assignment in Java is atomic, and value calculation + assignment are synchronized.
             */
            @Override
            public T get()
            {
                return delegate.get();
            }

            private synchronized T initialize()
            {
                if (!isInitialized)
                {
                    final T value = valueSupplier.get();

                    // Replace delegate by lambda providing value

                    delegate = () -> value;
                    isInitialized = true;
                }

                return delegate.get();
            }
        };
    }
}
