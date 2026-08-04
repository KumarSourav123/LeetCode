class Solution {
    public boolean searchMatrix(int[][] matrix, int target) {

        int m = matrix.length-1;
        int n = matrix[0].length-1;
        
        int row = 0;
        int col = n;

        while(row <= m && col >= 0){
            int current = matrix[row][col];
            if(target == current){
                return true;
            }else if(target > current){
                row++;
            }else{
                col--;
            }
        }

        return false;
    }
}