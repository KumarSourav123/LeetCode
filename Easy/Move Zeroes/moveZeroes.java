class Solution {
    public void moveZeroes(int[] nums) {
        int zero = 0;
        int number = 1;

        while(number < nums.length && zero < nums.length){
            if((nums[zero] == 0) && (nums[number] != 0)){
                int temp = nums[zero];
                nums[zero] = nums[number];
                nums[number] = temp;
            }else{
                if(nums[zero] == 0){
                    number++;
                }else {
                    zero++;
                    number++;
                }
            }
        }
        
    }
}
