class Solution {
    public int minEatingSpeed(int[] piles, int h) {
        int max = piles[0];
        long totalHours = 0;
        int value = max;

        for(int i = 1 ; i < piles.length ; i++){
            if(piles[i] > max){
                max = piles[i];
            }
        }

        int start = 1;
        int end = max;

        while(start <= end){
            int mid = start + (end - start) / 2;
            totalHours = hours(piles, mid);

            if(totalHours > h){
                start = mid+1;
            }else{
                value = mid;
                end = mid-1;
            }
        }

        return (int)value;
    }

    private long hours(int[] arr , int mid){
        long ans = 0;
        for(int i = 0 ; i < arr.length ; i++){
            ans += (arr[i] + (long) mid - 1) / mid;
        }

        return ans;
    }
}