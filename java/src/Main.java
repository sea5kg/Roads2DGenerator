/*
MIT License

Copyright (c) 2021-2025 Evgenii Sopov (mrseakg@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

// original source-code: https://github.com/sea5kg/Roads2DGenerator

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