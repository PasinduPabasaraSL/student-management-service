package com.example.student_management_service.service.impl;

import com.example.student_management_service.dto.StudentDto;
import com.example.student_management_service.entity.Student;
import com.example.student_management_service.service.StudentService;
import com.example.student_management_service.util.GetSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class StudentServiceImpl implements StudentService {
    GetSessionFactory getSessionFactory = new GetSessionFactory();
    SessionFactory sessionFactory = getSessionFactory.getSessionFactory();

    @Override
    public boolean addStudent(StudentDto studentDto) {
        boolean status = false;

        Student student = new Student();
        student.setName(studentDto.getName());
        student.setAge(studentDto.getAge());

        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.persist(student);
            transaction.commit();
            session.close();
            status = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return status;
    }

    @Override
    public boolean updateStudent(StudentDto studentDto) {
        boolean status = false;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Student student = session.get(Student.class, studentDto);

        if (student != null) {
            student.setId(studentDto.getId());
            student.setName(studentDto.getName());
            student.setAge(studentDto.getAge());
            session.persist(student);
            transaction.commit();
            session.close();
            status = true;
        }

        return status;
    }

    @Override
    public boolean deleteStudent(int id) {
        boolean status = false;
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();
        Student student = session.get(Student.class, id);

        if (student != null) {
            session.remove(student);
            transaction.commit();
            session.close();
            status = true;
        }

        return status;
    }

    @Override
    public StudentDto getStudentById(int id) {
        StudentDto studentDto = new StudentDto();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Student student = session.get(Student.class, id);
        if (student != null) {
            studentDto.setName(student.getName());
            studentDto.setAge(student.getAge());
            transaction.commit();
            session.close();
        } else {
            return null;
        }

        return studentDto;
    }

    @Override
    public List<StudentDto> getAllStudents() {
        List<Student> students;
        List<StudentDto> studentsDtos = new ArrayList<>();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        students = session.createQuery("select s from Student s", Student.class).list();

        for (Student student : students) {
            StudentDto studentDto = new StudentDto();
            studentDto.setId(student.getId());
            studentDto.setName(student.getName());
            studentDto.setAge(student.getAge());
            studentsDtos.add(studentDto);
        }

        transaction.commit();
        session.close();

        return studentsDtos;

    }
}
