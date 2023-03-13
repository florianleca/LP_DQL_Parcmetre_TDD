package fr.antoineflorian.parcmetre.immatriculation;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class ImmatriculationService {

	private static String regexImmatriculation = "[A-Z]{2}-[0-9]{3}-[A-Z]{2}";
	
	public boolean verified(String plaque) {
	        return Pattern.compile(regexImmatriculation).matcher(plaque).matches();
	}
}
