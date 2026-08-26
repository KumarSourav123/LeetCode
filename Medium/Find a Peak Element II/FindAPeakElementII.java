class Solution {
    public int[] findPeakGrid(int[][] mat) {
        int m = mat.length-1;
        int n = mat[0].length-1;

        int low = 0;
        int high = n;

        while(low <= high){
            int mid = (low+high)/2;

            int max = mat[0][mid];
            int row = 0;
            for(int i = 1 ; i <= m ; i++){
                if(mat[i][mid] > max){
                    max = mat[i][mid];
                    row = i;
                }
            }

            int left = mid-1 >= 0 ? mat[row][mid-1] : -1;
            int right = mid + 1 <= n ? mat[row][mid+1] : -1;

            if((mat[row][mid] > right) && (mat[row][mid] > left)){
                return new int[]{row,mid};
            }else if(max < right){
                low = mid+1;
            }else{
                high = mid-1;
            }
        }
        return new int[]{0,0};
    }
}
