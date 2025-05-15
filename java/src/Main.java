package src;

public class Main {
    public static void main(String[] args) {
        Roads2DGenerator road2gen = new Roads2DGenerator();
        road2gen.getConfig()
            .setWidth(50)
            .setHeight(25)
            .setDensity(0.7f)
        ;

        // You can use the following parameter (nSeedRandom) to reproduce the results.
        // If the parameters are the same, then the result will be the same on any machine.
        // road2gen.getConfig().setSeedInitRandom(std::time(0));
        road2gen.getConfig().setSeedInitRandom(1686154273);

        // You can exclude some area
        road2gen.getConfig().setPresetExcludes(12, 10, 18, 15);

        if (!road2gen.generate()) {
            String m_sErrorMessage = "FAILED. Could not generate. Try again or change input params and try again.\n";
            m_sErrorMessage += "Init Seed: " + road2gen.getConfig().getSeedInitRandom() + "\n";
            m_sErrorMessage += "Error Message: " + road2gen.getErrorMessage() + "\n";
            System.err.println(m_sErrorMessage);
            return;
        }
        // std::cout << "Init Seed: " << road2gen.getConfig().getSeedInitRandom() << std::endl;
        road2gen.printMap();
    }
}