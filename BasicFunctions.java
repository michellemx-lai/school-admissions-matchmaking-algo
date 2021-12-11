//project 4

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

//project 4
public class BasicFunctions {
	//object to read user input at command line
	public static BufferedReader cin = new BufferedReader(new InputStreamReader(System. in )); 
	
	/*function to take in user input of a valid integer between the lower bound (LB) and upper bound (UB) set by the user. 
	Prints error messages if user input is invalid. Returns an integer.*/
	public static int getInteger(String prompt, int LB, int UB) throws IOException, NumberFormatException {
		boolean valid;
		int integerInput = 0;

		//print an error message and ask user for a valid integer input each time the user provides an invalid input. 
		do {
			valid = true;
			System.out.print(prompt);

			//invalid if the input is not an integer
			try {
				integerInput = Integer.parseInt(cin.readLine());
			}
			catch(NumberFormatException e) {
				valid = false;
			}
			catch(IOException e) {
				valid = false;
			}

			//if UB is less than the maximum storable value of an int, then the actual UB value is displayed.
			if ((!valid || integerInput < LB || integerInput > UB) && UB < Integer.MAX_VALUE) {
				valid = false;
				System.out.println("\nERROR: Input must be an integer in [" + LB + ", " + UB + "]!\n");
			}

			//Otherwise if UB is equal to the maximum storable value of a double, then “infinity” is displayed instead of the actual UB value.
			else if ((!valid || integerInput < LB || integerInput > UB) && UB >= Integer.MAX_VALUE) {
				valid = false;
				System.out.println("\nERROR: Input must be an integer in [" + LB + ", infinity]!\n");
			}
		} while (! valid );
		return integerInput;
	} //end of getInteger()
	
	/*function to take in user input of a valid double between the lower bound (LB) and upper bound (UB) set by the user. 
	Prints error messages if user input is invalid. Returns a double.*/
	public static double getDouble(String prompt, double LB, double UB) throws IOException, NumberFormatException {
		boolean valid;
		Double doubleInput = 0.00;

		//print an error message and ask user for a valid double input each time the user provides an invalid input.
		do {
			valid = true;
			System.out.print(prompt);

			//invalid if the input is not a real number
			try {
				doubleInput = Double.parseDouble(cin.readLine());
			}
			catch(NumberFormatException e) {
				valid = false;
			}
			catch(IOException e) {
				valid = false;
			}

			//if UB is less than the maximum storable value of a double, then the actual UB value is displayed.
			if ((!valid || doubleInput < LB || doubleInput > UB) && UB < Double.MAX_VALUE) {
				valid = false;
				System.out.println("\nERROR: Input must be a real number in [" + LB + "0, " + UB + "0]!\n");
			}

			//Otherwise if UB is equal to the maximum storable value of a double, then “infinity” is displayed instead of the actual UB value.
			else if ((!valid || doubleInput < LB || doubleInput > UB) && UB >= Double.MAX_VALUE) {
				valid = false;
				System.out.println("\nERROR: Input must be a real number in [" + LB + "0, infinity]!\n");
			}
		} while (! valid );

		return doubleInput;
	} //end of getDouble()
	
	public static String getFile(String prompt) {
        boolean valid;
        boolean exists = true;
       	String fileName = "";

    	do {
    		valid = true;
            System.out.print(prompt);
            //invalid if file is not found
    		try {
    			fileName = BasicFunctions.cin.readLine(); //get filename from user
    			File file = new File(fileName);
    			exists = file.exists();
    		}
			catch(NumberFormatException e) {
				valid = false;
			}
    		catch(FileNotFoundException e) {
    			valid = false;
    			System.out.print("\n"
    					+ "ERROR: File not found!\n"
    					+ "\n"
    					+ "");
    		}
			catch(IOException e) {
				valid = false;
			}
			if (fileName.equals("0")) { //quits if the input is 0, print a message to indicate cancellation
				valid = true;
				System.out.println(" \n"
						+ "File loading process canceled.\n"
						+ "");
    		}
			else if (exists == false) {
				valid = false;
    			System.out.print("\n"
    					+ "ERROR: File not found!\n"
    					+ "\n"
    					+ "");
    		}
    	} while (valid == false);
    	
    	return fileName;
	}

	public static ArrayList < Double > removeDuplicates(ArrayList < Double > list) {
		ArrayList < Double > newList = new ArrayList < Double > (); // make a new ArrayList
		for (int i = 0; i < list.size(); i++) { // Loop through the old list
			double element = list.get(i);
			// if this element is not already in the new list, then add it our new list
			if (newList.contains(element) == false) {
				newList.add(element);
			}
		}
		return newList;
	} //end of removeDuplicates()

}