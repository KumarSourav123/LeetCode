import java.util.*;

class Solution {
    public int longestConsecutive(int[] nums) {
        HashSet<Integer> set = new HashSet<>();
        int n = nums.length;
        int maxLength = 1;

        if(n == 0){
            return 0;
        }

        for(int num : nums){
            set.add(num);
        }

        for(int num :set){
            if(set.contains(num -1)){
                continue;
            }else{
                int current = num;
                int count = 1;
                while(set.contains(current+1)){
                    count++;
                    current++;
                }
                maxLength = Math.max(count,maxLength);
            }
        }
        return maxLength;
        
    }
}