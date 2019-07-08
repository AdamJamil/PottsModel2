package com.company;

import java.util.ArrayList;

class Polynomial
{
    static Polynomial zero = zero(), one = one(), x = x();
    static ArrayList<Polynomial> pow = pow();
    ArrayList<Rational> coefficients = new ArrayList<>();

    private static final double err = 0.000000001;
    private static boolean[] ans = new boolean[]{true};
    private static int count = 0;

    boolean geqZero()
    {
        //constant polynomial
        if (coefficients.size() == 1)
            return coefficients.get(0).value() >= 0;

        //negative leading term
        if (coefficients.get(coefficients.size() - 1).value() < -err)
            return false;

        //evaluate a few points
        if (evaluate(1) < -err || evaluate(1.2) < -err || evaluate(2) < -err || evaluate(1.8) < -err || evaluate(6) < -err || evaluate(1.4) < -err)
            return false;

        //check if all terms are positive
        boolean allCoeffPos = true;

        for (Rational coefficient : coefficients)
            allCoeffPos &= coefficient.value() >= 0;

        if (allCoeffPos)
            return true;

        //factor out x
        while (coefficients.get(0).p == 0)
            coefficients.remove(0);

        //check if running sum of coefficients from highest degree to lowest is always positive
        double domCoeff = 0;
        boolean dominated = true;
        for (int i = coefficients.size() - 1; i >= 0; i--)
        {
            domCoeff += coefficients.get(i).value();
            if (domCoeff < err)
            {
                dominated = false;
                break;
            }
        }

        if (dominated)
            return true;

        //perform same trick after rewriting polynomial in terms of (x-1)
        double[] taylor = new double[coefficients.size()], arr = new double[coefficients.size()];
        int w = coefficients.size();

        for (int i = 0; i < w; i++)
            arr[i] = coefficients.get(i).value();

        for (int i = 0; i < w; i++)
            for (int j = i; j < w; j++)
            {
                taylor[i] += arr[j];
                arr[j] *= (j - i) / (double) (i + 1);
            }

        domCoeff = 0;
        for (int i = taylor.length - 1; i >= 0; i--)
        {
            domCoeff += taylor[i];
            if (domCoeff < err)
            {
                //could not process polynomial
                //System.out.println(this);
                //System.out.println("\treturned " + ans[count]);
                //return ans[count++];
                //return true;

                if (evaluate(1) > err)
                    return deriavteoijfdsfij().geqZero();

                System.out.println(this);
            }
        }

        //polynomial was dominated
        return true;
    }

    Polynomial deriavteoijfdsfij()
    {
        Polynomial derioaisdjf = new Polynomial();

        for (int i = 1; i < coefficients.size(); i++)
        {
            Rational temo = coefficients.get(i);
            temo.multiply(new Rational(i, 1));
            derioaisdjf.coefficients.add(temo);
        }

        return derioaisdjf;
    }

    double evaluate(double lambda)
    {
        double result = coefficients.get(coefficients.size() - 1).value();

        if (lambda != 1)
            for (int i = coefficients.size() - 2; i >= 0; i--)
                result = (result * lambda) + coefficients.get(i).value();
        else
            for (int i = coefficients.size() - 2; i >= 0; i--)
                result += coefficients.get(i).value();

        return result;
    }

    void add(Polynomial other)
    {
        for (int i = 0; i < Math.max(coefficients.size(), other.coefficients.size()); i++)
        {
            Rational temp = new Rational();
            if (i < coefficients.size())
                temp.add(coefficients.get(i));
            if (i < other.coefficients.size())
                temp.add(other.coefficients.get(i));
            if (coefficients.size() > i)
                coefficients.set(i, temp);
            else
                coefficients.add(temp);
        }

        for (int i = coefficients.size() - 1; i > 0; i--)
            if (coefficients.get(i).p == 0)
                coefficients.remove(i);
            else
                break;
    }

