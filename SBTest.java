/*
 * This code is released as public domain. But that doesn't mean you should 
 * claim credit that isn't yours or sell it when it could otherwise be had for
 * free, because that would be a shitty thing of you to do.
 * It was originally written by Sophie Kirschner. (sophiek@pineapplemachine.com)
 */

package SBTree;

public class SBTest {
    public static void main(String[] args){
        
        SBTree tree=new SBTree(true);
        
        // test insertion
        String[][] s={{"a","1"},{"c","3"},{"d","4"},{"b","2"},{"e","5"},{"c","3.5"}};
        tree.insertMultiple(s);
        System.out.println("Keys, sequentially:\n "+tree.keysToSequencedString());
        System.out.println("Values, sequentially\n "+tree.valuesToSequencedString());
        System.out.println("Key, value pairs, structured\n"+tree.toBranchedString(" "));
        System.out.println("Number of keys (nodes): "+tree.size());
        System.out.println("Number of values: "+tree.valuesSize());
        System.out.println("Height: "+tree.height());
        
        // test removal
        tree.remove("a","1");
        tree.remove("e","5");
        System.out.println("Keys, values after removing keys 'a' and 'e':\n"+tree.toSequencedString());
        
        // test splay toggle
        String[] s2={"f","g","h","i","j","k","l","m"};
        tree.setSplay(false);
        tree.insertMultiple(s2);
        System.out.println("After adding eight keys, no splaying:\n"+tree.toBranchedString(" "));
        System.out.println("Height: "+tree.height());
        
        // test tree optimization
        tree.optimize();
        System.out.println("After optimization:\n"+tree.toBranchedString(" "));
        System.out.println("Keys, sequentially:\n "+tree.keysToSequencedString());
        System.out.println("Height: "+tree.height());
        
        // test array conversion
        SBNode[] nodes=tree.toNodeArray(SBTree.SORTPREORDER);
        String str="";
        for(SBNode node:nodes){str+=node.key.toString()+", ";}
        System.out.println("Keys, pre-order from array:\n "+str);
        
        // in-order iteration example
        String str2="";
        for(SBNode node=tree.leftMostNode();node!=null;node=node.successor()){
            str2+=node.getFirstValue()+", ";
        }
        System.out.println("Oldest values only, retrieved in-order via a for loop:\n "+str2);
        
        // reverse-order iteration example
        String str3="";
        for(SBNode node=tree.rightMostNode();node!=null;node=node.predecessor()){
            str3+=node.getLastValue()+", ";
        }
        System.out.println("Newest values only, retrieved reverse-order via a for loop:\n "+str3);
        
    }
}
