package id.ac.ui.cs.advprog.b10.petdaycare.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthApplicationTests {
	@Test
	void testApplicationStartsSuccessfully() {
		// Arrange

		// Act
		AuthApplication.main(new String[]{});

		// Assert
		assertTrue(true); // If the application starts without any errors, the test passes
	}
}
