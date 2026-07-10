class Solution {
    public int removeElement(int[] nums, int val) {
        int s = 0;
        int h = nums.length-1;
        while(s <= h){
            if(nums[s] == val){
                int temp = nums[s];
                nums[s] = nums[h];
                nums[h] = temp;
                h--;
            }else{
                s++;
            }
        }
        return s;
    }
}