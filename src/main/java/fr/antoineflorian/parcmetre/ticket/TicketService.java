package fr.antoineflorian.parcmetre.ticket;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import fr.bouyguestelecom.joursouvres.JoursOuvresFrance;

@Service
public class TicketService {

//	"1h = 0€50, 2h = 1€ , 3h = 1,50€, 4h = 2 €" 
//	--> 1€ <=> 120min jusqu'à 4h, c'est à dire jusqu'à 2€

//	"au-delà : 3€ par heure soit 0,05€/min"
//	--> donc 4h (240mn) au 1er tarif, et le reste au tarif 1€ <=> 20mn

//	"on ne peut pas payer pour plus de 8h de stationnement"
//	--> c'est à dire 4h au tarif 1 (2€) et 4h au tarif 2 (12€)
//	--> donc maximum 14€ 

	public int calculTempsStationnement(double montantPaye) {
		int tempsEnMinutes = -1;
		if (montantPaye < 0 || montantPaye > 14) {
			throw new IllegalArgumentException();
		} else if (montantPaye <= 2) {
			tempsEnMinutes = (int) (120 * montantPaye);
		} else {
			tempsEnMinutes = 240 + (int) ((montantPaye - 2) * 20);
		}
		return tempsEnMinutes;
	}

//	Du lundi au samedi de 9h à 19h (sauf dimanches et jours fériés et du 1er au 15 août).

	public LocalDateTime heureSortie(LocalDateTime heureEntree, int tempsStationnementMinutes) {
		while (!heureEntreePayante(heureEntree)) {
			heureEntree = tryNextMorning(heureEntree);
		}
		LocalDateTime heureSortie = heureEntree.plusMinutes(tempsStationnementMinutes);
		if (!estEntre9et19(heureSortie)) {					// Si on dépasse 19h,
			heureSortie = heureSortie.plusMinutes(840);		// on ajoute 14h (840mn) à l'heure de sortie
		}
		if (estPendantWeekend(heureSortie)) {				// Si on tombe le weekend,
			heureSortie = heureSortie.plusMinutes(2880);	// on ajoute 48h (2880mn) à l'heure de sortie
		}
		if (estPendantJourFerie(heureSortie)) {				// Si on tombe un jour férié,
			heureSortie = heureSortie.plusMinutes(1440);	// on ajoute 24h (1440mn) à l'heure de sortie
		}
		if (estDebutAout(heureSortie)) {					// Si on tombe début aout,
			heureSortie = heureSortie.plusMinutes(21600);	// on ajoute 15j (21600mn) à l'heure de sortie
		}
		return heureSortie;

	}

	private LocalDateTime tryNextMorning(LocalDateTime heureEntree) {
		LocalDateTime newHeureEntree = LocalDateTime.of(heureEntree.getYear(), heureEntree.getMonthValue(), heureEntree.getDayOfMonth(), 9, 0);
		if (heureEntree.getHour() >= 9) {
			newHeureEntree = newHeureEntree.plusDays(1);
		}
		return newHeureEntree;
	}

	private boolean heureEntreePayante(LocalDateTime heureEntree) {
		boolean payant = true;
		if (!estEntre9et19(heureEntree) || estPendantWeekend(heureEntree) || estPendantJourFerie(heureEntree)
				|| estDebutAout(heureEntree)) {
			payant = false;
		}
		return payant;
	}

	public boolean estEntre9et19(LocalDateTime date) {
		return date.getHour() >= 9 && date.getHour() <= 18;
	}

	public boolean estPendantWeekend(LocalDateTime date) {
		DayOfWeek day = date.getDayOfWeek();
		return day.equals(DayOfWeek.SATURDAY) || day.equals(DayOfWeek.SUNDAY);
	}

	private boolean estPendantJourFerie(LocalDateTime heureEntree) {
		LocalDate jourATester = LocalDate.of(heureEntree.getYear(), heureEntree.getMonth(),
				heureEntree.getDayOfMonth());
		return JoursOuvresFrance.estFerie(jourATester);
	}

	public boolean estDebutAout(LocalDateTime date) {
		return date.getMonthValue() == 8 && date.getDayOfMonth() < 16;
	}

	public String formatageHeure(LocalDateTime heure) {
		String date_format = "dd-MM-yyyy HH:mm";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(date_format);
		return heure.format(formatter);
	}
}
