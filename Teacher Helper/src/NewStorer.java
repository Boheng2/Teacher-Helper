
public class NewStorer {// Class used for storing

	// Initialize variables
	private static String currentClass = "";
	private static String currentStudent = "";// Shows the current student and
												// classes that the user is at

	private static boolean onceClass = false;
	private static boolean onceStudent = false;// These control element that
												// will only show up if a class
												// button or a student button is
												// clicked
	private static boolean wrong = false;// For error checking

	public static void onceClass() {// Set method for onceClass

		onceClass = true;

	}// end method

	public static boolean checkClass() {// get method for onceClass

		return onceClass;

	}// end method

	public static void onceStudent() {// Set method for onceStudent

		onceStudent = true;

	}// end method

	public static boolean checkStudent() {// get method for onceStudent

		return onceStudent;

	}// end method

	public static void setCurrentClass(String n) {// Set method for currentClass

		currentClass = n;

	}// end method

	public static String getCurrentClass() {// Get method for currentClass

		return currentClass;

	}// end method

	public static void setCurrentStudent(String n) {// Set method for currentStudent


		currentStudent = n;

	}// end method

	public static String getCurrentStudent() {// Get method for currentClass

		return currentStudent;

	}// end method

	public static void wrong() {// Didn't pass error check

		wrong = true;

	}// end method

	public static void notWrong() {// Passed error check

		wrong = false;

	}// end method

	public static boolean getWrong() {// Looks at whether error check is passed or not

		return wrong;

	}// end method

}// end class
