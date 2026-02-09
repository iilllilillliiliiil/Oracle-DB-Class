package com.java.test;

import java.util.*;

class Professor extends User {
	public Professor(String id, String name, String password) {
		super(id, name, password);
	}

	// 전체 강의 목록 출력
	public void viewCourses(List<Course> courseList) {
		System.out.println("[전체 강의 목록]");
		if (courseList.isEmpty()) {
			System.out.println("강의가 없습니다.");
			return;
		}
		// 강의 정보를 인덱스와 함께 출력
		for (int i = 0; i < courseList.size(); i++) {
			Course c = courseList.get(i);
			System.out.println((i + 1) + ". " + c.getTitle() + " (담당 : " + c.getProfessor().name + ") " + "- 수강인원 : "
					+ c.getEnrolledCount() + " / " + c.getMaxStudents() + " - 시간 : " + c.getLectureTime());
		}
	}

	// 강의 시간 겹침(부분 겹침 포함) 체크 함수
	private boolean isTimeOverlap(String t1, String t2) {
		try {
			String[] p1 = t1.split(" ");
			String[] p2 = t2.split(" ");
			if (p1.length < 4 || p2.length < 4)
				return false;
			String day1 = p1[0], day2 = p2[0];
			if (!day1.equals(day2))
				return false;

			int start1 = Integer.parseInt(p1[1].substring(0, 2));
			int end1 = Integer.parseInt(p1[3].substring(0, 2));
			int start2 = Integer.parseInt(p2[1].substring(0, 2));
			int end2 = Integer.parseInt(p2[3].substring(0, 2));
			return (start1 < end2 && start2 < end1);
		} catch (Exception e) {
			return false;
		}
	}

	// 강의 개설 (시간 중복 체크 추가, 부분 겹침 포함)
	public void createCourse(List<Course> courseList, Scanner sc) {
		List<Course> myCourses = new ArrayList<>();
		for (Course c : courseList) {
			if (c.getProfessor() == this) {
				myCourses.add(c);
			}
		}
		if (myCourses.isEmpty()) {
			System.out.println("개설한 강의가 없습니다.");
			return;
		}
		System.out.println("[전체 강의 목록]");
		if (courseList.isEmpty()) {
			System.out.println("강의가 없습니다.");
			return;
		}
		// 강의 정보를 인덱스와 함께 출력
		for (int i = 0; i < courseList.size(); i++) {
			Course c = courseList.get(i);
			System.out.println((i + 1) + ". " + c.getTitle() + " (담당 : " + c.getProfessor().name + ") " + "- 수강인원 : "
					+ c.getEnrolledCount() + " / " + c.getMaxStudents() + " - 시간 : " + c.getLectureTime());
		}
		// 시간표 출력
		System.out.println("\n[강의 시간표]");
		TimeTable tt = new TimeTable();
		tt.insertCourses(myCourses);
		tt.print();

		System.out.println("\n[강의 개설]");
		System.out.print("강의명 입력 : ");
		String title = sc.nextLine();
		System.out.print("최대 수강 인원 : ");
		int max = sc.nextInt();
		sc.nextLine();
		String lectureTime;

		while (true) {
			System.out.print("강의 시간 입력 (예 : 월 09:00 ~ 12:00) : ");
			lectureTime = sc.nextLine();

			// 본인이 개설한 강의시간 중복 체크 (부분 겹침 포함)
			boolean myConflict = false;
			for (Course c : courseList) {
				if (c.getProfessor().equals(this) && c.getLectureTime() != null
						&& isTimeOverlap(c.getLectureTime(), lectureTime)) {
					myConflict = true;
					break;
				}
			}
			if (myConflict) {
				System.out.println("이미 같은 시간대에 개설한 강의가 있습니다.\n겹치지 않게 시간을 입력해주세요.");
				continue;
			}
			break;
		}

		Course c = new Course(title, this, max, lectureTime);
		courseList.add(c);
		System.out.println("강의 생성 완료 : " + title);
	}

