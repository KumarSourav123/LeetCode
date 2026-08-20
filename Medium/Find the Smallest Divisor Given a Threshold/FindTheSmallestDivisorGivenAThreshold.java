class Solution {
    public int smallestDivisor(int[] nums, int threshold) {
        int max = nums[0];
        int ans = 0;

        for(int i = 1 ; i < nums.length ; i++){
            if(nums[i] > max){
                max = nums[i];
            }
        }

        int start = 1;
        int end = max;

        while(start <= end){
            int mid = start + (end - start)/2;

            int check = result(nums, mid);

            if(check <= threshold){
                ans = mid;
                end = mid-1;
            }else{
                start = mid+1;
            }
        }

        return ans;
    }

    private int result(int[] arr , int div){
        int ans = 0;

        for(int i = 0 ; i < arr.length ; i++){
            ans += (int)Math.ceil((double)arr[i] / div);
        }

        return ans;
    }
}