package fr.insa.beuvron.tests.probaFLM;

import java.util.*;

public class ContinuousCDF_WithBounds {

  private final double[] xs; // all X points: xmin, sample..., xmax
  private final double[] Fs; // corresponding CDF values
  private final double[] a; // slopes
  private final double[] b; // intercepts
  private final int m; // number of segments (m = xs.length - 1)

  public ContinuousCDF_WithBounds(double[] sample, double xmin, double xmax) {
    if (xmin >= xmax) throw new IllegalArgumentException("xmin must be < xmax");

    int n = sample.length;
    double[] sorted = sample.clone();
    Arrays.sort(sorted);

    // build augmented points
    xs = new double[n + 2];
    Fs = new double[n + 2];

    xs[0] = xmin;
    Fs[0] = 0.0;

    for (int i = 1; i <= n; i++) {
      xs[i] = sorted[i - 1];
      Fs[i] = i / (double) (n + 1); // stays strictly below 1
    }

    xs[n + 1] = xmax;
    Fs[n + 1] = 1.0;

    // Build linear coefficients
    m = xs.length - 1;
    a = new double[m];
    b = new double[m];

    for (int i = 0; i < m; i++) {
      double dx = xs[i + 1] - xs[i];
      double dF = Fs[i + 1] - Fs[i];
      a[i] = dF / dx;
      b[i] = Fs[i] - a[i] * xs[i];
    }
  }

  /** Evaluate CDF */
  public double evaluate(double x) {
    if (x <= xs[0]) return 0.0;
    if (x >= xs[xs.length - 1]) return 1.0;

    // binary search for interval
    int idx = Arrays.binarySearch(xs, x);
    if (idx >= 0) {
      return Fs[idx];
    } else {
      idx = -idx - 2;
    }

    return a[idx] * x + b[idx];
  }

  /** Return (a,b) segments */
  public List<double[]> getSegments() {
    List<double[]> list = new ArrayList<>();
    for (int i = 0; i < m; i++) list.add(new double[] {a[i], b[i]});
    return list;
  }

  public double[] getPointsX() {
    return xs.clone();
  }

  public double[] getPointsF() {
    return Fs.clone();
  }
}
