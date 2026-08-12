class Solution {
    public int findMin(int[] nums) {

        int index = nums[0];
        int low = 0;
        int high = nums.length-1;

        while(low <= high){
            int mid = (low + high)/2;

            if(nums[mid] < index){
                index = nums[mid];
            }
            if(nums[mid] < nums[high]){
                high = mid-1;
            }else{
                low = mid+1;
            }
        }

        return index;
    }
}
