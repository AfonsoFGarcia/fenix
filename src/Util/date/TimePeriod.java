/*
 * Created on Dec 26, 2003 by jpvl
 *  
 */
package Util.date;

import java.util.Calendar;
import java.util.Date;

/**
 * @author jpvl
 */
public class TimePeriod
{
    private long start;

    private long end;

    public TimePeriod( long start, long end )
    {
        this.start = start;
        this.end = end;
    }

    /**
     *  
     */
    public TimePeriod( Date start, Date end )
    {
        this(start.getTime(), end.getTime());
    }

    public TimePeriod( Calendar start, Calendar end )
    {
        this(start.getTimeInMillis(), end.getTimeInMillis());
    }

    public Double hours()
    {
        Calendar endCalendar = Calendar.getInstance();

        endCalendar.setTimeInMillis(this.end);

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(start);

        endCalendar.roll(Calendar.HOUR_OF_DAY, -(startCalendar.get(Calendar.HOUR_OF_DAY)));
        endCalendar.roll(Calendar.MINUTE, -(startCalendar.get(Calendar.MINUTE)));

        int hours = endCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes = endCalendar.get(Calendar.MINUTE);

        double minutesInHours = minutes / 60.0;

        return new Double(hours + minutesInHours);
    }
}
