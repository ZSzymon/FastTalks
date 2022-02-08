package client;

public class UnorderedPair<T> {
    //https://stackoverflow.com/questions/8905528/how-to-write-write-a-set-for-unordered-pair-in-java
    final T first, second;

    public UnorderedPair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UnorderedPair))
            return false;
        UnorderedPair<T> up = (UnorderedPair<T>) o;
        return (up.first == this.first && up.second == this.second) ||
                (up.first == this.second && up.second == this.first);
    }

    @Override
    public int hashCode() {
        int hashFirst = first.hashCode();
        int hashSecond = second.hashCode();
        int maxHash = Math.max(hashFirst, hashSecond);
        int minHash = Math.min(hashFirst, hashSecond);
        // return Objects.hash(minHash, maxHash);
        // the above line also works but I tend to avoid this to avoid unnecessary auto-boxing
        return minHash * 31 + maxHash;
    }
}