class Solution {
    public boolean searchMatrix(int[][] matrix, int target) {
        int index = Integer.MIN_VALUE;
        int n = matrix.length-1;
        int m = matrix[0].length -1;

        int start1 = 0;
        int end1 = n;

        while(start1 <= end1){
            int mid1 = (start1+end1)/2;
            if(target >= matrix[mid1][0] && target <= matrix[mid1][m]){
                index = mid1;
                break;
            }else if(target < matrix[mid1][0]){
                end1 = mid1-1;
            }else{
                start1 = mid1+1;
            }
        }

        if(index == Integer.MIN_VALUE){
            return false;
        }

        int start2 = 0;
        int end2 = m;

        while(start2 <= end2){
            int mid2 = (start2+end2)/2;
            if(target == matrix[index][mid2]){
                return true;
            }
            else if(target > matrix[index][mid2]){
                start2 = mid2+1;
            }else{
                end2 = mid2-1;
            }
        }

        return false;
    }
}