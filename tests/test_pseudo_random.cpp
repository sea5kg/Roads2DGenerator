#include "Roads2DGenerator.h"

#include <vector>
#include <iostream>

int main() {
    Roads2DGeneratorPseudoRandom rand;
    rand.setInitSeed(1686154273);
    std::vector<int> vRandomNumbers;
    vRandomNumbers.push_back(213279087);
    vRandomNumbers.push_back(55101825);
    vRandomNumbers.push_back(132540442);
    vRandomNumbers.push_back(17647650);
    for (int i = 0; i < vRandomNumbers.size(); i++) {
        int nRand = rand.getNextRandom();
        if (vRandomNumbers[i] != nRand) {
            std::cerr
                << "Expected random: " << vRandomNumbers[i]
                << ", but got " << nRand
                << " on iteration: " << i
                << std::endl;
            return 1;
        }
    }
    return 0;
}