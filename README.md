# RHHH-Algorithm
Randomized Hierarchical Heavy Hitters Algorithm implemented in Java using Lossy counting for Finding Frequent Items in Data Streams.

In this context we will be dealing with specific IP addresses, their prefixes and the frequencies at which they send packets.

A HHH algorithm is an algorithm, which when given a group S, returns the group of its Hierarchical Heavy Hitters

Known algorithms are currently too slow to cope with line speeds. 

One of the solutions for the current HHH algorithms, is an algorithm named Randomized HHH (RHHH). This algorithm updates only one level, as opposed to other algorithms, which update all levels.

It is known that Approximate HHH algorithms find all the HHH prefixes but they also return non HHH prefixes (false positives).

The outcome for relatively small amounts of data isn’t ideal, but as the amount of data increases, the outcome improves and converges.

The algorithm implemented is a 1-d(source) algorithm. 

In the following traces you can find one source only trace, where each line contains four integers which together constitute the soruce ip address(1-d). The second trace is a source/destination set with eight numbers, the first four are the source IP address and the next four are the destination IP address.(2-d)

Traces:https://www.cs.bgu.ac.il/~tanm191/wiki.files/Traces.7z
