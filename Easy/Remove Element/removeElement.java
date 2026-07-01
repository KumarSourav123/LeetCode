class Solution {
    public int removeElement(int[] nums, int val) {
        int sameval = 0;
        int[] expectedNums = new int[nums.length];

        for(int i = 0 ; i<nums.length ; i++){
            if(nums[i] != val){
                expectedNums[i-sameval] = nums[i];
            }else{
                sameval++;
            }
        }
        for(int i = 0 ; i<expectedNums.length ; i++){
            nums[i] = expectedNums[i];
        }
        int k = nums.length-sameval;
        return k;
    }
}
