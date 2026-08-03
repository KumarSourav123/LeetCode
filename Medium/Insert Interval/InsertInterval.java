import java.util.*;

class Solution {
    public int[][] insert(int[][] intervals, int[] newInterval) {

        ArrayList<int[]> list = new ArrayList<>();
        int count = 0;

        for(int[] interval : intervals){

            if(count == 0){
                if(newInterval[1] < interval[0]){
                    list.add(newInterval);
                    list.add(interval);
                    count = 1;
                }else if(newInterval[1] >= interval[0] && newInterval[0] <= interval[1]){
                    int a = Math.min(interval[0] , newInterval[0]);
                    int b = Math.max(interval[1] , newInterval[1]);
                    list.add(new int[]{a,b});
                    count = 1;
                }else{
                    list.add(interval);
                }
            }else{
                if(list.isEmpty() || interval[0] > list.get(list.size()-1)[1] ){
                    list.add(interval);
                }else{
                    list.get(list.size()-1)[1] = Math.max(list.get(list.size()-1)[1], interval[1]);
                }
            }
        }

        if(count == 0){
            list.add(newInterval);
        }

        return list.toArray(new int[list.size()][]);
    }
}