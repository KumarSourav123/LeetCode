class Solution {
    public int singleNonDuplicate(int[] nums) {
        int low = 0;
        int high = nums.length-1;

        if(low == high){
            return nums[low];
        }
        if(nums[low] != nums[low+1]){
            return nums[low];
        }
        if(nums[high] != nums[high-1]){
            return nums[high];
        }

        low++;
        high--;

        while(low <= high){
            int mid = (low + high)/2;
            if((nums[mid] != nums[mid-1]) && (nums[mid] != nums[mid+1])){
                return nums[mid];
            }else if(mid%2 == 0){
                if(nums[mid] == nums[mid+1]){
                    low = mid+1;
                }else{
                    high = mid-1;
                }
            }else{
                if(nums[mid] == nums[mid+1]){
                    high = mid-1;
                }else{
                    low = mid+1;
                }
            }
        }
        return 0;
    }
}