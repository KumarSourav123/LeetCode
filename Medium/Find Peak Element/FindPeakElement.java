class Solution {
    public int findPeakElement(int[] nums) {

        int low = 0;
        int high = nums.length-1;

        if(nums.length == 1){
            return 0;
        }
        if(nums[low] > nums[low+1]){
            return low;
        }
        if(nums[high] > nums[high-1]){
            return high;
        }

        low++;
        high--;

        return binarySearch(nums, low, high);

    }

    private int binarySearch(int[] arr, int start, int end){

        while(start <= end){
            int mid = start + (end-start)/2;

            if((arr[mid-1] < arr[mid]) && (arr[mid] > arr[mid+1])){
                return mid;
            }else if(arr[mid-1] > arr[mid]){
                end = mid-1;
            }else{
                start = mid+1;
            }
            
        }
        return 0;
    }

}
