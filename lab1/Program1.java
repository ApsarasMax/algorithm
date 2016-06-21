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
	//need to repair
	if(given_matching==null 
		|| given_matching.getTenantMatching()==null 
		|| given_matching.getTenantMatching().size()==0) return false;
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

	boolean[] tenantIsMatched = new boolean[n];
	boolean[][] tenantAskedApart = new boolean[n][n];
	int[] apartResidedBy = new int[n];
	Arrays.fill(apartResidedBy, -1);
	boolean allMatched = false;
	while(!allMatched){
	    //pick current tenant
	    int curTenant=0;
	    for(;curTenant<n;curTenant++){
		if(tenantIsMatched[curTenant]==false){
		    break;
		}
	    }
	    tenantIsMatched[curTenant]=true;

	    //current tenant ask for an apartment
	    Vector<Integer> curTenantPref = given_matching.getTenantPref().get(curTenant); 
	    int curPref=n+1;
	    int curApart=0;
	    for(int i=0;i<n;i++){
		if(curTenantPref.get(i)<curPref && tenantAskedApart[curTenant][i]==false){
		    curApart=i;
		    curPref=curTenantPref.get(i);
		}
	    }
	    tenantAskedApart[curTenant][curApart]=true;

	    //compare curTenant with the resided tenant
	    if(apartResidedBy[curApart]==-1){
		apartResidedBy[curApart]=curTenant;
	    }else{
		int curLandlord = apartBelongTo[curApart];
		Vector<Integer> curLandlordPref = given_matching.getLandlordPref().get(curLandlord);
		int residedTenant = apartResidedBy[curApart];
		if(curLandlordPref.get(curTenant)<curLandlordPref.get(residedTenant)){
		    apartResidedBy[curApart]=curTenant;
		    tenantIsMatched[residedTenant]=false;
		}else{
		    tenantIsMatched[curTenant]=false;
		}
	    }	

	    allMatched=true;
	    for(boolean x: tenantIsMatched){
		if(x==false){
		    allMatched=false;
		    break;
		}
	    }
	}

	//assign
	Vector<Integer> tenant_matching = new Vector<Integer>(0);
	for (int i = 0; i < n; i++) {
            tenant_matching.add(-1);
        }
	for (int i = 0; i < n; i++) {
            tenant_matching.set(apartResidedBy[i], i);
        }
	return new Matching(given_matching, tenant_matching);
	

    }
}
