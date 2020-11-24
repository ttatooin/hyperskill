package solver;

public class Complex{

    public final double re;
    public final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public Complex(double re) {
        this.re = re;
        this.im = 0;
    }

    public Complex(String str) {
        int splitIndex;
        String tempString = str.substring(1);
        splitIndex = (tempString.indexOf('+') != -1) ? tempString.indexOf('+') : tempString.indexOf('-');
        String reString = null;
        String imString = null;
        if (splitIndex == -1) {
            if (str.charAt(str.length() - 1) == 'i') {
                imString = str;
            } else {
                reString = str;
            }
        } else {
            reString = str.substring(0, splitIndex + 1);
            imString = str.substring(splitIndex + 1);
        }
        if (imString != null) {
            if (imString.equals("i") || imString.equals("+i")) {
                imString = "1";
            } else if (imString.equals("-i")) {
                imString = "-1";
            } else {
                imString = imString.substring(0, imString.length() - 1);
            }
        }
        /*Debug*/
        System.out.println("Re:" + reString + " Im:" + imString);
        /*Debug*/
        re = (reString != null) ? Double.parseDouble(reString) : 0;
        im = (imString != null) ? Double.parseDouble(imString) : 0;
    }

    public boolean isZero() {
        return (re == 0) && (im == 0);
    }

    public Complex add(Complex x) {
        return new Complex(re + x.re, im + x.im);
    }

    public Complex multiply(Complex x) {
        return new Complex(re * x.re - im * x.im, re * x.im + im * x.re);
    }

    public Complex negate() {
        return new Complex(-re, -im);
    }

    public Complex reverse() {
        if (re != 0 || im != 0) {
            double squaredModule = re * re + im * im;
            return new Complex(re / squaredModule, -im / squaredModule);
        } else {
            throw new ArithmeticException("Reversing zero.");
        }
    }

    public double absolute() {
        return Math.sqrt(re * re + im * im);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        if (re != 0 || im == 0) {
            result.append(Double.toString(re));
        }
        if (im != 0) {
            if (im > 0) {
                result.append("+");
            }
            if (im != 1) {
                result.append(Double.toString(im));
            }
            result.append("i");
        }
        return result.toString();
    }

}
