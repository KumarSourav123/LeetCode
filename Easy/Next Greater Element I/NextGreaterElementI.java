class Solution {
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {

        int[] arr = new int[nums1.length];

        for(int i = 0 ; i < nums1.length ; i++){
            for(int j = 0 ; j < nums2.length ; j++){
                if(nums1[i] != nums2[j]){
                    continue;
                }else{                   
                    int flag = 0;
                    int z = j;
                    while(z < nums2.length-1){
                        if(nums2[z+1] > nums2[j]){
                            arr[i] = nums2[z+1];
                            flag = 1;
                            break;
                        }
                        z++;
                    }
                    if(flag == 0){
                        arr[i] = -1; 
                    }                                              
                    
                }
            }
        }
        return arr;
    }
}