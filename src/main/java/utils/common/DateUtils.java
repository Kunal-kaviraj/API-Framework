package utils.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * It contains date utilities.
 * 
 * @Class_Name: readFile
 * @Functionality: It will read a file using provided inputStream
 * @author: Kunal
 */
public class DateUtils
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

	/**
	 * It will give current date.
	 * 
	 * @return current date e.g: 2019-03-13 yyyy-MM-dd
	 */
	public static String getCurrentDate()
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String currentDate = formatter.format(LocalDate.now());
		return currentDate;
	}

	/**
	 * It will give the current date with time.
	 * 
	 * @return current date time e.g : 2019-03-13 12:42:48 yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrentDateWithTime()
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String currentDateTime = formatter.format(LocalDateTime.now());
		return currentDateTime;
	}

	/**
	 * It will give the current date with the format provided by user.
	 * 
	 * @param dateFormatPattern e.g yyyy-MM-dd
	 * @return current date
	 */
	public static String getCurrentDate(String dateFormatPattern)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormatPattern);
		String currentDate = formatter.format(LocalDate.now());
		return currentDate;
	}

	/**
	 * It will give formatted date for given format and date. For e.g date
	 * ="2019-05-13" and format="EEEE dd MMM yyyy" output will be "Monday 13 May
	 * 2019" you can a valid date and a valid format and will return the desired
	 * result.
	 * 
	 * @return date
	 */
	public static String getFormattedDate(String date, String dateFormat)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
		String faormattedDate = formatter.format(LocalDate.parse(date));
		return faormattedDate;
	}

	/**
	 * It will return extended date form current date. For eg . if daysToExtend =1
	 * it will return next date with respective to current date
	 * 
	 * @param dateFormat
	 * @param daysToExtend
	 * @return future date
	 */
	public static String getFutureDateFromCurrent(String dateFormat, long daysToExtend)
	{
		dateFormat = dateFormat.isEmpty() ? "yyyy-MM-dd" : dateFormat;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
		String formattedDate = formatter.format(LocalDate.now().plusDays(daysToExtend));
		return formattedDate;
	}

	/**
	 * It will return extended date form current date. For eg . if daysToExtend =1
	 * it will return next date with respective to current date
	 * 
	 * @param dateFormat
	 * @param daysToExtend
	 * @return future date
	 */
	public static String getPreviousDateFromCurrent(String dateFormat, long daysToExtend)
	{
		dateFormat = dateFormat.isEmpty() ? "yyyy-MM-dd" : dateFormat;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
		String faormattedDate = formatter.format(LocalDate.now().minusDays(daysToExtend));
		return faormattedDate;
	}

	/**
	 * It will generate random number based on current System Date and Time in form
	 * of ddhhmmss. e.g : if current date and time is 1stMay 2019 and time is
	 * 04:13:20, then it will return values 01041320
	 * 
	 * @param
	 * @return randomString
	 */
	public String generateRandomNoInDateAndTime()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("ddhhmmss");
		Date date = new Date();
		String randomnoInformofdate = formatter.format(date);
		return randomnoInformofdate;
	}

}
