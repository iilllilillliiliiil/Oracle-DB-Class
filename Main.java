package com.java.test;

import java.util.*;

public class Main {
	static List<Admin> admins = new ArrayList<>();
	static List<Student> students = new ArrayList<>();
	static List<Professor> professors = new ArrayList<>();

	static {
		admins.add(new Admin("a01", "최관리자", "1234"));
	}

	static {
		students.add(new Student("10", "정학생", "10"));
		students.add(new Student("20", "박학생", "20"));
	}

	static {
		professors.add(new Professor("1", "홍교수", "1"));
		professors.add(new Professor("2", "김교수", "2"));
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		List<Course> courseList = new ArrayList<>();
		CourseData.initializeCourses(courseList, professors); // 초기강의 등록

		while (true) {
			System.out.println("=== 수강신청 시스템 ===");
			System.out.println("1. 관리자 로그인");
			System.out.println("2. 학생 로그인");
			System.out.println("3. 교수 로그인");
			System.out.println("0. 종료");
			System.out.print("선택 : ");
			int role = sc.nextInt();
			sc.nextLine();

			switch (role) {
			case 1:
				System.out.print("아이디 입력 : ");
				String aid = sc.nextLine();
				System.out.print("비밀번호 입력 : ");
				String apw = sc.nextLine();
				Admin admin = findAdminByIdAndPassword(aid, apw); // 관리자 ID,비밀번호 일치 확인
				if (admin != null) {
					admin.showMenu(sc, courseList);
				} else {
					System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
				}
				break;
			case 2:
				System.out.print("학번 입력 : ");
				String sid = sc.nextLine();
				System.out.print("비밀번호 입력 : ");
				String spw = sc.nextLine();
				Student student = findStudentByIdAndPassword(sid, spw); // 학생 ID,비밀번호 일치 확인
				if (student != null) {
					student.showMenu(sc, courseList);
				} else {
					System.out.println("학번 또는 비밀번호가 일치하지 않습니다.");
				}
				break;
			case 3:
				System.out.print("교수 사번 입력 : ");
				String pid = sc.nextLine();
				System.out.print("비밀번호 입력 : ");
				String ppw = sc.nextLine();
				Professor professor = findProfessorByIdAndPassword(pid, ppw); // 교수 ID,비밀번호 일치 확인
				if (professor != null) {
					professor.showMenu(sc, courseList);
				} else {
					System.out.println("교수 사번 또는 비밀번호가 일치하지 않습니다.");
				}
				break;
			case 0:
				System.out.println("시스템을 종료합니다.");
				sc.close();
				return;
			default:
				System.out.println("잘못된 입력입니다.");
			}
		}
	}

	// 관리자 ID,비밀번호 일치 확인
	public static Admin findAdminByIdAndPassword(String id, String pw) {
		for (Admin a : admins) {
			if (a.id.equals(id) && a.password.equals(pw))
				return a;
		}
		return null;
	}

	// 학생 ID,비밀번호 일치 확인
	public static Student findStudentByIdAndPassword(String id, String pw) {
		for (Student s : students) {
			if (s.id.equals(id) && s.password.equals(pw))
				return s;
		}
		return null;
	}

	// 교수 ID,비밀번호 일치 확인
	public static Professor findProfessorByIdAndPassword(String id, String pw) {
		for (Professor p : professors) {
			if (p.id.equals(id) && p.password.equals(pw))
				return p;
		}
		return null;
	}
}