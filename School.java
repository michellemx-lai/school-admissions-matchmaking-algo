//project 4

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class School {
	// fields ------------------------------------------------------------------------
	private String name; // name
	private double alpha; // GPA weight
	private ArrayList < Integer > rankings = new ArrayList < Integer > (); // rankings of Students
	private int Student = 0; // index of matched Student 
	private int regret = 0; // regret
	private int ranking = 0; //rank of a given student

	// constructors ------------------------------------------------------------------------
	public School() {}
	public School(String name, double alpha) {
		this.name = name;
		this.alpha = alpha;
	} //removed nStudents since proj3

	// getters ------------------------------------------------------------------------
	public String getName() {
		return name;
	}
	public double getAlpha() {
		return alpha;
	}
	public int getRanking(int i) { //i = Student index
		for (int j = 0; j < rankings.size(); j++) {
			if (i == rankings.get(j)) {
				ranking = j + 1;
			}
		}
		return ranking;
	}
	public int getStudent() {
		return Student;
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
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	public void setRanking(int i, int r) { // i = index of Student, r = rank of Student
		this.rankings.set(r - 1, i);
	}
	public void setStudent(int i) {
		this.Student = i;
	}
	public void setRegret(int r) {
		this.regret = r - 1;
	}
	
	// methods ------------------------------------------------------------------------

	// find student ranking based on School ID
	public int findRankingByID(int ind) {
		int StudentRanking = rankings.get(ind);
		return StudentRanking;
	}

	// get new info from the user
	public void editInfo(ArrayList < Student > S) throws IOException { //removed boolean canEditRankings 
		System.out.print("Name: ");
		name = BasicFunctions.cin.readLine(); //get user input for name
		alpha = BasicFunctions.getDouble("GPA Weight: ", 0.00, 1.00); //get user input for alpha
	}
	
	// calculate rankings based on weight alpha
	public void calcRankings(ArrayList < Student > S) {
		eraseRankings(); //erase all pre-existing rankings
		
		int nStudents = S.size();
		
		if (nStudents == 1) {
			rankings.add(1);
		}
		
		else {
			ArrayList < Double > compositeScores = new ArrayList < Double > ();
	
			for (int i = 0; i < nStudents; i++) { //loop over every student
				Student student = S.get(i);
				Double studentCompositeScore = alpha * student.getGPA() + (1 - alpha) * student.getES(); //use formula to calc composite 
				compositeScores.add(studentCompositeScore); //add the composite score to the array in the same order as the students
			}
			
			ArrayList < Double > sortedCompositeScores = new ArrayList<Double>(compositeScores); //make a copy of the composite scores array 
			Collections.sort(sortedCompositeScores); //sort the array of composite scores in ascending order
			Collections.reverse(sortedCompositeScores); //reverse the array so composite scores are sorted in descending order
			
			//remove all duplicated scores from the sorted composite scores
			ArrayList < Double > uniqueSortedCompositeScores = new ArrayList<Double>(BasicFunctions.removeDuplicates(sortedCompositeScores)); //make a copy of the composite scores array 
						
			for (int i = 0; i < uniqueSortedCompositeScores.size(); i++) { //loop over unique and sorted composite scores
				Double uniqueCompositeScore = uniqueSortedCompositeScores.get(i);
				int nCorrespondingStudents = Collections.frequency(compositeScores, uniqueCompositeScore);
				
				for (int j = 0; j < compositeScores.size(); j++) { //loop over all composite scores
					
					if (nCorrespondingStudents > 0 && String.valueOf(uniqueCompositeScore).equals(String.valueOf(compositeScores.get(j)))) { //compare the values of the composite scores after converting both of them into strings
	
						int correspondingStudentIndex = j + 1;
						
						rankings.add(correspondingStudentIndex);
					
						nCorrespondingStudents --;
						
					}
				}	
			}
		}
	}
	
	// print School info and assigned Student in tabular format
	public void print(ArrayList < Student > S) { 
		String assignedStudent;
		
		if (Student != 0) { //if the Student has a matched School
			assignedStudent = S.get(Student - 1).getName();
		}
		
		else {
			assignedStudent = "-";
		}

		//print name, weight, and assigned Student 
		System.out.format("%-27s  %7.2f  %-27s", name, alpha, assignedStudent);
		if (rankings.size() != 0) {
			printRankings(S); //print preferred School order
		}
		else {
			System.out.format("%-23s\n", "-");
		}
	
	} //end print()
	
	// print the rankings separated by a comma
	public void printRankings(ArrayList < Student > S) {
		
		int nStudents = S.size();
		
		for (int i = 0; i < nStudents; i++) { //loop over each Student
			
			int studentIndex = rankings.get(i);
			
			String studentName = S.get(studentIndex - 1).getName();
			
			System.out.print(studentName);
			if (i != nStudents - 1) {
				System.out.print(", ");
			}
		}
		
	} //end printRankings()
	
	// erase rankings 
	public void eraseRankings() {
		rankings.clear();
	} //end eraseRankings()
	
	// erase assignments 
	public void eraseAssignment() {
		Student = 0;
	} //end eraseAssignment()
	// reset  
	public void reset() {
		eraseRankings();
		eraseAssignment();
		this.name = null;
		this.alpha = 0.00;
	} //end reset()
}