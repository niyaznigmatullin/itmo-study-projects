package ru.ifmo.niyaz.study.term6.seagull.math;

/**
 * Created with IntelliJ IDEA.
 * User: niyaz
 * Date: 6/21/12
 * Time: 4:33 PM
 */
public class ArrayUtils {
    public static double[][] arrayCopy2D(double[][] a) {
        double[][] ret = a.clone();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = ret[i].clone();
        }
        return ret;
    }

}
