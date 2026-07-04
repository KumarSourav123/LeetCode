class Solution {
    public int thirdMax(int[] nums) {
        int max = nums[0];
        int secondMax = nums[0];
        int thirdMax = nums[0];
        int count = 0;

        for(int i = 1; i<nums.length ; i++){
            if(nums[i] > max){
                max = nums[i];
            }
        }

        for(int i = 1; i<nums.length ; i++){

            if(secondMax == max){
                secondMax = nums[i];
            }
            if(secondMax != max){
                if(nums[i] >= secondMax && nums[i] != max){
                secondMax = nums[i];
                }
            }           
        }

        for(int i = 1; i<nums.length ; i++){

            if(thirdMax == max || thirdMax == secondMax){
                thirdMax = nums[i];
            }
            if(thirdMax != max && thirdMax != secondMax){
                count=1;
                if(nums[i] >= thirdMax && nums[i] != max && nums[i] != secondMax){
                    thirdMax = nums[i];
                }
            }
        }

        if(count == 1){
            return thirdMax;
        }else{
            return max;
        }
    }
}