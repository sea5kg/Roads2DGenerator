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

#include "Roads2DGeneratorUnigineSplineGraph.h"

#include <fstream>
#include <cmath>
#include <ctime>
#include <iostream>

#define M_PI           3.14159265358979323846  /* pi */

Roads2DGeneratorUnigineSplineGraph::Roads2DGeneratorUnigineSplineGraph(
    const Roads2DGeneratorGraph &graph
) {
    std::cout << "Initializing... " << std::endl;
    const std::vector<Roads2DGeneratorPoint> &points = graph.getPoints();
    for (int i = 0; i < points.size(); i++) {
        SPLPoint3D p(points[i].getX(), points[i].getY(), 0.0);
        m_vPoints.push_back(p);
    }

    const std::vector<std::pair<int,int>> &connections = graph.getConnections();
    for (int i = 0; i < connections.size(); i++) {
        SPLSegment seg;
        seg.start_index = connections[i].first;
        seg.end_index = connections[i].second;
        seg.start_tangent = SPLPoint3D(0,0,0);
        seg.end_tangent = SPLPoint3D(0,0,0);
        m_vSegments.push_back(seg);
    }
    updateTangents();
    std::cout << "Done. " << std::endl;
}

void Roads2DGeneratorUnigineSplineGraph::modifyRandom(float fluctuationX, float fluctuationY, float fluctuationZ) {
    fluctuationX = std::abs(fluctuationX);
    fluctuationY = std::abs(fluctuationY);
    fluctuationZ = std::abs(fluctuationZ);
    int nX = fluctuationX * 1000;
    int nY = fluctuationY * 1000;
    int nZ = fluctuationZ * 1000;
    fluctuationX = fluctuationX / 2;
    fluctuationY = fluctuationY / 2;
    fluctuationZ = fluctuationZ / 2;
    random.setInitSeed(std::time(0));
    // std::cout << "size: " << m_vPoints.size() << std::endl;
    for (int i = 0; i < m_vPoints.size(); i++) {
        // std::cout << m_vPoints[i].x << std::endl;
        m_vPoints[i].x += float(random.getNextRandom() % nX) / 1000.0f - fluctuationX;
        m_vPoints[i].y += float(random.getNextRandom() % nY) / 1000.0f - fluctuationY;
        m_vPoints[i].z += float(random.getNextRandom() % nZ) / 1000.0f - fluctuationZ;
        // std::cout << m_vPoints[i].x << std::endl;
    }
    updateTangents();
}

void Roads2DGeneratorUnigineSplineGraph::modifyScale(float scale) {
    for (int i = 0; i < m_vPoints.size(); i++) {
        m_vPoints[i].x = m_vPoints[i].x * scale;
        m_vPoints[i].y = m_vPoints[i].y * scale;
        m_vPoints[i].z = m_vPoints[i].z * scale;
    }
    updateTangents();
}

void Roads2DGeneratorUnigineSplineGraph::exportToSPLFile(const std::string &sFilepath) {
    std::cout << "exportToSPLFile: " << sFilepath << std::endl;
    std::ofstream fw(sFilepath, std::ofstream::out);
    if (fw.is_open()) {
        fw << "{\n";
        fw << "\t\"points\": [\n";
        fw << "\t\t[\n";
        for (int i = 0; i < m_vPoints.size(); i++) {
            fw << "\t\t\t" << m_vPoints[i].x << ",\n";
            fw << "\t\t\t" << m_vPoints[i].y << ",\n";
            fw << "\t\t\t" << m_vPoints[i].z << "\n";
            // SPLPoint3D p(points[i].getX(), points[i].getY(), 0.0);
            // m_vPoints.push_back(p);
            if (i != m_vPoints.size() - 1) {
                fw << "\t\t], [\n";
            }
        }
        fw << "\t\t]\n";
        fw << "\t],\n";
        fw << "\t\"segments\": [\n";
        fw << "\t\t{\n";
        for (int i = 0; i < m_vSegments.size(); i++) {
            fw << "\t\t\t\"start_index\": " << m_vSegments[i].start_index << ",\n";
            fw << "\t\t\t\"end_index\": " << m_vSegments[i].end_index << ",\n";
            fw << "\t\t\t\"start_up\": [\n";
            fw << "\t\t\t\t" << m_vSegments[i].start_up.x << ",\n";
            fw << "\t\t\t\t" << m_vSegments[i].start_up.y << ",\n";
            fw << "\t\t\t\t" << m_vSegments[i].start_up.z << "\n";
            fw << "\t\t\t],\n";
            fw << "\t\t\t\"end_up\": [\n";
            fw << "\t\t\t\t" << m_vSegments[i].end_up.x << ",\n";
            fw << "\t\t\t\t" << m_vSegments[i].end_up.y << ",\n";
            fw << "\t\t\t\t" << m_vSegments[i].end_up.z << "\n";
            fw << "\t\t\t],\n";
            fw << "\t\t\t\"start_tangent\": [\n";
            fw << "\t\t\t\t" << m_vSegments[i].start_tangent.x << ",\n";
            fw << "\t\t\t\t" << m_vSegments[i].start_tangent.y << ",\n";
            fw << "\t\t\t\t" << m_vSegments[i].start_tangent.z << "\n";
            fw << "\t\t\t],\n";
            fw << "\t\t\t\"end_tangent\": [\n";
            fw << "\t\t\t\t" << m_vSegments[i].end_tangent.x << ",\n";
            fw << "\t\t\t\t" << m_vSegments[i].end_tangent.y << ",\n";
            fw << "\t\t\t\t" << m_vSegments[i].end_tangent.z << "\n";
            fw << "\t\t\t],\n";
            if (i != m_vSegments.size() - 1) {
                fw << "\t\t}, {\n";
            }
        }
        fw << "\t\t}\n";
        fw << "\t]\n";
        fw << "}\n";
        fw.close();
    } else {
        std::cerr << "Problem with opening file" << std::endl;
    }
}

