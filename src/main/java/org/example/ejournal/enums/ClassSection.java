package org.example.ejournal.enums;

public enum ClassSection {
	A, B, C, D, E, F, G, H, I, J;
	
	@Override
	public String toString() {
		return name();  // By default, returns the letter of the section
	}
}