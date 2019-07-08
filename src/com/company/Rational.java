package com.company;

class Rational
{
    long p = 0, q = 1;

    Rational copy()
    {
        return new Rational(p, q);
    }

    double value()
    {
        return p / (double) q;
    }

    void multiply(Rational other)
    {
        long gcd = gcd(other.p, other.q);
        p *= other.p / gcd;
        q *= other.q / gcd;
    }

    void add(Rational other)
    {
        p = (p * other.q) + (other.p * q);
        q *= other.q;
        long gcd = gcd(p, q);
        p /= gcd;
        q /= gcd;
    }

    private static long gcd(long a, long b)
    {
        return gcdHelper(Math.abs(a), Math.abs(b));
    }

    private static long gcdHelper(long a, long b)
    {
        while (true)
        {
            if (a == 0)
                return b;
            if (b == 0)
                return a;

            if (a == b)
                return a;

            if (a > b)
                a = a - b;
            else
                b = b - a;
        }
    }

    Rational(){}

    Rational(long a, long b)
    {
        p = a;
        q = b;
    }

    @Override
    public String toString()
    {
        if (p == 1 && q == 1) return "";
        if (q == 1) return "" + p;
        return "(" + p + "/" + q + ")";
    }
}
