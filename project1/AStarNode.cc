// AStarNode.cpp
// Christopher Morgan
// 7468-2726-35
// CSCI 561 Project 1 Fall 2006
//
// Implements AStarNode class

#include "Node.h"
#include "AStarNode.h"
#include <list>
#include <iostream>

using namespace std;

AStarNode::AStarNode(list<char> l, int dist, int heuristic) :
				Node(l, dist)
{

	path = l;
	currentDistance = dist;
	h = heuristic;
	fOfN = h + currentDistance;
}

void AStarNode::print() {

	cout << "(" << h << ", " << currentDistance << ", (";

	list<char>::iterator it = path.begin();

	bool first = true;
	for ( ; it != path.end(); it++ ) {

		if (!first) {
			cout << " ";
		}
		cout << *it;
		first = false;
	}

	cout << ")) ";
}

void AStarNode::addToCurrentDistance (int d, int heuristic) { 
	
	currentDistance += d; 
	h = heuristic; 
	fOfN = currentDistance + h; 
}

bool AStarNode::operator < (const AStarNode &rhs) {

	return this->fOfN < rhs.fOfN;
}

