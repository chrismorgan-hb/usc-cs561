// AStarNode.h
// Christopher Morgan
// 7468-2726-35
// CSCI 561 Project 1 Fall 2006
//
// A* Node.  Contains f(n).
//

#include "Node.h"
#include <list>

using namespace std;

#ifndef ASTARNODE_H
#define ASTARNODE_H

class AStarNode : public Node {

		private:

			int h;
			int fOfN;

		public:

			AStarNode(list<char> l, int dist, int h);
			AStarNode() {}
			void print();
			int getHeuristic() { return h; }
			int getFOfN() { return fOfN; }
			void addToCurrentDistance (int d, int heuristic);
			bool operator < (const AStarNode &rhs); // needed to sort the list
};

#endif

