import java.util.*;

class Solution {
    public int[] findMissingAndRepeatedValues(int[][] grid) {
        HashMap<Integer ,Integer> map = new HashMap<>();
        int m = grid.length;
        int repeated = 0;
        int missing = 0;

        for(int i = 0 ; i < m ; i++){
            for(int j = 0 ; j < m ; j++){
                if(map.containsKey(grid[i][j])){
                    map.put(grid[i][j], map.get(grid[i][j])+1);
                    repeated = grid[i][j];

                }else{
                    map.put(grid[i][j] , 1);
                }
            }
        }

        int ans = 0;
        int count = 1;
        for(int i = 0 ; i < m ; i++){
            for(int j = 0 ; j < m ; j++){
                ans = ans ^ count ^ grid[i][j];
                count++;
            }
        }

        missing = ans ^ repeated;

        return new int[]{repeated,missing};
    }
}