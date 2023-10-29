package method;

/** This enumeration contains 3 objects, each corresponding to a possible status of a square in the n queens problem.
 * 
 * @author Daniel Gardner (2023)
 *
 */
public enum Square {
	
	EMPTY(" "), QUEEN("Q"), SCOPED("X");
	
	private String str;
	
	private Square(String str) {
		this.str = str;
	}
	
	/** Returns a String representing the contents of the square.
	 */
	public String toString() {
		return str;
	}

}
