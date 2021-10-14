package hr.fer.zemris.trisat;

import java.util.BitSet;

public class MutableBitVector extends BitVector {

    public MutableBitVector(BitSet bs, int len) {
        super(bs, len);
    }

    public MutableBitVector(boolean... bits) {
        super(bits);
    }

    public MutableBitVector(int n) {
        super(n);
    }

    public MutableBitVector not() {
        var copy = copyBackingBitSet();
        // must be .size(), because length doesnt flip all bits...
        // for fixed length
        copy.flip(0, copy.size());
        return new MutableBitVector(copy, len);
    }

    public MutableBitVector and(BitVector bv) {
        var copy = copyBackingBitSet();
        copy.and(bv.bitSet);
        return new MutableBitVector(copy, len);
    }

    public MutableBitVector or(BitVector bv) {
        var copy = copyBackingBitSet();
        copy.or(bv.bitSet);
        return new MutableBitVector(copy, len);
    }

    public MutableBitVector xor(BitVector bv) {
        var copy = copyBackingBitSet();
        copy.xor(bv.bitSet);
        return new MutableBitVector(copy, len);
    }

    // increments bit vector (binary counter)
    public void inc() {
        int i = 0;
        while (bitSet.get(i)) {
            bitSet.set(i++, false);
        }
        bitSet.set(i, true);
    }

    // zapisuje predanu vrijednost u zadanu varijablu
    public void set(int index, boolean value) {

    }
}