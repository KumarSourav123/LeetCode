class Solution {
    public int thirdMax(int[] nums) {
        long max = Long.MIN_VALUE;
        long secondMax = Long.MIN_VALUE;
        long thirdMax = Long.MIN_VALUE;
        int count = 0;

        for(int i = 0; i<nums.length ; i++){
            if(nums[i] > max){
                thirdMax = secondMax;
                secondMax = max;
                max = nums[i];
                count++;
            }
            else if((nums[i] > secondMax) && (nums[i] != max)){
                thirdMax = secondMax;
                secondMax = nums[i];
                count++;
            }
            else if((nums[i] > thirdMax) && (nums[i] != secondMax) && (nums[i] != max)){
                thirdMax = nums[i];
                count++;
            }
        }
        if(count >= 3){
            return (int)thirdMax;
        }else{
            return (int)max;
        }
    }
}