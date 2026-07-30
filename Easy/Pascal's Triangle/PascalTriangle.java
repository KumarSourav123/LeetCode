import java.util.*;

class Solution {
    public List<List<Integer>> generate(int numRows) {
        ArrayList<List<Integer>> result = new ArrayList<>();
        for(int i = 1; i <= numRows ; i++){
            result.add(printRow(i));
        }
        return result;
    }

    private List<Integer> printRow(int row){
        ArrayList<Integer> list = new ArrayList<>();
        int ans = 1;
        list.add(ans);

        for(int i = 1; i < row ; i++){
            ans = ans*(row-i);
            ans= ans/(i);
            list.add(ans);
        }

        return list;
    }

}