package fr.antoineflorian.parcmetre.ticket;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({ "plaque", "montantIntroduit", "montantPaye", "montantRendu" })
public class TicketController {

	@Autowired
	private TicketService ticketService;

	@GetMapping("/ticket")
	public String pageTicket(Model model) {
		double montantPaye = (double) model.getAttribute("montantPaye");
		int tempsStationnementMinutes = ticketService.calculTempsStationnement(montantPaye);
		LocalDateTime heureEntree = LocalDateTime.now();
		LocalDateTime heureSortie = ticketService.heureSortie(heureEntree, tempsStationnementMinutes);
		model.addAttribute("tempsStationnementMinutes", tempsStationnementMinutes);
		model.addAttribute("heureEntree", ticketService.formatageHeure(heureEntree));
		model.addAttribute("heureSortie", ticketService.formatageHeure(heureSortie));
		return "ticket";
	}
}
