package com.company;

import java.util.ArrayList;

class State
{
    private String name = "";
    int[] order = new int[3];

    State(int[] arr)
    {
        System.arraycopy(arr, 0, order, 0, arr.length);
    }

    @Override
    public int hashCode()
    {
        return order[0];
    }

    @Override
    public boolean equals(Object obj)
    {
        return (this.order[0] == ((State) obj).order[0]) && (this.order[1] == ((State) obj).order[1]) && (this.order[2] == ((State) obj).order[2]);
    }

    @Override
    public String toString()
    {
        if (name.isEmpty())
        {
            for (int i = 0; i < order[0]; i++)
                name += "B";
            for (int i = 0; i < order[1]; i++)
                name += "*";
            for (int i = 0; i < order[2]; i++)
                name += "†";
        }
        return name;
    }

    static ArrayList<State> generateStates()
    {
        ArrayList<State> out = new ArrayList<>();
        for (int blues = Main.n; blues >= 0; blues--)
            for (int stars = Main.n - blues; stars >= Main.n - blues - stars; stars--)
                out.add(new State(new int[]{blues, stars, Main.n - blues - stars}));
        return out;
    }
}
