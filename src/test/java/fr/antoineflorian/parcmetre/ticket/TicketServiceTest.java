package fr.antoineflorian.parcmetre.ticket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TicketServiceTest {

	@Autowired
	private TicketService ticketService;
	private LocalDateTime heureDebut = LocalDateTime.of(2023, 3, 28, 14, 29);

	@Test
	void testCalculTempsStationnement() {
		assertEquals(0, ticketService.calculTempsStationnement(0));
		assertEquals(120, ticketService.calculTempsStationnement(1));
		assertEquals(4 * 60, ticketService.calculTempsStationnement(2));
		assertEquals(4 * 60 + 20, ticketService.calculTempsStationnement(3));
		assertEquals(8 * 60, ticketService.calculTempsStationnement(14));
	}

	@Test
	void testCalculTempsStationnementException() {
		assertThrows(IllegalArgumentException.class, () -> ticketService.calculTempsStationnement(-100));
		assertThrows(IllegalArgumentException.class, () -> ticketService.calculTempsStationnement(14.1));

	}

	@Test
	void testCalculHeureSortie() {
		assertEquals(LocalDateTime.of(2023, 3, 28, 14, 54), ticketService.heureSortie(heureDebut, 25));
		assertEquals(LocalDateTime.of(2023, 3, 28, 15, 29), ticketService.heureSortie(heureDebut, 60));
	}
	
//	Du lundi au samedi de 9h à 19h (sauf dimanches et jours fériés et du 1er au 15 août).

	@Test
	void testFinJournee() {
		// Mercredi 15/03/23 à 18h30 + 1h = Jeudi 16/03/23 à 9h30
		LocalDateTime heureDebutFinJournee1 = LocalDateTime.of(2023, 3, 15, 18, 30);
		assertEquals(LocalDateTime.of(2023, 3, 16, 9, 30), ticketService.heureSortie(heureDebutFinJournee1, 60));
		// Mercredi 15/03/23 à 18h30 + 8h = Jeudi 16/03/23 à 16h30
		LocalDateTime heureDebutFinJournee2 = LocalDateTime.of(2023, 3, 15, 18, 30);
		assertEquals(LocalDateTime.of(2023, 3, 16, 16, 30), ticketService.heureSortie(heureDebutFinJournee2, 480));
		// Jeudi 16/03/23 à 03h00 + 1h = Jeudi 16/03/23 à 10h00
		LocalDateTime heureDebutFinJournee3 = LocalDateTime.of(2023, 3, 16, 3, 0);
		assertEquals(LocalDateTime.of(2023, 3, 16, 10, 0), ticketService.heureSortie(heureDebutFinJournee3, 60));
							
	}
	
	@Test
	void testFinSemaine() {
		// Vendredi 17/03/23 à 18h30 + 1h = Lundi 20/03/23 à 9h30
		LocalDateTime heureDebutFinSemaine1 = LocalDateTime.of(2023, 3, 17, 18, 30);
		assertEquals(LocalDateTime.of(2023, 3, 20, 9, 30), ticketService.heureSortie(heureDebutFinSemaine1, 60));
		// samedi 18 mars 2023 à 14h00 + 1h = Lundi 20/03/23 à 10h
		LocalDateTime heureDebutFinSemaine2 = LocalDateTime.of(2023, 3, 18, 14, 00);
		assertEquals(LocalDateTime.of(2023, 3, 20, 10, 0), ticketService.heureSortie(heureDebutFinSemaine2, 60));			
	}
	
	@Test
	void testJourFerie() {
		// Lundi 10/04/23 à 15h00 + 1h = Mardi 11/04/23 à 10h00 (lundi de pâques)
		LocalDateTime heureDebutJourFerie1 = LocalDateTime.of(2023, 4, 10, 15, 0);
		assertEquals(LocalDateTime.of(2023, 4, 11, 10, 0), ticketService.heureSortie(heureDebutJourFerie1, 60));
		// Vendredi 07/04/23 à 18h00 + 3h = Mardi 11/04/23 à 11h00 (weekend de 3j)
		LocalDateTime heureDebutJourFerie2 = LocalDateTime.of(2023, 4, 7, 18, 0);
		assertEquals(LocalDateTime.of(2023, 4, 11, 11, 0), ticketService.heureSortie(heureDebutJourFerie2, 180));
	}

	@Test
	void testDebutAout() {
		// 31/07/23 à 18h00 + 2h = 16/08/23 à 10h00
		LocalDateTime heureDebutAout1 = LocalDateTime.of(2023, 7, 31, 18, 0);
		assertEquals(LocalDateTime.of(2023, 8, 16, 10, 0), ticketService.heureSortie(heureDebutAout1, 120));
		// 03/08/23 à 15h00 + 4h = 16/08/23 à 13h00
		LocalDateTime heureDebutAout2 = LocalDateTime.of(2023, 8, 3, 15, 0);
		assertEquals(LocalDateTime.of(2023, 8, 16, 13, 0), ticketService.heureSortie(heureDebutAout2, 240));
	}
}
