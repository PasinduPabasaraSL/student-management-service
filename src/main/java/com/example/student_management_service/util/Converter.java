package com.example.student_management_service.util;

import com.example.student_management_service.dto.StudentDto;
import com.example.student_management_service.entity.Student;
import org.modelmapper.ModelMapper;

public class Converter {
    private final ModelMapper modelMapper;

    public Converter() {
        this.modelMapper = new ModelMapper();
    }

    public StudentDto studentToStudentDto(Student student) {
        return modelMapper.map(student, StudentDto.class);
    }

    public Student studentDtoToStudent(StudentDto studentDto) {
        return modelMapper.map(studentDto, Student.class);
    }

}
