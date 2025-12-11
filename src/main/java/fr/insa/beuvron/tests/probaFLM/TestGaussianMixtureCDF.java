package fr.insa.beuvron.tests.probaFLM;

import java.util.Arrays;
import java.util.Random;

public class TestGaussianMixtureCDF {

  public static void main(String[] args) {

    int N = 10000; // taille de l'échantillon
    double p = 0.5; // poids du mélange (50% - 50%)

    double[] sample = new double[N];
    Random rng = new Random();

    // -------------------------
    // 1) Génération du mélange gaussien
    // X = G1 avec prob. p
    // X = G2 avec prob. (1-p)
    // -------------------------
    for (int i = 0; i < N; i++) {

      if (rng.nextDouble() < p) {
        // G1 ~ N(0,1)
        sample[i] = rng.nextGaussian();
      } else {
        // G2 ~ N(3,1)
        sample[i] = 3 + rng.nextGaussian();
      }
    }

    // -------------------------
    // 2) xmin et xmax connus
    // Prenons par exemple :
    // xmin = min(sample)-marge
    // xmax = max(sample)+marge
    // -------------------------
    double min = Arrays.stream(sample).min().getAsDouble();
    double max = Arrays.stream(sample).max().getAsDouble();

    double xmin = min - 0.1;
    double xmax = max + 0.1;

    // -------------------------
    // 3) Construction de la CDF FLMC
    // -------------------------
    ContinuousCDF_WithBounds cdf = new ContinuousCDF_WithBounds(sample, xmin, xmax);

    // -------------------------
    // 4) Évaluations de la CDF
    // -------------------------
    System.out.println("=== Test CDF du mélange gaussien ===");

    double[] testX = {-2, -1, 0, 1, 2, 3, 4, 5};

    for (double x : testX) {
      System.out.printf("F(%.3f) = %.6f%n", x, cdf.evaluate(x));
    }

    // -------------------------
    // 5) Affichage de quelques segments (a,b)
    // -------------------------
    System.out.println("\nQuelques segments (a,b) :");
    int k = 0;
    for (double[] seg : cdf.getSegments()) {
      if (k++ == 8) break;
      System.out.printf("a = %.6f, b = %.6f%n", seg[0], seg[1]);
    }
    System.out.println(new double[1].getClass().getSimpleName());
    System.out.println(new double[1].getClass().getCanonicalName());
  }
}
