class Solution {
    public int[] xorQueries(int[] arr, int[][] queries) {
        int n = queries.length;
        int[] result = new int[n];

        for(int i = 0 ; i < n ; i++){
            int ans = 0;
            int start = queries[i][0];
            int end = queries[i][1];

            while(start <= end){
                if(start == end){
                    ans = ans ^ arr[start];
                    break;
                }else{
                    ans = (arr[start] ^ arr[end]) ^ ans;
                    start++;
                    end--;
                }
            }
            result[i] = ans;            
        }

        return result;     
    }

}