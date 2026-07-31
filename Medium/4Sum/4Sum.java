import java.util.*;

class Solution {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        ArrayList<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);

        for(int first = 0; first < nums.length ; first++){
            if(first > 0 && nums[first] == nums[first-1]){
                continue;
            }
            for(int second = first+1 ; second < nums.length ; second++){
                if(second > first+1 && nums[second] == nums[second-1]){
                    continue;
                }

                int i = second+1;
                int j = nums.length-1;

                while(i < j){
                    long sum = (long) nums[first] + nums[second] + nums[i] + nums[j];
                    if(sum < target){
                        i++;
                    }else if(sum > target){
                        j--;
                    }else{
                        ArrayList<Integer> list = new ArrayList<>();
                        list.add(nums[first]);
                        list.add(nums[second]);
                        list.add(nums[i]);
                        list.add(nums[j]);
                        result.add(list);
                        i++;
                        while(i < j && nums[i] == nums[i-1]){
                            i++;
                        }
                        j--;
                        while(j > i && nums[j] == nums[j+1]){
                            j--;
                        }
                    }
                }
            }
        }
        return result;
    }
}
