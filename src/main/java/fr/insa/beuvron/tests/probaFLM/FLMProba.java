package fr.insa.beuvron.tests.probaFLM;

import java.util.Arrays;

public class FLMProba {

    // -------------------------
    // Public fields / results
    // -------------------------
    private final double xmin;
    private final double xmax;
    private final int K;    // number of knot-intervals (so K+1 knots)
    private final int M;    // number of histogram bins

    private double[] knots; // length K+1
    private double[] y;     // estimated density values at knots, length K+1
    private double[] bins;  // length M+1, bin edges
    private double[] pHat;  // empirical bin frequencies length M
    private double[][] A;   // M x (K+1) matrix

    // -------------------------
    // Constructor
    // -------------------------
    public FLMProba(double xmin, double xmax, int K, int M) {
        if (!(xmax > xmin)) throw new IllegalArgumentException("xmax must be > xmin");
        this.xmin = xmin;
        this.xmax = xmax;
        if (K < 1) throw new IllegalArgumentException("K>=1 required");
        this.K = K;
        this.M = Math.max(1, M);
    }

    // -------------------------
    // Public API
    // -------------------------
    /**
     * Fit FLMC density to sample x (array of doubles).
     * After calling fit(...), call getKnots(), getY(), evaluate(x) etc.
     */
    public void fit(double[] x) {
        buildKnots();
        buildBinsAndHistogram(x);
        buildA();
        fitByProjectedGradient();
    }

    public double[] getKnots() { return knots.clone(); }
    public double[] getY() { return y.clone(); }
    public double[] getBins() { return bins.clone(); }
    public double[] getBinFrequencies() { return pHat.clone(); }

    /** Evaluate the piecewise-linear density at a single point xx. */
    public double evaluate(double xx) {
        if (xx <= xmin) return (xx == xmin) ? y[0] : 0.0;
        if (xx >= xmax) return (xx == xmax) ? y[K] : 0.0;
        // find interval between knots
        int k = Arrays.binarySearch(knots, xx);
        if (k >= 0) return y[k];
        int idx = -k - 2; // index s.t. knots[idx] < xx < knots[idx+1]
        double t0 = knots[idx], t1 = knots[idx+1];
        double w = (xx - t0) / (t1 - t0);
        return (1 - w) * y[idx] + w * y[idx+1];
    }

    // -------------------------
    // Internal building blocks
    // -------------------------
    private void buildKnots() {
        knots = new double[K+1];
        for (int i = 0; i <= K; i++) {
            knots[i] = xmin + (xmax - xmin) * ( (double) i / K );
        }
    }

    private void buildBinsAndHistogram(double[] x) {
        bins = new double[M+1];
        for (int j = 0; j <= M; j++) bins[j] = xmin + (xmax - xmin) * ( (double) j / M );
        int[] counts = new int[M];
        for (double v : x) {
            if (v < xmin || v > xmax) continue; // ignore out-of-range
            // find bin index: if v==xmax put it in last bin-1
            int bi = (v == xmax) ? (M-1) : (int) Math.floor( (v - xmin) / (xmax - xmin) * M );
            if (bi < 0) bi = 0;
            if (bi >= M) bi = M-1;
            counts[bi]++;
        }
        pHat = new double[M];
        double n = 0.0;
        for (int c : counts) n += c;
        if (n <= 0) n = 1.0; // avoid div by zero if empty sample
        for (int j = 0; j < M; j++) pHat[j] = counts[j] / n;
    }

    private void buildA() {
        A = new double[M][K+1];
        // For each bin j = [u,v], integrate piecewise linear basis contributions
        for (int j = 0; j < M; j++) {
            double u = bins[j], v = bins[j+1];
            // overlap with each knot interval [t_{k-1}, t_k]
            for (int k = 1; k <= K; k++) {
                double t0 = knots[k-1], t1 = knots[k];
                double left = Math.max(u, t0);
                double right = Math.min(v, t1);
                if (right > left) {
                    double[] coef = areaOnSegment(left, right, t0, t1);
                    A[j][k-1] += coef[0];
                    A[j][k  ] += coef[1];
                }
            }
        }
        // note: A[j,*] are integrals (not normalized by bin width)
        // But pHat are frequencies (mass) per bin; A gives mass contributions consistent with that.
    }

    /**
     * Integrals of basis functions on [left,right] inside knot interval [t0,t1].
     * Returns {I_left, I_right} where integral = I_left*y_left + I_right*y_right.
     */
    private double[] areaOnSegment(double left, double right, double t0, double t1) {
        double dt = t1 - t0;
        // phi_left(x) = (t1 - x)/dt
        // phi_right(x) = (x - t0)/dt
        // I_left = ∫_{left}^{right} (t1 - x)/dt dx = [t1 x - x^2/2] / dt | left^right
        double I_left = (t1 * (right - left) - 0.5 * (right*right - left*left)) / dt;
        // I_right = ∫_{left}^{right} (x - t0)/dt dx = [x^2/2 - t0 x] / dt | left^right
        double I_right = (0.5 * (right*right - left*left) - t0 * (right - left)) / dt;
        return new double[]{I_left, I_right};
    }