Roads2DGeneratorUnigineSplineGraph::SPLPoint3D Roads2DGeneratorUnigineSplineGraph::calculateTangent(
    int indexPoint1,
    int indexPoint2,
    bool isStartPoint
) {
    // std::cout << "Calculating for " << indexPoint << std::endl;
    std::vector<int> indexesPoints = findConnectedSegments(indexPoint1);
    float x = 0.0;
    float y = 0.0;
    float z = 0.0;
    SPLPoint3D p1 = m_vPoints[indexPoint1];
    SPLPoint3D p2 = m_vPoints[indexPoint2];
    SPLPoint3D vec = SPLPoint3D(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
    // if (isStartPoint) {
    //     vec = SPLPoint3D(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
    // } else {
    //     vec = SPLPoint3D(p1.x - p2.x, p1.y - p2.y, p1.z - p2.z);
    // }

    for (int i = 0; i < indexesPoints.size(); i++) {
        SPLPoint3D p = m_vPoints[indexesPoints[i]];
        x += p.x - p1.x;
        y += p.y - p1.y;
        z += p.z - p1.z;
        // indexPoint2
    }
    if (indexesPoints.size() > 0) {
        x = x / float(indexesPoints.size());
        y = y / float(indexesPoints.size());
        z = z / float(indexesPoints.size());
    }
    float length = std::sqrt(std::pow(x,2) + std::pow(y,2) + std::pow(z,2));
    if (length < 0.2 && (indexesPoints.size() == 2 || indexesPoints.size() == 4)) { // line or cross
        SPLPoint3D p = m_vPoints[indexPoint2];
        x = (p.x - p1.x) / 2.0;
        y = (p.y - p1.y) / 2.0;
        z = (p.z - p1.z) / 2.0;
        // length = std::sqrt(std::pow(x,2) + std::pow(y,2) + std::pow(z,2));
        // std::cout
        //     << "length = " << length << "; "
        //     << "indexesPoints.size() " << indexesPoints.size()
        //     << std::endl;
        return SPLPoint3D(x, y, z);
    }
    length = std::sqrt(std::pow(x,2) + std::pow(y,2) + std::pow(z,2));

    // float dot = vec.x * x + vec.y * y;
    // float length_vec = std::sqrt(std::pow(vec.x,2) + std::pow(vec.y,2));
    // float length_xy = std::sqrt(std::pow(x,2) + std::pow(y,2));
    // float angle = std::acos(dot / (length_vec * length_xy));
    //(bx-ax)*(py-ay)-(by-ay)*(px-ax);
    float side = x * vec.y - y * vec.x;

    // if s > 0 then WherePoint:=1

    float sign = 1.0;
    if (side < 0) {
        sign = -1.0;
    }
    // std::cout
    //     << "length = " << length << "; "
    //     << "side = " << side << "; "
    //     << "indexesPoints.size() " << indexesPoints.size()
    //     << std::endl;

    // tangent
    // by z to 90
    float x1 = x * std::cos(sign * M_PI/2) - y * std::sin(sign * M_PI/2);
    float y1 = x * std::sin(sign * M_PI/2) + y * std::cos(sign * M_PI/2);
    float z1 = z;
    // float x1 = x * std::cos(angle) - y * std::sin(angle);
    // float y1 = x * std::sin(angle) + y * std::cos(angle);
    // float z1 = z;

    return SPLPoint3D(x1 / 2.0, y1 / 2.0 , z1 / 2.0);
}

void Roads2DGeneratorUnigineSplineGraph::updateTangents() {
    for (int i = 0; i < m_vSegments.size(); i++) {
        m_vSegments[i].start_tangent = calculateTangent(
            m_vSegments[i].start_index,
            m_vSegments[i].end_index,
            true
        );
        m_vSegments[i].end_tangent = calculateTangent(
            m_vSegments[i].end_index,
            m_vSegments[i].start_index,
            false
        );
    }
}

std::vector<int> Roads2DGeneratorUnigineSplineGraph::findConnectedSegments(int indexPoint) {
    std::vector<int> vRet;
    for (int i = 0; i < m_vSegments.size(); i++) {
        if (m_vSegments[i].start_index == indexPoint) {
            int index = m_vSegments[i].end_index;
            bool bFound = false;
            for (int k = 0; k < vRet.size(); k++) {
                if (vRet[k] == index) {
                    bFound = true;
                }
            }
            if (!bFound && index != indexPoint) {
                vRet.push_back(index);
            }
        }
        if (m_vSegments[i].end_index == indexPoint) {
            int index = m_vSegments[i].start_index;
            bool bFound = false;
            for (int k = 0; k < vRet.size(); k++) {
                if (vRet[k] == index) {
                    bFound = true;
                }
            }
            if (!bFound && index != indexPoint) {
                vRet.push_back(index);
            }
        }
    }
    return vRet;
}
