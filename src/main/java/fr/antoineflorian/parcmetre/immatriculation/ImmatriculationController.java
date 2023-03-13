package fr.antoineflorian.parcmetre.immatriculation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("plaque")
public class ImmatriculationController {

	@Autowired
	private ImmatriculationService immatriculationService;

	@GetMapping("/")
	public String redirectionAccueil() {
		return "redirect:/parcmetre";
	}

	@GetMapping("/parcmetre")
	public String pageAccueil() {
		return "accueil";
	}

	@PostMapping("/parcmetre")
	public String recupererPlaque(@RequestParam String plaquePart1, @RequestParam String plaquePart2,
			@RequestParam String plaquePart3, Model model) {
		model.addAttribute("plaquePart1", plaquePart1);
		model.addAttribute("plaquePart2", plaquePart2);
		model.addAttribute("plaquePart3", plaquePart3);
		String plaque = plaquePart1 + "-" + plaquePart2 + "-" + plaquePart3;
		if (immatriculationService.verified(plaque)) {
			model.addAttribute("plaque", plaque);
			return "redirect:/paiement";
		}
		model.addAttribute("errorMessage", "Plaque d'immatriculation incorrecte, veuillez rééessayer.");
		return "accueil";
	}

}
