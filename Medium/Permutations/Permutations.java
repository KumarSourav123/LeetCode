import java.util.*;

class Solution {
    public List<List<Integer>> permute(int[] nums) {

        ArrayList<List<Integer>> result = new ArrayList<>();

        int check = 1;
        for(int i = 2 ; i <= nums.length ; i++){
            check = check * i;
        }

        while(check > 0){
            result.add(Permutation(nums));
            check--;
        }

        return result;

    }
    private List<Integer> Permutation(int[] arr){

        ArrayList<Integer> list = new ArrayList<>();

        int first = 0;
        int second = 0;
        int start = 0;
        int end = arr.length-1;

        for(int i = 0 ; i < arr.length-1 ; i++){
            if(arr[i] < arr[i+1]){
                first = i;
            }
        }
        for(int i = arr.length -1 ; i >= 0 ; i--){
            if(arr[i] > arr[first]){
                second = i;
                int temp = arr[first];
                arr[first] = arr[second];
                arr[second] = temp;
                break;
            }
        }
        if(first != second){
            start = first+1;
        }else{
            start = first;
        }

        while(start < end){
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
        for(int num : arr){
            list.add(num);
        }

        return list;
    }
}