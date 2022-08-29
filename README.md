# EducationPlatform

An educational platform web application created using Spring Framework, Spring Boot, Spring Security, Spring Data and Thymeleaf.  

Supports CRUD operations for users, courses, assignments and submissions. Uses MySQL for data persistence.  

Pages:  
/ -> homepage, provides links for authentication, registration and for navigating to the following pages:  
/users -> a table of all users in the database  
/course/courses -> a table of all courses, allows users to enroll in courses and to edit them  

Other pages:  
/user/courses -> a dashboard containing all the courses the currently logged user is enrolled in.  
/course -> page containing course assignments and user submissions for those assignments, request must contain id attribute  
/course/new -> page for creating new courses
/assignment/new -> page containing the form for creating new assignments, GET request must contain courseId attribute  

Currently allows users to access course materials, submit assignments.

# Authorization
Currently there are the following authorities:
"course-{course.id}-owner" - the user is authorized to create/delete assignments for the course and to the all the submissions, can also accept enrollment requests
"course-{course.id}-enrolled" - the user is enrolled in the course, being able to submit and delete submissions for the assignments.