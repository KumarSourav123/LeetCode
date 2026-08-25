class Solution {
    public int splitArray(int[] nums, int k) {
        if(nums.length < k){
            return -1;
        }

        int low = Integer.MIN_VALUE;
        int high = 0;

        for(int num : nums){
            low = Math.max(low, num);
            high = high + num;
        }

        while(low <= high){
            int mid = low + (high-low)/2;

            int answer = numCount(nums, mid);

            if(answer > k){
                low = mid+1;
            }else{
                high = mid-1;
            }
        }

        return low;
    }

    private int numCount(int[] arr, int mid){
        int count = 1;
        int value = 0;

        for(int i = 0 ; i < arr.length ; i++){
            if(value + arr[i] <= mid){
                value += arr[i];
            }else{
                count++;
                value = arr[i];
            }
        }

        return count;
    }
}