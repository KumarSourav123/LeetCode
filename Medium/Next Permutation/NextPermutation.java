    class Solution {
        public void nextPermutation(int[] nums) {
            int first = 0;
            int second = 0;
            int start = 0;
            int end = nums.length-1;

            for(int i = 0 ; i < nums.length-1 ; i++){
                if(nums[i] < nums[i+1]){
                    first = i;
                }
            }
            for(int i = nums.length - 1 ; i > first ; i--){
                if(nums[i] > nums[first]){
                    second = i;
                    break;
                }
            }
            if(first != second){
                int change = nums[first];
                nums[first] = nums[second];
                nums[second] = change;
                start = first+1;
            }else{
                start = first;
            }

            while(start < end){
                int temp = nums[start];
                nums[start] = nums[end];
                nums[end] = temp;
                start++;
                end--;
            }
        }
    }