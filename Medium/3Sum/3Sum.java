import java.util.*;

class Solution {
    public List<List<Integer>> threeSum(int[] nums) {

        ArrayList<List<Integer>> result = new ArrayList<>();

        Arrays.sort(nums);

        int i = 0;
        while(i < nums.length){
            int j = i+1;
            int k = nums.length-1;
            while(j < k){
                ArrayList<Integer> list = new ArrayList<>();
                int sum = nums[i] + nums[j] + nums[k];
                if(sum == 0){
                    list.add(nums[i]);
                    list.add(nums[j]);
                    list.add(nums[k]);
                    result.add(list);
                    j++;
                    while(j < k && nums[j] == nums[j-1]){
                        j++;
                    }
                    k--;
                    while(k > j && nums[k] == nums[k+1]){
                        k--;
                    }
                }else if(sum < 0){
                    j++;
                }else{
                    k--;
                }
            }
            i++;
            while(i < nums.length && nums[i] == nums[i-1]){
                i++;
            }
        }

        return result;
    }

}