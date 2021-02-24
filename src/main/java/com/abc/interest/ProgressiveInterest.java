package com.abc.interest;

public class ProgressiveInterest {
    private Bracket brackets;
    
    public ProgressiveInterest(double baseRate) {
        brackets = new Bracket(0.0, baseRate);
    }
    
    public ProgressiveInterest next(double threshold, double rate) {
        if (threshold <= brackets.threshold) {
            throw new IllegalArgumentException("Next threshold has to be larger than the previous");
        }
        brackets = new Bracket(threshold, rate, brackets);
        return this;
    }
    
    public double calculate(double amount) {
        double result = 0.0;
        double remainder = amount;
        for (Bracket bracket = brackets; bracket != null; bracket = bracket.previous) {
            if (remainder > bracket.threshold) {
                result = result + (remainder - bracket.threshold) * bracket.rate;
                remainder = bracket.threshold;
            }
        }
        return result;
    }
    
    private static class Bracket {
        final double threshold;
        final double rate;
        final Bracket previous;
        
        private Bracket(double threshold, double rate) {
            this.threshold = threshold;
            this.rate = rate;
            previous = null;
        }
        
        private Bracket(double threshold, double rate, Bracket previous) {
            this.threshold = threshold;
            this.rate = rate;
            this.previous = previous;
        }
        
        @Override
        public String toString() {
            return "limit:" + threshold + "], rate:" + rate * 100 + "%";
        }
    }
}
