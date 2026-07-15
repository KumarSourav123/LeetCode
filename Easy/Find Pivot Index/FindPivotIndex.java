class Solution {
    public int pivotIndex(int[] nums) {
        int[] leftSum = new int[nums.length];
        int[] rightSum = new int[nums.length];
        int lsum = 0;
        int rsum = 0;

        for(int i = 0; i< nums.length ; i++){
            lsum = nums[i] + lsum;
            leftSum[i] = lsum;
        }

        for(int i = nums.length-1 ; i>=0 ; i--){
            rsum = nums[i] + rsum;
            rightSum[i] = rsum;
            
        }

        for(int i = 0; i< nums.length ; i++){
            if(leftSum[i] == rightSum[i]){
                return i;
            }
        }
        return -1;
    }
}