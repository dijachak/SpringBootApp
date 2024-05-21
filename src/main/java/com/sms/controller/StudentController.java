package com.sms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.sms.entity.Student;
import com.sms.service.StudentService;

//ce contrôleur gère les opérations CRUD (Create, Read, Update, Delete) pour les étudiants.
@Controller
public class StudentController {
	
	private Logger logger  = LoggerFactory.getLogger(StudentController.class);

	private StudentService studentService;

	public StudentController(StudentService studentService) {
		super();
		this.studentService = studentService;
	}
	
	@GetMapping("/")
	public String listStudents(Model model) {
		model.addAttribute("students", studentService.getAllStudents());
		return "students.html";
	}
	
	@GetMapping("/students")
	public String Students(Model model) {
		model.addAttribute("students", studentService.getAllStudents());
		return "students";
	}
	
	@GetMapping("/students/new")
	public String createStudentForm(Model model) {
		
		// create student object to hold student form data
		Student student = new Student();
		model.addAttribute("student", student);
		return "create_student";
		
	}
	
	@PostMapping("/students")
	public String saveStudent(@ModelAttribute("student") Student student) {
		try {
	        if (student != null && isValidStudent(student)) {
	            studentService.saveStudent(student);
	        } else {
	            logger.error("Invalid student data. Student cannot be saved.");
	        }
	    } catch (Exception e) {
	        logger.error("Error occurred while saving student: {}", e.getMessage());
	    }
		return "redirect:/students";
	}
	
	private boolean isValidStudent(Student student) {
	    return student.getFirstName() != null && !student.getFirstName().isEmpty() &&
	            student.getLastName() != null && !student.getLastName().isEmpty() &&
	            student.getEmail() != null && !student.getEmail().isEmpty();
	}

	@GetMapping("/students/edit/{id}")
	public String editStudentForm(@PathVariable Long id, Model model) {
		model.addAttribute("student", studentService.getStudentById(id));
		return "edit_student";
	}

	@PostMapping("/students/{id}")
	public String updateStudent(@PathVariable Long id,
			@ModelAttribute("student") Student student,
			Model model) {
		
		// get student from database by id
		Student existingStudent = studentService.getStudentById(id);
		existingStudent.setId(id);
		existingStudent.setFirstName(student.getFirstName());
		existingStudent.setLastName(student.getLastName());
		existingStudent.setEmail(student.getEmail());
		
		// save updated student object
		studentService.updateStudent(existingStudent);
        logger.info("Student updated: {}",student.getFirstName());
		return "redirect:/students";		
	}
	
	// handler method to handle delete student request
	
	@GetMapping("/students/{id}")
	public String deleteStudent(@PathVariable Long id) {
        logger.info("Student deleted with id: {}", id);
		studentService.deleteStudentById(id);
		return "redirect:/students";
	}
	
}
