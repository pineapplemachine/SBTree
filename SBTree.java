/*
 * This code is released as public domain. But that doesn't mean you should 
 * claim credit that isn't yours or sell it when it could otherwise be had for
 * free, because that would be a shitty thing of you to do.
 * It was originally written by Sophie Kirschner. (sophiek@pineapplemachine.com)
 */

package SBTree;

import java.util.LinkedList;

/**
 * The SBTree class represents a splayed binary search tree. It holds any number
 * of key, value pairs which can be inserted, searched, and removed in a
 * worst-case time of amortized O(log n) and an average time of O(log n). The
 * tree class contains the reference to a root node and a boolean which defines 
 * whether the tree will be splayed after insertion and search operations. (This 
 * means that the most recent node will become the new root of the tree. This 
 * helps to keep the tree balanced, and reduces search time for commonly 
 * looked-for nodes.)
 * 
 * Its nodes are represented by the SBNode class. Each node corresponds to a
 * single key, and contains a LinkedList of all values assigned to that key.
 * Since I'm far too lazy to implement Iterator support, you'll want to iterate
 * through the tree's nodes like so:
 * for(SBNode node=tree.leftMostNode();node!=null;node=node.successor()){}
 * 
 * And through the tree's values thusly:
 * for(SBNode node=tree.leftMostNode();node!=null;node=node.successor()){
 *  for(Object value:node.values){}
 * }
 * 
 * I apologize for not using a template, just going with the Object class for
 * stuff was way easier.
 * 
 * @author Sophie Kirschner
 */
