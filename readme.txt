LEGAL

This code is released as public domain. But that doesn't mean you should 
claim credit that isn't yours or sell it when it could otherwise be had for
free, because that would be a shitty thing of you to do.
It was originally written by Sophie Kirschner. (sophiek@pineapplemachine.com)

CLASSES

SBTest

	Summary:

	 Contains code used to test functionality of the various methods.
	 Incidentally, it makes for very good example code.


SBTree

	Summary: 

	 Splayed binary search tree implementation. Supports multiple
	 values assigned to the same key.

	Key methods:

	 SBNode insert ( Comparable key , Object value )
	 SBNode remove ( Comparable key )
	 SBNode remove ( Comparable key , Object value )
	 Object findFirstValue ( Comparable key )
	 Object findLastValue ( Comparable key )
	 LinkedList<Object> findAllValues ( Comparable key )
	 SBNode splay ( SBNode node )


SBNode

	Summary:

	 A node belonging to a SBTree. Every SBNode is the root of its own
	 subtree and can be operated upon accordingly.

	Key methods:

	 SBNode remove ( Object value )
	 SBNode remove ( )
	 Object getFirstValue ( )
	 Object getLastValue ( )
	 LinkedList<Object> getAllValues ( )
	