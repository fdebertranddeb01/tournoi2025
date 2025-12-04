package fr.insa.beuvron.tests.probaFLM;

import java.util.*;

public class ContinuousCDF_FLMC {

    private final double[] xs;     // sorted sample
    private final double[] a;      // slopes
    private final double[] b;      // intercepts
    private final int n;

    public ContinuousCDF_FLMC(double[] sample) {
        if (sample.length == 0)
            throw new IllegalArgumentException("Sample must not be empty.");

        // 1. Sort sample
        this.n = sample.length;
        this.xs = sample.clone();
        Arrays.sort(this.xs);

        // We have n points -> n pieces minus 1
        this.a = new double[n - 1];
        this.b = new double[n - 1];

        // 2. Compute empirical CDF values at each sorted point
        double[] F = new double[n];
        for (int i = 0; i < n; i++)
            F[i] = (i + 1) / (double) n;

        // 3. Build linear segments
        for (int i = 0; i < n - 1; i++) {
            double x1 = xs[i];
            double x2 = xs[i+1];
            double F1 = F[i];
            double F2 = F[i+1];

            // slope
            a[i] = (F2 - F1) / (x2 - x1);

            // intercept
            b[i] = F1 - a[i] * x1;
        }
    }

    /** Evaluate FLMC CDF at x */
    public double evaluate(double x) {
        if (x <= xs[0]) return 0.0;
        if (x >= xs[n - 1]) return 1.0;

        // find segment index
        int idx = Arrays.binarySearch(xs, x);
        if (idx >= 0) {
            // x exactly on a data point
            return (idx + 1) / (double) n;
        } else {
            idx = -idx - 2; // segment between xs[idx], xs[idx+1]
        }

        // linear interpolation
        return a[idx] * x + b[idx];
    }

    /** Retrieve segments as (a, b) couples */
    public List<double[]> getSegments() {
        List<double[]> segs = new ArrayList<>();
        for (int i = 0; i < a.length; i++)
            segs.add(new double[] { a[i], b[i] });
        return segs;
    }

    /** Optional: return sorted xs */
    public double[] getSortedSample() {
        return xs.clone();
    }
}
