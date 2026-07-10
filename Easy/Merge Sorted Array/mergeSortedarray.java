class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int s = m-1;
        int b = n-1;
        int h = (m+n)-1; 
        
        while(b >= 0 && s >= 0){
            if(nums1[s] >= nums2[b]){
                nums1[h] = nums1[s];
                s--;
                h--;
            }else{
                nums1[h] = nums2[b];
                b--;
                h--;
            }
        }
        while(b >= 0){
            nums1[h] = nums2[b];
                b--;
                h--;
        }
    }
}