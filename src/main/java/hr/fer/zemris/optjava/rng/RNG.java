package hr.fer.zemris.optjava.rng;

import java.io.IOException;
import java.util.Properties;

public class RNG {
    private static IRNGProvider rngProvider;

    static {
        // Stvorite primjerak razreda Properties;
        // Nad Classloaderom razreda RNG tražite InputStream prema resursu rng-config.properties // recite stvorenom objektu razreda Properties da se učita podatcima iz tog streama.
        // Dohvatite ime razreda pridruženo ključu "rng-provider"; zatražite Classloader razreda // RNG da učita razred takvog imena i nad dobivenim razredom pozovite metodu newInstance() // kako biste dobili jedan primjerak tog razreda; castajte ga u IRNGProvider i zapamtite.
        var props = new Properties();
        try {
            props.load(RNG.class.getResourceAsStream("/rng-config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            rngProvider = (IRNGProvider) ClassLoader.getSystemClassLoader().loadClass(props.getProperty("rng-provider")).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static IRNG getRNG() {
        return rngProvider.getRNG();
    }
}