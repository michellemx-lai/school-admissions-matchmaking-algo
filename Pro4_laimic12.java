//project 4

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class Pro4_laimic12 {
	public static void main(String[] args) throws IOException, FileNotFoundException, NumberFormatException {

		String menuInput = "";
		//initialize variables
		ArrayList<Student> S = new ArrayList<Student>(); //create an ArrayList to store students
		ArrayList<School> H = new ArrayList<School>(); //create an ArrayList to store schools
    	SMPSolver smpSolver = new SMPSolver(S,H);
		
		do {
			//print an error message and ask user for a valid string input each time the user provides an invalid input. 
            displayMenu();
            
			boolean valid;
			do {
				valid = true;
				
				//invalid if the input is not a string
				try {
					menuInput = BasicFunctions.cin.readLine();
				}
				catch(IOException e) {
					valid = false;
				}
				catch(NumberFormatException e) {
					valid = false;
				}
	
				//error message 
				if (!valid || !((menuInput.equalsIgnoreCase("L") || menuInput.equalsIgnoreCase("E") || menuInput.equalsIgnoreCase("P") || menuInput.equalsIgnoreCase("M") || menuInput.equalsIgnoreCase("D") || menuInput.equalsIgnoreCase("R") || menuInput.equalsIgnoreCase("Q")))) {
					valid = false;
					System.out.print("\n"
							+ "ERROR: Invalid menu choice!\n"
							+ "\n"
							+ "");
				}
			} while (valid == false);
			
			//call functions based on the user's menu input
			if (menuInput.equalsIgnoreCase("L")) {

				int nNewSchools = loadSchools(H); //load new schools
				int nSchools = H.size();
			
		        if (nNewSchools != 0) { //if any new schools were added
		        	S.clear(); //clear all students since their number of rankings are no longer equal to the number of schools
		        }
		        
				int nNewStudents = loadStudents(S,H);
				
				if (nNewStudents != 0) {
					
	    			//recalculate school rankings
					for (int i = 0; i < nSchools; i++) {
		    			School school = H.get(i);
		    			school.calcRankings(S);
					}
				}
				
    			if (smpSolver.getMatchesExist() == true) {
    				smpSolver.reset(S, H);
    			}

			}
	
			else if (menuInput.equalsIgnoreCase("E")) {
				editData(S,H);
				smpSolver.reset(S, H); 
			}
	
			else if (menuInput.equalsIgnoreCase("P")) {
				int nStudents = S.size();
				int nSchools = H.size();
				
				if (nStudents == 0) { //print error message if no students are loaded
					System.out.print("\nERROR: No students are loaded!\n");
				}
				else {
			        System.out.print("STUDENTS:\n"
			        		+ "\n"
			        		+ "");
			        
			        printStudents(S,H);
				}

				if (nSchools == 0) { //print error message if no schools are loaded
					System.out.print("\nERROR: No schools are loaded!\n");
				}
				else {
			        System.out.print("SCHOOLS:\n"
			        		+ "\n"
			        		+ "");
			        printSchools(S,H);
				}
			}
	
			else if (menuInput.equalsIgnoreCase("M")) {
				
				boolean matchingCanProceed = smpSolver.matchingCanProceed();

				if (matchingCanProceed == true) {
					
					long start = System.currentTimeMillis(); //Get current time
			        smpSolver.match();
			    	long elapsedTime = System.currentTimeMillis() - start; // Get elapsed time in ms

					smpSolver.printStats(); //print regrets and stability
					
					//print time elapsed
			        System.out.print(S.size() + " matches made in " + elapsedTime + "ms!\n"
			        		+ "\n"
			        		+ "");
				}
			}
	
			else if (menuInput.equalsIgnoreCase("D")) {
				if (smpSolver.getMatchesExist() == false) { 
					System.out.print("ERROR: No matches exist!");
				}
				else {
	                smpSolver.print();
				}
			}
	
			else if (menuInput.equalsIgnoreCase("R")) {
				smpSolver.reset(S, H);
				S.clear();
				H.clear();
				
				System.out.print("Database cleared!\n"
						+ "\n"
						+ "");
			}
	
			else if (menuInput.equalsIgnoreCase("Q")) {
	            System.out.print(" \n"
	            		+ "Arrivederci!\n"
	            		+ "");
				System.exit(0);
			}
		} while (menuInput.equalsIgnoreCase("Q") == false);
	}
	
	//display the main menu...and get user input for menu option
    public static void displayMenu() throws IOException, NumberFormatException {
		System.out.println("JAVA STABLE MARRIAGE PROBLEM v2\n"
				+ "\n"
				+ "L - Load students and schools from file\n"
				+ "E - Edit students and schools\n"
				+ "P - Print students and schools\n"
				+ "M - Match students and schools using Gale-Shapley algorithm\n"
				+ "D - Display matches and statistics\n"
				+ "R - Reset database\n"
				+ "Q - Quit\n"
				+ "\n"
				+ "Enter choice: ");

    } //end of displayMenu()

    //load school info from a user-provided file. Add new schools to list of existing schools. Return # new schools.
    public static int loadSchools(ArrayList<School> H) throws IOException, FileNotFoundException, NumberFormatException {

    	//initialize variables
    	int nNewSchools = 0;
    	String fileName = BasicFunctions.getFile("Enter school file name (0 to cancel): ");

    	
    	if (fileName.equals("0") == false) {
    		//tokenize comma separated file
	    	String line;
        	BufferedReader fin = new BufferedReader(new FileReader(fileName));
	    	int nSchoolsInFile = 0; //counter variable, total valid AND invalid new students in the file
    		
	        do {
	        	line = fin.readLine();
	        	if (line != null) {   
	        		
	        		String[] splitString = line.split(",");	//split up the line at each comma
	        		String name; //the first element is the name of the school
	        		Double alpha;
	        		
	        		if (splitString.length == 2) { //make sure the school is valid (has name and alpha)
		    			nSchoolsInFile ++; //the number of schools increases by 1
	
	    				name = splitString[0];
	    				alpha = Double.parseDouble(splitString[1]); //the second element is the GPA weight (alpha) 
		        	    
	    				//make sure the school is valid 
	    				if (alpha >= 0.0 && alpha <= 1.0) { //alpha must be between 0 and 1
		    				School school = new School(name, alpha); //create a new school object with the name and alpha
			    			H.add(school); //add this school to the list of schools
			    			nNewSchools ++; //the number of (valid) new schools added increases by 1
	    				}
	        		}
	        	}
	        } while(line != null);
	        
	        fin.close(); //close the file 

	        System.out.print("\n"
	        		+ nNewSchools + " of " + nSchoolsInFile + " schools loaded!\n"
	        				+ "\n"
	        				+ "");
    	}
    	
    	return nNewSchools;
    }
    
    
    //load student info from a user-provided file. Add new students to list of existing students. Return # of new students. 
    public static int loadStudents(ArrayList<Student> S, ArrayList<School> H) throws IOException, FileNotFoundException, NumberFormatException {
    	
    	int nNewStudents = 0; //counter variable, total valid new students 
    	int nSchools = H.size();
    	int nStudentsInFile = 0; //counter variable, total valid AND invalid new students in the file
        
    	//checked if nSchools = nRankings, and that ranks are not reused
        String fileName = BasicFunctions.getFile("Enter student file name (0 to cancel): ");
        
        if (fileName.equals("0") == false) {
	        		
	        if (nSchools == 0) { //if there are no schools added, then just count the number of students in file, no new students would be added
		    	//initialize variables
		    	String line;
	        	BufferedReader fin = new BufferedReader(new FileReader(fileName));
		    	
		        //tokenize comma separated file
		        do {
		        	line = fin.readLine();
	
		        	if (line != null) {      	
		                nStudentsInFile ++; //add 1 to the total number of students in file
		        	}
		        } while(line != null);
		        
		        fin.close(); //close the file 
	        }
	
	        else {
		        if (fileName.equals("0") == false) {
			    	//initialize variables
			    	String line;
		        	BufferedReader fin = new BufferedReader(new FileReader(fileName));
		        	
			        //tokenize comma separated file
			        do {
			        	line = fin.readLine();
		
			        	if (line != null) {      	
			                nStudentsInFile ++; //add 1 to the total number of students in file
			                
			                //initialize variables
			        		Double GPA;
			        		int ES;
			        		String name;
			        		String[] splitString = line.split(",");	//tokenize comma separated items, split up at comma
			        		
			        		name = splitString[0]; //the first element is the name of the school
			        		GPA = Double.parseDouble(splitString[1]); //the second element is the GPA weight (alpha) 
			        		ES = Integer.parseInt(splitString[2]); //the second element is the GPA weight (alpha) 
			        	    int nRankings = splitString.length - 3; //the number of elements is equal to the number of elements in splitString minus 3 (slots taken up by name, GPA, ES)
		        	        int schoolIndex = 0;
		        	       
		        	    	ArrayList <Integer> schoolRankings = new ArrayList <Integer> ();		        	    	
		        	 
			        	    for (int i = 0; i < nRankings; i++) { //add all the rank
			        	    	schoolIndex = Integer.parseInt(splitString[3 + i]);
			        	    	schoolRankings.add(schoolIndex);
			        	    }
			        	    
			        	    boolean studentIsValid = true; 
			        	    
			        	    for (int i = 0; i < nRankings; i++) { 
			        	    	schoolIndex = Integer.parseInt(splitString[3 + i]);
			        	    	
			        	    	if (schoolIndex < 1 || schoolIndex > nSchools) { //school is invalid if it doesn't have an existing corresponding school index
			        	    		studentIsValid = false;
			        	    	}
			        	    	else if (Collections.frequency(schoolRankings, schoolIndex) > 1) { //school is invalid if it is is ranked more than once
		        	    			studentIsValid = false;
			        	    	}
			        	    }
			        	   
			        	    if (GPA < 0.0 || GPA > 4.0 || ES < 0 || ES > 5 || nRankings != nSchools) { //student is invalid if either GPA or extracurricular score it not between 0.0 and 4.0, and 0 and 5, respectively
			        	    	studentIsValid = false;
			        	    }
		        	    	
		        	    	if (studentIsValid == true) { //only add student to list if student is valid
	
	        	        	    Student student = new Student(name, GPA, ES); //create a new school object with the name and alpha
	        	        	    
        	    	    		S.add(student); //add this school to the list of schools
			        	    	nNewStudents ++;

			    	    		student.setRankings(schoolRankings); //assign the school rankings to the student
   
		        	    	} // end of if-statement
		        	    	
			        	} // end of if-statement
			        	
			        } while(line != null);
			        
			        fin.close(); //close the file 
		        }
	        }
	        
	        System.out.print("\n"
	        		+ nNewStudents + " of " + nStudentsInFile + " students loaded!\n"
	        				+ "\n"
	        				+ "");
        }
        
    	return nNewStudents; //return the number of new schools
    }
    
    //display sub-menu to edit students and schools
    public static void editData(ArrayList<Student> S, ArrayList<School> H) throws IOException, NumberFormatException {
		int nStudents = S.size();
		int nSchools = H.size();
		
    	boolean valid;
		String menuInput = "";

		//print an error message and ask user for a valid string input each time the user provides an invalid input. 
		do {
			valid = true;
	    	System.out.print("\n"
	    			+ "Edit data\n"
	    			+ "---------\n"
	    			+ "S - Edit students\n"
	    			+ "H - Edit high schools\n"
	    			+ "Q - Quit\n"
	    			+ "\n"
	    			+ "Enter choice: ");

			//invalid if the input is not a string
			try {
				menuInput = BasicFunctions.cin.readLine();
			}
			catch(IOException e) {
				valid = false;
			}
			catch(NumberFormatException e) {
				valid = false;
			}

			//error message 
			if (!valid || !(menuInput.equalsIgnoreCase("S") || menuInput.equalsIgnoreCase("H") || menuInput.equalsIgnoreCase("Q"))) {
				valid = false;
				System.out.print("\n"
						+ "ERROR: Invalid menu choice!\n"
						+ "\n"
						+ "");
			}
		} while (! valid );
		
		//call functions based on the user's menu input
		if (menuInput.equalsIgnoreCase("S")) {
			//if there are no students loaded, print an error message and display sub-menu
			if (nStudents == 0) {
				System.out.print("\nERROR: No Students are loaded!\n");
				editData(S, H); //bring user back to sub-menu; display sub-menu to edit data
			}
			else {
				editStudents(S, H);
			}
		}

		else if (menuInput.equalsIgnoreCase("H")) {
			//if there are no schools loaded, print an error message and display sub-menu
			if (nSchools == 0) {
				System.out.print("\nERROR: No schools are loaded!\n");
				editData(S, H); //bring user back to sub-menu; display sub-menu to edit data
			}
			else {
				editSchools(S, H);
			}
		}

		else if (menuInput.equalsIgnoreCase("Q")) {
			System.out.print("\n");
			displayMenu();
		}
    }
    
    //sub-area to edit students. Update the edited student's regret if necessary. Any existing school rankings & regrets are recalculated after editing a student.
    public static void editStudents(ArrayList<Student> S, ArrayList<School> H) throws IOException {
    	int nStudents = S.size();
    	int nSchools = H.size();
		int studentIndex = 1; //initialize student index
		
		do {
			printStudents(S, H); //print current Student info
			
			//get user input for the index of the Student they want to edit
			studentIndex = BasicFunctions.getInteger("Enter student (0 to quit): \n", 0, nStudents); //assign student index to user input for the student to be edited
	        
			//if input for student index is 0, then bring user back to sub-menu; display sub-menu to edit data
			if (studentIndex == 0){
				editData(S, H); 
			}
			
			//otherwise, edit students (editing rankings is optional)
			else {			
				Student student = S.get(studentIndex - 1);
				boolean valid = true;

					
				student.editInfo(H, false); //allow user to just edit general student info but not ranking, for now
					
				
				//ask user if they want to edit rankings (yes or no)
				String userInput = "";	//initialize user input 
				
				//print an error message and ask user for a valid string input each time the user provides an invalid input
				do {
					System.out.print("Edit rankings (y/n): ");
					//invalid if the input is not a string
					try {
						userInput = BasicFunctions.cin.readLine();
						valid = true;
					}
					catch(IOException e) {
						valid = false;
					}
					catch(NumberFormatException e) {
						valid = false;
					}
					if (!valid || !(userInput.equalsIgnoreCase("Y") || userInput.equalsIgnoreCase("N"))) {
						System.out.print("ERROR: Choice must be 'y' or 'n'!");
						valid = false;
					}
				} while ( valid == false );
				
				//error message 
				if (userInput.equalsIgnoreCase("Y")) { 
					student.editRankings(H); //edit the student's rankings


			        //erase then recalculate every school's ranking of students
					for (int i = 0; i < nSchools; i++) { //loop over each School
						School school = H.get(i);
						school.eraseRankings(); //erase the school's rankings of students
					}
					
					//recalculate regrets for both schools and students, and clear rankings of schools
					for (int i = 0; i < nSchools; i++) {
						
						//recalculate regrets for students
						int matchedStudentIndex = H.get(i).getStudent();
						int matchedStudentRank = H.get(i).getRanking(matchedStudentIndex);
						H.get(i).setRegret(matchedStudentRank);
						
						//recalculate regrets for schools
						int matchedSchoolIndex = S.get(i).getSchool();
						int matchedSchoolRank = S.get(i).getRanking(matchedSchoolIndex); 
						S.get(i).setRegret(matchedSchoolRank);
					}
				}	
			}
			System.out.println("\n");
		} while ( studentIndex != 0 );
    }
    
    
    //sub-area to edit schools. Update the edited school's existing rankings and regrets.
    public static void editSchools(ArrayList<Student> S, ArrayList<School> H) throws IOException{
		int nSchools = H.size();
		int schoolIndex = 1; //initialize school index
		
		do {	
			printSchools(S, H); //print current School info
			
			schoolIndex = BasicFunctions.getInteger("Enter School (0 to quit): \n", 0, nSchools); //get user input for which student is to be edited
			
			//if there are no schools loaded, 
			if (nSchools == 0) {
				System.out.print("\nERROR: No schools are loaded!\n"); //then print an error message
				editData(S, H); //and then bring user back to sub-menu; display sub-menu
			}
			
			//if input is 0, quit and go to sub-menu
			else if (schoolIndex == 0){ //or if the user chooses 0 to quit
				editData(S, H); //then bring user back to sub-menu; display sub-menu
			}
			
			//otherwise, edit school
			else {
				School school = H.get(schoolIndex - 1);
				school.eraseRankings(); //erase the pre-existing rankings of this school
				school.editInfo(S); //edit the info of this school
				school.calcRankings(S); //recalculate the rankings for all schools
				
				//recalculate regrets for the edited school
				int matchedStudentIndex = school.getStudent();
				int matchedStudentRank = school.getRanking(matchedStudentIndex);
				school.setRegret(matchedStudentRank);
			} 
		} while (schoolIndex != 0 );
    }
    
    //print students to screen (include matched school if one exists)
    public static void printStudents(ArrayList<Student> S, ArrayList<School> H) throws IOException{
    	
    	int nStudents = S.size();
    	
		System.out.format("%-3s %-27s%8s%4s  %-27s%-22s\n", " #", "Name", "GPA", "ES", "Assigned school", "Preferred school order");
		
		for (int i = 0; i < 94; i++) {
			System.out.print("-");
		}
        
		System.out.print("\n");
		
		for (int i = 0; i < nStudents; i++) {
			Student student = S.get(i); //create a student object 
			int studentIndex = i + 1; 
			System.out.print(" " + studentIndex + ". "); //print the student index 
			student.print(H); 

			if (i != nStudents - 1) { //print a line after the row unless it's the last row
				System.out.print("\n");
			}
		}

		System.out.print("\n");
		
		for (int i = 0; i < 94; i++) {
			System.out.print("-");
		}  
		
		System.out.println("\n");
    }
    
    //print schools to screen (include matched student if one exists)
    public static void printSchools(ArrayList<Student> S, ArrayList<School> H) {

    	int nSchools = H.size();
    	
		System.out.format("%-3s %-27s  %7s  %-27s%-23s\n", " #", "Name", "Weight", "Assigned student", "Preferred student order");
		
		for (int i = 0; i < 94; i++) {
			System.out.print("-");
		}
        
		System.out.print("\n");
		
		for (int i = 0; i < nSchools; i++) {
			School school = H.get(i); //create a school object
			int schoolIndex = i + 1;
			System.out.print(" " + schoolIndex + ". ");
			school.print(S);
			
			if (i != nSchools - 1) { //print a line after the row unless it's the last row
				System.out.print("\n");
			}
		}
		
		System.out.print("\n");
		
		for (int i = 0; i < 94; i++) {
			System.out.print("-");
		}
		
		System.out.println("\n");
    }
  
}
