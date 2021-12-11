import java.util.ArrayList;

//project 4
public class SMPSolver {
    private ArrayList<Student> S = new ArrayList<Student>(); //suitors
    private ArrayList<School> R = new ArrayList<School>(); //receivers
	private int nSuitors; //I added
	private int nReceivers; //I added
    private double avgSuitorRegret = 0.0; //average suitor regret
    private double avgReceiverRegret =0.0 ; //average receiver regret
    private double avgTotalRegret = 0.0; //average total regret
    private boolean matchesExist = false; //whether or not matches exist
    
	// constructors ------------------------------------------------------------------------
    public SMPSolver() {}
    public SMPSolver(ArrayList<Student>S, ArrayList<School> R) {
    	this.S = S;
    	this.R = R;
    }
    
	// getters ------------------------------------------------------------------------
    public double getAvgSuitorRegret() {
    	return this.avgSuitorRegret;
    }
    public double getAvgReceiverRegret() {
    	return this.avgReceiverRegret;
    }
    public double getAvgTotalRegret() {
    	return this.avgTotalRegret;
    }
    public boolean getMatchesExist() {
    	return this.matchesExist; 
    }
    
	// methods ------------------------------------------------------------------------
    
    //reset all matches with new suitors and receivers
    public void reset(ArrayList<Student>S, ArrayList<School> R) {
    	this.S = S;
    	this.R = R;
		nSuitors = S.size();
		nReceivers = R.size();
		matchesExist = false;
	    avgSuitorRegret = 0.0; //average suitor regret
	    avgReceiverRegret = 0.0; //average receiver regret
	    avgTotalRegret = 0.0; //average total regret

		//clear rankings and assignments suitors
		for (int i = 0; i < nSuitors; i++) {
			Student suitor = S.get(i);
			suitor.eraseAssignment();
		}
		//clear rankings and assignments for receivers
		for (int i = 0; i < nReceivers; i++) {
			School receiver = R.get(i);
			receiver.eraseAssignment();
			receiver.calcRankings(S);
		}
    }
        
    public boolean match() { //Gale-Shapley algorithm to match students are suitors
    	
    	reset(S,R);
    	    	
        //ArrayList<Integer> freeReceivers = new ArrayList<Integer>(S.get(0).getRankings()); //place all the free receivers into an ArrayList
        ArrayList<Integer> freeSuitors = new ArrayList<Integer>(R.get(0).getRankings()); //place all the free suitors into an ArrayList
		int proposingSuitorIndex = 0;
		
    	do {
			proposingSuitorIndex = freeSuitors.get(0); //get the first free suitor from the ArrayList of free suitors
    		
	        ArrayList<Integer> freeReceivers = new ArrayList<Integer>(S.get(proposingSuitorIndex - 1).getRankings()); //place all the free suitors into an ArrayList

	        int j = 0;
	        do {
		        int receiverIndex = freeReceivers.get(j);
        		School receiverObj = R.get(receiverIndex - 1); //get the corresponding receiver object
        		
    			if (receiverObj.getStudent() == 0) { //if the receiver is free, then the pair becomes engaged
    				makeEngagement(proposingSuitorIndex, receiverIndex); 
    				freeSuitors.remove(0); //remove the engaged receiver from the free list of receivers

    			} //end of if-statement
    			else { //otherwise, if the receiver is already engaged, compare the ranks of the proposing suitor and the engaged suitor
    				int engagedSuitorIndex = receiverObj.getStudent();
    				int engagedSuitorRank = receiverObj.getRanking(engagedSuitorIndex);
    				int proposingSuitorRank = receiverObj.getRanking(proposingSuitorIndex);
    				
    				if (proposingSuitorRank < engagedSuitorRank) { //if the receiver prefers the proposing suitor over the engaged suitor
    					receiverObj.eraseAssignment(); //free the receiver from its engagement
    					Student engagedSuitorObj = S.get(engagedSuitorIndex - 1);
    					engagedSuitorObj.eraseAssignment();
    					
    					makeEngagement(proposingSuitorIndex, receiverIndex); //the pair becomes engaged
        				freeSuitors.set(0,engagedSuitorIndex); //replace the proposed receiver with the engaged receiver in the free list of receivers 
    				}
    			} //end of else statement
    			
    			j++;
	        } while (freeSuitors.contains(proposingSuitorIndex) && freeSuitors.size() > 0); //while the proposing suitor is still free
    		//} //end of for-loop
    		
    	} while (freeSuitors.size() > 0);

    	//return value
    	boolean matchingHappened = true;
    	matchesExist = true;
    	return matchingHappened;
    }

    private void makeEngagement(int suitor, int receiver) { //make engagement, parameters are suitor index and receiver index
    	int suitorIndex = suitor; 
    	int receiverIndex = receiver;
    	
    	Student suitorObj = S.get(suitorIndex - 1);
    	suitorObj.setSchool(receiverIndex);
    	
        School receiverObj = R.get(receiverIndex - 1);
        receiverObj.setStudent(suitorIndex);
    }
    
