package com.yjh.base.util;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * 日期常用操作类
 */
public class DateUtil {
	/**
	 * 同步锁
	 */
	private static Object timeZoneLock = new Object();
	
	/**
	 * 时区集合
	 */
	private static String[][] timeZoneList ;
	
	/**
	 * 日期格式集合
	 */
	private static Map dateFormatCache = new HashMap();
		
	/**
	 * 定义 时间差类型枚举 TimeType（毫秒、秒、分钟、小时、天） 
	 */
	public enum TimeType {
	        /**
	         * 时间枚举类型
	         */
		diffMillis, diffSecs,diffMins,diffHours,diffDays
	}
	
	/**
	 * 获取当前日期
	 * @param format 日期格式
	 * @return String 日期字符串
	 */
	public static String getCurrentTime(String format){
		SimpleDateFormat formatObj = new SimpleDateFormat(format);
		String date = formatObj.format(new Date());
		return date;
	}
	
	/**
	 * 根据num获得距今的日期
	 * @param num int天数
	 * @param format 格式
	 * @return 距今num的日期
	 */
	public static String getDate(int num,String format){
		GregorianCalendar gc = new GregorianCalendar();
		gc.add(Calendar.DAY_OF_MONTH,num);
		Date time = gc.getTime();
		SimpleDateFormat formatObj = new SimpleDateFormat(format);
		String date = formatObj.format(time);
		return date;
	}
	
