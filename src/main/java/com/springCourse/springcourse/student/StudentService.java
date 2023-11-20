package com.springCourse.springcourse.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        System.out.println(studentRepository.getClass());
        return studentRepository.findAll();
//        new Student(1l, "Eyad", "Eyad@gmail.com", LocalDate.of(2002, Month.JULY, 4), 21)
    }

    public void addStudent(Student student) {
        // Check if email already exists
        Optional<Student> studentOptional = studentRepository
                .findStudentByEmail(student.getEmail());

        if (studentOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }

        studentRepository.save(student);
        System.out.println(student);
    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("student with id = " + studentId + " doesn't exists.");
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent (Long studentId, String newStudentName, String newStudentEmail) {
        Student student = studentRepository
                .findById(studentId)
                .orElseThrow(() -> new IllegalStateException("student with id = " + studentId + " doesn't exists"));

        // Check for new name
        if (newStudentName != null && // if there is a name sent in request
                newStudentName.length() > 0 && // if name isn't an empty field
                !student.getName().equals(newStudentName) // if new name doesn't match the old one
        ) {
            student.setName(newStudentName);
        }

        // Check for new email
        if (newStudentEmail != null && // if there is an email sent in request
                newStudentEmail.length() > 0 && // if email isn't an empty field
                !student.getEmail().equals(newStudentEmail) // if new email doesn't match the old one
        ) {
            // Check if email already exists
            Optional<Student> studentOptional = studentRepository
                    .findStudentByEmail(newStudentEmail);

            if (studentOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }

            student.setEmail(newStudentEmail);
        }
    }
}
