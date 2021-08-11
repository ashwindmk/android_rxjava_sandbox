# Flowable

Used for handlling back-pressure.

## Backpressure Strategy

Provided while creating the Flowable.
Used when the subscriber has not handled the back-pressure.

1. DROP: Drops the value on emit if the consumer can't keep up.
2. LATEST: Overwrites and keeps only the latest values if the consumer can't keep up.
3. BUFFER: Keeps all the emitted values in the buffer. Take care of OOM Error.
4. ERROR: Throws error if the consumer can't keep up.
5. MISSING: Same as ERROR but error will not be thrown by the flowable, it needs to be handled by the subscriber, by default the subscriber throws error.


**Note**: If the subscriber handles back-pressure (onBackpressureBuffer), then this strategy will not be used.

## Subscriber

We can subscribe using `FlowableSubscriber` or `ResourceSubscriber` for Flowable.

## References

- https://www.baeldung.com/rxjava-2-flowable
- https://proandroiddev.com/rxjava-backpressure-and-why-you-should-care-369c5242c9e6