	/**
	 * 取得指定日期增加/减少（n为负数时）n天后的日期
	 * @param date Date 指定日期
	 * @param n int 天数
	 * @return Date 增加/减少后的日期
	 */
	public static Date add(Date date,int n) {
		if(date == null){
		    return null;
		}
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.DATE,n);
		return gc.getTime();
	}
	
	/**
	 * 计算分钟差（单位：timeType）
	 * @param data1 时间1 （yyyy-MM-dd hh:mm）or（yyyy/MM/dd hh:mm）等
	 * @param data2 时间2 （yyyy-MM-dd hh:mm）or（yyyy/MM/dd hh:mm）等
	 * @param timeType 时间差类型
	 * @return long 分钟差（timeType单位）
	 */
	public static long timeDiff(String data1,String data2,TimeType timeType){
	    Calendar c1 = new GregorianCalendar(Integer.parseInt(data1.substring(0,
                4)), Integer.parseInt(data1.substring(5, 7)), Integer
                .parseInt(data1.substring(8, 10)), Integer.parseInt(data1
                .substring(11, 13)), Integer.parseInt(data1.substring(14, 16)));
            Calendar c2 = new GregorianCalendar(Integer.parseInt(data2.substring(0,
                4)), Integer.parseInt(data2.substring(5, 7)), Integer
                .parseInt(data2.substring(8, 10)), Integer.parseInt(data2
                .substring(11, 13)), Integer.parseInt(data2.substring(14, 16)));  
		           
		long   diffMillis = 0;
		if(c1.after(c2)){  
			diffMillis   =   c1.getTimeInMillis()-c2.getTimeInMillis();  
		}else{
			diffMillis   =   c2.getTimeInMillis()-c1.getTimeInMillis(); 
		}
		if(timeType == TimeType.diffMillis){
			 return diffMillis;
		}else if(timeType == TimeType.diffSecs){
			return diffMillis/1000;
		} else if(timeType == TimeType.diffMins){
			return   diffMillis/(60*1000);
		}else if(timeType == TimeType.diffHours){
			return   diffMillis/(60*60*1000);
		}else{
			return   diffMillis/(24*60*60*1000); 
		}
	}
	
	/**
	 * 将日期分隔符去掉 支持 "/","-"字符
	 * @param date 有分隔符日期
	 * @return 无分隔符日期
	 */
	public static String toIndexDate(String date){
		String indexDate = "19700101";
		if(date!=null&&date.trim().length()>=10){
			int i = date.indexOf(" ");
			if(i!=-1){
				indexDate = date.substring(0, i).replaceAll("-", "").replace("/","");
			}else{
				indexDate = date.replaceAll("-", "").replace("/","");
			}
		}
		return indexDate;
	}
	
	/**
	 * 将无分隔符日期转换为普通分隔符日期 1970-01-01
	 * @param indexDate String 无分隔符日期字符串
	 * @return String 分隔符日期字符串
	 */
	public static String toDate(String indexDate){
		StringBuilder sb = new StringBuilder("1970-01-01");
		if(indexDate!=null&&indexDate.trim().length()==8){
			sb = new StringBuilder(indexDate);
			sb.insert(4, '-');
			sb.insert(7, '-');
		}
		return sb.toString();
	}
		
	/**
	 * 根据指定日期格式将给出的日期字符串dateStr转换成一个日期对象
	 * @param dateStr String 日期字符串
	 * @param pattern String 日期格式
	 * @return Date 日期
	 */
	public static Date parseDate(String dateStr,String pattern) {
		if(dateStr == null || dateStr.length()==0 || pattern == null || pattern.length() == 0){
		    return null;
		}
		DateFormat fmt = new SimpleDateFormat(pattern);
		Date result = null;
		try {
			result = fmt.parse(dateStr);
		} catch (ParseException e) {
		    e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 将日期对象按默认时区转换成"yyyy-MM-dd HH:mm:ss"格式的字符串
	 * @param d Date 日期
	 * @return String yyyy-MM-dd HH:mm:ss"格式的字符串
	 */
	public static String asHtml(Date d) {
		return asHtml(d, TimeZone.getDefault());
	}
	
	/**
	 * 将日期对象按指定时区转换成"yyyy-MM-dd HH:mm:ss"格式的字符串
	 * @param date Date 日期
	 * @param timeZone TimeZone 时区
	 * @return String  日期字符串
	 */
	public static String asHtml(Date date, TimeZone timeZone) {
		String key = timeZone.getID();
		DateFormat formatter = (DateFormat) dateFormatCache.get(key);
		if (formatter == null) {
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			formatter.setTimeZone(timeZone);
			dateFormatCache.put(key, formatter);
		}
		synchronized (formatter) {
			String s = formatter.format(date);
			return s;
		}
	}
	
	/**
	 * 将日期对象转换成"yyyy-MM-dd HH:mm:ss"格式的字符串
	 * @param date Date 日期
	 * @return Stringyyyy-MM-dd HH:mm:ss"格式的字符串
	 */
	public static String asString(Date date) {
		if(date==null){
		    return null;
		}
		String key = "asString";
		DateFormat formatter = (DateFormat) dateFormatCache.get(key);
		if (formatter == null) {
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dateFormatCache.put(key, formatter);
		}
		return formatter.format(date);
	}
		
	/**
	 * 将data对象转换成"yyyy-MM-dd"格式的字符串
	 * @param date Date 日期
	 * @return String yyyy-MM-dd"格式的字符串
	 */
	public static String asShortString(Date date) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}
	
	/**
	 * 将data对象按指定时区转换成"yyyy-MM-dd"格式的字符串
	 * @param date Date日期
	 * @param timeZone TimeZone 时区
	 * @return String yyyy-MM-dd"格式的字符串
	 */
	public static String asShortString(Date date, TimeZone timeZone) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.setTimeZone(timeZone);
		return formatter.format(date);
	}
	
	/**
	 * 获取给定秒的日期
	 * @param time int秒数
	 * @return Date 日期
	 */
	public static Date getDate(int time) {
		return new Date(time * 1000L);
	}
	
	/**
	 * 获取当前时间的秒
	 * @return int 秒数
	 */
	public static int currentTimeSeconds() {
		return (int) (System.currentTimeMillis() / 1000L);
	}
	
	/**
	 * 获取两日期的秒差
	 * @param a Date 日期
	 * @param b Date 日期
	 * @return int 秒差
	 */
	public static int secondsAfter(Date a, Date b) {
		return (int) ((a.getTime() - b.getTime()) / 1000L);
	}
		
	/**
	 * 获取指定的Date对象
	 * @param yy int 年
	 * @param mm int 月
	 * @param dd int 日
	 * @return Date 日期
	 */
	public static Date getDate(int yy, int mm, int dd) {
		return (new GregorianCalendar(yy, mm - 1, dd)).getTime();
	}
	
	/**
	 * 获取当前时间的秒
	 * @return int 秒
	 */
	public static int unixTimeStamp() {
		return (int) (System.currentTimeMillis() / 1000L);
	}
	
	/**
	 * 获取指定Date的秒
	 * @param date Date 日期
	 * @return int 秒
	 */
	public static int unixTimeStamp(Date date) {
		return (int) (date.getTime() / 1000L);
	}
	
	/**
	 * 获取全部时区
	 * @return String[][] 时区集合
	 */
	public static String[][] getTimeZoneList() {
		synchronized (timeZoneLock) {
			if (timeZoneList == null) {
				Date now = new Date();
				String[]  timeZoneIDs= TimeZone.getAvailableIDs();
				timeZoneList = new String[timeZoneIDs.length][2];
				for (int i = 0; i < timeZoneList.length; i++) {
					String zoneID = timeZoneIDs[i];
					timeZoneList[i][0] = zoneID;
					timeZoneList[i][1] = getTimeZoneName(zoneID, now, Locale.getDefault());
				}
			}
		}
		return timeZoneList;
	}
	
	/**
	 * 获得当前时间
	 * @param zoneID String 时区id
	 * @param now Date 当前日期
	 * @param locale Locale 地区
	 * @return String 时间
	 */
	private static String getTimeZoneName(String zoneID, Date now, Locale locale) {
		TimeZone zone = TimeZone.getTimeZone(zoneID);
		StringBuffer buf = new StringBuffer();
		int offset = zone.getRawOffset();
		if (zone.inDaylightTime(now) && zone.useDaylightTime()){
		    offset = (int) (offset + 0x36ee80L);
		}
		if (offset < 0){
		    buf.append("(GMT-");
		}else{
		    buf.append("(GMT+");
		}
		offset = Math.abs(offset);
		int hours = offset / 0x36ee80;
		int minutes = (offset % 0x36ee80) / 60000;
		if (hours < 10){
		    buf.append("0").append(hours).append(":");
		}else{
		    buf.append(hours).append(":");
		}
		if (minutes < 10){
		    buf.append("0").append(minutes);
		}else{
		    buf.append(minutes);
		}
		buf.append(") ").append(zoneID).append(" ");
		buf.append(zone.getDisplayName(true, 0, locale));
		return buf.toString();
	}
	
	/**
	 * 将日期类型转化为字符串，默认格式yyyy-MM-dd
	 * @param date Date 日期
	 * @return string 日期字符串
	 */
	public static String dateToStr(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
		return newFormat.format(date);
	}
	
	/**
	 * 将日期类型转化为字符串,根据传入的格式输出日期（如YYYY年MM月DD日等）
	 * @param date 日期
	 * @param sFormat 日期格式
	 * @return string 日期字符串
	 */
	public static String dateToStr(Date date, String sFormat) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat newFormat = new SimpleDateFormat(sFormat);
		return newFormat.format(date);
	}
	
	/**
	 * 字符串转化为日期类型，默认格式yyyy-MM-dd HH:mm:ss
	 * @param strDate 字符串
	 * @return 日期
	 */
	public static Date strToDate(String strDate) {
		if (StringUtil.isEmpty(strDate) || "".equals(strDate.trim())) {
			return null;
		}
		SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return newFormat.parse(strDate);
		} catch (ParseException ex) {
			return null;
		}
	}
	
	/**
	 * 字符创转化成指定格式的日期类型(如yyyy年MM月DD日->date)
	 * @param strDate 字符串
	 * @param sFormat 字符串
	 * @return 返回日期
	 */
	public static Date strToDate(String strDate, String sFormat) {
		if (StringUtil.isEmpty(strDate) || "".equals(strDate.trim())) {
			return null;
		}
		SimpleDateFormat newFormat = new SimpleDateFormat(sFormat);
		try {
			return newFormat.parse(strDate);
		} catch (ParseException ex) {
			return null;
		}
	}
	
	/**
	 * 将日期String转化成当前年龄(如1984年09月8日->24)
	 * @param date 日期
	 * @return 年龄
	 */
	public static String dateToAge(String date) {
		if (StringUtil.isEmpty(date) || "".equals(date.trim())) {
			return null;
		}
		String sReturn = "";
		try {
			Date dt = strToDate(date);
			String bMonDay = new SimpleDateFormat("MMdd").format(dt);
			String nMonDay = new SimpleDateFormat("MMdd").format(new Date());
			long md = Long.parseLong(nMonDay) - Long.parseLong(bMonDay);

			String sYear = date.trim().substring(0, 4);
			long lYear = Long.parseLong(sYear);
			SimpleDateFormat newFormat = new SimpleDateFormat("yyyy");
			long curYear = Long.parseLong(newFormat
					.format(new Date()));
			if (md < 0) {
				sReturn = String.valueOf(curYear - lYear - 1);
			} else {
				sReturn = String.valueOf(curYear - lYear);
			}

			return sReturn;
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * 日期中本月月末的天
	 * @param date 日期
	 * @return 月末天
	 */
	public static int getMonthEndDay(java.sql.Date date) {
		// 取 本月 月末的天
		if (date == null){
		    return 0;
		}
		int year = Integer.parseInt(date.toString().substring(0, 4));
		int month = Integer.parseInt(date.toString().substring(5, 7));
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONDAY, month - 1);
		cal.set(Calendar.DATE, 1);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);

	}
	
	/**
	 * 日期中本月月末的日期
	 * @param date 日期
	 * @return 月末日期
	 * @throws Exception 抛出异常 
	 */
	public static java.sql.Date getMonthEnd(java.sql.Date date)
			throws Exception {
		// 取 本月 月末
		if (date == null){
		    return null;
		}
		int year = Integer.parseInt(date.toString().substring(0, 4));
		int month = Integer.parseInt(date.toString().substring(5, 7));
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONDAY, month - 1);
		cal.set(Calendar.DATE, getMonthEndDay(date));

		java.sql.Date d = new java.sql.Date(cal.getTime().getTime());
		return d;
	}
	
	/**
	 * 日期中本月月中的日期
	 * @param date 日期
	 * @return 月中日期
	 * @throws Exception 抛出异常 
	 */
	public static java.sql.Date getMonthMid(java.sql.Date date)
			throws Exception {
		// 取 本月 月中
		if (date == null){
		    return null;
		}
		int year = Integer.parseInt(date.toString().substring(0, 4));
		int month = Integer.parseInt(date.toString().substring(5, 7));
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONDAY, month - 1);
		cal.set(Calendar.DATE, 15);

		java.sql.Date d = new java.sql.Date(cal.getTime().getTime());
		return d;

	}
	
	/**
	 * 日期中下过月月初的日期
	 * @param date 日期
	 * @return 月初日期
	 * @throws Exception 抛出异常 
	 */
	public static java.sql.Date getNextMonthFirst(java.sql.Date date)
			throws Exception {
		// 取 次月 月初
		if (date == null){
		    return null;
		}
		int year = Integer.parseInt(date.toString().substring(0, 4));
		int month = Integer.parseInt(date.toString().substring(5, 7));

		if (month == 12) {
			year = year + 1;
			month = 1;
		} else{
		    month = month + 1;
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONDAY, month - 1);
		cal.set(Calendar.DATE, 1);

		java.sql.Date d = new java.sql.Date(cal.getTime().getTime());
		return d;
	}
	
	/**
	 * 日期中上月月末的日期
	 * @param date 日期
	 * @return 上月月末日期
	 * @throws Exception 抛出异常 
	 */
	public static java.sql.Date getPreviousMonthEnd(java.sql.Date date)
			throws Exception {
		// 取 上一月末
		if (date == null){
		    return null;
		}
		int year = Integer.parseInt(date.toString().substring(0, 4));
		int month = Integer.parseInt(date.toString().substring(5, 7));

		if (month == 1) {
			year = year - 1;
			month = 12;
		} else{
		    month = month - 1;
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONDAY, month - 1);
		cal.set(Calendar.DATE, 1);
		java.sql.Date d = new java.sql.Date(cal.getTime().getTime());
		cal.set(Calendar.DATE, getMonthEndDay(d));
		d = new java.sql.Date(cal.getTime().getTime());
		return d;
	}
	
	/**
	 * 传入日期上一天的日期
	 * @param date 日期
	 * @return 上一天的日期
	 * @throws Exception 抛出异常 
	 */
	public static java.sql.Date getPreviousDay(java.sql.Date date)
			throws Exception {
		// 取 上一天
		if (date == null){
		    return null;
		}
		int year = Integer.parseInt(date.toString().substring(0, 4));
		int month = Integer.parseInt(date.toString().substring(5, 7));
		int day = Integer.parseInt(date.toString().substring(8, 10));
		if (day == 1) {
			if (month == 1) {
				year = year - 1;
				month = 12;
				day = 31;
			} else {
				month = month - 1;
				Calendar c = Calendar.getInstance();
				c.set(Calendar.YEAR, year);
				c.set(Calendar.MONTH, month - 1);
				c.set(Calendar.DATE, 1);
				java.sql.Date d = new java.sql.Date(c.getTime().getTime());
				day = getMonthEndDay(d);
			}
		} else{
		    day = day - 1;
		}
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DATE, day);
		java.sql.Date d = new java.sql.Date(c.getTime().getTime());
		// 可以这样 d = java.sql.Date.valueOf("2004-01-01");
		return d;
	}
	
	/**
	 * 传入日期下一天的日期
	 * @param date 日期
	 * @return 下一天的日期
	 * @throws Exception 抛出异常 
	 */
	public static java.sql.Date getNextDay(java.sql.Date date) throws Exception {
		// 取 下一天
		if (date == null){
		    return null;
		}
		int year = Integer.parseInt(date.toString().substring(0, 4));
		int month = Integer.parseInt(date.toString().substring(5, 7));
		int day = Integer.parseInt(date.toString().substring(8, 10));

		if (day == getMonthEndDay(date)) {
			if (month == 12) {
				year = year + 1;
				month = 1;
			} else{
			    month = month + 1;
			}
			day = 1;
		} else{
		    day = day + 1;
		}
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DATE, day);
		java.sql.Date d = new java.sql.Date(c.getTime().getTime());

		return d;
	}
	
	/**
	 * 获得日期的下一个星期一的日期
	 * @param date 日期
	 * @return java.sql.Date 下一个星期一的日期
	 */
	public static java.sql.Date getNextMonday(java.sql.Date date) {
		Calendar d1 = Calendar.getInstance();
		d1.setTime(date);
		java.sql.Date result = date;
		do {
			d1 = (Calendar) d1.clone();
			d1.add(Calendar.DATE, 1);
		} while (d1.get(Calendar.DAY_OF_WEEK) != 2);
		
		result = new java.sql.Date(d1.getTime().getTime());
		return result;
	}
	
	/**
	 * 日期加天数后日期
	 * @param date SQL日期
	 * @param days 增加天数
	 * @return 加天数后日期
	 */
	public static String getAddDaysDate(java.sql.Date date,int days){
		long das = date.getTime() / (3600 * 24 * 1000) + days + 1;
		return dateToStr(new Date(das*(3600 * 24 * 1000)));
	}
	
	/**
	 * 计算日期加天数后的日期
	 * @param date 日期
	 * @param days 增加天数
	 * @return 加天数后的日期
	 */
	public static String getAddDaysDate(String date,int days){
		Date d = strToDate(date);
		long das = d.getTime() / (3600 * 24 * 1000) + days + 1;
		
		return dateToStr(new Date(das*(3600 * 24 * 1000)));
	}
	
	/**
	 * 两个日期进行计算相差天数
	 * @param sd 开始日期
	 * @param ed 结束日期
	 * @return long 相差天数
	 */
	public static long getXcDays(java.sql.Date sd, java.sql.Date ed) {
		return (ed.getTime() - sd.getTime()) / (3600 * 24 * 1000);
	}
	
	/**
	 * 计算传入两个日期相差月数，同月返回0
	 * @param mind 开始日期
	 * @param maxd 结束日期
	 * @return int 相差月数
	 */
	public static int getXcyf(java.sql.Date mind, java.sql.Date maxd) {
		// 月份 相差 同月为 0
		int year = Integer.parseInt(maxd.toString().substring(0, 4));
		int month = Integer.parseInt(maxd.toString().substring(5, 7));
		int year1 = Integer.parseInt(mind.toString().substring(0, 4));
		int month1 = Integer.parseInt(mind.toString().substring(5, 7));
		int xc = (year - year1) * 12 + (month - month1);
		if (xc < 0){
		    xc = 0;
		}
		return xc;
	}
	
	/**
	 * 计算2个日期之间的工作日(去除周六、日)天数
	 * @param sd 开始日期
	 * @param ed 结束日期
	 * @return int 2个日期之间的工作日(去除周六、日)天数
	 */
	public static int getWorkingDays(java.sql.Date sd, java.sql.Date ed) {
		Calendar d1 = Calendar.getInstance();
		Calendar d2 = Calendar.getInstance();
		d1.setTime(sd);
		d2.setTime(ed);
		
		int result = -1;
		// swap dates so that d1 is start and d2 is end
		if (d1.after(d2)){ 
			Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		// 开始日期的日期偏移量
		int chargeStartDate = 0;
		// 结束日期的日期偏移量
		int chargeEndDate = 0;
		// 日期不在同一个日期内
		int stmp;
		int etmp;
		stmp = 7 - d1.get(Calendar.DAY_OF_WEEK);
		etmp = 7 - d2.get(Calendar.DAY_OF_WEEK);

		if (stmp != 0 && stmp != 6) {
			// 开始日期为星期六和星期日时偏移量为0
		    chargeStartDate = stmp;
		}
		if (etmp != 0 && etmp != 6) {
			// 结束日期为星期六和星期日时偏移量为0
		    chargeEndDate = etmp - 1;
		}
		result = (((int)DateUtil.getXcDays(getNextMonday(sd), getNextMonday(ed)) + 1) / 7) 
					* 5 + chargeStartDate - chargeEndDate;
		return result;
	}
	
	/**
	 * 计算2个日期之间的休息日(周六、日)天数
	 * @param sd 开始日期
	 * @param ed 结束日期
	 * @return int 2个日期之间的休息日(周六、日)天数
	 */
	public static int getHolidays(java.sql.Date sd, java.sql.Date ed) {
		return (int)DateUtil.getXcDays(sd, ed) + 1 - getWorkingDays(sd, ed);

	}
	
	/**
	 * 获取当前日期，并返回java.sql.Date数据类型
	 * @return 返回java.sql.Date数据类型
	 */
	public static java.sql.Date getNowSqlDate() {
		java.sql.Date cd = null;
		try {
			Date d = new Date();
			cd = new java.sql.Date(d.getTime());
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return cd;
	}
	
	/**
	 * 取得该日期的年
	 * @param  mdate 日期
	 * @return int 该日期的年
	 */
	public static int getYear(Date mdate) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy");
		String syear = sd.format(mdate);
		return Integer.parseInt(syear);
	}
	
	/**
	 * 取得该字符串的年
	 * @param mdate 日期形字符串，如“2002-10-11” "2002-9-2"
	 * @return int 该字符串的年
	 */
	public static int getYear(String mdate) {
		String syear = mdate.substring(0, 4);
		return Integer.parseInt(syear);
	}
	
	/**
	 * 取得该日期的月
	 * @param mdate 日期形字符串，如“2002-10-11” "2002-9-2"
	 * @return int 该日期的月
	 */
	public static int getMonth(Date mdate) {
		SimpleDateFormat sm = new SimpleDateFormat("MM");
		String smon = sm.format(mdate);
		return Integer.parseInt(smon);
	}

	/**
	 * 取得该字符串的月
	 * @param mdate 日期形字符串
	 * @return int 该字符串的月
	 */
	public static int getMonth(String mdate) {
		String smon1;
		String smon2;
		StringBuffer smon = new StringBuffer();
		smon1 = mdate.substring(5, 6);
		smon2 = mdate.substring(6, 7);
		if (smon2.equals("-")) {
			smon.append(smon1);
		} else {
			smon.append(smon1);
			smon.append(smon2);
		}
		return Integer.parseInt(smon.toString());
	}
	
	/**
	 * 取得该日期的天
	 * @param mdate 日期
	 * @return int 该日期的天
	 */
	public static int getDay(Date mdate) {
		SimpleDateFormat sd = new SimpleDateFormat("dd");
		String sday = sd.format(mdate);
		return Integer.parseInt(sday);
	}

	
	/**
	 * 取得该字符串的天
	 * @param mdate 日期形字符串，如“2002-10-11” "2002-9-2"
	 * @return int 该字符串的天
	 */
	public static int getDay(String mdate) {
		int i = 0;
		int j;
		int l = mdate.length();
		String s = "";
		String sday1;
		String sday2;
		StringBuffer sday = new StringBuffer();
		for (j = 0; j < 2; j++, s = "") {
			while (!(s.equals("-"))) {
				s = mdate.substring(i, i + 1);
				i++;
			}
		}
		sday1 = mdate.substring(i, i + 1);
		sday.append(sday1);
		if (!(i == l - 1)) {
			sday2 = mdate.substring(i + 1, i + 2);
			sday.append(sday2);
		}
		return Integer.parseInt(sday.toString());
	}
	
	/**
	 * 获得当前年-月最大天数
	 * @param year 年度
	 * @param month 月份
	 * @return int 当前年-月最大天数
	 */
	public static int getMonMDay(int year, int month) {
		if ((month == 1) || (month == 3) || (month == 5) || (month == 7)
				|| (month == 8) || (month == 10) || (month == 12)) {
			return 31;
		} else {
			if (month != 2) {
				return 30;
			} else {
				if (ifLeap(year)) {
					return 29;
				} else {
					return 28;
				}
			}
		}
	}
	
	/**
	 * 如果是闰年，返回true 否则返回false
	 * @param year 传入的年份
	 * @return boolean 是否闰年
	 */
	public static boolean ifLeap(int year) {
		if (!(year % 4 == 0)) {
			return false;
		} else {
			if (year % 100 != 0) {
				return true;
			} else {
				if (year % 400 == 0) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
	
	/**
	 * 判断是否日期类型，如果是日期类型，返回true else 返回false
	 * @param mdate 字符串
	 * @return boolean 是否是日期
	 */
	public static boolean istDate(String mdate){
		String s;
		int syearInt;
		int smonInt;
		int sdayInt;
		int k;
		int m = mdate.indexOf("-");
		if (m == -1) {
			return false;
		} else {
			s = mdate.substring(0, m);
			try {
				k = Integer.parseInt(s);
			} catch (NumberFormatException e) {
				return false;
			}
			if (k > 1900) {
			    syearInt = getYear(mdate);
			} else {
				return false;
			}
		}

		if (syearInt < 9999) {
		    smonInt = getMonth(mdate);

			if ((smonInt > 0) && (smonInt < 13)) {
			    sdayInt = getDay(mdate);
				if ((sdayInt > 0) && (sdayInt < 32)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * 两个日期进行比较
	 * @param rq1 日期
	 * @param rq2 日期
	 * @return 如果rq1小于rq2,返回-1;rq1等于rq2,返回0;rq1大于rq2,返回1;
	 */
	public static int compareDate(Date rq1, Date rq2) {
		int flag = 0;
		try {
			flag = rq1.compareTo(rq2);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 判断字符串是否是日期时间 例"1981-12-12 12:12:12"
	 * @param str 日期字符串
	 * @return 是否是日期时间
	 */
	public static boolean isDateTime(String str) {
		if (str == null){
		    return true;
		}
		return str
                .matches("^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29)) ([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$");
    }	

	/**
	 * 判断字符串是否是日期 例如"1981-12-12"
	 * @param str 字符串
	 * @return 是否是日期
	 */
	public static boolean isDate(String str) {
		if (str == null){
		    return true;
		}
		return str.matches("^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29))$");
	}

	/**
	 * 判断字符串是否是时间 例"12:12:12"
	 * @param str 字符串
	 * @return 是否是时间
	 */
	public static boolean isTime(String str) {
		if (str == null){
		    return true;
		}
		return str.matches("^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$");
	}
	
	/**
	 * 获得任意天的是星期几
	 * @param y 年
	 * @param m 月
	 * @param d 日
	 * @return 星期几
	 */
	public int getWeek(int y, int m, int d) {
		if (m < 3) {
			m += 12;
			--y;
		}
		int w = (d + 1 + 2 * m + 3 * (m + 1) / 5 + y + (y >> 2) - y / 100 + y / 400) % 7;
		return w;
	}

	/**
	 * 获得日期是星期几
	 * @param date 日期
	 * @return 星期几
	 */
	public static String getChineseWeek(Date date) {
		Calendar d1 = Calendar.getInstance();
		d1.setTime(date);
		final String[] dayNames = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五","星期六" };
		int dayOfWeek = d1.get(Calendar.DAY_OF_WEEK);

		return dayNames[dayOfWeek - 1];

	}	
	
	/**
	 * 指定时间距离当前时间的中文信息
	 * @param time
	 * @return
	 */
	public static String getLnow(Date date) {
		Date now = new Date();
		long timel = now.getTime() - date.getTime();
		
		if (timel / 1000 < 60) {
			return "刚刚";
		} else if (timel / 1000 / 60 < 60) {
			return timel / 1000 / 60 + "分钟前";
		} else if (timel / 1000 / 60 / 60 < 24) {
			return timel / 1000 / 60 / 60 + "小时前";
		} else if(timel / 1000 / 60 / 60 /24 < 7){
			return timel / 1000 / 60 / 60 / 24 + "天前";
			//return getChineseWeek(date);
		} else if(date.getYear() == now.getYear()){
			return date.getMonth()+1 + "月" + date.getDate() + "日";
		}else{
			return date.getYear()+1900 + "年" + (date.getMonth()+1) + "月" + date.getDate() + "日";
		}
	}
	
	/**
	 * 时间社交化
	 * @param time
	 * @return
	 */
	public static String getLnow2(Date date) {
		Date now = new Date();
		if(dateToStr(date, "yyyyMMdd").equals(dateToStr(now, "yyyyMMdd"))){
			return dateToStr(date, "HH:mm");
		}else if(dateToStr(date, "yyyy").equals(dateToStr(now, "yyyy"))){
			return dateToStr(date, "MM-dd");
		}else{
			return dateToStr(date, "yyyy-MM-dd");
		}
	}
}