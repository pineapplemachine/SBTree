/*
 * This code is released as public domain. But that doesn't mean you should 
 * claim credit that isn't yours or sell it when it could otherwise be had for
 * free, because that would be a shitty thing of you to do.
 * It was originally written by Sophie Kirschner. (sophiek@pineapplemachine.com)
 */

package SBTree;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * The SBNode class represents the nodes of an SBTree object. The subtree of
 * which any SBNode is the root can be operated on in the same way as an SBTree.
 */
public class SBNode implements Cloneable, Comparable<SBNode> {
    /** Reference to the node's key. */
    public Comparable key=null;
    /** A list of values assigned to the node. Each value belongs to its key. */
    public LinkedList<Object> values=new LinkedList<>();
    /** Reference to the parent node. */
    public SBNode parent=null;
    /** Reference to the left child node. */
    public SBNode left=null;
    /** Reference to the right child node. */
    public SBNode right=null;
    /** 
     * Initializes a new SBNode. 
     * @param nodekey a key.
     * @param nodevalue a value.
     */
    public SBNode(Comparable nodekey,Object nodevalue){
        key=nodekey; addValue(nodevalue);
    }
    /** 
     * Initializes a new SBNode. 
     * @param nodekey a key.
     * @param nodevalues a list of values.
     */
    public SBNode(Comparable nodekey,LinkedList<Object> nodevalues){
        key=nodekey; values=nodevalues;
    }
    /** @return the leftmost node in the subtree. */
    public SBNode leftMostNode(){
        SBNode node=this;
        while(node.left!=null){node=node.left;}
        return node;
    }
    /** @return the rightmost node in the subtree. */
    public SBNode rightMostNode(){
        SBNode node=this;
        while(node.right!=null){node=node.right;}
        return node;
    }
    /** 
     * Moves this subtree to the leftmost position on the subtree of which the
     * specified node is the root.
     * @param node a node.
     */
    public void moveToLeft(SBNode node){
        SBNode newparent=node.rightMostNode();
        orphan(); newparent.left=this; parent=newparent;
    }
    /** 
     * Moves this subtree to the rightmost position on the subtree of which the
     * specified node is the root.
     * @param node a node.
     */
    public void moveToRight(SBNode node){
        SBNode newparent=node.rightMostNode();
        orphan(); newparent.right=this; parent=newparent;
    }
    /** Removes all references to parent and children nodes. */
    public void orphan(){
        if(parent!=null){
            if(parent.left==this){
                parent.left=null;
            }else if(parent.right==this){
                parent.right=null;
            }
            parent=null;
        }
    }
    /** 
     * Removes the node from the tree of which it's a member.
     * @return the node which has taken the removed node's position in the tree,
     * or null if none exists.
     */
    public SBNode remove(){
        if(left!=null){
            removeSub(left);
            if(right!=null){right.moveToRight(left.rightMostNode());}
            return left;
        }else if(right!=null){
            removeSub(right);
            return right;
        }else{
            orphan(); return null;
        }
    }
    /** 
     * Convenience function cuts down on the amount of code in the public 
     * remove() method.
     * @param child Must be either the node's left or right child.
     */
    private void removeSub(SBNode child){
        if(parent!=null){
            child.parent=parent;
            if(parent.left==this){
                parent.left=child;
            }else if(parent.right==this){
                parent.right=child;
            }
        }
    }
    /** 
     * Removes all instances of specific value associated with the node. If
     * there are no more values associated with the node, the node itself is
     * also removed.
     * @param removevalue A value.
     * @return if the node was removed, the node which has taken its position in
     * the tree, or null if none exists. Otherwise, the node itself.
     */
    public SBNode removeValue(Object removevalue){
        return removeValue(removevalue,true);
    }
    /** 
     * Removes all instances of specific value associated with the node.
     * @param removevalue A value.
     * @param removeempty Whether the node is itself removed if it contains no
     * more values.
     * @return if the node was removed, the node which has taken its position in
     * the tree, or null if none exists. Otherwise, the node itself.
     */
    public SBNode removeValue(Object removevalue,boolean removeempty){
        Iterator it=values.iterator();
        while(it.hasNext()){
            Object currentvalue=it.next();
            if(removevalue==currentvalue){it.remove();}
        }
        return checkEmptyValues(removeempty);
    }
    /** 
     * Removes the oldest value associated with the node. If there are no more
     * values associated with the node, the node itself is also removed.
     * @return if the node was removed, the node which has taken its position in
     * the tree, or null if none exists. Otherwise, the node itself.
     */
    public SBNode removeFirstValue(){
        return removeFirstValue(true);
    }
    /** 
     * Removes the oldest value associated with the node.
     * @param removeempty Whether the node is itself removed if it contains no
     * more values.
     * @return if the node was removed, the node which has taken its position in
     * the tree, or null if none exists. Otherwise, the node itself.
     */
    public SBNode removeFirstValue(boolean removeempty){
        values.removeFirst();
        return checkEmptyValues(removeempty);
    }
    /** 
     * Removes the newest value associated with the node. If there are no more
     * values associated with the node, the node itself is also removed.
     * @return if the node was removed, the node which has taken its position in
     * the tree, or null if none exists. Otherwise, the node itself.
     */
    public SBNode removeLastValue(){
        return removeLastValue(true);
    }
    /** 
     * Removes the newest value associated with the node.
     * @param removeempty Whether the node is itself removed if it contains no
     * more values.
     * @return if the node was removed, the node which has taken its position in
     * the tree, or null if none exists. Otherwise, the node itself.
     */
    public SBNode removeLastValue(boolean removeempty){
        values.removeLast();
        return checkEmptyValues(removeempty);
    }
    /** 
     * Removes the all values associated with the node. The node itself is not
     * removed.
     */
    public void removeAllValues(){
        values.clear();
    }
    /** 
     * Removes the all values associated with the node and all its children. 
     * The nodes themselves are not removed.
     */
    public void removeAllValuesRecursive(){
        values.clear();
        if(left!=null){left.removeAllValuesRecursive();}
        if(right!=null){right.removeAllValuesRecursive();}
    }
    /** 
     * Convenience function related to value removal.
     * @param removeempty Whether the node is itself removed if it contains no
     * more values.
     * @return if the node was removed, the node which has taken its position in
     * the tree, or null if none exists. Otherwise, the node itself.
     */
    private SBNode checkEmptyValues(boolean removeempty){
        if(removeempty && values.isEmpty()){
            return remove();
        }else{
            return this;
        }
    }
    /** @return the oldest value associated with the node, null if none exists. */
    public Object getFirstValue(){
        return values.peekFirst();
    }
    /** @return the newest value associated with the node, null if none exists. */
    public Object getLastValue(){
        return values.peekLast();
    }
    /** @return a LinkedList containing all values corresponding to the key. */
    public LinkedList<Object> getAllValues(){
        return (LinkedList<Object>)values.clone();
    }
    /** 
     * @param value A value.
     * @return true if the node contains the value, false otherwise. 
     */
    public boolean contains(Object value){
        return values.contains(value);
    }
    /** 
     * @param value A value.
     * @return true if the node or any of its children contain the value, false 
     * otherwise. 
     */
    public boolean treeContains(Object value){
        if(contains(value)){
            return true;
        }else{
            boolean leftcontains=(left==null)?false:left.treeContains(value);
            if(leftcontains){return true;}
            boolean rightcontains=(right==null)?false:right.treeContains(value);
            return rightcontains;
        }
    }
    /** 
     * Adds a new value to the node.
     * @param nodevalue A value.
     */
    final public void addValue(Object nodevalue){
        values.addLast(nodevalue);
    }
    /** 
     * @return the number of nodes in the subtree of which this node is the 
     * root. 
     */
    public int size(){
        int sum=1;
        if(left!=null){sum+=left.size();}
        if(right!=null){sum+=right.size();}
        return sum;
    }
    /** @return the number of values associated with the node. */
    public int valuesSize(){
        return values.size();
    }
    /** 
     * @return number of values contained in the subtree of which this node is
     * the root. 
     */
    public int valuesSizeRecursive(){
        int sum=valuesSize();
        if(left!=null){sum+=left.size();}
        if(right!=null){sum+=right.size();}
        return sum;
    }
    /** @return the height of the subtree of which this node is the root. */
    public int height(){
        int heightleft=0,heightright=0;
        if(left!=null){heightleft=left.height();}
        if(right!=null){heightright=right.height();}
        return Math.max(heightleft,heightright)+1;
    }
    /** Clears all references in the subtree to other nodes. */
    public void clearLinks(){
        if(left!=null){left.clearLinks();}
        if(right!=null){right.clearLinks();}
        orphan();
    }
    /** Clears all references in the subtree to values and other nodes. */
    public void clear(){
        values.clear();
        if(left!=null){left.clear();}
        if(right!=null){right.clear();}
        orphan();
    }
    /** 
     * Inserts a new key, value pair into the subtree of which this node is the
     * root.
     * @param key A key.
     * @param value A value.
     * @return the node containing the new key, value pair. 
     */
    public SBNode insert(Comparable key,Object value){
        SBNode node=null,current=this;
        while(true){
            int comparison=key.compareTo(current.key);
            if(comparison<0){
                if(current.left!=null){
                    current=current.left;
                }else{
                    node=new SBNode(key,value);
                    current.left=node; node.parent=current;
                    break;
                }
            }else if(comparison>0){
                if(current.right!=null){
                    current=current.right;
                }else{
                    node=new SBNode(key,value);
                    current.right=node; node.parent=current;
                    break;
                }
            }else if(comparison==0){
                node=current;
                current.addValue(value);
                break;
            }
        }
        return node;
    }
    /** 
     * Searches in the subtree of which this node is the root for a node with
     * a matching key.
     * @param key A key.
     * @return the node corresponding to the specified key, null if none exists. 
     */
    public SBNode findNode(Comparable key){
        SBNode current=this;
        while(current!=null){
            int comparison=key.compareTo(current.key);
            if(comparison<0 && current.left!=null){
                current=current.left;
            }else if(comparison>0 && current.right!=null){
                current=current.right;
            }else if(comparison==0){
                return current;
            }
        }
        return null;
    }
    /** 
     * Searches in the subtree of which this node is the root for the node with 
     * the most closely matching key.
     * @param key A key.
     * @return the node most closely corresponding to the specified key.
     */
    public SBNode findClosestNode(Comparable key){
        SBNode current=this;
        while(current!=null){
            int comparison=key.compareTo(current.key);
            if(comparison==-1 && current.left!=null){
                current=current.left;
            }else if(comparison==1 && current.right!=null){
                current=current.right;
            }else{
                return current;
            }
        }
        return null; // this shouldn't happen
    }
    /** 
     * Balances the subtree of which this node is the root.
     * @return the new root of the subtree. 
     */
    public SBNode optimize(){
        SBNode[] array=nodesInOrder();
        SBNode rootparent=parent; int parentside=0;
        if(parent!=null){parentside=(parent.left==this)?-1:1;}
        clearLinks();
        SBNode newroot=optimizeSub(0,array.length-1,array);
        if(newroot!=null){
            newroot.parent=rootparent;
            if(parentside==-1){
                rootparent.left=newroot;
            }else if(parentside==1){
                rootparent.right=newroot;
            }
        }
        return newroot;
    }
    /** 
     * Recursive function used for balancing a subtree. Don't expect me to
     * articulate how this works, it's complicated.
     * @param start Start index of this subtree.
     * @param end End index of this subtree.
     * @param array An in-order array of the nodes in the greater subtree.
     * @return the node which is the root of the subtree defined by the array
     * bounds [start,end].
     */
    private static SBNode optimizeSub(int start,int end,SBNode[] array){
        if(end>=start){
            int middle=(int)Math.ceil((double)(end-start)/2d)+start;
            int lf=middle-1,rs=middle+1;
            if(lf>=start){
                array[middle].left=optimizeSub(start,lf,array);
            }
            if(array[middle].left!=null){
                array[middle].left.parent=array[middle];
            }
            if(rs<=end){
                array[middle].right=optimizeSub(rs,end,array);
            }
            if(array[middle].right!=null){
                array[middle].right.parent=array[middle];
            }
            return array[middle];
        }
        return null;
    }  
    /** 
     * Returns an array of all values in the subtree of which this node is the
     * root. The nodes are ordered the same as they would be encountered in an
     * in-order traversal of the subtree.
     * @return an array of all values in the subtree.
     */
    public Object[] valuesInOrder(){
        int[] index={0}; // Has to be an array since Java won't pass ints by reference
        Object[] array=new Object[valuesSizeRecursive()];
        valuesInOrderSub(index,array);
        return array;
    }
    /** 
     * Recursive method fills the array with values.
     * @param index Current index in array.
     * @param array The array being filled.
     */
    private void valuesInOrderSub(int[] index,Object[] array){
        if(left!=null){left.valuesInOrderSub(index,array);}
        for(Object value:values){array[index[0]]=value; index[0]++;}
        if(right!=null){right.valuesInOrderSub(index,array);}
    }
    /** 
     * Returns an array of all values in the subtree of which this node is the
     * root. The nodes are ordered the same as they would be encountered in an
     * reverse-order traversal of the subtree.
     * @return an array of all values in the subtree.
     */
    public Object[] valuesReverseOrder(){
        int[] index={0}; // Has to be an array since Java won't pass ints by reference
        Object[] array=new Object[valuesSizeRecursive()];
        valuesReverseOrderSub(index,array);
        return array;
    }
    /** 
     * Recursive method fills the array with values.
     * @param index Current index in array.
     * @param array The array being filled.
     */
    private void valuesReverseOrderSub(int[] index,Object[] array){
        if(right!=null){right.valuesReverseOrderSub(index,array);}
        for(Object value:values){array[index[0]]=value; index[0]++;}
        if(left!=null){left.valuesReverseOrderSub(index,array);}
    }
    /** 
     * Returns an array of all values in the subtree of which this node is the
     * root. The nodes are ordered the same as they would be encountered in an
     * pre-order traversal of the subtree.
     * @return an array of all values in the subtree.
     */
    public Object[] valuesPreOrder(){
        int[] index={0}; // Has to be an array since Java won't pass ints by reference
        Object[] array=new Object[valuesSizeRecursive()];
        valuesPreOrderSub(index,array);
        return array;
    }
    /** 
     * Recursive method fills the array with values.
     * @param index Current index in array.
     * @param array The array being filled.
     */
    private void valuesPreOrderSub(int[] index,Object[] array){
        for(Object value:values){array[index[0]]=value; index[0]++;}
        if(left!=null){left.valuesPreOrderSub(index,array);}
        if(right!=null){right.valuesPreOrderSub(index,array);}
    }
    /** 
     * Returns an array of all values in the subtree of which this node is the
     * root. The nodes are ordered the same as they would be encountered in an
     * post-order traversal of the subtree.
     * @return an array of all values in the subtree.
     */
    public Object[] valuesPostOrder(){
        int[] index={0}; // Has to be an array since Java won't pass ints by reference
        Object[] array=new Object[valuesSizeRecursive()];
        valuesPostOrderSub(index,array);
        return array;
    }
    /** 
     * Recursive method fills the array with values.
     * @param index Current index in array.
     * @param array The array being filled.
     */
    private void valuesPostOrderSub(int[] index,Object[] array){
        if(left!=null){left.valuesPostOrderSub(index,array);}
        if(right!=null){right.valuesPostOrderSub(index,array);}
        for(Object value:values){array[index[0]]=value; index[0]++;}
    }
    /** 
     * Returns an array of all nodes in the subtree of which this node is the
     * root. The nodes are ordered the same as they would be encountered in an
     * in-order traversal of the subtree.
     * @return an array of all values in the subtree.
     */
    public SBNode[] nodesInOrder(){
        int[] index={0}; // Has to be an array since Java won't pass ints by reference
        SBNode[] array=new SBNode[size()];
        nodesInOrderSub(index,array);
        return array;
    }
    /** 
     * Recursive method fills the array with nodes.
     * @param index Current index in array.
     * @param array The array being filled.
     */
    private void nodesInOrderSub(int[] index,SBNode[] array){
        if(left!=null){left.nodesInOrderSub(index,array);}
        array[index[0]]=this; index[0]++;
        if(right!=null){right.nodesInOrderSub(index,array);}
    }
    /** 
     * Returns an array of all nodes in the subtree of which this node is the
     * root. The nodes are ordered the same as they would be encountered in an
     * reverse-order traversal of the subtree.
     * @return an array of all values in the subtree.
     */
    public SBNode[] nodesReverseOrder(){
        int[] index={0}; // Has to be an array since Java won't pass ints by reference
        SBNode[] array=new SBNode[size()];
        nodesReverseOrderSub(index,array);
        return array;
    }
    /** 
     * Recursive method fills the array with nodes.
     * @param index Current index in array.
     * @param array The array being filled.
     */
    private void nodesReverseOrderSub(int[] index,SBNode[] array){
        if(right!=null){right.nodesReverseOrderSub(index,array);}
        array[index[0]]=this; index[0]++;
        if(left!=null){left.nodesReverseOrderSub(index,array);}
    }
    /** 
     * Returns an array of all nodes in the subtree of which this node is the
     * root. The nodes are ordered the same as they would be encountered in an
     * pre-order traversal of the subtree.
     * @return an array of all values in the subtree.
     */
    public SBNode[] nodesPreOrder(){
        int[] index={0}; // Has to be an array since Java won't pass ints by reference
        SBNode[] array=new SBNode[size()];
        nodesPreOrderSub(index,array);
        return array;
    }
    /** 
     * Recursive method fills the array with nodes.
     * @param index Current index in array.
     * @param array The array being filled.
     */
    private void nodesPreOrderSub(int[] index,SBNode[] array){
        array[index[0]]=this; index[0]++;
        if(left!=null){left.nodesPreOrderSub(index,array);}
        if(right!=null){right.nodesPreOrderSub(index,array);}
    }
    /** 
     * Returns an array of all nodes in the subtree of which this node is the
     * root. The nodes are ordered the same as they would be encountered in an
     * post-order traversal of the subtree.
     * @return an array of all values in the subtree.
     */
    public SBNode[] nodesPostOrder(){
        int[] index={0}; // Has to be an array since Java won't pass ints by reference
        SBNode[] array=new SBNode[size()];
        nodesPostOrderSub(index,array);
        return array;
    }
    /** 
     * Recursive method fills the array with nodes.
     * @param index Current index in array.
     * @param array The array being filled.
     */
    private void nodesPostOrderSub(int[] index,SBNode[] array){
        if(left!=null){left.nodesPostOrderSub(index,array);}
        if(right!=null){right.nodesPostOrderSub(index,array);}
        array[index[0]]=this; index[0]++;
    }
    /** @return the node to the immediate right of this one, null if none exists. */
    public SBNode successor(){
        if(right!=null){
            return right.leftMostNode();
        }else{
            SBNode current=this,currentparent=parent;
            while(currentparent!=null && current==currentparent.right){
                current=currentparent;
                currentparent=currentparent.parent;
            }
            return currentparent;
        }
    }
    /** @return the node to the immediate left of this one, null if none exists. */
    public SBNode predecessor(){
        if(left!=null){
            return left.rightMostNode();
        }else{
            SBNode current=this,currentparent=parent;
            while(currentparent!=null && current==currentparent.left){
                current=currentparent;
                currentparent=currentparent.parent;
            }
            return currentparent;
        }
    }
    /** 
     * Splays the subtree of which this node is the root so that the specified
     * node becomes the new root.
     * @param node The node which is to become the new root.
     * @return the new root of the subtree.
     */
    public SBNode splay(SBNode node){
        SBNode[] root={this}; // Array'd so it can be passed by reference
        SBNode rootparent=parent; int parentside=0;
        if(parent!=null){parentside=(parent.left==this)?-1:1;}
        splay(node,root);
        root[0].parent=rootparent;
        if(parentside==-1){
            rootparent.left=root[0];
        }else if(parentside==1){
            rootparent.right=root[0];
        }
        return root[0];
    }
    /** 
     * Recursive method which does the actual splaying.
     * @param node The node which is to become the new root.
     * @param root Reference to the current root, which changes as the subtree
     * is operated upon.
     */
    private void splay(SBNode node,SBNode[] root){
        if(node==null || node==this || node.parent==null){return;}
        if(node.parent==this){
            if(left==node){
                rotRight(root);
            }else{
                rotLeft(root);
            }
        }else{
            SBNode p1=node.parent, p2=p1.parent;
            if(p1.left==node && p2.left==p1){
                p2.rotRight(root); p1.rotRight(root);
            }else if(p1.left==node && p2.right==p1){
                p1.rotRight(root); p2.rotLeft(root);
            }else if(p1.right==node && p2.left==p1){
                p1.rotLeft(root); p2.rotRight(root);
            }else if(p1.right==node && p2.right==p1){
                p2.rotLeft(root); p1.rotLeft(root);
            }
            splay(node,root);
        }
    }
    /** 
     * Rotates the tree left around this node.
     * @param root Reference to the current root, which changes as the subtree
     * is operated upon by the splay() method.
     */
    private void rotLeft(SBNode[] root){
        SBNode node=right;
        right=node.left;
        if(node.left!=null){node.left.parent=this;}
        node.left=this;
        if(parent!=null){
            if(this==parent.left){
                parent.left=node;
            }else{
                parent.right=node;
            }
        }
        node.parent=parent; parent=node;
        if(root[0]==this){root[0]=node;}
    }
    /** 
     * Rotates the tree right around this node.
     * @param root Reference to the current root, which changes as the subtree
     * is operated upon by the splay() method.
     */
    private void rotRight(SBNode[] root){
        SBNode node=left;
        left=node.right;
        if(node.right!=null){node.right.parent=this;}
        node.right=this;
        if(parent!=null){
            if(this==parent.left){
                parent.left=node;
            }else{
                parent.right=node;
            }
        }
        node.parent=parent; parent=node;
        if(root[0]==this){root[0]=node;}
    }
    /** 
     * @return a clone of the subtree of which this node is the root. 
     * @throws java.lang.CloneNotSupportedException 
     */
    @Override
    public SBNode clone() throws CloneNotSupportedException{
        SBNode node=(SBNode)super.clone();
        node.key=key; node.values=(LinkedList<Object>)values.clone();
        if(left!=null){
            node.left=left.clone();
            node.left.parent=node;
        }
        if(right!=null){
            node.right=right.clone();
            node.right.parent=null;
        }
        return node;
    }
    /** @return the comparison of this node's key against the other's. */
    @Override
    public int compareTo(SBNode o) {
        return key.compareTo((Comparable)o);
    }
    /** @return true if the keys of the two nodes are equal, false otherwise. */
    @Override
    public boolean equals(Object o){
        return compareTo((SBNode)o)==0;
    }
    @Override
    /** @return a String representation of the node and its values. */
    public String toString(){
        if(values.isEmpty()){
            return key.toString();
        }else{
            String str=key.toString()+": ";
            for(Object value:values){
                str+=value.toString();
                if(value!=values.peekLast()){str+=", ";}
            }
            return str;
        }
    }
    /** @return a simple sequential list of the subtree's nodes. */
    public String toSequencedString(){
        String str="";
        for(SBNode node=leftMostNode();node!=null && node!=parent;node=node.successor()){
            str+=node.toString();
            if(node!=rightMostNode()){str+="; ";}
        }
        return str;
    }
    /** @return a simple sequential list of the subtree's keys. */
    public String keysToSequencedString(){
        String str="";
        for(SBNode node=leftMostNode();node!=null && node!=parent;node=node.successor()){
            str+=node.key.toString();
            if(node!=rightMostNode()){str+=", ";}
        }
        return str;
    }
    /** @return a simple sequential list of the subtree's values. */
    public String valuesToSequencedString(){
        String str="";
        for(SBNode node=leftMostNode();node!=null && node!=parent;node=node.successor()){
            Iterator it=node.values.iterator();
            while(it.hasNext()){
                Object value=it.next();
                str+=value.toString();
                if(it.hasNext()){str+=", ";}
            }
            if(node!=rightMostNode()){str+="; ";}
        }
        return str;
    }
    /**
     * @return a structured String showing the subtree's nodes and their
     * relationships to one another. 
     */
    public String toBranchedString(){
        return toBranchedString("");
    }
    /**
     * @param prefix A string which precedes all lines of the String.
     * @return a structured String showing the subtree's nodes and their
     * relationships to one another. 
     */
    public String toBranchedString(String prefix){
        String str=prefix+toString();
        if(left!=null){str+="\n"+left.toBranchedString(prefix+"L ");}
        if(right!=null){str+="\n"+right.toBranchedString(prefix+"R ");}
        return str;
    }
}