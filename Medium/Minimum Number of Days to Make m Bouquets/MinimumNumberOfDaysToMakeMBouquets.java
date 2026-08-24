class Solution {
    public int minDays(int[] bloomDay, int m, int k) {

        if((long) k * m > bloomDay.length){
            return -1;
        }

        int low = Integer.MAX_VALUE;
        int high = Integer.MIN_VALUE;
        int ans = 0;

        for(int bloom : bloomDay){
            low = Math.min(low, bloom);
            high = Math.max(high, bloom);
        }

        while(low <= high){
            int mid = low + (high-low)/2;
            int result = bouquet(bloomDay, mid, k);

            if(result >= m){
                ans = mid;
                high = mid-1;
            }else{
                low = mid+1;
            }
        }
        return ans;
    }

    private int bouquet(int[] arr, int mid, int k){
        int count = 0;
        int day = 0;
        for(int i = 0 ; i < arr.length ; i++){
            if(Math.ceil((double)arr[i]/mid) == 1){
                count++;
            }else{
                count = 0;
            }
            if(count == k){
                day++;
                count = 0;
            }
        }
        return day;
    }
}
