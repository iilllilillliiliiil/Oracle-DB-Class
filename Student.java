package com.java.test;

import java.util.*;

class Student extends User {
	private List<Course> enrolledCourses = new ArrayList<>();

	public Student(String id, String name, String password) {
		super(id, name, password);
	}

	public void viewCourses(List<Course> courseList) {
		System.out.println("[전체 강의 목록]");
		if (courseList.isEmpty()) {
			System.out.println("강의가 없습니다.");
			return;
		}
		for (int i = 0; i < courseList.size(); i++) {
			Course c = courseList.get(i);
			System.out.println((i + 1) + ". " + c.getTitle() + " (담당 : " + c.getProfessor().name + ")" + " - 수강인원 : "
					+ c.getEnrolledCount() + " / " + c.getMaxStudents() + " - 시간 : " + c.getLectureTime());
		}
	}

	// 시간 중복 체크 함수 - TimeUtil 활용
	private boolean hasTimeConflict(Course newCourse) {
		for (Course c : enrolledCourses) {
			if (TimeUtil.isTimeOverlap(c.getLectureTime(), newCourse.getLectureTime())) {
				return true; // 시간이 겹침
			}
		}
		return false; // 충돌 없음
	}

	// && !hasTimeConflict 추가로 시간중복시 수강가능한 강의에서 삭제
	public void enrollCourse(List<Course> courseList, Scanner sc) {
		List<Course> availableCourses = new ArrayList<>();
		for (Course c : courseList) {
			if (!enrolledCourses.contains(c) && c.getEnrolledCount() < c.getMaxStudents() && !hasTimeConflict(c)) {
				availableCourses.add(c);
			}
		}

		if (availableCourses.isEmpty()) {
			System.out.println("수강 가능한 강의가 없습니다.");
			return;
		}

		System.out.println("수강 가능한 강의 목록 :");
		for (int i = 0; i < availableCourses.size(); i++) {
			Course c = availableCourses.get(i);
			System.out.println((i + 1) + ". " + c.getTitle() + " (담당 : " + c.getProfessor().name + ")" + " - 수강인원 : "
					+ c.getEnrolledCount() + " / " + c.getMaxStudents() + " - 시간 : " + c.getLectureTime());
		}

		System.out.print("수강할 강의 번호 입력 : ");
		int choice = sc.nextInt();
		sc.nextLine();

		if (choice >= 1 && choice <= availableCourses.size()) {
			Course selected = availableCourses.get(choice - 1);
			// 예외 발생시 2차 중복 방지 추가
			if (hasTimeConflict(selected)) {
				System.out.println("시간이 중복되는 강의를 신청할 수 없습니다.");
				return;
			}
			if (selected.addStudent(this)) {
				enrolledCourses.add(selected);
				System.out.println("수강 신청 완료 : " + selected.getTitle());
			} else {
				System.out.println("정원이 초과되었습니다.");
			}
		} else {
			System.out.println("잘못된 입력입니다.");
		}
	}

	// 수강 취소 하는 기능
	public void cancelEnrollment(Scanner sc) {
		if (enrolledCourses.isEmpty()) {
			System.out.println("취소할 수강 과목이 없습니다.");
			return;
		}

		for (int i = 0; i < enrolledCourses.size(); i++) {
			Course c = enrolledCourses.get(i);
			System.out.println((i + 1) + ". " + c.getTitle() + " (담당 : " + c.getProfessor().name + ")" + " - 수강인원 : "
					+ c.getEnrolledCount() + " / " + c.getMaxStudents() + " - 시간 : " + c.getLectureTime());
		}

		System.out.print("수강 취소할 강의 번호 입력 : ");
		int choice = sc.nextInt();
		sc.nextLine();

		if (choice >= 1 && choice <= enrolledCourses.size()) {
			Course removed = enrolledCourses.remove(choice - 1);
			removed.removeStudent(this);
			System.out.println("수강 취소 완료 : " + removed.getTitle());
		} else {
			System.out.println("잘못된 입력입니다.");
		}
	}

	// 신청 과목 확인 하는 기능
	public void showEnrolledCourses() {
		System.out.println("[" + name + "]님의 강의목록");
		if (enrolledCourses.isEmpty()) {
			System.out.println("신청된 강의가 없습니다.");
		} else {
			for (Course c : enrolledCourses) {
				System.out.println("- " + c.getTitle() + " (담당 : " + c.getProfessor().name + ")" + " - 수강인원 : "
						+ c.getEnrolledCount() + " / " + c.getMaxStudents() + " - 시간 : " + c.getLectureTime());
			}
			System.out.println("[시간표 보기]");
			TimeTable tt = new TimeTable();
			tt.insertCourses(enrolledCourses);
			tt.print();
		}
	}

	@Override
	public void showMenu(Scanner sc, List<Course> courseList) {
		while (true) {
			System.out.println("\n[학생 메뉴]");
			System.out.println("1. 전체 강의 보기");
			System.out.println("2. 수강 신청");
			System.out.println("3. 수강 취소");
			System.out.println("4. 내 수강 목록 보기");
			System.out.println("0. 로그아웃");
			System.out.print("선택 : ");
			int sel = sc.nextInt();
			sc.nextLine();
			switch (sel) {
			case 1:
				viewCourses(courseList);
				break;
			case 2:
				enrollCourse(courseList, sc);
				break;
			case 3:
				cancelEnrollment(sc);
				break;
			case 4:
				showEnrolledCourses();
				break;
			case 0:
				return;
			default:
				System.out.println("잘못된 입력입니다.");
			}
		}
	}
}