package net.sourceforge.fenixedu.presentationTier.renderers.converters;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.components.converters.BiDirectionalConverter;

public class YearMonthDayConverter extends BiDirectionalConverter {

    @Override
    public Object convert(Class type, Object value) {
        
        if (value == null) {
            return null;
        }
        
        String valueString = (String) value;
        int year = Integer.parseInt(valueString.substring(0, 4));
        int month = Integer.parseInt(valueString.substring(5, 7));
        int day = Integer.parseInt(valueString.substring(8, 10));
        if (year == 0 || month == 0 || day == 0) {
            return null;
        }
        
        return new YearMonthDay(year, month, day);        
    }

    @Override
    public String deserialize(Object object) {
        YearMonthDay date = (YearMonthDay) object;
        if(date != null) {
            return date.toString("yyyy-MM-dd");   
        }        
        return "";
    }
}
