class Solution {
    public int[] productExceptSelf(int[] nums) {
        int[] arr = new int[nums.length];
        int product = 1;
        int count = 0;
        int index = Integer.MIN_VALUE;
        int value = 0;

        for(int i = 0 ; i < nums.length ; i++){
            if(nums[i] != 0){
                product = product * nums[i];
                count = 1;
            }
            if(nums[i] == 0){
                index = i;
                value++;
            }
        }

        for(int i = 0 ; i < nums.length ; i++){
            if(value >= 2){
                arr[i] = 0;
            }
            else if(nums[i] == 0){
                if(count == 1){
                    arr[i] = product;
                }else{
                    arr[i] = 0;
                }
            }else{
                if(index >= 0 && index <= nums.length-1){
                    arr[i] = 0;
                }else{
                    arr[i] = product/nums[i];
                }
            }
        }

        return arr;
    }
}