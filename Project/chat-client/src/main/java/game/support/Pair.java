package game.support;

public class Pair<K, V> {
    private K key;
    private V value;
    
    public Pair() {};
    
    public Pair(Pair<K, V> pair) {
        copyOf(pair);
    }    

    public Pair(K key, V value) {
        setKey(key);
        setValue(value);
    }
    
    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
    
    public void copyOf(Pair<K, V> pair) {
        this.key = pair.getKey();
        this.value = pair.getValue();
    }
    
    @Override
    public String toString() {
        return "[" + key + ", " + value + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Pair)) return false;
        Pair pairO = (Pair) obj;
        return (this.key.equals(pairO.getKey()) && this.value.equals(pairO.getValue()));
    }

    @Override
    public int hashCode() {
        return key.hashCode() ^ value.hashCode();
    }
}