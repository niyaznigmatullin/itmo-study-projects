package lab1;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.linear.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Niyaz Nigmatullin on 9/17/14.
 */
public class LinearRegression {

    static class Information {
        RealVector theta;
        RealVector mean;
        RealVector deviation;

        Information(RealVector theta, RealVector mean, RealVector deviation) {
            this.theta = theta;
            this.mean = mean;
            this.deviation = deviation;
        }
    }

    static Information learn(InputStream input) {
        Scanner learningFile = new Scanner(input);
        List<RealVector> data = new ArrayList<RealVector>();
        List<Double> values = new ArrayList<Double>();
        while (learningFile.hasNext()) {
            double x = Double.parseDouble(learningFile.next());
            double y = Double.parseDouble(learningFile.next());
            double cost = Double.parseDouble(learningFile.next());
            data.add(new ArrayRealVector(new double[]{x, y, 1}));
            values.add(cost);
        }
        RealVector mean = new ArrayRealVector(3);
        RealVector deviation = new ArrayRealVector(3);
        for (int i = 0; i < 2; i++) {
            double sum = 0;
            double max = Double.NEGATIVE_INFINITY;
            double min = Double.POSITIVE_INFINITY;
            for (int j = 0; j < data.size(); j++) {
                double v = data.get(j).getEntry(i);
                sum += v;
                max = Math.max(max, v);
                min = Math.min(min, v);
            }
            mean.setEntry(i, sum / data.size());
            deviation.setEntry(i, 1. / (max - min));
        }
        mean.setEntry(2, 0);
        deviation.setEntry(2, 1);
        for (int i = 0; i < data.size(); i++) {
            RealVector v = data.get(i);
            data.set(i, v.subtract(mean).ebeMultiply(deviation));
        }
        RealMatrix mx = new Array2DRowRealMatrix(data.size(), 3);
        for (int i = 0; i < data.size(); i++) {
            RealVector v = data.get(i);
            for (int j = 0; j < v.getDimension(); j++) {
                mx.setEntry(i, j, v.getEntry(j));
            }
        }
        RealMatrix my = new Array2DRowRealMatrix(ArrayUtils.toPrimitive(values.toArray(new Double[values.size()])));
        return new Information(MatrixUtils.inverse(mx.transpose().multiply(mx)).multiply(mx.transpose()).multiply(my).getColumnVector(0),
                mean, deviation);
    }

    static void test(Information learnt, InputStream in, PrintStream out) {
        Scanner input = new Scanner(in);
        while (input.hasNext()) {
            double x = Double.parseDouble(input.next());
            double y = Double.parseDouble(input.next());
            double cost = Double.parseDouble(input.next());
            RealVector v = new ArrayRealVector(new double[]{x, y, 1});
            v = v.subtract(learnt.mean).ebeMultiply(learnt.deviation);
            double predictedCost = v.dotProduct(learnt.theta);
            out.println("real cost = " + cost + ", predicted cost = " + predictedCost + ", error = " + Math.abs(predictedCost - cost) / cost * 100 + "%");
        }
        out.flush();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Information learnt = learn(new FileInputStream(args[0]));
        test(learnt, new FileInputStream(args[1]), System.out);
    }
}
