package Util;

import java.text.DateFormat;
import java.util.Locale;
import java.util.regex.PatternSyntaxException;

public abstract class FormataData extends DateFormat {
  
  private FormataData() {
  }

  public static DateFormat getDateFormat(Locale locale) {
  	return DateFormat.getDateInstance(DateFormat.SHORT, locale);
  }

  public static String getDay(String data) throws PatternSyntaxException, NullPointerException {
  	return (String) ((data.split("-", -1))[0]);
  }

  public static String getMonth(String data) throws PatternSyntaxException, NullPointerException {
  	return (String) ((data.split("-", -1))[1]);
  }

  public static String getYear(String data) throws PatternSyntaxException, NullPointerException {
  	return (String) ((data.split("-", -1))[2]);
  }
}