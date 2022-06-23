package hr.fer.oer.jmbag0036514720;

public class Main {
    public static void main(String[] args) {
        IFunction fun = Prijenosna.getFunction(new String[]{"100", "prijenosna.txt"});
        // int maxIter = 100;
        int maxIter = 100000;

        // DE/rand/1/bin
        double err1 = 0;
        // int nRuns1 = 10;
        int nRuns1 = 1;
        for (int i = 0; i < nRuns1; i++) {
            var de = new DifferentialEvolution(fun, 40,1, 0.9, maxIter, "rand");
            err1 += Math.pow(de.run(), 2); // ocekujemo 0 pa zato samo kvadridam
        }
        err1 = err1 / nRuns1;
        System.out.println("DE/rand/1/bin (%s runs AVG error, maxiter=%s): %s".formatted(nRuns1, maxIter, err1)); // 50 - 80 ispada najcesce
        System.out.println("-----------------------");
        // DE/target-to-best/1/bin
        double err2 = 0;
        // int nRuns2 = 10;
        int nRuns2 = 1;
        for (int i = 0; i < nRuns2; i++) {
            var de = new DifferentialEvolution(fun, 40,0.5, 0.6, maxIter, "best");
            err2 += Math.pow(de.run(), 2); // ocekujemo 0 pa zato samo kvadridam
        }
        err2 = err2 / nRuns2;

        // rand je konzistentno bolji za iste parametre
        // vise jedinki 15 -> 20, bolji rezultat, znatno nizi error
        // pretpostavka: bolji sampling prostora (-10, 10) x (-10, 10)
        // i više genetskog materijala

        // 40 jedinki je takoder dobro, 0.5 je najbolji
        System.out.println("DE/best/1/bin (%s AVG error, maxiter=%s): %s".formatted(nRuns2,maxIter, err2)); // 50 - 80 ispada
    }


}