	// 강의 수정 기능 추가 (시간 중복 체크 추가, 부분 겹침 포함)
	public void editCourse(List<Course> courseList, Scanner sc) {
		// 본인이 개설한 강의만 보여주기
		List<Course> myCourses = new ArrayList<>();
		for (Course c : courseList) {
			if (c.getProfessor() == this) {
				myCourses.add(c);
			}
		}
		if (myCourses.isEmpty()) {
			System.out.println("개설한 강의가 없습니다.");
			return;
		}
		System.out.println("[내가 개설한 강의 목록]");
		for (int i = 0; i < myCourses.size(); i++) {
			Course c = myCourses.get(i);
			System.out.println((i + 1) + ". " + c.getTitle() + " - 수강인원 : " + c.getEnrolledCount() + " / "
					+ c.getMaxStudents() + " - 시간 : " + c.getLectureTime());
		}
		System.out.print("수정할 강의 번호 입력 : ");
		int idx = sc.nextInt();
		sc.nextLine();
		if (idx < 1 || idx > myCourses.size()) {
			System.out.println("잘못된 번호입니다.");
			return;
		}
		Course selected = myCourses.get(idx - 1);

		System.out.println("1. 강의명 수정");
		System.out.println("2. 최대 수강 인원 수정");
		System.out.println("3. 강의 시간 수정");
		System.out.println("0. 취소");
		System.out.print("수정할 항목 선택 : ");
		int sel = sc.nextInt();
		sc.nextLine();
		switch (sel) {
		case 1:
			System.out.print("새 강의명 입력 : ");
			String newTitle = sc.nextLine();
			selected.setTitle(newTitle);
			System.out.println("강의명이 수정되었습니다.");
			break;
		case 2:
			System.out.print("새 최대 수강 인원 입력 : ");
			int newMax = sc.nextInt();
			sc.nextLine();
			if (newMax < selected.getEnrolledCount()) {
				System.out.println("이미 신청한 학생 수보다 적게 설정할 수 없습니다.");
			} else {
				selected.setMaxStudents(newMax);
				System.out.println("최대 수강 인원이 수정되었습니다.");
			}
			break;
		case 3:
			String newLectureTime;
			while (true) {
				System.out.print("수정 시간 입력 : ");
				newLectureTime = sc.nextLine();

				// 본인이 개설한 강의와 시간 중복 체크 (부분 겹침 포함)
				boolean myConflict = false;
				for (Course c : courseList) {
					if (c != selected && c.getProfessor().equals(this) && c.getLectureTime() != null
							&& isTimeOverlap(c.getLectureTime(), newLectureTime)) {
						myConflict = true;
						break;
					}
				}
				if (myConflict) {
					System.out.println("이미 같은 시간대에 개설한 강의가 있습니다.\n겹치지 않게 시간을 입력해주세요.");
					continue;
				}
				break;
			}
			selected.setLectureTime(newLectureTime);
			System.out.println("시간이 수정되었습니다.");
			break;
		case 0:
			System.out.println("수정을 취소합니다.");
			break;
		default:
			System.out.println("잘못된 입력입니다.");
		}
	}

	// 본인이 개설한 강의만 삭제할 수 있는 기능
	public void deleteMyCourse(List<Course> courseList, Scanner sc) {
		// 본인이 개설한 강의만 보여주기
		List<Course> myCourses = new ArrayList<>();
		for (Course c : courseList) {
			if (c.getProfessor() == this) {
				myCourses.add(c);
			}
		}
		if (myCourses.isEmpty()) {
			System.out.println("개설한 강의가 없습니다.");
			return;
		}
		System.out.println("[내가 개설한 강의 목록]");
		for (int i = 0; i < myCourses.size(); i++) {
			Course c = myCourses.get(i);
			System.out.println((i + 1) + ". " + c.getTitle() + " - 수강인원 : " + c.getEnrolledCount() + " / "
					+ c.getMaxStudents() + " - 시간 : " + c.getLectureTime());
		}
		System.out.print("삭제할 강의 번호 입력 : ");
		int idx = sc.nextInt();
		sc.nextLine();
		if (idx < 1 || idx > myCourses.size()) {
			System.out.println("잘못된 번호입니다.");
			return;
		}
		Course toRemove = myCourses.get(idx - 1);
		if (courseList.remove(toRemove)) {
			System.out.println("강의 삭제 완료 : " + toRemove.getTitle());
		} else {
			System.out.println("강의 삭제에 실패했습니다.");
		}
	}

