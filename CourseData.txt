package com.java.test;

import java.util.*;

public class CourseData {
	public static void initializeCourses(List<Course> courseList, List<Professor> professors) {
		if (professors.size() < 2) {
			System.out.println("교수가 부족하여 초기 강의를 등록할 수 없습니다.");
			return;
		}

		courseList.add(new Course("Java 언어", professors.get(0), 30, "월 09:00 ~ 12:00"));
		courseList.add(new Course("C 언어", professors.get(1), 25, "화 14:00 ~ 16:00"));
		courseList.add(new Course("C# 언어", professors.get(0), 20, "수 09:00 ~ 11:00"));
		courseList.add(new Course("데이터베이스", professors.get(1), 35, "수 10:00 ~ 12:00"));
		courseList.add(new Course("운영체제", professors.get(0), 40, "목 13:00 ~ 15:00"));
	}
}