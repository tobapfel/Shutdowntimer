package de.tobapfel.programm.timer;

public class time {
	private int h, min, s;

	public time() {
		h = 0;
		min = 0;
		s = 0;
	}

	public time(int hour, int minute, int second) {
		if (hour >= 0 && minute >= 0 && second >= 0) {
			h = hour;
			min = minute;
			s = second;
		} else {
			throw new IllegalArgumentException("time can't be negative");
		}
	}

	public time(time t) {
		h = t.h;
		min = t.min;
		s = t.s;
	}

	public int getHour() {
		return this.h;
	}

	public int getMinute() {
		return this.min;
	}

	public int getSecond() {
		return this.s;
	}

	public time normalizeTime() {
		int s = this.toSecond();
		time formatTime = new time(normalizeSec(s));
		return formatTime;

	}

	static public time normalizeSec(int second) {
		int h = second / 3600;
		int m = (second - (h * 3600)) / 60;
		int s = second - ((h * 3600) + (m * 60));
		time seconds = new time(h, m, s);
		return seconds;
	}

	public int toSecond() {
		return this.h * 3600 + this.min * 60 + this.s;
	}

	@Override
	public String toString() {
		String str = String.format("%02d", this.h) + ":" + String.format("%02d", this.min) + ":"
				+ String.format("%02d", this.s);
		return str;
	}
}
