package com.example.fraction_calculator.model;

public class Fraction {
    private final long numerator;
    private final long denominator;

    public Fraction() {
            this.numerator = 1;
            this.denominator = 1;
        }

    public Fraction(long numerator, long denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Mẫu số không được bằng 0");
        }
        
        // Rút gọn phân số
        long gcd = gcd(Math.abs(numerator), Math.abs(denominator));
        long simplifiedNum = numerator / gcd;
        long simplifiedDenom = Math.abs(denominator) / gcd;
        
        // Đảm bảo dấu âm luôn ở tử số, mẫu số luôn dương
        if (simplifiedDenom < 0) {
            simplifiedNum = -simplifiedNum;
            simplifiedDenom = -simplifiedDenom;
        }
        
        this.numerator = simplifiedNum;
        this.denominator = simplifiedDenom;
    }

    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public Fraction add(Fraction other) {
        long num = this.numerator * other.denominator + other.numerator * this.denominator;
        long denom = this.denominator * other.denominator;
        return new Fraction(num, denom);
    }

    public Fraction subtract(Fraction other) {
        long num = this.numerator * other.denominator - other.numerator * this.denominator;
        long denom = this.denominator * other.denominator;
        return new Fraction(num, denom);
    }

    public Fraction multiply(Fraction other) {
        long num = this.numerator * other.numerator;
        long denom = this.denominator * other.denominator;
        return new Fraction(num, denom);
    }

    public Fraction divide(Fraction other) {
        if (other.numerator == 0) {
            throw new IllegalArgumentException("Không thể chia cho phân số 0");
        }
        long num = this.numerator * other.denominator;
        long denom = this.denominator * other.numerator;
        return new Fraction(num, denom);
    }

    public long getNumerator() {
        return numerator;
    }

    public long getDenominator() {
        return denominator;
    }

    @Override
    public String toString() {
        if (denominator == 1) {
            return String.valueOf(numerator);
        }
        return numerator + "/" + denominator;
    }

    public double toDecimal() {
        return (double) numerator / denominator;
    }
}
