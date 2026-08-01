import java.util.*;

class Solution {
    public List<List<Integer>> subsets(int[] nums) {
        ArrayList<List<Integer>> result = new ArrayList<>();
        ArrayList<Integer> list = new ArrayList<>();
        set(0,list,nums,result);
        return result;
    }
    private void set(int i,ArrayList<Integer> list ,int[] arr,ArrayList<List<Integer>> result){
        if(i == arr.length){
            result.add(new ArrayList<>(list)); 
            return;
        }
        list.add(arr[i]);
        set(i+1,list,arr,result);

        list.remove(list.size() - 1);
        set(i+1,list,arr,result);

    }
}