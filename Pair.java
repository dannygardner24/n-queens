package method;

/** Pair represents two objects, a key and a value, stored correspondingly.
 * 
 * @author Daniel Gardner (2023)
 *
 * @param <K> The object type of the key object.
 * @param <V> The object type of the value object.
 */
public class Pair<K, V> {
	
	private K key;
	private V value;

	
	/** Constructs a Pair with containing the corresponding parameters.
	 * 
	 * @param key The key to be stored.
	 * @param value The value to be stored.
	 */
	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	
	/** Gets a reference to the key object.
	 * 
	 * @return The key object.
	 */
	public K getKey() {
		return key;
	}
	
	/** Gets a reference to the value object.
	 * 
	 * @return The value object.
	 */
	public V getValue() {
		return value;
	}
}
