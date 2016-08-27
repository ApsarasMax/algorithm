public class Program3 implements IProgram3 {

    private int numClasses;
    private int maxGrade;
    GradeFunction gf;
    //create a matrix to implement the dynamic programming
    //private int[][] dpMatrix;
    private int[][][][] res;

    public Program3() {
    	 this.numClasses = 0;
         this.maxGrade = 0;
         this.gf = null;
    }

    public void initialize(int n, int g, GradeFunction gf) {
    	 this.numClasses = n;
         this.maxGrade = g;
         this.gf = gf;
    }
    
      public int[] computeHours(int totalHours) {
    	  
    	  //int[] computeHours = new int[numClasses];
        //int[][] res;
    	  if(res==null)
          return dp(numClasses, totalHours)[0];
        return res[totalHours][numClasses][0];
    	  //return computeHours;
    }

    public int[] computeGrades(int totalHours) {

        //int[] computeGrades = new int[numClasses];
        if(res==null)
          return dp(numClasses, totalHours)[1];
        return res[totalHours][numClasses][1];
        //return computeGrades;

    }

    public int[][] dp(int nTotal, int hTotal){
      //hold the results of hours and grades
      res = new int[hTotal+1][nTotal+1][2][nTotal];
      //initializing the matrix
      int[][] dpMatrix = new int[hTotal+1][nTotal+1];
      dpMatrix[0][0]=0;

      for(int h=1;h<=hTotal;h++){
        dpMatrix[h][0]=0;
        for(int n=1;n<=nTotal;n++){
          dpMatrix[0][n]=gf.grade(n-1,0);
          res[0][n][1][n-1]=gf.grade(n-1,0);
          for(int i=0;i<=h;i++){
            //use the formula c[h,n]=max{c[h-i, n-1]+f(i)} for i>=0 and i<=h
            int tmp = dpMatrix[h-i][n-1] + gf.grade(n-1,i);
            if(tmp>dpMatrix[h][n]){
              //store the result for both hours taken and grade obtained
              dpMatrix[h][n]=tmp;

              for(int j=0;j<n-1;j++){
                res[h][n][0][j]=res[h-i][n-1][0][j];
                res[h][n][1][j]=res[h-i][n-1][1][j];
              }
              res[h][n][0][n-1]=i;
              res[h][n][1][n-1]=gf.grade(n-1,i);
            }
          }
        }
      }
      return res[hTotal][nTotal];
    }
}
