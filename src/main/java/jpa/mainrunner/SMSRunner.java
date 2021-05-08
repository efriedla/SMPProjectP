/*
 * Filename: SMSRunner.java
* Author: Stefanski
* 02/25/2020 
 */
package jpa.mainrunner;

import static java.lang.System.out;

import java.util.List;
import java.util.Scanner;

import jpa.entitymodels.Course;
import jpa.entitymodels.Student;
import jpa.entitymodels.StudentCourses;
import jpa.service.CourseService;
import jpa.service.StudentCourseService;
import jpa.service.StudentService;

/**1
 * 
 * @author Liz
 *
 */
public class SMSRunner {

	private Scanner sin;
	private StringBuilder sb;

	private CourseService courseService;
	private StudentService studentService;
	private Student currentStudent;

	public SMSRunner() {
		sin = new Scanner(System.in);
		sb = new StringBuilder();
		courseService = new CourseService();
		studentService = new StudentService();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SMSRunner sms = new SMSRunner();
		sms.run();
	}

	private void run() {
		// Login or quit
		switch (menu1()) {
		case 1:
			if (studentLogin()) {
				registerMenu();
			}
			break;
		case 2:
			out.println("Goodbye!");
			break;

		default:

		}
	}

	private int menu1() {
		sb.append("\n1.Student Login\n2. Quit Application\nPlease Enter Selection: ");
		out.print(sb.toString());
		sb.delete(0, sb.length());

		return sin.nextInt();
	}

	private boolean studentLogin() {
		boolean retValue = false;
		out.print("Enter your email address: ");
		String email = sin.next();
		out.print("Enter your password: ");
		String password = sin.next();

		List<Student> students = studentService.getStudentByEmail(email);
		if (students != null) {
			currentStudent = students.get(0);
		}

		if (currentStudent != null && currentStudent.getStudentPassword().equals(password)) {
			List<Course> courses = studentService.getStudentCourses(email);
			out.println("MyClasses");
			// creating c course and sc courseService
			Course c = new Course();
			CourseService cs = new CourseService();
			out.printf("%-5s %-35s %-25s\n", "ID", "Name", "Instructor");
			for (StudentCourses course : courses) {
				c.setCId(course.getCourseID());
				Course c1 = cs.getCourseById(c.getCId());
				out.printf("%-5s %-35s %-25s\n", c1.getCId(), c1.getCName(), c1.getCInstructorName());
			}
			retValue = true;
		} else {
			out.println("User Validation failed. GoodBye!");
		}
		return retValue;
	}

	private void registerMenu() {
		sb.append("\n1.Register a class\n2. Logout\nPlease Enter Selection: ");
		out.print(sb.toString());
		sb.delete(0, sb.length());

		switch (sin.nextInt()) {
		case 1:
			List<Course> allCourses = courseService.getAllCourses();
			//List<Course> studentCourses = studentService.getStudentCourses(currentStudent.getStudentEmail());
			//allCourses.removeAll(studentCourses);
			out.printf("%5s%15S%15s\n", "ID", "Course", "Instructor");
			for (Course course : allCourses) {
				out.printf("%-5s %-35s %-25s\n", course.getCId(), course.getCName(), course.getCInstructorName());
			}
			out.println();
			out.print("Enter Course Number: ");
			int number = sin.nextInt();
			Course newCourse = courseService.GetCourseById(number);

			if (newCourse != null) {
				studentService.registerStudentToCourse(currentStudent.getStudentEmail(), newCourse);
				Student temp = studentService.getStudentByEmail(currentStudent.getStudentEmail());
				
				StudentCourseService scService = new StudentCourseService();
				List<StudentCourses> sCourses = scService.getAllStudentCourses(temp.getStudentEmail());
				

				//print output
				Course c = new Course();
				CourseService cs = new CourseService();
				System.out.printf("%-5s %-35s %-25s\n", "ID", "Course Name", "Instructor Name");
				System.out.println("MyClasses");
				for (StudentCourses course : sCourses) {
					c.setCId(course.getCourseID());
					Course c1 = cs.getCourseById(c.getCId());

					System.out.printf("%-5s %-35s %-25s\n", c1.getCId(), c1.getCName(), c1.getCInstructorName());
				}
			}
			break;
		case 2:
		default:
			out.println("Goodbye!");
		}
	}
}
