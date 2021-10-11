package hr.fer.zemris.trisat;

import java.util.Iterator;

public class BitVectorNGenerator implements Iterable<MutableBitVector> {
    public BitVectorNGenerator(BitVector assignment) {
    }

    // Vraća lijeni iterator koji na svaki next() računa sljedećeg susjeda @Override
    public Iterator<MutableBitVector> iterator() {
        return null;
    }

    // Vraća kompletno susjedstvo kao jedno polje
    public MutableBitVector[] createNeighborhood() {

        return new MutableBitVector[0];
    }
}
