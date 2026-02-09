package com.java.test;

import java.util.*;

public class Admin extends User {
	// 관리자 객체 생성
	public Admin(String id, String name, String password) {
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
		// t1, t2 예시: "월 09:00 ~ 12:00"
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
			// 겹치는지 확인
			return (start1 < end2 && start2 < end1);
		} catch (Exception e) {
			return false;
		}
	}

	// 강의 등록 (시간 중복 체크, 부분 겹침도 체크)
	public void addCourse(List<Course> courseList, Scanner sc) {
		if (Main.professors.isEmpty()) {
			System.out.println("등록된 교수가 없습니다.\n먼저 교수를 등록하세요.");
			return;
		}
		viewCourses(courseList);
		// 교수 목록 출력
		System.out.println("[교수 목록]");
		for (int i = 0; i < Main.professors.size(); i++) {
			Professor prof = Main.professors.get(i);
			System.out.println((i + 1) + ". " + prof.name + " (" + prof.id + ")");
		}
		// 교수 선택
		System.out.print("담당 교수 번호 선택 : ");
		int profIdx = sc.nextInt();
		sc.nextLine();
		// 입력 유효성 검사
		if (profIdx < 1 || profIdx > Main.professors.size()) {
			System.out.println("잘못된 번호입니다.");
			return;
		}
		Professor selectedProfessor = Main.professors.get(profIdx - 1);
		// 강의 정보 입력
		System.out.print("강의명 입력 : ");
		String title = sc.nextLine();
		System.out.print("최대 수강 인원 입력 : ");
		int max = sc.nextInt();
		sc.nextLine();

		String newTime;
		while (true) {
			System.out.print("강의 시간 입력 (예 : 월 09:00 ~ 12:00) : ");
			newTime = sc.nextLine();

			// 교수가 개설한 강의시간 중복 체크 (부분 겹침 포함)
			boolean profConflict = false;
			for (Course c : courseList) {
				if (c.getProfessor().equals(selectedProfessor) && c.getLectureTime() != null
						&& isTimeOverlap(c.getLectureTime(), newTime)) {
					profConflict = true;
					break;
				}
			}
			if (profConflict) {
				System.out.println("해당 교수님이 이미 같은 시간대에 개설한 강의가 있습니다.\n다른 시간으로 지정해주세요.");
				continue;
			}
			break;
		}

		// 강의 생성 및 추가
		Course newCourse = new Course(title, selectedProfessor, max, newTime);
		courseList.add(newCourse);
		System.out.println("강의 등록 완료 : " + title + " (담당 : " + selectedProfessor.name + ")");
	}

	// 강의 수정 (시간 중복 체크 추가, 부분 겹침 포함)
	public void editCourse(List<Course> courseList, Scanner sc) {
		viewCourses(courseList);
		if (courseList.isEmpty()) {
			System.out.println("강의가 없습니다.");
			return;
		}
		System.out.println("[수정 가능한 강의 목록]");
		for (int i = 0; i < courseList.size(); i++) {
			Course edit = courseList.get(i);
			System.out.println((i + 1) + ". " + edit.getTitle() + " (담당 : " + edit.getProfessor().name + ") "
					+ "- 수강인원 : " + edit.getEnrolledCount() + " / " + edit.getMaxStudents() + " - 시간 : "
					+ edit.getLectureTime());
		}
		// 수정할 강의 선택
		System.out.print("수정할 강의 번호 입력 : ");
		int idx = sc.nextInt();
		sc.nextLine();
		// 유효성 검사
		if (idx >= 1 && idx <= courseList.size()) {
			Course edit = courseList.get(idx - 1);
			System.out.print("새 강의명 입력(현재 : " + edit.getTitle() + ") : ");
			String newTitle = sc.nextLine();
			System.out.print("새 최대 수강 인원 입력 (현재 : " + edit.getMaxStudents() + ") : ");
			int newMax = sc.nextInt();
			sc.nextLine();

			String newLectureTime;
			while (true) {
				System.out.print("새 강의 시간 입력 (현재 : " + edit.getLectureTime() + ") : ");
				newLectureTime = sc.nextLine();

				// 교수가 개설한 강의시간 중복 체크 (부분 겹침 포함)
				boolean profConflict = false;
				for (int i = 0; i < courseList.size(); i++) {
					Course c = courseList.get(i);
					if (c != edit && c.getProfessor().equals(edit.getProfessor()) && c.getLectureTime() != null
							&& isTimeOverlap(c.getLectureTime(), newLectureTime)) {
						profConflict = true;
						break;
					}
				}
				if (profConflict) {
					System.out.println("해당 교수님이 이미 같은 시간대에 개설한 강의가 있습니다.\n다른 시간으로 지정해주세요.");
					continue;
				}
				break;
			}
			// 강의 정보 수정
			edit.setTitle(newTitle);
			edit.setMaxStudents(newMax);
			edit.setLectureTime(newLectureTime);
			System.out.println("강의 수정 완료.");
		} else {
			System.out.println("잘못된 번호입니다.");
		}
		viewCourses(courseList);
	}

	// 강의 삭제
	public void deleteCourse(List<Course> courseList, Scanner sc) {
		viewCourses(courseList);
		System.out.print("삭제할 강의 번호 입력 : ");
		int idx = sc.nextInt();
		sc.nextLine();
		// 유효성 검사 및 삭제
		if (idx >= 1 && idx <= courseList.size()) {
			Course removed = courseList.remove(idx - 1);
			System.out.println("강의 삭제 완료 : " + removed.getTitle());
		} else {
			System.out.println("잘못된 번호입니다.");
		}
		viewCourses(courseList);
	}

	// 강의 관리 메뉴
	public void manageCourses(Scanner sc, List<Course> courseList) {
		while (true) {
			System.out.println("\n[강의 관리]");
			System.out.println("1. 전체 강의 보기");
			System.out.println("2. 강의 등록");
			System.out.println("3. 강의 수정");
			System.out.println("4. 강의 삭제");
			System.out.println("0. 돌아가기");
			System.out.print("선택 : ");
			int choice = sc.nextInt();
			sc.nextLine();

			switch (choice) {
			case 1:
				viewCourses(courseList);
				break;
			case 2:
				addCourse(courseList, sc);
				break;
			case 3:
				editCourse(courseList, sc);
				break;
			case 4:
				deleteCourse(courseList, sc);
				break;
			case 0:
				return;
			default:
				System.out.println("잘못된 입력입니다.");
			}
		}
	}

	// 관리자 메뉴
	public void manageUsers(Scanner sc, List<Student> students, List<Professor> professors) {
		while (true) {
			System.out.println("\n[사용자 관리]");
			System.out.println("1. 학생 등록");
			System.out.println("2. 교수 등록");
			System.out.println("3. 학생 수정");
			System.out.println("4. 교수 수정");
			System.out.println("5. 학생 삭제");
			System.out.println("6. 교수 삭제");
			System.out.println("7. 학생 목록");
			System.out.println("8. 교수 목록");
			System.out.println("0. 돌아가기");
			System.out.print("선택 : ");
			int sel = sc.nextInt();
			sc.nextLine();

			switch (sel) {
			// 학생 등록
			case 1:
				System.out.print("학생 학번 : ");
				String sid = sc.nextLine();
				System.out.print("학생 이름 : ");
				String sname = sc.nextLine();
				System.out.print("학생 비밀번호 : ");
				String spw = sc.nextLine();
				students.add(new Student(sid, sname, spw));
				System.out.println("학생 등록 완료.");
				break;
			// 교수 등록
			case 2:
				System.out.print("교수 사번 : ");
				String pid = sc.nextLine();
				System.out.print("교수 이름 : ");
				String pname = sc.nextLine();
				System.out.print("교수 비밀번호 : ");
				String ppw = sc.nextLine();
				professors.add(new Professor(pid, pname, ppw));
				System.out.println("교수 등록 완료.");
				break;
			// 학생 수정
			case 3:
				System.out.println("[학생 목록]");
				for (Student s : students)
					System.out.println("- " + s.id + " " + s.name + " (비밀번호 : " + s.password + ")");

				System.out.print("수정할 학생 학번 : ");
				String editSid = sc.nextLine();
				Student studentToEdit = null;
				for (Student s : students) {
					if (s.id.equals(editSid)) {
						studentToEdit = s;
						break;
					}
				}
				if (studentToEdit != null) {
					System.out.print("새 이름 입력 : ");
					String newName = sc.nextLine();
					System.out.print("새 비밀번호 입력 : ");
					String newPw = sc.nextLine();
					studentToEdit.name = newName;
					studentToEdit.password = newPw;
					System.out.println("학생 이름 수정 완료.");
				} else {
					System.out.println("해당 학번의 학생이 없습니다.");
				}
				break;
			// 교수 수정
			case 4:
				System.out.println("[교수 목록]");
				for (Professor p : professors)
					System.out.println("- " + p.id + " " + p.name + " (비밀번호 : " + p.password + ")");

				System.out.print("수정할 교수 사번 : ");
				String editPid = sc.nextLine();
				Professor professorToEdit = null;
				for (Professor p : professors) {
					if (p.id.equals(editPid)) {
						professorToEdit = p;
						break;
					}
				}
				if (professorToEdit != null) {
					System.out.print("새 이름 입력 : ");
					String newName = sc.nextLine();
					System.out.print("새 비밀번호 입력 : ");
					String newPw = sc.nextLine();
					professorToEdit.name = newName;
					professorToEdit.password = newPw;
					System.out.println("교수 이름 수정 완료.");
				} else {
					System.out.println("해당 사번의 교수가 없습니다.");
				}
				break;
			// 학생 삭제
			case 5:
				System.out.println("[학생 목록]");
				for (Student s : students)
					System.out.println("- " + s.id + " " + s.name + " (비밀번호 : " + s.password + ")");

				System.out.print("삭제할 학생 학번 : ");
				String delSid = sc.nextLine();
				if (students.removeIf(s -> s.id.equals(delSid))) {
					System.out.println("학생 삭제 완료.");
				} else {
					System.out.println("해당 학번의 학생이 없습니다.");
				}
				break;
			// 교수 삭제
			case 6:
				System.out.println("[교수 목록]");
				for (Professor p : professors)
					System.out.println("- " + p.id + " " + p.name + " (비밀번호 : " + p.password + ")");

				System.out.print("삭제할 교수 사번 : ");
				String delPid = sc.nextLine();
				if (professors.removeIf(p -> p.id.equals(delPid))) {
					System.out.println("교수 삭제 완료.");
				} else {
					System.out.println("해당 사번의 교수가 없습니다.");
				}
				break;
			// 학생 목록 출력
			case 7:
				System.out.println("[학생 목록]");
				for (Student s : students)
					System.out.println("- " + s.id + " " + s.name + " (비밀번호 : " + s.password + ")");
				break;
			// 교수 목록 출력
			case 8:
				System.out.println("[교수 목록]");
				for (Professor p : professors)
					System.out.println("- " + p.id + " " + p.name + " (비밀번호 : " + p.password + ")");
				break;
			// 돌아가기
			case 0:
				return;

			default:
				System.out.println("잘못된 입력입니다.");
			}
		}
	}

	// 관리자 메인 메뉴
	@Override
	public void showMenu(Scanner sc, List<Course> courseList) {
		while (true) {
			System.out.println("\n[관리자 메뉴]");
			System.out.println("1. 전체 강의 관리");
			System.out.println("2. 사용자 관리");
			System.out.println("0. 로그아웃");
			System.out.print("선택 : ");
			int sel = sc.nextInt();
			sc.nextLine();
			switch (sel) {
			case 1:
				manageCourses(sc, courseList);
				break;
			case 2:
				manageUsers(sc, Main.students, Main.professors);
				break;
			case 0:
				return;
			default:
				System.out.println("잘못된 입력입니다.");
			}
		}
	}
}