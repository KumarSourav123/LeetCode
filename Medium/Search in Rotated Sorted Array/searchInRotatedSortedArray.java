class Solution {
    public int search(int[] nums, int target) {
        int small = nums[0];
        int count = 0;
        int index = 0;
        int low = 0;
        int high = nums.length-1;
        int mid = 0;

        for(int i = 1; i<nums.length ; i++){
            if(nums[i] < small){
                small = nums[i];
                index = i;
                count = 1;
            }
        }
        if(count == 1){
            if(nums[index] == target){
                return index;
            }else if(nums[0] > target){
                low = index+1;
            }else{
                high = index -1;
            }
        }

        while(low <= high){
            mid = (low+high)/2;

            if(nums[mid] == target){
                return mid;
            }else if(nums[mid] > target){
                high = mid - 1;
            }else{
                low = mid + 1;
            }
        }
        return -1;
    }
}