package util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SetMap<K,V> {
	Map<K, Set<V>> contents = new HashMap<K, Set<V>>();
	
	public Set<K> keySet() {
		return contents.keySet();
	}
	
	public Set<V> get(K key) {
		return ensureKey(key);
	}
	
	public boolean put(K key, V value) {
		return ensureKey(key).add(value);
	}
	
	private Set<V> ensureKey(K key) {
		Set<V> values = contents.get(key);
		if( values == null ) {
			values = new HashSet<V>();
			contents.put(key, values);
		}
		return values;
	}
	
	public boolean contains(K key) {
		return contents.containsKey(key);
	}
	
	public boolean contains(K key, V value) {
		Set<V> values = contents.get(key);
		if( values == null ) return false;
		return values.contains(value);
	}
	
	public Set<V> remove(K key) {
		return contents.remove(key);
	}
	
	public boolean remove(K key, V value) {
		if( !contents.containsKey(key) )
			return false;
		return get(key).remove(value);
	}
}
