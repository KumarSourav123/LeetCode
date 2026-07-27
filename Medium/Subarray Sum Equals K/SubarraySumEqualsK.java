import java.util.*;

class Solution {
    public int subarraySum(int[] nums, int k) {
        HashMap<Integer , Integer> map = new HashMap<>();

        int sum = 0;
        int count = 0;
        map.put(sum,1);

        for(int i = 0 ; i< nums.length ; i++){
            sum = sum + nums[i];
            int rem = sum - k;
            count = count + map.getOrDefault(rem,0);
            map.put(sum, map.getOrDefault(sum, 0) + 1);  
        }
        return count;
    }
}