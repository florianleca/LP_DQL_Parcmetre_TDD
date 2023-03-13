package fr.antoineflorian.parcmetre.immatriculation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ImmatriculationServiceTest {

	@Autowired
	ImmatriculationService immatriculationService;

	@Test
	void testImmatriculationCorrecte() {
		assertTrue(immatriculationService.verified("AB-123-YZ"));
	}

	@Test
	void testMinuscule() {
		assertFalse(immatriculationService.verified("aB-123-YZ"));
		assertFalse(immatriculationService.verified("AB-123-Yz"));
	}

	@Test
	void testTropCourt() {
		assertFalse(immatriculationService.verified("B-123-YZ"));
		assertFalse(immatriculationService.verified("AB-12-YZ"));
		assertFalse(immatriculationService.verified("AB-123-Y"));
	}

	@Test
	void testTropLong() {
		assertFalse(immatriculationService.verified("ABCD-123-YZ"));
		assertFalse(immatriculationService.verified("ABC-1234-YZ"));
		assertFalse(immatriculationService.verified("ABC-123-XYZ"));
	}

	@Test
	void testMauvaisCaractere1() {
		assertFalse(immatriculationService.verified("0B-123-YZ"));
		assertFalse(immatriculationService.verified("AB-1C3-YZ"));
		assertFalse(immatriculationService.verified("AB-123-Y9"));
	}
}
