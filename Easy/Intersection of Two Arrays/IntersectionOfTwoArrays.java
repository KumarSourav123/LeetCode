import java.util.*;

class Solution {
    public int[] intersection(int[] nums1, int[] nums2) {

        HashSet<Integer> set = new HashSet<>();
        
        for(int element : nums1){
            set.add(element);
        }

        HashSet<Integer> intersection = new HashSet<>();
        
        for(int element : nums2){
            if(set.contains(element)){
                intersection.add(element);
            }
        }
        
        int[] result = new int[intersection.size()];

        int i = 0;
        for(int element : intersection){
            result[i] = element;
            i++;
        }

        return result;
    }
}