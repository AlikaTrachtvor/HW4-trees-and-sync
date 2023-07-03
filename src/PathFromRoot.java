public class PathFromRoot {
    /***
     * the method checks for a possible root path so that the root values form a String identical to the given String
     * @param root a current root that is being checked
     * @param str the given String
     * @return True if such path exists , False otherwise
     */
    public static boolean doesPathExist(BinNode<Character> root, String str) {

        if(root==null || str.length()==0)
            return true;

        if(root.getData() != str.charAt(0))
            return false;

        return doesPathExist(root.getLeft(),str.substring(1))
                || doesPathExist(root.getRight(),str.substring(1));
    }
}