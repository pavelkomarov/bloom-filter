`bf.tex` is the actual report. Compiles to `bf.pdf`. I recommend reading this.

The assignment description is `Project-BloomFilters.pdf`.

`Analysis.java` tests my hash function to ensure it's uniform. It outputs data read and plotted with `Analysis.m`. That yields the noise and histogram plots.

`BloomFilter.java` implements the actual Bloom Filter and outputs data from simulations across several values for parameter `c`. `BloomFilter.m` reads and plots this data, yielding `loglinear` and `loglog`.