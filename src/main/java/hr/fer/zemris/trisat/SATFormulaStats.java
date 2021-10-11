package hr.fer.zemris.trisat;

public class SATFormulaStats {
    public SATFormulaStats(SATFormula formula) {

    }

    // analizira se predano rješenje i pamte svi relevantni pokazatelji
    // primjerice, ažurira elemente polja post[...] ako drugi argument to dozvoli; računa Z; ...
    public void setAssignment(BitVector assignment, boolean updatePercentages) {
    }

    // vraća temeljem onoga što je setAssignment zapamtio: broj klauzula koje su zadovoljene
    public int getNumberOfSatisfied() {
        return 0;
    }

    // vraća temeljem onoga što je setAssignment zapamtio
    public boolean isSatisfied() {
        return false;
    }

    // vraća temeljem onoga što je setAssignment zapamtio: suma korekcija klauzula // to je korigirani Z iz algoritma 3
    public double getPercentageBonus() {
        return 0;
    }

    // vraća temeljem onoga što je setAssignment zapamtio: procjena postotka za klauzulu
    // to su elementi polja post[...]
    public double getPercentage(int index) {
        return 0;
    }

    // resetira sve zapamćene vrijednosti na početne (tipa: zapamćene statistike)
    public void reset() {
    }
}
