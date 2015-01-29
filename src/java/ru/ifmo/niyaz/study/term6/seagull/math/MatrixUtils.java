package ru.ifmo.niyaz.study.term6.seagull.math;

import org.apache.commons.math.MathRuntimeException;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Нияз
 * Date: 21.06.12
 * Time: 0:44
 * To change this template use File | Settings | File Templates.
 */
public class MatrixUtils {

    static final Random MATRIX_RANDOM = new Random();

    static double[] gauss(double[][] a, double[] b) {
        a = ArrayUtils.arrayCopy2D(a);
        b = b.clone();
        int n = a.length;
        for (int i = 0; i < n; i++) {
            if (a[i][i] == 0) {
                for (int j = i + 1; j < n; j++) {
                    if (a[i][j] != 0) {
                        {
                            double[] t = a[i];
                            a[i] = a[j];
                            a[j] = t;
                        }
                        {
                            double t = b[i];
                            b[i] = b[j];
                            b[j] = t;
                        }
                        break;
                    }
                }
                if (a[i][i] == 0) {
                    throw new MathRuntimeException("matrix has zero determinant");
                }
            }
            double val = a[i][i];
            for (int k = 0; k < n; k++) {
                a[i][k] /= val;
            }
            b[i] /= val;
            for (int j = i + 1; j < n; j++) {
                double mul = a[j][i];
                for (int k = 0; k < n; k++) {
                    a[j][k] -= mul * a[i][k];
                }
                b[j] -= mul * b[i];
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                double mul = a[j][i];
                for (int k = 0; k < n; k++) {
                    a[j][k] -= mul * a[i][k];
                }
                b[j] -= mul * b[i];
            }
        }
        return b;
    }

    static double[] simpleIterations(double[][] a, double[] b, double eps) {
        a = ArrayUtils.arrayCopy2D(a);
        b = b.clone();
        int n = a.length;
        for (int i = 0; i < n; i++) {
            a[i][i]++;
        }
        double[] x = new double[n];
        boolean ok;
        do {
            double[] newX = new double[n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    newX[i] += a[i][j] * x[j];
                }
                newX[i] -= b[i];
            }
            double change = 0;
            for (int i = 0; i < n; i++) {
                double dx = x[i] - newX[i];
                change += dx * dx;
            }
            ok = change > eps * eps;
            x = newX;
        } while (ok);
        return x;
    }

    static double[] seidel(double[][] a, double[] b, double eps) {
        a = ArrayUtils.arrayCopy2D(a);
        b = b.clone();
        int n = a.length;
        double[] x = new double[n];
        boolean ok;
        do {
            double change = 0;
            for (int i = 0; i < n; i++) {
                double old = x[i];
                x[i] = b[i];
                for (int j = 0; j < n; j++) {
                    if (i == j) {
                        continue;
                    }
                    x[i] -= x[j] * a[i][j];
                }
                x[i] /= a[i][i];
                change += (old - x[i]) * (old - x[i]);
            }
            ok = change > eps * eps;
        } while (ok);
        return x;
    }

    static double[] successiveOverRelaxation(double[][] a, double[] b, double eps, double relaxationFactor) {
        a = ArrayUtils.arrayCopy2D(a);
        b = b.clone();
        int n = a.length;
        double[] x = new double[n];
        boolean ok;
        do {
            double change = 0;
            for (int i = 0; i < n; i++) {
                double old = x[i];
                x[i] = b[i];
                for (int j = 0; j < n; j++) {
                    if (i == j) {
                        continue;
                    }
                    x[i] -= x[j] * a[i][j];
                }
                x[i] *= relaxationFactor / a[i][i];
                x[i] += old * (1 - relaxationFactor);
                change += (old - x[i]) * (old - x[i]);
            }
            ok = change > eps * eps;
        } while (ok);
        return x;
    }

    static double[][] genHilbertMatrix(int n) {
        double[][] ret = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ret[i][j] = 1. / (i + j + 1);
            }
        }
        return ret;
    }

    static double[] genFreeTerms(int n) {
        double[] b = new double[n];
        for (int i = 0; i < n; i++) {
            b[i] = i;
        }
        return b;
    }

    static double[][] genWellConditioned(int n) {
        double[][] ret = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n;j++) {
                if (i == j) {
                    continue;
                }
                ret[i][j] = Math.pow(-0.5, Math.abs(i - j));
            }
            ret[i][i] = n;
        }
        return ret;
    }

}
