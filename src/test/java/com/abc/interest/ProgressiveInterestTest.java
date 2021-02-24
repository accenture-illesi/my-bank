package com.abc.interest;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class ProgressiveInterestTest {
    private static final double DOUBLE_DELTA = 1e-15;
    private static final BigDecimal THOUSAND = BigDecimal.valueOf(1000);
    
    @Test
    public void baseInterest() {
        //given
        ProgressiveInterest underTest = new ProgressiveInterest(0.5);
        //when
        double actual = underTest.calculate(THOUSAND).doubleValue();
        //then
        double expected = 1000 * 0.5;
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
    
    @Test
    public void twoInterests() {
        //given
        ProgressiveInterest underTest = new ProgressiveInterest(0.1)
                .next(400, 0.2);
        //when
        double actual = underTest.calculate(THOUSAND).doubleValue();
        //then
        double expected = 400 * 0.1 + 600 * 0.2;
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
    
    @Test
    public void overComplexInterests() {
        //given
        ProgressiveInterest underTest = new ProgressiveInterest(0.1)
                .next(400, 0.2)
                .next(700, 0.3)
                .next(900, 0.4)
                .next(1050, 0.5);
        //when
        double actual = underTest.calculate(THOUSAND).doubleValue();
        //then
        double expected = 400 * 0.1 + 300 * 0.2 + 200 * 0.3 + 100 * 0.4;
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
}
