class Solution {
    public int shipWithinDays(int[] weights, int days) {
        int start = 0;
        int end = 0;

        for(int weight : weights){
            start = Math.max(start, weight);
            end += weight;
        }

        while(start <= end){
            int mid = start + (end-start)/2;
            int result = range(weights, mid);

            if(result <= days){
                end = mid-1;
            }else{
                start = mid+1;
            }
        }
        return start;
    }

    private int range(int[] arr, int mid){
        int count = 0;
        int day = 1;
        for(int num : arr){
            if(count + num > mid){
                day++;
                count = num;
            }else{
                count += num;
            }
        }

        return day;
    }
}
