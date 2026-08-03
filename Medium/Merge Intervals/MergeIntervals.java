class Solution {
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        
        ArrayList<int[]> list = new ArrayList<>();

        for(int[] interval : intervals){
            if(list.isEmpty() || interval[0] > list.get(list.size() -1)[1]){
                list.add(interval);
            }else{
                list.get(list.size() - 1)[1] = Math.max(interval[1],list.get(list.size() - 1)[1]);
            }
        }
        return list.toArray(new int[list.size()][]);
    }
}