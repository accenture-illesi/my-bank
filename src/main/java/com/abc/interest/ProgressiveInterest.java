package com.abc.interest;

import java.math.BigDecimal;

public class ProgressiveInterest {
    private Bracket brackets;
    
    public ProgressiveInterest(double baseRate) {
        brackets = new Bracket(0.0, baseRate);
    }
    
    public ProgressiveInterest next(double threshold, double rate) {
        if (threshold <= brackets.threshold.doubleValue()) {
            throw new IllegalArgumentException("Next threshold has to be larger than the previous");
        }
        brackets = new Bracket(threshold, rate, brackets);
        return this;
    }
    
    public BigDecimal calculate(BigDecimal amount) {
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal remainder = amount;
        for (Bracket bracket = brackets; bracket != null; bracket = bracket.previous) {
            if (remainder.compareTo(bracket.threshold) > 0) {
                result = result.add((remainder.subtract(bracket.threshold)).multiply(bracket.rate));
                remainder = bracket.threshold;
            }
        }
        return result;
    }
    
    private static class Bracket {
        final BigDecimal threshold;
        final BigDecimal rate;
        final Bracket previous;
        
        private Bracket(double threshold, double rate) {
            this(threshold, rate, null);
        }
        
        private Bracket(double threshold, double rate, Bracket previous) {
            this.threshold = BigDecimal.valueOf(threshold);
            this.rate = BigDecimal.valueOf(rate);
            this.previous = previous;
        }
        
        @Override
        public String toString() {
            return "threshold:" + threshold + ", rate:" + rate + (previous == null ? "" : ("; " + previous));
        }
    }
}
