class Solution {
    public int removeDuplicates(int[] nums) {
        int s = 0;
        int f = 1;
        for(int i = 0 ; i<nums.length ; i++){
            if(nums[s] != nums[f]){
                s++;
                int temp = nums[s];
                nums[s] = nums[f];
                nums[f] = temp;
            }
            f++;
        }
        return s+1;
    }
}