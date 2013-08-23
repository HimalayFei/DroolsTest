package com.encima.utils;

import java.util.Calendar;
import java.util.Date;

public class TimeTools {

	public static boolean checkAfternoon(Date time) {
		Calendar eve = Calendar.getInstance();
		eve.setTime(time);
		eve.set(Calendar.HOUR_OF_DAY, 17);
		eve.set(Calendar.SECOND, 0);
		Calendar aft = Calendar.getInstance();
		aft.setTime(time);
		aft.set(Calendar.HOUR_OF_DAY, 12);
		aft.set(Calendar.SECOND, 0);
		Calendar timeCal = Calendar.getInstance();
		timeCal.setTime(time);
		if(timeCal.before(eve) && timeCal.after(aft)) {
			return true;
		}
		return false;
	}
}
