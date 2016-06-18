/*
 * Name: <your name>
 * EID: <your EID>
 */

import java.util.Vector;
import java.util.LinkedList;
import java.util.Arrays;

/**
 * Your solution goes in this class.
 * 
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program1 extends AbstractProgram1 {
    /**
     * Determines whether a candidate Matching represents a solution to the
     * Stable Matching problem. Study the description of a Matching in the
     * project documentation to help you with this.
     */
    public boolean isStableMatching(Matching given_matching) {
        /* TODO implement this function */
	if(given_matching.getTenantMatching().size()==0) return false;
	int l = given_matching.getLandlordCount();
	int n = given_matching.getTenantCount();

	//Apartments and LandLords correspondence
	int[] apartBelongTo = new int[n];
	for(int i=0;i<l;i++){ 
	    Vector<Integer> curLandlord = given_matching.getLandlordOwners().get(i);
	    for(int j=0;j<curLandlord.size();j++){
		apartBelongTo[curLandlord.get(j)]=i;
	    }
	}

	//Apartments and Tenants correspondence
	int[] apartResidedBy = new int[n];
	for(int i=0;i<n;i++){
	    apartResidedBy[given_matching.getTenantMatching().get(i)] = i;
	}

	//start
	for(int i=0;i<n;i++){
	    Vector<Integer> curTenantPref = given_matching.getTenantPref().get(i);
	    int curApartment = given_matching.getTenantMatching().get(i);
	    int curPrefToCurApart = curTenantPref.get(curApartment);
	    if(curPrefToCurApart==1) continue;
	    Vector<Integer> apartsToCheck = new Vector<>();
	    for(int j=0;j<n;j++){
	        if(curTenantPref.get(j)<curPrefToCurApart){
	        	apartsToCheck.add(j);
	        } 
            }
	    for(int apart: apartsToCheck){
	        int landlordToCheck = apartBelongTo[apart];
	        Vector<Integer> curLandlordPref = given_matching.getLandlordPref().get(landlordToCheck);
	        int curTenant = apartResidedBy[apart];
		int curLordToCurTenant = curLandlordPref.get(curTenant); 
		if(curLordToCurTenant==1) continue;
		if(curLandlordPref.get(i)<curLordToCurTenant) return false;		
	    }
	}
        return true; 
    }

    /**
     * Determines a solution to the Stable Matching problem from the given input
     * set. Study the project description to understand the variables which
     * represent the input to your solution.
     * 
     * @return A stable Matching.
     */
    public Matching stableMatchingGaleShapley(Matching given_matching) {
        /* TODO implement this function */
        return null; /* TODO remove this line */
    }
}
