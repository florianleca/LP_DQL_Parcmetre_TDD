package fr.antoineflorian.parcmetre.paiement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({ "montantIntroduit", "montantPaye", "montantRendu" })
public class PaiementController {

	@Autowired
	private PaiementService paiementService;

	@GetMapping("/paiement")
	public String pagePaiement() {
		return "paiement";
	}

	@PostMapping("/paiement")
	public String gererMontants(@RequestParam String montantIntroduitString, Model model) {
		model.addAttribute("montantIntroduitString", montantIntroduitString);
		try {
			double montantIntroduit = paiementService.verifierMontant(montantIntroduitString);
			double montantPaye = Math.min(montantIntroduit, 14);
			double montantRendu = montantIntroduit - montantPaye;
			model.addAttribute("montantIntroduit", montantIntroduit);
			model.addAttribute("montantPaye", montantPaye);
			model.addAttribute("montantRendu", montantRendu);
			return "redirect:/ticket";
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Montant incorrect, veuillez rééessayer.");
			return "paiement";
		}

	}

}
