public class MyGradeFunction implements GradeFunction{

    private int numClasses;
    private int maxGrade;

    public MyGradeFunction(int n, int g){
	    this.numClasses = n;
	    this.maxGrade = g;
    }

    /*
     * This grade function returns values from a
     * predefined matrix
     */
    public int grade(int classID, int hours){

	if(numClasses==0) return 0;
    	return (classID*hours>(maxGrade/numClasses))?maxGrade/numClasses:classID*hours;
    }

}
