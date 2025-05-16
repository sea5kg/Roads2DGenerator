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

#ifndef __ROADS_2D_GENERATOR_UNIGENE_SPLINE_GRAPH_H__
#define __ROADS_2D_GENERATOR_UNIGENE_SPLINE_GRAPH_H__

#include "Roads2DGenerator.h"

// documentation here:
// https://developer.unigine.com/en/docs/2.16.1/code/formats/spline_format?rlang=cpp

class Roads2DGeneratorUnigineSplineGraph {
    public:
        Roads2DGeneratorUnigineSplineGraph(const Roads2DGeneratorGraph &graph);
        void modifyRandom(float fluctuationX, float fluctuationY, float fluctuationZ);
        void modifyScale(float scale);
        void exportToSPLFile(const std::string &sFilepath);

    private:
        int m_nWidth;
        int m_nHeight;
        struct SPLPoint3D {
            SPLPoint3D() : x(0), y(0), z(0) {};
            SPLPoint3D(float x, float y, float z) : x(x), y(y), z(z) {};
            float x;
            float y;
            float z;
        };
        struct SPLSegment {
            SPLSegment() {
                start_up = SPLPoint3D(0,0,1);
                end_up = SPLPoint3D(0,0,1);
            }
            int start_index;
            int end_index;
            SPLPoint3D start_tangent;
            SPLPoint3D end_tangent;
            SPLPoint3D start_up; // 0,0,1
            SPLPoint3D end_up; // 0,0,1
        };
        Roads2DGeneratorUnigineSplineGraph::SPLPoint3D calculateTangent(
            int indexPoint1,
            int indexPoint2,
            bool isStartPoint
        );
        void updateTangents();
        std::vector<int> findConnectedSegments(int indexPoint);
        std::vector<SPLPoint3D> m_vPoints;
        std::vector<SPLSegment> m_vSegments;
        Roads2DGeneratorPseudoRandom random;
};

#endif // __ROADS_2D_GENERATOR_UNIGENE_SPLINE_GRAPH_H__