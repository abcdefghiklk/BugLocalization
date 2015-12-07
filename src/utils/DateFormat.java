package utils;

import java.text.SimpleDateFormat;

public class DateFormat {
	private SimpleDateFormat _format;
	public DateFormat(){
		this._format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	}
	public static SimpleDateFormat getFormat(){
		return new DateFormat()._format;
	}
}
