package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class Driver
{
    boolean[][] partialOrder;
    static ArrayList<State> states = State.generateStates();
    static int[][] neighbors;

    private TransitionMatrix tm;
    private final int s = states.size();
    private boolean[][] badPair;
    private ArrayList<Integer>[] checklist;
    private static final double err = 0.000000001;

    boolean run()
    {
        System.out.println(states);
        tm = new TransitionMatrix();
        initializeNeighbors();
        partialOrder = guessMaxPartialOrder();

        System.out.println("hajime!");

        boolean done;

        do
        {
            init();

            for (int i = 0; i < s; i++)
                outer: for (int w = checklist[i].size() - 1; w >= 0; w--)
                {
                    int j = checklist[i].get(w);

                    for (int key2 = (1 << neighbors[j].length) - 1; key2 >= 1; key2--)
                    {
                        int key1 = 0;

                        inner: for (int idx1 = 0; idx1 < neighbors[i].length; idx1++)
                            for (int idx2 = 0; idx2 < neighbors[j].length; idx2++)
                                if ((key2 & (1 << idx2)) != 0 && partialOrder[neighbors[i][idx1]][neighbors[j][idx2]])
                                {
                                    key1 += (1 << idx1);
                                    continue inner;
                                }

                        int pKey1 = (i << 7) + key1;
                        int pKey2 = (j << 7) + key2;

                        boolean temp;
                        long key = (((long) pKey1) << 32) + pKey2;

                        if (result.containsKey(key))
                            temp = result.get(key);
                        else
                            result.put(key, temp = getProbability(i, pKey1).geq(getProbability(j, pKey2)));

                        if (!temp)
                        {
                            badPair[i][j] = true;
                            checklist[i].remove(w);
                            continue outer;
                        }
                    }
                }

            done = true;

            for (int i = 0; i < s; i++)
                if (badPair[0][i])
                    return false;

            for (int i = 1; i < s; i++)
                for (int j = 0; j < s; j++)
                    if (badPair[i][j])
                        done = partialOrder[i][j] = badPair[i][j] = false;

        } while (!done);

        //transitive
        for (int i = 0; i < s; i++)
            for (int j = 0; j < s; j++)
                for (int k = 0; k < s; k++)
                    if (partialOrder[i][j] && partialOrder[j][k] && !partialOrder[i][k])
                        System.out.println("not transitive");

        //anti-symmetric
        for (int i = 0; i < s; i++)
            for (int j = i + 1; j < s; j++)
                if (partialOrder[i][j] && partialOrder[j][i])
                    System.out.println("not anti-symmetric");

        return true;
    }

    private boolean[][] guessMaxPartialOrder()
    {
        boolean[][] geq = new boolean[s][s];

        for (int i = 0; i < s; i++)
            for (int j = 0; j < s; j++)
                geq[i][j] = true;

//        if (Main.n == 2)
//            geq[s - 2][s - 1] = false;
//        if (Main.n == 1)
//            geq[s - 1][s - 2] = false;

        for (double lambda = 1.01; lambda < 100; lambda *= 1.01)
        {
            double[][] arr = tm.evaluate(lambda), temp = new double[s][s];
            for (int i = 0; i < s; i++)
                System.arraycopy(arr[i], 0, temp[i], 0, s);
            for (int pow = 0; pow < 100; pow++)
            {
                arr = multiply(arr, temp);

                for (int i = 0; i < s; i++)
                    for (int j = 0; j < s; j++)
                        geq[i][j] &= arr[i][0] + err >= arr[j][0];
            }
        }

        for (int i = 0; i < s; i++)
            if (!geq[0][i])
                System.out.println("!!");

        for (int i = 0; i < s; i++)
            for (int j = i + 1; j < s; j++)
                if (geq[i][j] && geq[j][i])
                {
                    geq[i][j] = geq[j][i] = false;
                    System.out.println(states.get(i) + " >= " + states.get(j) + " and " + states.get(j) + " >= " + states.get(i));
                }

        return geq;
    }

    private HashMap<Long, Boolean> result = new HashMap<>();
    private HashMap<Integer, RESum> probability = new HashMap<>();

    private double[][] multiply(double[][] arr1, double[][] arr2)
    {
        double[][] out = new double[s][s];

        for (int i = 0; i < s; i++)
            for (int j = 0; j < s; j++)
                for (int k = 0; k < s; k++)
                    out[i][j] += arr1[i][k] * arr2[k][j];

        return out;
    }

    private void initializeNeighbors()
    {
        neighbors = new int[s][];

        for (int i = 0; i < s; i++)
        {
            int count = 0;

            for (int j = 0; j < s; j++)
                if (!tm.arr[i][j].terms.isEmpty())
                    count++;

            neighbors[i] = new int[count];
            count = 0;

            for (int j = 0; j < s; j++)
                if (!tm.arr[i][j].terms.isEmpty())
                    neighbors[i][count++] = j;
        }
    }

    private void init()
    {
        badPair = new boolean[s][s];
        boolean[][] minRel = minRel();

        checklist = new ArrayList[s];

        for (int i = 0; i < s; i++)
        {
            checklist[i] = new ArrayList<>(s);
            for (int j = 0; j < s; j++)
                if (i != j && partialOrder[i][j] && minRel[j][i])
                    checklist[i].add(j);
        }
    }

    boolean[][] minRel()
    {
        boolean[][] out = new boolean[s][s];

        for (int i = 0; i < s; i++)
            for (int j = 0; j < s; j++)
                out[i][j] = partialOrder[j][i];

        for (int i = 0; i < s; i++)
        {
            boolean[] set = out[i];
            set[i] = false;

            outer:
            for (int j = 0; j < s; j++)
            {
                if (!set[j])
                    continue;

                for (int k = 0; k < s; k++)
                {
                    if (!set[k])
                        continue;

                    if (j == k)
                        continue;

                    if (partialOrder[j][k])
                    {
                        set[j] = false;
                        continue outer;
                    }
                }
            }
        }

        return out;
    }

    private RESum getProbability(int state, int key)
    {
        RESum p;

        if (probability.containsKey(key))
            p = probability.get(key);
        else
        {
            p = new RESum();
            for (int k = 0; k < 7; k++)
                if ((key & (1 << k)) != 0)
                    p.add(tm.arr[state][neighbors[state][k]]);
            if (p.terms.isEmpty())
                p.terms.add(RationalExpression.zero.copy());
            probability.put(key, p);
        }

        return p;
    }
}