    public boolean matchingCanProceed() { //check that matching rules are satisfied
    	boolean matchingCanProceed = false;
    	
    	nSuitors = S.size();
    	nReceivers = R.size();
    	
    	if (nSuitors == 0) { //check if students are loaded
    		System.out.print("ERROR: No suitors are loaded!");
    	}
    	else if (nReceivers == 0) { //check if schools are loaded
    		System.out.print("ERROR: No receivers are loaded!");
    	}
    	else if (nSuitors != nReceivers) { //check if the number of students equals the number of schools
    		System.out.print("ERROR: The number of suitors and receivers must be equal!\n"
    				+ "\n"
    				+ "");
    	}
		else {
    		matchingCanProceed = true;
		}
		return matchingCanProceed;
    }
    public void calcRegrets() { //calculate regrets
    	
    	//initialize variables
		int totalReceiverRegret = 0;
		int totalSuitorRegret = 0;
		int nSuitors = S.size();
		int nReceivers = R.size();
		
		if (matchesExist == true && nSuitors != 1) {
			
	        //set regrets
	        for (int i = 0; i < nSuitors; i++) { 
		        //set regrets once matching is completed
		    	Student suitor = S.get(i);
		    	
		        int matchedReceiverIndex = suitor.getSchool();
		        
	    		ArrayList<Integer> suitorRankings = new ArrayList<Integer>(suitor.getRankings());
		        for (int j = 0; j < nReceivers; j++) { //loop through the suitor's rankings of receivers
		        	if (matchedReceiverIndex == suitorRankings.get(j)) { //if we find the matched receiver in the suitor's list of rankings
		        		int matchedReceiverRank = j + 1;
		        		suitor.setRegret(matchedReceiverRank); //set the suitor's regrets
		        	}
		        }
	        }
	        
	        for (int i = 0; i < nReceivers; i++) { 
		        //set regrets once matching is completed
		    	School receiver = R.get(i);
		    	
		        int matchedSuitorIndex = receiver.getStudent();
		        ArrayList<Integer> receiverRankings = new ArrayList<Integer>(receiver.getRankings());
		        
		        for (int j = 0; j < nSuitors; j++) { //loop through the corresponding receiver's rankings of suitors
		        	if (matchedSuitorIndex == receiverRankings.get(j)) { //if we find the suitor in the matched receiver's list of rankings
		        		int matchedSuitorRank = j + 1;
		        		receiver.setRegret(matchedSuitorRank);	  
		        	}
		        }
	        }
	        
			//loop through each school
			for (int i = 0; i < nReceivers; i++) { 
				//determine total school regrets
				School receiver = R.get(i);
				totalReceiverRegret += receiver.getRegret(); //add school regret to total school regret
				
				//determine total student regrets
				Student matchedSuitor = S.get(i);
				totalSuitorRegret += matchedSuitor.getRegret(); //add student regret to total student regret
				
			} //end of for loop
			
		    //calculate average regrets
			avgSuitorRegret = Double.valueOf(totalSuitorRegret)/ Double.valueOf(nSuitors);	    
			avgReceiverRegret = Double.valueOf(totalReceiverRegret) / Double.valueOf(nReceivers);
			avgTotalRegret = (avgSuitorRegret + avgReceiverRegret) / 2.00;
        }	
    }
    public boolean isStable() { //check if a matching is stable
    	calcRegrets();
    	
    	boolean matchingIsStable = true;
    	
    	//loop through each school
		for (int i = 0; i < nReceivers; i++) { 

			School receiver = R.get(i);
			int receiverIndex = i + 1;
			int matchedSuitorIndex = receiver.getStudent(); //get the index of the matched student
			int matchedSuitorRank = receiver.getRanking(matchedSuitorIndex); //get the rank of the school's matched student
			
	    	//loop through students that the school prefers more than its actual match to determine whether matching is stable
			for (int j = 0; j < matchedSuitorRank - 1; j++) {
				int preferredSuitorIndex = receiver.getRankings().get(j); //get the index of the preferred student
				Student preferredSuitor = S.get(preferredSuitorIndex - 1); //create the student object
			    int preferredSuitorMatchedReceiverIndex = preferredSuitor.getSchool(); //get the index of the preferred student's matched school
			    int preferredSuitorMatchedReceiverRank = preferredSuitor.getRanking(preferredSuitorMatchedReceiverIndex); //get the rank of the preferred student's matched school
			    
				//loop through schools that the preferred student prefers more than its actual match to determine whether matching is stable
			    for (int k = 0; k < preferredSuitorMatchedReceiverRank - 1; k++) { 
			    	int preferredReceiverIndex = preferredSuitor.getRankings().get(k); //get the index of the preferred school
			    	if (receiverIndex == preferredReceiverIndex) { //if the preferred student of the school also prefers this school more than their match
			    		matchingIsStable = false; //then the match is unstable
			    	} // end of if-statement
			    } //end of for loop
			} //end of for loop
		} //end of for-loop
		return matchingIsStable;
    }
    
    //print methods
    public void print() { //print the matching result and statistics
	    printMatches();
		System.out.print("\n"
				+ "");
	    printStats();
    }
    
    public void printMatches() { //print matches
    	
    	if (matchesExist == true) {
    		    		
	    	//if an arbitrary student in the S arrayList does not have a matched school, then display an error message
			System.out.println("Matches: ");
			System.out.println("--------");
		
	
			for (int i = 0; i < R.size(); i++) {
				School receiver = R.get(i);
				int matchedSuitorIndex = receiver.getStudent(); 
				Student matchedSuitor = S.get(matchedSuitorIndex - 1);
				
				System.out.println(receiver.getName() + ": " + matchedSuitor.getName());
			}
    	}
    }
    
    public void printStats() { //print matching statistic
    	
    	if (matchesExist == true) {
	    	boolean matchingIsStable = isStable();
	    	
		    //print whether the matching is stable or unstable
			if (matchingIsStable == true) {
				System.out.println("Stable matching? Yes");
			}
			else {
				System.out.println("Stable matching? No");
			}
	        
			//print average regrets
			System.out.format("Average student regret: %.2f%n", Math.round(avgSuitorRegret * 100.0)/100.0); //rounded to 2 decimals
			System.out.format("Average school regret: %.2f%n", avgReceiverRegret); //rounded to 2 decimals
			System.out.format("Average total regret: %.2f%n", avgTotalRegret); //rounded to 2 decimals
	        System.out.print("\n");
    	}
    }
}
