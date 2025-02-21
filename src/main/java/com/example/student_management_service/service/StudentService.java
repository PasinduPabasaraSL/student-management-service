package com.example.student_management_service.service;

import com.example.student_management_service.dto.StudentDto;

import java.util.List;

public interface StudentService {
    boolean addStudent(StudentDto studentDto);

    boolean updateStudent(StudentDto studentDto);

    boolean deleteStudent(int id);

    StudentDto getStudentById(int id);

    List<StudentDto> getAllStudents();
}