package hr.fer.zemris.trisat;

import java.util.BitSet;
import java.util.Random;

public class BitVector {

    int len;
    protected final BitSet bitSet;

    public BitVector(Random rand, int numberOfBits) {
        len = numberOfBits;
        bitSet = new BitSet(numberOfBits);
        for (int i = 0; i < numberOfBits; i++) {
            bitSet.set(i, rand.nextBoolean());
        }
    }

    public BitVector(BitSet backingSet, int numberOfBits) {
        len = numberOfBits;
        bitSet = backingSet;
    }

    public BitVector(boolean... bits) {
        len = bits.length;
        bitSet = new BitSet(bits.length);
        for (int i = 0; i < bits.length; i++) {
            bitSet.set(i, bits[i]);
        }
    }

    public BitVector(int n) {
        len = n;
        bitSet = new BitSet(n);
    }

    // vraća vrijednost index-te varijable
    public boolean get(int index) {
        return bitSet.get(index);
    }

    // vraća broj varijabli koje predstavlja
    public int getSize() {
        return len;
    }

    public boolean isEmpty() {
        return bitSet.isEmpty();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (int i = 0; i < len; i ++) {
            sb.append(bitSet.get(i) ? "1" : "0");
        }
        return sb.toString();
    }

    public BitSet copyBackingBitSet() {
        return (BitSet) bitSet.clone();
    }

    // vraća promjenjivu kopiju trenutnog rješenja
    public MutableBitVector copy() {
        boolean[] newBits = new boolean[len];
        for (int i = 0; i < newBits.length; i ++) {
            newBits[i] = bitSet.get(i);
        }
        return new MutableBitVector(newBits);
    }
}