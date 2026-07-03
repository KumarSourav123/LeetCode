class Solution {
    public int missingNumber(int[] nums) {
        int sum = 0;
        int actualSum = 0;

        for(int i =0 ; i< nums.length ; i++){
            sum = sum + nums[i];
        }

        for(int i =0 ; i<= nums.length ; i++){
            actualSum = actualSum + i;
        }

        int missingNumber = actualSum - sum;
        return missingNumber;
    }
}
