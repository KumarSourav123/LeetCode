class Solution {
    public int[] xorQueries(int[] arr, int[][] queries) {
        int n = queries.length;
        int[] result = new int[n];

        for(int i = 0 ; i < n ; i++){
            result[i] = xor1((queries[i][0]),(queries[i][1]), arr);
        }
        return result;       
    }

    private int xor1(int start , int end, int[] arr){
        int result = 0;
        if(start == end){
            return arr[start];
        }
        while(start < end){
            result = (arr[start] ^ arr[end]) ^ result;
            start++;
            end--;
        }
        if(start == end){
            result = result ^ arr[start];
        }
        return result;
    }

}
