package com.company;

class RationalExpression
{
    static RationalExpression zero = zero();
    Polynomial num, denom;

    double evaluate(double lambda)
    {
        return num.evaluate(lambda) / denom.evaluate(lambda);
    }

    RationalExpression copy()
    {
        RationalExpression out = new RationalExpression();
        out.num = num.copy();
        out.denom = denom.copy();
        return out;
    }

    static RationalExpression zero()
    {
        RationalExpression out = new RationalExpression();
        out.num = Polynomial.zero.copy();
        out.denom = Polynomial.one.copy();

        return out;
    }

    @Override
    public String toString()
    {
        return "[" + num + "]/[" + denom + "]";
    }
}
