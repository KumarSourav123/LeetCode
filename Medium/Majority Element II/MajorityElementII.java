import java.util.*;

class Solution {
    public List<Integer> majorityElement(int[] nums) {

        ArrayList<Integer> list = new ArrayList<>();

        int count1 = 0;
        int count2 = 0;
        int el1 = Integer.MIN_VALUE;
        int el2 = Integer.MIN_VALUE;

        for(int i = 0 ; i< nums.length ; i++){
            if(count1 == 0 && (nums[i] != el2)){
                count1 = 1;
                el1 = nums[i];
            }else if(count2 == 0 && (nums[i] != el1)){
                count2 = 1;
                el2 = nums[i];
            }

            else if(el1 == nums[i]){
                count1++;
            }
            else if(el2 == nums[i]){
                count2++;
            }else{
                count1--;
                count2--;
            }
        }
        count1 = 0;
        count2 = 0;

        for(int i = 0 ; i < nums.length ; i++){
            if(nums[i] == el1){
                count1++;
            }
            if(nums[i] == el2){
                count2++;
            }
        }

        int limit = nums.length/3;

        if(count1 > limit){
            list.add(el1);
        }
        if(count2 > limit){
            list.add(el2);
        }

        return list;
    }
}