public class SBTree implements Cloneable {
    /** Constant passed optionally to the toArray() and toNodeArray() methods. */
    final static public int SORTINORDER=0;
    /** Constant passed optionally to the toArray() and toNodeArray() methods. */
    final static public int SORTREVERSEORDER=1;
    /** Constant passed optionally to the toArray() and toNodeArray() methods. */
    final static public int SORTPREORDER=2;
    /** Constant passed optionally to the toArray() and toNodeArray() methods. */
    final static public int SORTPOSTORDER=3;
    /** Reference to the root node. */
    public SBNode root=null;
    /** 
     * Boolean determines whether the tree is splayed after insertion
     * operations. 
     */
    public boolean splayedinsertion=true;
    /** Boolean determines whether the tree is splayed after search operations. */
    public boolean splayedsearch=true;
    /** Initializes a new SBTree. */
    public SBTree(){}
    /** 
     * Initializes a new SBTree. 
     * @param splayed Set whether the tree is splayed after insertion and search
     * operations.
     */
    public SBTree(boolean splayed){
        setSplay(splayed);
    }
    /** 
     * Initializes a new SBTree. 
     * @param insertion Set whether the tree is splayed after insertion 
     * operations.
     * @param search Set whether the tree is splayed after search operations.
     */
    public SBTree(boolean insertion,boolean search){
        setSplay(insertion,search);
    }
    /** 
     * Initializes a new SBTree. 
     * @param treeroot Set the root of the tree.
     */
    public SBTree(SBNode treeroot){
        root=treeroot;
    }
    /** 
     * Initializes a new SBTree. 
     * @param treeroot Set the root of the tree.
     * @param splayed Set whether the tree is splayed after insertion and search operations.
     */
    public SBTree(SBNode treeroot,boolean splayed){
        root=treeroot;
        setSplay(splayed);
    }
    /** 
     * Initializes a new SBTree. 
     * @param treeroot Set the root of the tree.
     * @param insertion Set whether the tree is splayed after insertion 
     * operations.
     * @param search Set whether the tree is splayed after search operations.
     */
    public SBTree(SBNode treeroot,boolean insertion,boolean search){
        root=treeroot;
        setSplay(insertion,search);
    }
    /** @return true if the tree is empty (has no root node), false otherwise. */
    public boolean isEmpty(){
        return root!=null;
    }
    /** @return the number of keys (nodes) in the tree. */
    public int size(){
        return (root==null)?0:root.size();
    }
    /** @return the number of values in the tree. */
    public int valuesSize(){
        return (root==null)?0:root.valuesSizeRecursive();
    }
    /** @return the tree's height. */
    public int height(){
        return (root==null)?0:root.height();
    }
    /** Clears all references in the tree to nodes and values. */
    public void clear(){
        if(root!=null){root.clear(); root=null;}
    }
    /** 
     * Balances the tree. This takes time, so it isn't something to be done
     * frequently. Also, if the tree is set to splay after insertions and
     * searches then the tree will stay more or less balanced all on its own.
     * @return the new root of the tree. 
     */
    public SBNode optimize(){
        if(root!=null){root=root.optimize();}
        return root;
    }
    /** @return whether the tree is splayed after insertion operations. */
    public boolean getSplayInsertion(){
        return splayedinsertion;
    }
    /** @param set Set whether the tree is splayed after insertion operations. */
    public void setSplayInsertion(boolean set){
        splayedinsertion=set;
    }
    /** @return whether the tree is splayed after search operations. */
    public boolean getSplaySearch(){
        return splayedsearch;
    }
    /** @param set Set whether the tree is splayed after search operations. */
    public void setSplaySearch(boolean set){
        splayedsearch=set;
    }
    /** 
     * @param set Set whether the tree is splayed after insertion and search 
     * operations. 
     */
    final public void setSplay(boolean set){
        splayedinsertion=set;
        splayedsearch=set;
    }
    /** 
     * @param insertion Set whether the tree is splayed after insertion 
     * operations.
     * @param search Set whether the tree is splayed after search operations. 
     */
    final public void setSplay(boolean insertion,boolean search){
        splayedinsertion=insertion;
        splayedsearch=search;
    }
    /** 
     * Inserts a new key, value pair into the tree. Both the key and the value
     * will be the same object.
     * @param keyandvalue A key and value.
     * @return the node containing the new key, value pair. 
     */
    public SBNode insert(Comparable keyandvalue){
        return insert(keyandvalue,keyandvalue);
    }
    /** 
     * Inserts a new key, value pair into the tree. Accepts an Object array as
     * its argument. The value at index 0 will be the key (must inherit from
     * the Comparable class) and the value at index 1 will be the value.
     * @param keyandvalue An array containing the key, value pair.
     * @return the node containing the new key, value pair. 
     */
    public SBNode insert(Object[] keyandvalue){
        return insert((Comparable)keyandvalue[0],keyandvalue[1]);
    }
    /** 
     * Inserts a new key, value pair into the tree.
     * @param key A key.
     * @param value A value.
     * @return the node containing the new key, value pair. 
     */
    public SBNode insert(Comparable key,Object value){
        if(root!=null){
            SBNode node=root.insert(key,value);
            splay(node,splayedinsertion);
            return node;
        }else{
            root=new SBNode(key,value);
            return root;
        }
    }
    /** 
     * Searches for a node with a matching key.
     * @param key A key.
     * @return the node corresponding to the specified key, null if none exists. 
     */
    public SBNode findNode(Comparable key){
        return findNode(key,splayedsearch);
    }
    /** 
     * Searches for a node with a matching key.
     * @param key A key.
     * @param splay Whether the tree should be splayed.
     * @return the node corresponding to the specified key, null if none exists. 
     */
    public SBNode findNode(Comparable key,boolean splay){
        if(root!=null){
            SBNode node=root.findNode(key);
            splay(node,splay);
            return node;
        }else{
            return null;
        }
    }
    /** 
     * Searches for the node with the most closely matching key.
     * @param key A key.
     * @return the node most closely corresponding to the specified key, null if
     * none exists. 
     */
    public SBNode findClosestNode(Comparable key){
        if(root!=null){
            SBNode node=root.findClosestNode(key);
            splay(node,splayedsearch);
            return node;
        }else{
            return null;
        }
    }
    /** 
     * Searches for the oldest value which corresponds to a key.
     * @param key A key.
     * @return the oldest Object corresponding to the key, null if none exists. 
     */
    public Object findFirstValue(Comparable key){
        SBNode node=findNode(key);
        splay(node,splayedsearch);
        return (node==null)?null:node.getFirstValue();
    }
    /** 
     * Searches for the newest value which corresponds to a key.
     * @param key A key.
     * @return the newest Object corresponding to the key, null if none exists. 
     */
    public Object findLastValue(Comparable key){
        SBNode node=findNode(key);
        splay(node,splayedsearch);
        return (node==null)?null:node.getLastValue();
    }
    /** 
     * Searches for the list of values which corresponds to a key.
     * @param key A key.
     * @return a LinkedList containing all values corresponding to the key,
     * null if none exist. (Though it could also return an empty list if the
     * default deletion behavior is set to not remove nodes which contain no
     * values.)
     */
    public LinkedList<Object> findAllValues(Comparable key){
        SBNode node=findNode(key);
        splay(node,splayedsearch);
        return (node==null)?null:node.getAllValues();
    }
    /** 
     * Searches for the number of values which correspond to a key.
     * @param key A key.
     * @return the number of values corresponding to the key.
     */
    public int findValuesSize(Comparable key){
        SBNode node=findNode(key);
        splay(node,splayedsearch);
        return (node==null)?0:node.valuesSize();
    }
    /** 
     * Searches for a new key, value pair into the tree. Accepts an Object array
     * as its argument. The value at index 0 will be the key (must inherit from
     * the Comparable class) and the value at index 1 will be the value.
     * @param keyandvalue An array containing the key, value pair.
     * @return true if the key, value pair exists within the tree, false
     * otherwise.
     */
    public boolean contains(Object[] keyandvalue){
        return contains((Comparable)keyandvalue[0],keyandvalue[1]);
    }
    /** 
     * Searches for a key, value pair.
     * @param key A key.
     * @param value A value.
     * @return true if the key, value pair exists within the tree, false
     * otherwise.
     */
    public boolean contains(Comparable key,Object value){
        SBNode node=findNode(key);
        return (node==null)?false:node.contains(value);
    }
    /** 
     * Searches for a key.
     * @param key A key.
     * @return true if the key exists within the tree, false otherwise.
     */
    public boolean containsKey(Comparable key){
        return findNode(key)!=null;
    }
    /** 
     * Searches for a value. This is not very efficient; without a key the
     * entire tree must be indiscriminately searched.
     * @param value A value.
     * @return true if the value exists within the tree, false otherwise.
     */
    public boolean containsValue(Object value){
        return (root==null)?false:root.treeContains(value);
    }
    /** 
     * Removes for a key, value pair from the tree. Accepts an Object array as
     * its argument. The value at index 0 will be the key (must inherit from
     * the Comparable class) and the value at index 1 will be the value.
     * @param keyandvalue An array containing the key, value pair.
     * @return the node which has taken the removed node's position in the tree.
     */
    public SBNode remove(Object[] keyandvalue){
        return remove((Comparable)keyandvalue[0],keyandvalue[1]);
    }
    /** 
     * Removes a key, value pair.
     * @param key A key.
     * @param value A value.
     * @return the node which has taken the removed node's position in the tree.
     */
    public SBNode remove(Comparable key,Object value){
        SBNode node=findNode(key,false);
        return (node==null)?null:node.removeValue(value);
    }
    /** 
     * Removes all values associated with a key. In this case, the node
     * corresponding to the key is removed entirely.
     * @param key A key.
     * @return the node which has taken the removed node's position in the tree,
     * null if none exists.
     */
    public SBNode remove(Comparable key){
        SBNode node=findNode(key,false);
        return (node==null)?null:node.remove();
    }
    /** 
     * Removes the oldest value associated with a key. If there are no more
     * values associated with the key, the node itself is also removed.
     * @param key A key.
     * @return if the node was removed, the node which has taken the removed
     * node's position in the tree. Otherwise, the node corresponding to the
     * key, or null if none exists.
     */
    public SBNode removeFirstValue(Comparable key){
        SBNode node=findNode(key,false);
        return (node==null)?null:node.removeFirstValue();
    }
    /** 
     * Removes the newest value associated with a key. If there are no more
     * values associated with the key, the node itself is also removed.
     * @param key A key.
     * @return if the node was removed, the node which has taken the removed
     * node's position in the tree. Otherwise, the node corresponding to the
     * key, or null if none exists.
     */
    public SBNode removeLastValue(Comparable key){
        SBNode node=findNode(key,false);
        return (node==null)?null:node.removeLastValue();
    }
    /** 
     * Removes all values associated with a key. In this case, the node
     * corresponding to the key is not removed. (This probably isn't the method
     * you're looking for, but whatever.)
     * @param key A key.
     * @return whether the node corresponding to the key exists and was expunged
     * of its associated values.
     */
    public boolean removeAllValues(Comparable key){
        SBNode node=findNode(key,false);
        if(node!=null){
            node.removeAllValues(); return true;
        }else{
            return false;
        }
    }
    /** @return an array of all values in the tree. */
    public Object[] toArray(){
        return toArray(SORTINORDER);
    }
    /** 
     * Returns an array of all values in the tree.
     * @param sort Which sorting method to use. Possible values: SORTINORDER,
     * SORTREVERSEORDER, SORTPREORDER, SORTPOSTORDER.
     * @return an array of all values in the tree, null if none exist or if
     * an invalid value was used for the sort argument.
     */
    public Object[] toArray(int sort){
        if(root!=null){
            if(sort==SORTINORDER){
                return root.valuesInOrder();
            }else if(sort==SORTREVERSEORDER){
                return root.valuesReverseOrder();
            }else if(sort==SORTPREORDER){
                return root.valuesPreOrder();
            }else if(sort==SORTPOSTORDER){
                return root.valuesPostOrder();
            }
        }
        return null;
    }
    /** @return an array of all nodes in the tree. */
    public SBNode[] toNodeArray(){
        return toNodeArray(SORTINORDER);
    }
    /** 
     * Returns an array of all nodes in the tree.
     * @param sort Which sorting method to use. Possible values: SORTINORDER,
     * SORTREVERSEORDER, SORTPREORDER, SORTPOSTORDER.
     * @return an array of all nodes in the tree, null if none exist or if
     * an invalid value was used for the sort argument.
     */
    public SBNode[] toNodeArray(int sort){
        if(root!=null){
            if(sort==SORTINORDER){
                return root.nodesInOrder();
            }else if(sort==SORTREVERSEORDER){
                return root.nodesReverseOrder();
            }else if(sort==SORTPREORDER){
                return root.nodesPreOrder();
            }else if(sort==SORTPOSTORDER){
                return root.nodesPostOrder();
            }
        }
        return null;
    }
    /** 
     * @param array an array of keys to be inserted into the tree. Inserted 
     * values will have themselves as the keys. 
     */
    public void insertMultiple(Comparable[] array){
        for(Comparable value:array){insert(value);}
    }
    /** 
     * Inserts an array of key, value pairs into the tree. Accepts an array of
     * Object arrays as its argument. The value at index 0 will be the key
     * (must inherit from the Comparable class) and the value at index 1 will
     * be the value.
     * @param array An array containing the key, value pairs.
     */
    public void insertMultiple(Object[][] array){
        for(Object[] value:array){insert((Comparable)value[0],value[1]);}
    }
    /** 
     * Splays the tree so that the specified node becomes the new root.
     * @param node The node which is to become the new root.
     */
    public void splay(SBNode node){
        if(root!=null && node!=null){root=root.splay(node);}
    }
    /** 
     * Splays the tree so that the specified node becomes the new root.
     * @param node The node which is to become the new root.
     * @param splayed Boolean determines whether it actually happens. (This is
     * really just a convenience function.)
     */
    private void splay(SBNode node,boolean splayed){
        if(splayed && root!=null && node!=null){root=root.splay(node);}
    }
    /** @return the leftmost node in the tree. */
    public SBNode leftMostNode(){
        return (root==null)?null:root.leftMostNode();
    }
    /** @return the rightmost node in the tree. */
    public SBNode rightMostNode(){
        return (root==null)?null:root.rightMostNode();
    }
    /** 
     * @return a clone of the tree. 
     * @throws java.lang.CloneNotSupportedException 
     */
    @Override
    public SBTree clone() throws CloneNotSupportedException {
        SBTree tree=(SBTree)super.clone();
        tree.splayedinsertion=splayedinsertion;
        tree.splayedsearch=splayedsearch;
        tree.root=(root==null)?null:root.clone();
        return tree;
    }
    /** @return a String representation of the tree. */
    @Override
    public String toString(){
        return toSequencedString();
    }
    /** @return a simple sequential list of the tree's nodes. */
    public String toSequencedString(){
        return (root==null)?null:root.toSequencedString();
    }
    /** @return a simple sequential list of the tree's keys. */
    public String keysToSequencedString(){
        return (root==null)?null:root.keysToSequencedString();
    }
    /** @return a simple sequential list of the tree's values. */
    public String valuesToSequencedString(){
        return (root==null)?null:root.valuesToSequencedString();
    }
    /** 
     * @return a structured String showing the tree's nodes and their
     * relationships to one another. 
     */
    public String toBranchedString(){
        return toBranchedString("");
    }
    /**
     * @param prefix A string which precedes all lines of the String.
     * @return a structured String showing the tree's nodes and their
     * relationships to one another. 
     */
    public String toBranchedString(String prefix){
        return (root==null)?null:root.toBranchedString(prefix);
    }
}



