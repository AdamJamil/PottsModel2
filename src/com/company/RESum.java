package com.company;

import java.util.ArrayList;
import java.util.HashSet;

class RESum
{
    ArrayList<RationalExpression> terms = new ArrayList<>();

    boolean geq(RESum other)
    {
        HashSet<Polynomial> denoms = new HashSet<>();

        for (RationalExpression term : terms)
            denoms.add(term.denom);

        for (RationalExpression term : other.terms)
            denoms.add(term.denom);

        Polynomial f = Polynomial.zero.copy();

        for (RationalExpression term : terms)
        {
            if (term.num == null)
            {
                System.out.println(this);
            }
            Polynomial temp = term.num.copy();

            for (Polynomial denom : denoms)
                if (!denom.equals(term.denom))
                    temp.multiply(denom);

            f.add(temp);
        }

        for (RationalExpression term : other.terms)
        {
            Polynomial temp = term.num.copy();

            for (Polynomial denom : denoms)
                if (!denom.equals(term.denom))
                    temp.multiply(denom);

            temp.multiply(new Rational(-1, 1));
            f.add(temp);
        }

        return f.geqZero();
    }

    RESum copy()
    {
        RESum out = new RESum();
        out.terms.clear();

        for (RationalExpression term : terms)
            out.terms.add(term.copy());

        return out;
    }

    double evaluate(double lambda)
    {
        double out = 0;
        for (RationalExpression term : terms)
            out += term.evaluate(lambda);
        return out;
    }

    void add(RESum newTerms)
    {
        for (RationalExpression term : newTerms.terms)
            add(term);
    }

    void add(RationalExpression newTerm)
    {
        if (newTerm.num.equals(Polynomial.zero))
            return;

        if (terms.isEmpty())
        {
            terms.add(newTerm.copy());
            return;
        }

        for (RationalExpression term : terms)
            if (term.denom.equals(newTerm.denom))
            {
                term.num.add(newTerm.num);
                return;
            }

        terms.add(newTerm.copy());
    }

    void simplify()
    {
        for (int i = terms.size() - 1; i >= 0; i--)
            if (terms.get(i).num == null)
                terms.remove(i);

        ArrayList<RationalExpression> newTerms = new ArrayList<>();

        outer: for (RationalExpression term : terms)
        {
            for (RationalExpression newTerm : newTerms)
                if (term.denom.equals(newTerm.denom))
                {
                    newTerm.num.add(term.num);
                    continue outer;
                }

            newTerms.add(term);
        }

        terms = newTerms;
    }

    @Override
    public int hashCode()
    {
        int sum = 0;
        for (RationalExpression term : terms)
            sum += term.num.coefficients.size() + term.denom.coefficients.size();
        return sum;
    }

    @Override
    public boolean equals(Object other)
    {
        return evaluate(1.41421356237309504880168872420969) == ((RESum) other).evaluate(1.41421356237309504880168872420969);
    }

    @Override
    public String toString()
    {
        String temp = terms.get(0).toString();

        for (int i = 1; i < terms.size(); i++)
            temp += " + " + terms.get(i);

        return temp;
    }
}