    // -------------------------
    // Solver: projected gradient descent
    // -------------------------
    private void fitByProjectedGradient() {
        int nVars = K+1;
        // c vector for integral constraint: c_k = sum over intervals adjacent dt/2
        double[] c = new double[nVars];
        for (int k = 1; k <= K; k++) {
            double dt = knots[k] - knots[k-1];
            c[k-1] += dt / 2.0;
            c[k  ] += dt / 2.0;
        }

        // initial guess: uniform density value = 1/(xmax-xmin)
        y = new double[nVars];
        double uniform = 1.0 / (xmax - xmin);
        for (int i = 0; i < nVars; i++) y[i] = uniform;

        // Precompute A^T * A and A^T * p for faster gradients (small-medium sizes)
        double[][] ATA = new double[nVars][nVars];
        double[] ATp = new double[nVars];
        for (int i = 0; i < nVars; i++) {
            for (int j = 0; j < nVars; j++) {
                double s = 0.0;
                for (int m = 0; m < M; m++) s += A[m][i] * A[m][j];
                ATA[i][j] = s;
            }
            double sp = 0.0;
            for (int m = 0; m < M; m++) sp += A[m][i] * pHat[m];
            ATp[i] = sp;
        }

        // objective: 0.5 * ||A y - pHat||^2 ; grad = ATA*y - ATp
        double step = 1e-3;   // step size (tunable)
        int maxIter = 20000;
        double tol = 1e-9;
        double prevObj = Double.POSITIVE_INFINITY;

        for (int iter = 0; iter < maxIter; iter++) {
            // compute gradient g = ATA*y - ATp
            double[] g = new double[nVars];
            for (int i = 0; i < nVars; i++) {
                double s = 0.0;
                for (int j = 0; j < nVars; j++) s += ATA[i][j] * y[j];
                g[i] = s - ATp[i];
            }
            // gradient step
            for (int i = 0; i < nVars; i++) y[i] -= step * g[i];

            // projection: enforce nonnegativity
            for (int i = 0; i < nVars; i++) if (y[i] < 0.0) y[i] = 0.0;

            // projection: enforce c^T y = 1 by scaling (c_k > 0, so scaling preserves nonnegativity)
            double sC = 0.0;
            for (int i = 0; i < nVars; i++) sC += c[i] * y[i];
            if (sC <= 0) {
                // reset to uniform if collapsed to zero
                for (int i = 0; i < nVars; i++) y[i] = uniform;
            } else {
                double factor = 1.0 / sC;
                for (int i = 0; i < nVars; i++) y[i] *= factor;
            }

            // compute objective value
            double obj = 0.0;
            // compute residual r = A*y - pHat (vector of length M)
            for (int m = 0; m < M; m++) {
                double s = 0.0;
                for (int j = 0; j < nVars; j++) s += A[m][j] * y[j];
                double r = s - pHat[m];
                obj += 0.5 * r * r;
            }

            // check convergence (relative)
            if (Math.abs(prevObj - obj) < tol * Math.max(1.0, prevObj)) break;
            prevObj = obj;

            // simple adaptive step: if not decreasing occasionally shrink step
            if (iter % 500 == 0 && iter > 0) {
                // optionally reduce step for stability
                step *= 0.9;
            }
        }

        // final safety renormalization to ensure exact integral 1
        double sCfinal = 0.0;
        for (int i = 0; i < nVars; i++) sCfinal += c[i] * y[i];
        if (sCfinal <= 0) throw new RuntimeException("fitted integral collapsed to zero");
        for (int i = 0; i < nVars; i++) y[i] /= sCfinal;
    }

    // -------------------------
    // Usage example (main)
    // -------------------------
    public static void main(String[] args) {
        // Example synthetic data: sample from triangular-like distribution in [0,1]
        int n = 1000;
        double[] x = new double[n];
        for (int i = 0; i < n; i++) {
            // simple mixture to create non-uniform sample:
            double u = Math.random();
            x[i] = 0.2 * u + 0.8 * Math.random(); // arbitrary
        }

        double xmin = 0.0;
        double xmax = 1.0;
        int K = 12;   // number of knot intervals
        int M = 40;   // histogram bins

        FLMProba fitter = new FLMProba(xmin, xmax, K, M);
        fitter.fit(x);

        System.out.println("Knots: " + Arrays.toString(fitter.getKnots()));
        System.out.println("Estimated y (density at knots): " + Arrays.toString(fitter.getY()));

        // evaluate density at some points
        for (double t = 0.0; t <= 1.0; t += 0.25) {
            System.out.printf("f(%.3f) = %.6f\n", t, fitter.evaluate(t));
        }
    }
}
