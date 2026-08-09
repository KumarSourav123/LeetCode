import java.util.*;

class Solution {
    public int reversePairs(int[] nums) {
        int start = 0;
        int end = nums.length-1;
        return mergeSort(nums, start, end);
    }
    private int mergeSort(int[] arr , int start , int end){
        if (start >= end) {
            return 0;
        }
        int count = 0;
        int mid = (start+end)/2;
        count += mergeSort(arr,start,mid);
        count += mergeSort(arr,mid+1,end);
        count += countPairs(arr, start , mid, end);
        merge(arr, start, mid, end);  
        return count;     
    }
    private int countPairs(int[] arr, int start, int mid, int end){
        int count = 0 ;
        int right = mid+1;

        for(int i = start ; i <= mid ; i++){
            while(right <= end && (arr[i] > 2L*arr[right])){
                right++;
            }
            count += (right - (mid+1));
        }
        return count;
    }
    private void merge(int[] arr, int start , int mid, int end){
        ArrayList<Integer> list = new ArrayList<>();

        int i = start;
        int j = mid+1;

        while( i <= mid && j <= end){
            if(arr[i] > arr[j]){
                list.add(arr[j]);
                j++;
            }else{
                list.add(arr[i]);
                i++;
            }
        }
        while(i <= mid){
            list.add(arr[i]);
            i++;
        }
        while(j <= end){
            list.add(arr[j]);
            j++;
        }
        for(int k = 0 ; k < list.size() ; k++){
            arr[start+k] = list.get(k);
        }
    }
}
