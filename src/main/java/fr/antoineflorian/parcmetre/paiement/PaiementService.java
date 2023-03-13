package fr.antoineflorian.parcmetre.paiement;

import org.springframework.stereotype.Service;

@Service
public class PaiementService {

	public double verifierMontant(String montantIntroduitString) {
		double montantIntroduit = Double.parseDouble(montantIntroduitString);
		if (montantIntroduit <= 0) {
			throw new IllegalArgumentException();
		}
		return montantIntroduit;
	}

}
