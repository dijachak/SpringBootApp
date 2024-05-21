package com.sms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.sms.entity.Teacher;
import com.sms.service.TeacherService;



@Controller
public class TeacherController {
	
	private Logger logger  = LoggerFactory.getLogger(TeacherController.class);

	private TeacherService teacherService;

	public TeacherController(TeacherService teacherService) {
		super();
		this.teacherService = teacherService;
	}
	
	
	
	@GetMapping("/teachers")
	public String Teachers(Model model) {
		model.addAttribute("teachers", teacherService.getAllTeachers());
		return "teacher";
	}
	
	@GetMapping("/teachers/new")
	public String createTeacherForm(Model model) {
		
		Teacher teacher = new Teacher();
		model.addAttribute("teacher", teacher);
		return "create_teacher";
		
	}
	
	@PostMapping("/teachers")
	public String saveTeacher(@ModelAttribute("teacher") Teacher teacher) {
		try {
	        if (teacher != null && isValidTeacher(teacher)) {
	        	teacherService.saveTeacher(teacher);
	            logger.error("Invalid teacher data. teacher cannot be saved.");
	        }
	    } catch (Exception e) {
	        logger.error("Error occurred while saving teacher: {}", e.getMessage());
	    }
		return "redirect:/teachers";
	}
	
	private boolean isValidTeacher(Teacher teacher) {
	    return teacher.getFirstName() != null && !teacher.getFirstName().isEmpty() &&
	    		teacher.getLastName() != null && !teacher.getLastName().isEmpty() &&
	    		teacher.getEmail() != null && !teacher.getEmail().isEmpty();
	}

	@GetMapping("/teachers/edit/{id}")
	public String editTeacherForm(@PathVariable Long id, Model model) {
		model.addAttribute("teacher", teacherService.getTeacherById(id));
		return "edit_teacher";
	}

	@PostMapping("/teachers/{id}")
	public String updateTeacher(@PathVariable Long id,
			@ModelAttribute("teacher") Teacher teacher,
			Model model) {
		
		// get teacher from database by id
		Teacher existingTeacher = teacherService.getTeacherById(id);
		existingTeacher.setId(id);
		existingTeacher.setFirstName(teacher.getFirstName());
		existingTeacher.setLastName(teacher.getLastName());
		existingTeacher.setEmail(teacher.getEmail());
		
		// save updated teacher object
		teacherService.updateTeacher(existingTeacher);
        logger.info("Teacher updated: {}",teacher.getFirstName());
		return "redirect:/teachers";		
	}
	
	// handler method to handle delete teacher request
	
	@GetMapping("/teachers/{id}")
	public String deleteTeacher(@PathVariable Long id) {
        logger.info("Teacher deleted with id: {}", id);
        teacherService.deleteTeacherById(id);
		return "redirect:/teachers";
	}
	
}
