package fr.antoineflorian.parcmetre.paiement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PaiementServiceTest {

	@Autowired
	private PaiementService paiementService;

	@Test
	void testMontantCorrect() {
		assertEquals(10, paiementService.verifierMontant("10"));
		assertEquals(10.2, paiementService.verifierMontant("10.2"));
	}
	
	@Test
	void testCaractereIncorrect() {
		assertThrows(IllegalArgumentException.class, () -> paiementService.verifierMontant("Salut"));
		assertThrows(IllegalArgumentException.class, () -> paiementService.verifierMontant("10,2"));
	}
	
	@Test
	void testMontantNegatifOuNul() {
		assertThrows(IllegalArgumentException.class, () -> paiementService.verifierMontant("-5"));
		assertThrows(IllegalArgumentException.class, () -> paiementService.verifierMontant("0"));
	}

}
