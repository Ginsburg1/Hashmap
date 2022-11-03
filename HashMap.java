import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class HashMap<K, V> implements Map<K, V>
{
	//List of primes to be used for p value in the MAD compression method
	private final int[] primes = {100663319, 201326611, 402653189,805306457, 1610612741};
	private static final double LOADTHRESHOLD = 0.75;

	//Data members	
	private LinkedList<Entry<K, V>>[] buckets;
	private int numEntries;

	//Parameters for Multiply-Add-Divide compression
	//hash function = ((hashCode() * a + b) % p) % buckets.length
	private int p;
	private int a;
	private int b;

	public HashMap()
	{
		initMap(16);
	}

	private void initMap(int bc)
	{
		//Init the array
		this.buckets = new LinkedList[ bc ];
		this.numEntries = 0;

		//Init the linked lists (buckets)
		for(int i=0; i < buckets.length; i++)
		{
			buckets[i] = new LinkedList<>();
		}

		//Init MAD param
		this.p = primes[(int) (Math.random() * primes.length)];
		this.a = (int)(Math.random() * (p-1) + 1);
		this.b = (int)(Math.random() * p);
	}

	private int hashFunction(K k)
	{
		// implements the MAD method of compression
		return (Math.abs(k.hashCode() * a + b) % p) % this.buckets.length;
	}

	//Map Methods
	@Override
	public V put(K k, V v) 
	{
		expandIfNeeded();

		int index = hashFunction(k);
		// If the key is already in the table,
		// overwrite the old value with the new one
		// and return the old value.
		LinkedList<Entry<K, V>> bucket = buckets[index];
		for(Entry<K, V> e : bucket)
		{
			if(e.getKey().equals(k))
			{
				return e.setValue(v);
			}
		}

		// If the key doesn't already exist in the map,
		// put the new entry into its bucket.
		bucket.addFirst(new MapEntry(k, v));
		numEntries++;
		return null;
	}

	@Override
	public V get(K k) 
	{

		// Find index
		// Get the correct bucket
		// Traverse the bucket
		// returning the value if a matching key is found
		// return null if no matching key is found.
		for (Entry<K, V> temp: buckets[hashFunction(k)]) {
			if (temp.getKey().equals(k)) {
				return temp.getValue();
			}
		}
		return null;
	}

	@Override
	public V remove(K k) {

		int index = hashFunction(k);
		LinkedList<Entry<K,V>> bucket = buckets[index];
		Iterator<Entry<K, V>> iter = bucket.iterator();
		while(iter.hasNext()) {
			Entry<K,V> temp = iter.next();
			if (temp.getKey().equals(k)) {
				iter.remove();
				numEntries--;
				return temp.getValue();
			}
		}

		return null;
	}

	@Override
	public boolean isEmpty() 
	{
		return this.numEntries == 0;
	}

	@Override
	public int size() {
		return this.numEntries;
	}

	@Override
	public Iterable<K> keySet() 
	{

		// create a "snapshot" iterator as follows:
		// Make an ArrayList to serve as an iterator
		// (It will store references to all the keys)
		// Traverse the List of buckets
		// traverse the entries in each bucket
		//  add the key for each entry to the iterator
		// return the list we built
		// Practice question: what is the runtime of this algorithm?

		ArrayList<K> temp = new ArrayList<K>();
		for (LinkedList<Entry<K, V>> bucket: buckets) {
			for(Entry<K,V> e: bucket) {
				temp.add(e.getKey());
			}

		}

		return temp;
	}

	@Override
	public Iterable<V> values() 
	{
		ArrayList<V> values = new ArrayList<>();
		for(LinkedList<Entry<K, V>> bucket : buckets)
		{
			for(Entry<K, V> e : bucket)
			{
				values.add(e.getValue());
			}
		}
		return values;
	}

	@Override
	public Iterable<Entry<K, V>> entrySet() 
	{
		ArrayList<Entry<K, V>> entries = new ArrayList<>();
		for(LinkedList<Entry<K, V>> bucket : buckets)
		{
			for(Entry<K, V> e : bucket)
			{
				entries.add(e);
			}
		}
		return entries;
	}

	public String toString()
	{
		String r = "";
		int largestBucket = 0;

		for(int i=0; i < buckets.length; i++)
		{
			if(this.buckets[i].size() > largestBucket)
			{
				largestBucket = this.buckets[i].size();
			}
			r += "Bucket " + i + "( " + this.buckets[i].size() + " ) - ";
			for(Entry<K, V> e : this.buckets[i])
			{
				r += e + " ";
			}
			r += "\n";
		}
		r += "\nNumber of Entries: " + this.size() + "\nLargest Bucket: " + largestBucket + "\nLambda = " + (double)this.size()/buckets.length;

		return r;

	}
	//	If past the load threshold, then expand the hash table 
	// to twice its previous size and rehash every entry. 
	private void expandIfNeeded(){

		if (((float) numEntries / (float) buckets.length) > LOADTHRESHOLD){
			LinkedList<Entry<K,V>>[] temp = buckets;
			initMap(buckets.length *2);
			for (LinkedList<Entry<K,V>> list: temp) {
				for (Entry<K,V> item: list) {
					put(item.getKey(), item.getValue());
				}
			}
		}
	}


	/**
	 * MapEntry class implements the public Entry Interface. 
	 * Supports getKey, setValue and getValue
	 *
	 */
	private class MapEntry implements Entry<K, V>
	{
		private K key;
		private V value;

		public MapEntry(K k, V v)
		{
			this.key = k;
			this.value = v;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V val) 
		{
			V oldValue = this.value;
			this.value = val;
			return oldValue;
		}

		public String toString()
		{
			return "(" + this.key.toString() + " : " + this.value.toString() + ")";
		}

	}
}
