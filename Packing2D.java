import localsearch.constraints.basic.*;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Administrator on 03/03/2018.
 */
class Data {
    int xi;
    int yi;

    public Data(int xi, int yi) {
        this.xi = xi;
        this.yi = yi;
    }
}

public class Packing2D {
    int W;
    int H;
    int[] w;
    int[] h;
    int size;
    VarIntLS[] x;
    VarIntLS[] y;
    VarIntLS[] o;
    ArrayList<Data> list = new ArrayList<>();
    LocalSearchManager ls;
    ConstraintSystem cs;

    public void readFile() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("bin-packing-2D.txt"));
        W = sc.nextInt();
        H = sc.nextInt();
        while (true) {
            int t = sc.nextInt();

            if (t == -1) {
                break;
            }
            int t1 = sc.nextInt();
            list.add(new Data(t, t1));

        }
        sc.close();
        size = list.size();
        w = new int[size];
        h = new int[size];

        for (int i = 0; i < size; i++) {
            w[i] = list.get(i).xi;
            h[i] = list.get(i).yi;

        }


    }

    public void stateModel() {

        x = new VarIntLS[size];
        y = new VarIntLS[size];
        o = new VarIntLS[size];
        for (int i = 0; i < size; i++) {
            x[i] = new VarIntLS(ls, 0, W);
            y[i] = new VarIntLS(ls, 0, H);
            o[i] = new VarIntLS(ls, 0, 1);
        }

        for (int i = 0; i < size; i++) {
            cs.post(new Implicate(new IsEqual(o[i], 0), new LessOrEqual(new FuncPlus(x[i], w[i]), W)));
            cs.post(new Implicate(new IsEqual(o[i], 0), new LessOrEqual(new FuncPlus(y[i], h[i]), H)));
            cs.post(new Implicate(new IsEqual(o[i], 1), new LessOrEqual(new FuncPlus(x[i], h[i]), W)));
            cs.post(new Implicate(new IsEqual(o[i], 1), new LessOrEqual(new FuncPlus(y[i], w[i]), H)));
        }

        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                // 0 0
                IConstraint[] c = new IConstraint[4];
                c[0] = new LessOrEqual(new FuncPlus(x[i], w[i]), new FuncPlus(x[j], w[j]));
                c[1] = new LessOrEqual(new FuncPlus(y[i], h[i]), new FuncPlus(y[j], h[j]));
                c[2] = new LessOrEqual(new FuncPlus(x[j], w[j]), new FuncPlus(x[i], w[i]));
                c[3] = new LessOrEqual(new FuncPlus(y[j], h[j]), new FuncPlus(y[i], h[i]));
                cs.post(new Implicate(new AND(new IsEqual(o[i], 0), new IsEqual(o[j], 0)), new OR(c)));
                // 0 1
                c = new IConstraint[4];
                c[0] = new LessOrEqual(new FuncPlus(x[i], w[i]), new FuncPlus(x[j], h[j]));
                c[1] = new LessOrEqual(new FuncPlus(y[i], h[i]), new FuncPlus(y[j], w[j]));
                c[2] = new LessOrEqual(new FuncPlus(x[j], h[j]), new FuncPlus(x[i], w[i]));
                c[3] = new LessOrEqual(new FuncPlus(y[j], w[j]), new FuncPlus(y[i], h[i]));
                cs.post(new Implicate(new AND(new IsEqual(o[i], 0), new IsEqual(o[j], 1)), new OR(c)));
                // 1 0
                c = new IConstraint[4];
                c[0] = new LessOrEqual(new FuncPlus(x[i], h[i]), new FuncPlus(x[j], h[j]));
                c[1] = new LessOrEqual(new FuncPlus(y[i], w[i]), new FuncPlus(y[j], w[j]));
                c[2] = new LessOrEqual(new FuncPlus(x[j], h[j]), new FuncPlus(x[i], h[i]));
                c[3] = new LessOrEqual(new FuncPlus(y[j], w[j]), new FuncPlus(y[i], w[i]));
                cs.post(new Implicate(new AND(new IsEqual(o[i], 1), new IsEqual(o[j], 0)), new OR(c)));
                // 1 1
                c = new IConstraint[4];
                c[0] = new LessOrEqual(new FuncPlus(x[i], h[i]), new FuncPlus(x[j], h[j]));
                c[1] = new LessOrEqual(new FuncPlus(y[i], w[i]), new FuncPlus(y[j], w[j]));
                c[2] = new LessOrEqual(new FuncPlus(x[j], h[j]), new FuncPlus(x[i], h[i]));
                c[3] = new LessOrEqual(new FuncPlus(y[j], w[j]), new FuncPlus(y[i], w[i]));
                cs.post(new Implicate(new AND(new IsEqual(o[i], 1), new IsEqual(o[j], 1)), new OR(c)));
            }

        }

        ls.close();

    }
}
