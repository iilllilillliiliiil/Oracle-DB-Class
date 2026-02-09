package com.java.test;

public class TimeUtil {
    // 시간 문자열 예 : "월 09:00 ~ 12:00"
    public static boolean isTimeOverlap(String time1, String time2) {
        // 요일 추출
        String day1 = time1.substring(0, 1);
        String day2 = time2.substring(0, 1);

        if (!day1.equals(day2)) {
            return false; // 요일 다르면 겹칠 수 없음
        }

        // 시간 부분 추출 : "09:00 ~ 12:00"
        String times1 = time1.substring(2).trim();
        String times2 = time2.substring(2).trim();

        // 시작, 종료 시간 분리
        String[] parts1 = times1.split("~");
        String[] parts2 = times2.split("~");

        int start1 = convertToMinutes(parts1[0].trim());
        int end1 = convertToMinutes(parts1[1].trim());
        int start2 = convertToMinutes(parts2[0].trim());
        int end2 = convertToMinutes(parts2[1].trim());

        // 시간 겹침 여부 판단
        // 겹치지 않는 조건 : end1 <= start2 || end2 <= start1
        if (end1 <= start2 || end2 <= start1) {
            return false;
        } else {
            return true; // 겹침
        }
    }

    private static int convertToMinutes(String time) {
        // "09:00" -> 9*60 + 0 = 540
        String[] hm = time.split(":");
        int hour = Integer.parseInt(hm[0]);
        int min = Integer.parseInt(hm[1]);
        return hour * 60 + min;
    }
}