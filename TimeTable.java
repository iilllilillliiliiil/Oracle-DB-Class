package com.java.test;

import java.util.*;

public class TimeTable {
	// 요일 5개, 시간 9칸짜리 2차원 배열
	private String[][] table = new String[5][9];

	// 요일과 시간 문자열 배열
	private String[] days = { "월", "화", "수", "목", "금" };
	private String[] times = { "09~10시", "10~11시", "11~12시", "12~13시", "13~14시", "14~15시", "15~16시", "16~17시",
			"17~18시" };

	// 강의 데이터를 시간표 배열에 채워넣는 메서드
	public void insertCourses(List<Course> courses) {
		for (Course course : courses) {
			// 사용자 입력 시간을 정리된 포맷으로 변환 (예: "월9:00~11:00" → "월 09:00 ~ 11:00")
			String lectureTime = normalizeLectureTime(course.getLectureTime());
			if (lectureTime == null)
				continue;

			// "월 09:00 ~ 11:00" → ["월", "09:00", "~", "11:00"]
			String[] parts = lectureTime.split(" ");
			if (parts.length != 4)
				continue;

			int dayIndex = getDayIndex(parts[0]);
			int startHour = Integer.parseInt(parts[1].substring(0, 2));
			int endHour = Integer.parseInt(parts[3].substring(0, 2));
			int startIdx = startHour - 9;
			int endIdx = endHour - 9;

			// 시간표 배열에 과목명 채우기
			for (int i = startIdx; i < endIdx; i++) {
				if (dayIndex >= 0 && dayIndex < 5 && i >= 0 && i < 9) {
					if (table[dayIndex][i] == null) {
						table[dayIndex][i] = course.getTitle();
					} else if (!table[dayIndex][i].contains(course.getTitle())) {
						table[dayIndex][i] += "/" + course.getTitle();
					}
				}
			}
		}
	}

	// 시간표를 화면에 출력하는 메서드
	public void print() {
		// 상단 라벨
		System.out.printf("%-10s", "시간\\요일");
		for (String day : days) {
			System.out.printf("%-20s", day);
		}
		System.out.println();

		// 구분선
		System.out.println(
				"----------------------------------------------------------------------------------------------------");

		// 각 시간 줄 출력
		for (int i = 0; i < times.length; i++) {
			System.out.printf("%-10s", times[i]);
			for (int j = 0; j < days.length; j++) {
				String subject = table[j][i];
				if (subject == null)
					subject = ""; // 빈 칸이면 공백
				System.out.printf("%-20s", subject);
			}
			System.out.println();
			System.out.println(
					"----------------------------------------------------------------------------------------------------");
		}
	}

	// "월" → 0, "화" → 1, ... 으로 변환
	private int getDayIndex(String day) {
		if (day.equals("월"))
			return 0;
		if (day.equals("화"))
			return 1;
		if (day.equals("수"))
			return 2;
		if (day.equals("목"))
			return 3;
		if (day.equals("금"))
			return 4;
		return -1;
	}

	// 사용자 입력 시간 문자열을 정리해서 표준 형식으로 바꾸는 메서드
	public static String normalizeLectureTime(String input) {
		if (input == null)
			return null;

		// 공백 제거: "월 9:00~10:00" → "월9:00~10:00"
		input = input.trim().replace(" ", "");

		// 최소한의 길이와 "~" 포함 여부 확인
		if (input.length() < 10 || !input.contains("~"))
			return null;

		String day = input.substring(0, 1);
		String timePart = input.substring(1); // "9:00~10:00"
		String[] timeSplit = timePart.split("~");

		if (timeSplit.length != 2)
			return null;

		String start = padZero(timeSplit[0]); // "9:00" → "09:00"
		String end = padZero(timeSplit[1]); // "10:00" → 그대로

		return day + " " + start + " ~ " + end;
	}

	// "9:00" 같은 시간을 "09:00"으로 바꿔주는 메서드
	private static String padZero(String time) {
		String[] parts = time.split(":");
		if (parts.length != 2)
			return time;

		String hour = parts[0];
		if (hour.length() == 1)
			hour = "0" + hour;

		return hour + ":" + parts[1];
	}
}