	// 본인이 개설한 강의의 신청 학생 현황을 확인하는 기능
	public void viewMyCourseApplicants(List<Course> courseList, Scanner sc) {
		// 본인이 개설한 강의만 보여주기
		List<Course> myCourses = new ArrayList<>();
		for (Course c : courseList) {
			if (c.getProfessor() == this) {
				myCourses.add(c);
			}
		}
		if (myCourses.isEmpty()) {
			System.out.println("개설한 강의가 없습니다.");
			return;
		}
		System.out.println("[내가 개설한 강의 목록]");
		for (int i = 0; i < myCourses.size(); i++) {
			Course c = myCourses.get(i);
			System.out.println((i + 1) + ". " + c.getTitle() + " - 수강인원 : " + c.getEnrolledCount() + " / "
					+ c.getMaxStudents() + " - 시간 : " + c.getLectureTime());
		}
		System.out.print("신청 현황을 볼 강의 번호 입력 : ");
		int idx = sc.nextInt();
		sc.nextLine();
		if (idx < 1 || idx > myCourses.size()) {
			System.out.println("잘못된 번호입니다.");
			return;
		}
		Course selected = myCourses.get(idx - 1);
		List<Student> applicants = selected.getEnrolledStudents();
		System.out.println("[" + selected.getTitle() + "] 신청 학생 목록");
		if (applicants.isEmpty()) {
			System.out.println("신청한 학생이 없습니다.");
		} else {
			for (Student s : applicants) {
				System.out.println("▶ " + s.id + " " + s.name);
			}
		}
	}

	// 강의 시간표 보는 기능
	public void viewMyTimeTable(List<Course> courseList) {
		List<Course> myCourses = new ArrayList<>();
		for (Course c : courseList) {
			if (c.getProfessor() == this) {
				myCourses.add(c);
			}
		}
		if (myCourses.isEmpty()) {
			System.out.println("개설한 강의가 없습니다.");
			return;
		}
		// 시간표 출력
		System.out.println("\n[강의 시간표]");
		TimeTable tt = new TimeTable();
		tt.insertCourses(myCourses);
		tt.print();
	}

	@Override
	public void showMenu(Scanner sc, List<Course> courseList) {
		while (true) {
			System.out.println("\n[교수 메뉴]");
			System.out.println("1. 전체 강의 목록");
			System.out.println("2. 강의 시간표");
			System.out.println("3. 강의 개설");
			System.out.println("4. 강의 수정");
			System.out.println("5. 강의 삭제");
			System.out.println("6. 내 강의 신청 현황");
			System.out.println("0. 로그아웃");
			System.out.print("선택 : ");
			int sel = sc.nextInt();
			sc.nextLine();
			switch (sel) {
			case 1:
				viewCourses(courseList);
				break;
			case 2:
				viewMyTimeTable(courseList);
				break;
			case 3:
				createCourse(courseList, sc);
				break;
			case 4:
				editCourse(courseList, sc);
				break;
			case 5:
				deleteMyCourse(courseList, sc);
				break;
			case 6:
				viewMyCourseApplicants(courseList, sc);
				break;
			case 0:
				return;
			default:
				System.out.println("잘못된 입력입니다.");
			}
		}
	}
}