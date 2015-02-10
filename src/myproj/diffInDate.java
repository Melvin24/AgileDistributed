package myproj;
//<<<<<<<<<<<<<<<<<<<<REFERANCE: http://www.java2s.com/Tutorial/Java/0040__Data-Type/FindtheDifferenceBetweenTwoGivenDates.htm>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class diffInDate {

  public static int diff(Date date1, Date date2) {
    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();

    c1.setTime(date1);
    c2.setTime(date2);
    int diffDay = 0;

    if (c1.before(c2)) {
      diffDay = countDiffDay(c1, c2);
    } else {
      diffDay = countDiffDay(c2, c1);
    }

    return diffDay;
  }

  public static int DateDiff(Date date1, Date date2) {
    int diffDay = diff(date1, date2);
    return diffDay;
  }

  public static int countDiffDay(Calendar c1, Calendar c2) {
    int returnInt = 0;
    while (!c1.after(c2)) {
      c1.add(Calendar.DAY_OF_MONTH, 1);
      returnInt++;
    }

    if (returnInt > 0) {
      returnInt = returnInt - 1;
    }

    return (returnInt);
  }

  public static Date makeDate(String dateString) throws Exception {
    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    return formatter.parse(dateString);
  }


}