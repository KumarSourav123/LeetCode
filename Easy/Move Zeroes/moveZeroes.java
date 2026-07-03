class Solution {
    public void moveZeroes(int[] nums) {
        int val = 0;
        int count = 0;

        for(int i = 0 ; i<nums.length-1 ;i++){

            if(val == nums[i] && nums[i] == nums[i+1]){
                count++;
            }

            if(val == nums[i] && nums[i] != nums[i+1]){
                if(count>0){
                    int swap = nums[i+1];
                    nums[i+1] = nums[i-count];
                    nums[i-count] = swap;
                }
                int swap = nums[i+1];
                nums[i+1] = nums[i];
                nums[i] = swap;
            }
        }
    }
}
