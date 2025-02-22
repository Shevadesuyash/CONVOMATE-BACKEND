package com.net.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@EnableCaching
@SpringBootApplication
public class BackendApplication {
//	public static void main(String[] args) {
//		User user = new User();
//		user.setPassword("testPassword");
//		System.out.println(user.getPassword()); // Should print "testPassword"
//	}

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    // Main driver method
//	public static void main(String[] args) {
//		String url = "jdbc:postgresql://localhost:5432/convomant";
//		String user = "root";
//		String password = "root";
//
//		try (Connection connection = DriverManager.getConnection(url, user, password)) {
//			if (connection != null) {
//				System.out.println("Connected to the database!");
//			} else {
//				System.out.println("Failed to make connection!");
//			}
//		} catch (SQLException e) {
//			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}


//http://localhost:8080/swagger-ui/index.html

//https://github.com/tericcabrel/blog-tutorials/blob/main/springboot-email/src/main/java/com/tericcabrel/mail/controllers/UserController.java