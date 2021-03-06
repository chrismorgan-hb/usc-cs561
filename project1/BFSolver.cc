// BFSolver.cpp
// Christopher Morgan
// 7468-2726-35
// CSCI 561 Project 1 Fall 2006
//
// Breadth-First Solver implementation.

#include "BFSolver.h"
#include "Node.h"
#include "Pair.h"
#include <list>
#include <iostream>

using namespace std;

void BFSolver::makeQueue() {

	list<char> l;
	l.push_front(problem.getStart());
	Node s(l, 0); 

	queue.clear();
	queue.push_back(s);
}

void BFSolver::queuingFn(Node n) {

	queue.push_back(n);
}

list<Node> BFSolver::expand(Node n) {

	list<Node> expanded;

	list<Pair> newCities = problem.getMap()[n.getPath().back()];

	for ( list<Pair>::iterator it = newCities.begin(); it != newCities.end(); it++ ) {
		
		Node m(n.getPath(), n.getCurrentDistance());

		// only add if not already on list
		
		if (!m.cityInPath((*it).city)) {
			m.addToPath((*it).city);
			m.addToCurrentDistance((*it).dist);
			expanded.push_back(m);
	
		}
	}

	return expanded;
}

bool BFSolver::goalTest(Node n) {

	if ( n.getPath().back() == problem.getGoal() ) {
		solution = n;
		return true;
	}

	else {
		return false;
	}
}
