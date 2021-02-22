package com.abc;

import java.util.Calendar;
import java.util.Date;

public class DateProvider {
    private static class SingletonHolder {
        private static final DateProvider instance = new DateProvider();
    }
    
    public static DateProvider getInstance() {
        return SingletonHolder.instance;
    }
    
    public Date now() { // TODO replace Date with LocalDateTime
        return Calendar.getInstance().getTime();
    }
}
