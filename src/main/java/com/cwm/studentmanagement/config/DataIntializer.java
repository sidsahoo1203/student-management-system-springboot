package com.cwm.studentmanagement.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cwm.studentmanagement.model.Courses;
import com.cwm.studentmanagement.model.Students;
import com.cwm.studentmanagement.model.Users;
import com.cwm.studentmanagement.repository.CourseRepository;
import com.cwm.studentmanagement.repository.EnrollmentRepository;
import com.cwm.studentmanagement.repository.StudentRepository;
import com.cwm.studentmanagement.repository.UsersRepository;

/*
 * Copyright (c) 2026 Mahesh Shet
 * Licensed under the MIT License.
 */

@Configuration
public class DataIntializer {
	
	@Bean
	CommandLineRunner loadSampleData(UsersRepository usersRepository,
			CourseRepository courseRepository,
			StudentRepository studentRepository,
			EnrollmentRepository enrollmentRepository,
			PasswordEncoder passwordEncoder) {
		
		return args -> {
			if(!usersRepository.existsByUsername("Admin")) {
				Users users = new Users();
				users.setUsername("Admin");
				users.setPassword(passwordEncoder.encode("admin@123"));
				users.setActive(true);
				usersRepository.save(users);
			}

			// Reset old records and seed fresh demo data for courses and students.
			enrollmentRepository.deleteAllInBatch();
			studentRepository.deleteAllInBatch();
			courseRepository.deleteAllInBatch();

			courseRepository.saveAll(buildCourses());
			studentRepository.saveAll(buildStudents());
		};
	}

	private List<Courses> buildCourses() {
		return List.of(
				course("Java Programming Fundamentals", "JAVA101", "10 Weeks", "Core Java programming with OOP and collections.", "4500.00"),
				course("Spring Boot Development", "SPRING201", "12 Weeks", "Build REST and MVC applications with Spring Boot.", "6500.00"),
				course("Database Management with SQL", "DBA110", "8 Weeks", "Relational modeling, SQL queries, and optimization.", "4000.00"),
				course("Web Development Basics", "WEB100", "9 Weeks", "HTML, CSS, and JavaScript fundamentals.", "3800.00"),
				course("Data Structures and Algorithms", "DSA210", "12 Weeks", "Problem solving with efficient data structures.", "5200.00"),
				course("Python for Developers", "PY200", "10 Weeks", "Python syntax, libraries, and scripting.", "4300.00"),
				course("Cloud Fundamentals", "CLOUD120", "6 Weeks", "Cloud concepts, deployment basics, and services.", "3600.00"),
				course("DevOps Essentials", "DEVOPS250", "8 Weeks", "CI/CD, containers, and automation workflows.", "5400.00"),
				course("Frontend with React", "REACT220", "10 Weeks", "Component-based frontend development with React.", "5900.00"),
				course("Microservices Architecture", "MS300", "12 Weeks", "Distributed systems, APIs, and service communication.", "7200.00")
		);
	}

	private List<Students> buildStudents() {
		return List.of(
				student("Aarav", "Sharma", "aarav.sharma@example.com", "9876500001", "Bhubaneswar"),
				student("Diya", "Patel", "diya.patel@example.com", "9876500002", "Ahmedabad"),
				student("Vivaan", "Rao", "vivaan.rao@example.com", "9876500003", "Hyderabad"),
				student("Ananya", "Das", "ananya.das@example.com", "9876500004", "Kolkata"),
				student("Ishaan", "Nair", "ishaan.nair@example.com", "9876500005", "Kochi"),
				student("Meera", "Singh", "meera.singh@example.com", "9876500006", "Lucknow"),
				student("Aditya", "Verma", "aditya.verma@example.com", "9876500007", "Pune"),
				student("Kavya", "Reddy", "kavya.reddy@example.com", "9876500008", "Vijayawada"),
				student("Arjun", "Mishra", "arjun.mishra@example.com", "9876500009", "Varanasi"),
				student("Sneha", "Iyer", "sneha.iyer@example.com", "9876500010", "Chennai"),
				student("Rohan", "Kulkarni", "rohan.kulkarni@example.com", "9876500011", "Nagpur"),
				student("Priya", "Menon", "priya.menon@example.com", "9876500012", "Thiruvananthapuram"),
				student("Kabir", "Khan", "kabir.khan@example.com", "9876500013", "Bhopal"),
				student("Nisha", "Gupta", "nisha.gupta@example.com", "9876500014", "Jaipur"),
				student("Siddharth", "Joshi", "siddharth.joshi@example.com", "9876500015", "Nashik"),
				student("Pooja", "Roy", "pooja.roy@example.com", "9876500016", "Durgapur"),
				student("Rahul", "Saxena", "rahul.saxena@example.com", "9876500017", "Noida"),
				student("Neha", "Chawla", "neha.chawla@example.com", "9876500018", "Gurugram"),
				student("Yash", "Tripathi", "yash.tripathi@example.com", "9876500019", "Prayagraj"),
				student("Tanvi", "Bose", "tanvi.bose@example.com", "9876500020", "Siliguri")
		);
	}

	private Courses course(String name, String code, String duration, String description, String fee) {
		Courses courses = new Courses();
		courses.setCourseName(name);
		courses.setCourseCode(code);
		courses.setDuration(duration);
		courses.setDescription(description);
		courses.setFee(new BigDecimal(fee));
		courses.setActive(true);
		return courses;
	}

	private Students student(String firstName, String lastName, String email, String phone, String city) {
		Students students = new Students();
		students.setFirstName(firstName);
		students.setLastName(lastName);
		students.setEmail(email);
		students.setPhoneNumber(phone);
		students.setAddress(city + ", India");
		students.setActive(true);
		return students;
	}

}
