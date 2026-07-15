class Solution {
    public int maxSubArray(int[] nums) {
        int index = 0;
        int sum = 0;
        int totalSum = nums[0];

        while(index < nums.length){

            sum = sum + nums[index];

            if(sum > totalSum){
                totalSum = sum;
            }

            if(sum < 0 ){
                sum = 0;
            }

            index++;
        }

        return totalSum;
    }
}