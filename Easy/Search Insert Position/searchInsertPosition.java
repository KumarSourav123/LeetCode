class Solution {
    public int searchInsert(int[] nums, int target) {
        int start = 0;
        int end = nums.length-1;
        int index = 0 ;

        while(start <= end){

            index = (start+end)/2;

            if(nums[index] == target){
                return index;
            }
            else if(target > nums[index]){
                start = index+1;
            }else{
                end = index-1;
            }
        }
        if(start > end){
            return start;
        }else{
            return index;
        }
    }
}
