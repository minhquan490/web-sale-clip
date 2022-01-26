package com.system.spring.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Random {

	private static long nowTime = ZonedDateTime.now(ZoneId.of("UTC+7")).toInstant().toEpochMilli();
	private static long oppositeTime = ZonedDateTime.now(ZoneId.of("UTC-7")).toInstant().toEpochMilli();

	public static long getRandomNumber() {
		String nowTimeString = String.valueOf(nowTime).substring(5, 7);
		String oppositeTimeString = String.valueOf(oppositeTime).substring(3, 5);
		long now = Long.parseLong(nowTimeString);
		long opposite = Long.parseLong(oppositeTimeString);
		return now * opposite;
	}
}
