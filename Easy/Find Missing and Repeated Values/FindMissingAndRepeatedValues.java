class Solution {
    public int[] findMissingAndRepeatedValues(int[][] grid) {
        int n = grid.length;
        long repeated = 0;
        long missing = 0;
        int count = 1;
        long s = 1;
        long sn = 1;
        long s2 = 1;
        long s2n = 1;

        for(int i = 0 ; i < n ; i++){
            for(int j = 0 ; j < n ; j++){
                s = s + grid[i][j];
                s2 = s2 + (grid[i][j] * grid[i][j]);
                sn = sn + count;
                s2n = s2n + (count * count);
                count++;
            }
        }

        long val1 = s - sn;
        long val2 = s2 - s2n;
        val2 = val2/val1;
        repeated = (val1 + val2)/2;
        missing = repeated - val1;

        return new int[]{(int)repeated,(int)missing};
        
    }
}