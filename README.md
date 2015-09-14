## Description

Scala BigCollections offers collections indexed by Long values, meaning they are not limited to ~2 billion elements like the standard collections.

## Motivation

The goal of Scala BigCollections is to provide a few simple collections that can hold as many elements as can actually fit in memory.
With hundreds of gigabytes of memory available in modern servers, some applications can be kept operationally simple by perform all computations on a single node.
The initially envisioned use case for BigCollections is a analysis software for epigenetic markers on the human genome.
Since the reference genome is 3 billion base pairs, a bitmap or integer array of Int.MaxValue won't work.
Formats like Bed and BigBed are the standard transports for this type of information, but they don't seem to directly translate to an in-memory representation that's simple and easy to perform calculations on at scale with a large number of samples.

## Future work

Hope to look at the compression technique used by H2O.
Improved testing suite - my laptop doesn't have hundreds of gigabytes necessary to perform all possible tests.

## Features

  * BitMap offers functionality similar to Redis bitmap. Used successfully in production for tracking cardinality.
  * BigBitMap offers control over individual bits, indexed by Long.
  * BigCollection trait offers a reasonably easy way to create Long-indexed collections for other types.
  * BigVector provides a Long-indexed collection of Double.
  * All BigCollections provide elementwise math operations equivalent to the built-in operators
  * Operations provided include +, - , *, &, |, ^ and these simply do what you'd expect, on every element of the BigCollection
  * Provides some examples of working with bytes in Scala

## Language Features and Libraries Used

  * Trait containing common methods for override
  * Bit shift to determine id of sub-array faster than division
  * Scalatest unit testing specifications
  
## License

This project uses the Apache 2.0 license. Read LICENSE file.

## Authors and Copyright

Copyright (C) 2015 Jason Mar