    void multiply(Polynomial other)
    {
        ArrayList<Rational> newCoefficients = new ArrayList<>();

        for (int outDegree = 0; outDegree <= coefficients.size() + other.coefficients.size() - 2; outDegree++)
        {
            Rational current = new Rational();
            for (int firstDegree = Math.max(0, outDegree - other.coefficients.size() + 1); firstDegree <= Math.min(outDegree, coefficients.size() - 1); firstDegree++)
            {
                Rational temp = coefficients.get(firstDegree).copy();
                temp.multiply(other.coefficients.get(outDegree - firstDegree));
                current.add(temp);
            }
            newCoefficients.add(current);
        }

        coefficients = newCoefficients;

        for (int i = coefficients.size() - 1; i > 0; i--)
            if (coefficients.get(i).p == 0)
                coefficients.remove(i);
            else
                break;
    }

    void multiply(Rational rat)
    {
        if (rat.p == 0)
        {
            coefficients.clear();
            coefficients.add(new Rational(0, 1));
            return;
        }

        for (Rational coefficient : coefficients)
            coefficient.multiply(rat);
    }

    Polynomial copy()
    {
        Polynomial out = new Polynomial();
        for (Rational rational : coefficients)
            out.coefficients.add(new Rational(rational.p, rational.q));

        return out;
    }

    @Override
    public int hashCode()
    {
        return this.coefficients.size();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Polynomial))
            return false;

        Polynomial other = (Polynomial) obj;

        if (other.coefficients.size() != coefficients.size())
            return false;

        for (int i = 0; i < coefficients.size(); i++)
            if (other.coefficients.get(i).p != coefficients.get(i).p || other.coefficients.get(i).q != coefficients.get(i).q)
                return false;

        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        if (coefficients.size() == 1)
        {
            if (coefficients.get(0).q == 1)
                return "" + coefficients.get(0).p;
            return "(" + coefficients.get(0).p + "/" + coefficients.get(0).q + ")";
        }

        for (int i = coefficients.size() - 1; i > 0; i--)
        {
            if (coefficients.get(i).p == 0)
                continue;

            sb.append(coefficients.get(i));
            sb.append("x");
            if (i != 1)
                sb.append("^{").append(i).append("}");
            sb.append("+");
        }

        if (coefficients.get(0).p != 0)
        {
            if (coefficients.get(0).q == 1)
                return sb.toString() + coefficients.get(0).p;
            else
                return sb.toString() + "(" + coefficients.get(0).p + "/" + coefficients.get(0).q + ")";
        }
        else
            return sb.toString().substring(0, sb.toString().length() - 1);
    }

    String LaTeX()
    {
        StringBuilder sb = new StringBuilder();

        if (coefficients.size() == 1)
            if (coefficients.get(0).p != 1 || coefficients.get(0).q != 1)
                return coefficients.get(0).toString();
            else return "1";

        for (int i = coefficients.size() - 1; i > 0; i--)
        {
            if (coefficients.get(i).p == 0)
                continue;

            sb.append(coefficients.get(i)).append("x");
            if (i != 1)
                sb.append("^{").append(i).append("}");
            sb.append("+");
        }

        if (coefficients.get(0).p != 0)
        {
            if (coefficients.get(0).q == 1)
                return sb.toString() + coefficients.get(0).p;
            else
                return sb.toString() + "(" + coefficients.get(0).p + "/" + coefficients.get(0).q + ")";
        }
        else
            return sb.toString().substring(0, sb.toString().length() - 1);
    }

    private static Polynomial zero()
    {
        Polynomial out = new Polynomial();
        out.coefficients.add(new Rational());
        return out;
    }

    private static Polynomial one()
    {
        Polynomial out = new Polynomial();
        out.coefficients.add(new Rational(1, 1));
        return out;
    }

    private static Polynomial x()
    {
        Polynomial out = new Polynomial();
        out.coefficients.add(new Rational());
        out.coefficients.add(new Rational(1, 1));
        return out;
    }

    private static ArrayList<Polynomial> pow()
    {
        ArrayList<Polynomial> out = new ArrayList<>();
        out.add(one.copy());

        for (int i = 1; i < 400; i++)
        {
            Polynomial next = out.get(i - 1).copy();
            next.multiply(x);
            out.add(next);
        }

        return out;
    }
}
