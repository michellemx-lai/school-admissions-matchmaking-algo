//project 4
import java.io.IOException;
import java.util.ArrayList;

public class Student {
	private String name; // name
	private double GPA; // GPA
	private int ES; // extracurricular score
	private ArrayList < Integer > rankings = new ArrayList < Integer > (); // rankings of Schools
	private int School = 0; // index of matched School
	private int regret = 0; // regret
	private int ranking = 0;

	// constructors ------------------------------------------------------------------------
	public Student() {
    }
	
	public Student(String name, double GPA, int ES) { //removed int nSchools
		this.name = name;
		this.GPA = GPA;
		this.ES = ES;
	}

	// getters ------------------------------------------------------------------------
	public String getName() {
		return name;
	}
	public double getGPA() {
		return GPA;
	}
	public int getES() {
		return ES;
	}
	public int getRanking(int i) { //i = School index
		for (int j = 0; j < rankings.size(); j++) {
			if (i == rankings.get(j)) {
				ranking = j + 1;
			}
		}
		return ranking;
	}
	public int getSchool() {
		return School;
	}
	public int getRegret() {
		return regret;
	}
	public ArrayList < Integer > getRankings() { //had this from proj 3
		return rankings;
	}

	// setters ------------------------------------------------------------------------
	public void setName(String name) {
		this.name = name;
	}
	public void setGPA(double GPA) {
		this.GPA = GPA;
	}
	public void setES(int ES) {
		this.ES = ES;
	}
	
	public void setRanking(int i, int r) { // i = index of School, r = rank of School
		this.rankings.set(r - 1, i);
	}

	public void setSchool(int i) {
		this.School = i;
	}
	public void setRegret(int r) { //rank of matched school [5,4,6,2], if matched school is 6, its rank is 3, so the regret is 2
		this.regret = r - 1;
	}
	public void setRankings(ArrayList < Integer > rankings) {
		this.rankings = rankings;
	}

	// find School ranking based on School ID
	public int findRankingByID(int ind) {
		int SchoolRanking = rankings.get(ind);
		return SchoolRanking;
	}

	// get new info from the user
	public void editInfo(ArrayList < School > H, boolean canEditRankings) throws IOException {
		System.out.print("Name: ");
		name = BasicFunctions.cin.readLine(); //get user input for name
		GPA = BasicFunctions.getDouble("GPA: ", 0.00, 4.0); //get user input for GPA
		ES = BasicFunctions.getInteger("Extracurricular score: ", 0, 5); //get user input for ES
		if (canEditRankings == true) {
			editRankings(H);
		}
	}

	// edit rankings
	public void editRankings(ArrayList < School > H) throws IOException { //removed rankingsSet since proj3
		eraseRankings(); //erase all pre-existing student rankings of schools
		
		int nSchools = H.size();
		for (int i = 0; i < nSchools; i++) { //make an arrayList with a size equal to the number of schools
			rankings.add(0); //rankings is the student's ranking of schools
		}

		//loop over each school in the arrayList of schools
		for (int i = 0; i < nSchools; i++) {
			int schoolIndex = i + 1; //the index of the school is one above its index in the schools arrayList 
			
			//index the school object from the list of schools 
			School school = H.get(i);
		
			boolean rankAvailable = false;

			//while the user keeps assigning the same rank to more than 1 School, run the loop
			do {
				//ask user to type in an assigned rank to the corresponding schools
				int assignedRank = BasicFunctions.getInteger("School " + school.getName() + ": ", 1, nSchools);
				
				//note: we index the school in the rankings arrayList: a school's index in the rankings arrayList is always one below its assigned rank
				//if the slot for the assigned rank is taken
				if (rankings.get(assignedRank - 1) != 0){ 
					//then an error message is shown (rank has already been used, it's unavailable)
					System.out.println("ERROR: Rank " + String.valueOf(assignedRank) + " is already used!");
				}
				
				//otherwise the assigned rank is equal to a rank that has not been assigned
				else { 
					//the school index is added to the corresponding index in the ranking array according to the assigned ranking
					setRanking(schoolIndex, assignedRank);
					rankings.set(assignedRank - 1, schoolIndex); 
					
					rankAvailable = true; //and then the rank is available
				}
			} while (rankAvailable == false); //the loop continues until the user enters an available rank
		}
	}
	
	// print Student info and assigned School in tabular format
	public void print(ArrayList < School > H) throws IOException { 
		String assignedSchool;
		
		if (School != 0) { //if the Student has a matched School
			assignedSchool = H.get(School - 1).getName();
		}
		
		else {
			assignedSchool = "-";
		}

		//print name, weight, and assigned school 
		System.out.format("%-27s%8.2f%4s  %-27s", name, GPA, ES, assignedSchool);
		if (rankings.size() != 0) { //if the rankings have been set
			printRankings(H); //print preferred school order
		}
		else {
			System.out.format("%-22s\n", "-");
		}
	} //end print()

	// print the rankings separated by a comma
	public void printRankings(ArrayList < School > H) {   
		int nSchools = H.size();
		
		for (int i = 0; i < nSchools; i++) { //loop over each School
			
		    int schoolIndex = rankings.get(i);
		    
			School school = H.get(schoolIndex - 1);
			String schoolName = school.getName();
            
			System.out.print(""); //what
			System.out.print(schoolName);
			if (i != nSchools - 1) { //if it's not the last ranked school
				System.out.print(", "); //then print a comma after the school
			}
		}
	} //end printRankings()

	// erase rankings 
	public void eraseRankings() {
		rankings.clear();
	}
	// erase assignments 
	public void eraseAssignment() {
		School = 0;
	}

	// reset  
	public void reset() {
		eraseRankings();
		eraseAssignment();
		this.name = null;
		this.GPA = 0.00;
		this.ES = 0;
	} //end reset()
}