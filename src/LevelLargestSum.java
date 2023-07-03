import java.util.ArrayDeque;

public class LevelLargestSum {
    /***
     * the class checks for the level with the highest sum of root values
     * @param root a current root that is being checked for value
     * @return the level number of which the root values sum is the highest, and -1 if the tree is empty
     */
    public static int getLevelWithLargestSum(BinNode<Integer> root){
        if (root == null)
            return -1;

        int level=0, maxLevel=0, maxSum=0,sum,i,levelSize;
        ArrayDeque<BinNode> levelData = new ArrayDeque<>();
        levelData.add(root);

        while (!levelData.isEmpty()){
            levelSize = levelData.size();
            sum = 0;

            for (i=0; i<levelSize; i++){
                BinNode<Integer> currRoot = levelData.poll();
                sum += currRoot.getData();

                if (currRoot.getLeft() != null)
                    levelData.add(currRoot.getLeft());
                if (currRoot.getRight() != null)
                    levelData.add(currRoot.getRight());
            }

            if (sum > maxSum){
                maxSum = sum;
                maxLevel = level;
            }

            level++;
        }
        return maxLevel;
    }
}