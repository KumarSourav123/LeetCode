class Solution {
    public void rotate(int[] nums, int k) {
        k = k%nums.length;

        for(int i = 0 ; i < (nums.length - k)/2 ; i++){
            int temp = nums[i];
            nums[i] = nums[nums.length - 1 - k - i];
            nums[nums.length - 1 - k - i] = temp;
        }
        int j = 0;
        for(int i = nums.length - k ; i < ((nums.length) + (nums.length-k))/2  ; i++){
            int temp = nums[i];
            nums[i] = nums[nums.length-1-j];
            nums[nums.length-1-j] = temp;
            j++;
        }
        for(int i = 0; i< nums.length/2 ; i++){
            int temp = nums[i];
            nums[i] = nums[nums.length-1-i];
            nums[nums.length-1-i] = temp;
        }
    }
}
