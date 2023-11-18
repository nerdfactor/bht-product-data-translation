package de.bhtberlin.paf2023.productdatatranslation;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Basic Application tests.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class AppTests {

	@Autowired
	ApplicationContext applicationContext;

	/**
	 * Check if the Spring ApplicationContext is loaded after starting the Application.
	 */
	@Test
	void springApplicationContextLoads() {
		assertNotNull(applicationContext);
	}
}
