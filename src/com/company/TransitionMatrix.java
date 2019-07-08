package com.company;

import java.util.ArrayList;

class TransitionMatrix
{
    //map.get(s1).get(s2) -> gives P(s1 -> s2)
    private ArrayList<State> states = State.generateStates();
    private final int s = states.size();
    RESum[][] arr;

    double[][] evaluate(double lambda)
    {
        double[][] out = new double[s][s];

        for (int i = 0; i < s; i++)
            for (int j = 0; j < s; j++)
                out[i][j] = arr[i][j].evaluate(lambda);

        return out;
    }

    Polynomial f(int x)
    {
        Polynomial out = Polynomial.x.copy();
        Polynomial temp = Polynomial.one.copy();
        temp.multiply(new Rational(x * x - x, 1));
        out.add(temp);

        return out;
    }

    TransitionMatrix()
    {
        arr = new RESum[s][s];

        for (int i = 0; i < s; i++)
            for (int j = 0; j < s; j++)
                arr[i][j] = new RESum();

        for (int s1 = 0; s1 < s; s1++)
        {
            int[] startOrder = states.get(s1).order;

            for (int src = 0; src < 3; src++)
            {
                if (startOrder[src] == 0)
                    continue;

                Rational pickSrc = new Rational(startOrder[src], Main.n);
                Polynomial denom = Polynomial.zero.copy();

                for (int dst = 0; dst < 3; dst++)
                    denom.add(f(startOrder[dst] - ((src == dst) ? 1 : 0)));

//                for (int dst = 0; dst < 3; dst++)
//                    denom.add(Polynomial.pow.get(neighbors[dst]));

                for (int dst = 0; dst < 3; dst++)
                {
                    int[] endOrder = new int[3];

                    System.arraycopy(startOrder, 0, endOrder, 0, 3);
                    endOrder[src]--;
                    endOrder[dst]++;

                    if (endOrder[2] > endOrder[1])
                    {
                        int temp = endOrder[2];
                        endOrder[2] = endOrder[1];
                        endOrder[1] = temp;
                    }

                    RationalExpression p = new RationalExpression();
                    p.denom = denom.copy();

//                    p.num = Polynomial.pow.get(neighbors[dst]).copy();
                    //NEW
                    p.num = f(startOrder[dst] - ((src == dst) ? 1 : 0));

                    p.num.multiply(pickSrc);

                    for (int s2 = 0; s2 < s; s2++)
                        if (states.get(s2).equals(new State(endOrder)))
                        {
                            arr[s1][s2].add(p);
                            break;
                        }
                }
            }
        }

        for (RESum[] temp1 : arr)
            for (RESum temp2 : temp1)
                temp2.simplify();

        if (Main.printTM)
            print();
    }

    private void print()
    {
        for (State state : states)
            System.out.print(" & " + (state.toString().replace("†", "\\dagger")));

        for (int s1 = 0; s1 < s; s1++)
        {
            System.out.print(" \\\\ \n" + states.get(s1).toString().replace("†", "\\dagger") + " & ");

            for (int s2 = 0; s2 < s; s2++)
            {
                boolean first = true;
                for (RationalExpression rE : arr[s1][s2].terms)
                {
                    if (rE.num.equals(Polynomial.zero))
                        continue;

                    if (first)
                    {
                        System.out.print("\\frac{" + rE.num.LaTeX() + "}{" + rE.denom.LaTeX() + "}");
                        first = false;
                    }
                    else
                        System.out.print(" + \\frac{" + rE.num.LaTeX() + "}{" + rE.denom.LaTeX() + "}");
                }

                if (arr[s1][s2].terms.isEmpty())
                    System.out.print(0);

                System.out.print(" & ");
            }
        }

        System.out.println();
        System.out.println();
    }
}