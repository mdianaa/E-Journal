package org.example.ejournal.enums;

import lombok.Getter;

@Getter
public enum GradeLevel {
	FIRST("1"),
	SECOND("2"),
	THIRD("3"),
	FOURTH("4"),
	FIFTH("5"),
	SIXTH("6"),
	SEVENTH("7"),
	EIGHTH("8"),
	NINTH("9"),
	TENTH("10"),
	ELEVENTH("11"),
	TWELFTH("12");
	
	private final String value;
	
	GradeLevel(String value) {
		this.value = value;
	}
	
	// Custom method to get the enum from the string value
	public static GradeLevel fromValue(String value) {
		for (GradeLevel level : GradeLevel.values()) {
			if (level.value.equals(value)) {
				return level;
			}
		}
		throw new IllegalArgumentException("Invalid grade level: " + value);
	}
	
	@Override
	public String toString() {
		return this.value;
	}
}