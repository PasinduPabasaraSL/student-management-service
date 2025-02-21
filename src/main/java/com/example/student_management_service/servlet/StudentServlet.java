package com.example.student_management_service.servlet;

import com.example.student_management_service.dto.StudentDto;
import com.example.student_management_service.service.StudentService;
import com.example.student_management_service.service.impl.StudentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "studentApi", value = "/student/*")
@MultipartConfig
public class StudentServlet extends HttpServlet {

    private final ObjectMapper objectMapper;
    private final StudentService studentService;

    public StudentServlet() {
        this.objectMapper = new ObjectMapper();
        this.studentService = new StudentServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        switch (pathInfo) {
            case "/byId":
                int id = Integer.parseInt(req.getParameter("id"));
                StudentDto student = studentService.getStudentById(id);
                if (student != null) {
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    resp.getWriter().write(objectMapper.writeValueAsString(student));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("Not found");
                }
                break;
            case "/getAll":
                List<StudentDto> studentDtos = studentService.getAllStudents();
                if (studentDtos != null) {
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    resp.getWriter().write(objectMapper.writeValueAsString(studentDtos));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
                break;
            default:
                resp.getWriter().write("Not Found");
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        try {
            // Read the request body (JSON)
            BufferedReader reader = req.getReader();
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String requestBody = stringBuilder.toString();

            // Parse the JSON to get name and age
            ObjectMapper objectMapper = new ObjectMapper(); // Jackson JSON parser
            StudentDto studentDto = objectMapper.readValue(requestBody, StudentDto.class); // Deserialize JSON to StudentDto

            if (studentDto.getAge() <= 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Invalid age\"}");
                return;
            }

            // Add student using the service
            boolean isAdded = studentService.addStudent(studentDto);

            // Respond based on the result
            if (isAdded) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"message\":\"Student Added Successfully\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\":\"Student Not Added\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"An error occurred while processing the request\"}");
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            boolean isDeleted = studentService.deleteStudent(id);

            if (isDeleted) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("text/plain");
                resp.getWriter().write("Student Deleted Successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Student Not Found");
            }
        } catch (NumberFormatException | IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                resp.getWriter().write("Invalid ID format");
            } catch (IOException ioException) {
                System.out.println(ioException.getMessage());
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        req.setCharacterEncoding("UTF-8");

        // Retrieve the 'id' from query parameters
        String idParam = req.getParameter("id");
        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid id format\"}");
            return;
        }

        // Check if the body was already consumed by another process (e.g., filter)
        if (req.getReader() == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Request body already consumed\"}");
            return;
        }

        // Read the JSON body and map it to a StudentDto
        ObjectMapper objectMapper = new ObjectMapper();
        StudentDto studentDto = objectMapper.readValue(req.getReader(), StudentDto.class);

        // Set the 'id' from query parameters to the StudentDto object
        studentDto.setId(id);

        // Update the student using the service
        boolean isUpdated = studentService.updateStudent(studentDto);

        // Respond to the client based on the update result
        if (isUpdated) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"Student updated successfully\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"message\": \"Student not found\"}");
        }
    }

}