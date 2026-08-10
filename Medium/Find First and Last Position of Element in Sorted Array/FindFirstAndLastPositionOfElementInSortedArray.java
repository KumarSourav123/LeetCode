class Solution {
    public int[] searchRange(int[] nums, int target) {

        if(nums.length == 0){
            return new int[] {-1,-1};
        }

        int a = firstIndex(nums,target);
        int b = secondIndex(nums,target);
        
        return new int[] {a,b};
    }
    private int firstIndex(int[] arr, int target){
        int low = 0;
        int high = arr.length-1;
        int ans = 0;

        while(low <= high){
            int mid = low + (high - low) / 2;

            if(arr[mid] >= target){
                ans = mid;
                high = mid-1;
            }else{
                low = mid+1;
            }
        }
        if(target == arr[ans]){
            return ans;
        }else{
            return -1;
        }
    }
    private int secondIndex(int[] arr, int target){
        int low = 0;
        int high = arr.length-1;
        int ans = 0;

        while(low <= high){
            int mid = low + (high - low) / 2;

            if(arr[mid] <= target){
                ans = mid;
                low = mid+1;               
            }else{
                high = mid-1;
            }
        }
        if(target == arr[ans]){
            return ans;
        }else{
            return -1;
        }
    }
}
