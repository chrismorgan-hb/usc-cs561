// Node.h
// Christopher Morgan
// 7468-2726-35
// CSCI 561 Project 1 Fall 2006
//
// Base class for nodes.
// This class is implemented for BF and
// DF, but needs to be derived from
// to get an A* node.


#include <list>

using namespace std;

#ifndef NODE_H
#define NODE_H

class Node {

		protected:

			int currentDistance;
			list<char> path;

		public:

			Node(list<char> l, int dist);
			Node() {}
			void print();
			list<char> getPath() { return path; }
			void addToPath(char c);
			bool cityInPath(char c);
			int getCurrentDistance() { return currentDistance; }
			void addToCurrentDistance (int d) { currentDistance += d; }
};

#endif
