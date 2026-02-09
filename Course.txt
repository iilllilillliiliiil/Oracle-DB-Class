package com.java.test;

import java.util.*;

class Course {
	private String title;
	private Professor professor;
	private int maxStudents;
	private List<Student> enrolledStudents = new ArrayList<>();
	private String lectureTime;

	public Course(String title, Professor professor, int maxStudents, String lectureTime) {
		this.title = title;
		this.professor = professor;
		this.maxStudents = maxStudents;
		this.lectureTime = lectureTime;
	}

	// 학생 수강 등록 시 리스트에서 추가
	public boolean addStudent(Student s) {
		if (enrolledStudents.size() < maxStudents) {
			enrolledStudents.add(s);
			return true;
		}
		return false;
	}

	// 학생 수강 취소 시 리스트에서 제거
	public boolean removeStudent(Student s) {
		return enrolledStudents.remove(s);
	}

	// 강의명 반환
	public String getTitle() {
		return title;
	}

	// 강의명 설정
	public void setTitle(String title) {
		this.title = title;
	}

	// 담당 교수 반환
	public Professor getProfessor() {
		return professor;
	}

	// 최대 수강 인원 반환
	public int getMaxStudents() {
		return maxStudents;
	}

	// 최대 수강 인원 설정
	public void setMaxStudents(int maxStudents) {
		this.maxStudents = maxStudents;
	}

	// 현재 수강 신청한 학생 수 반환
	public int getEnrolledCount() {
		return enrolledStudents.size();
	}

	// 수강 신청한 학생 목록 반환
	public List<Student> getEnrolledStudents() {
		return enrolledStudents;
	}

	// 강의 시간 반환
	public String getLectureTime() {
		return lectureTime;
	}

	// 강의 시간 설정
	public void setLectureTime(String lectureTime) {
		this.lectureTime = lectureTime;
	}
}