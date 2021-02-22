package com.abc;

import java.time.LocalDateTime;

public class DateProvider {
    private static class SingletonHolder {
        private static final DateProvider instance = new DateProvider();
    }
    
    public static DateProvider getInstance() {
        return SingletonHolder.instance;
    }
    
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
