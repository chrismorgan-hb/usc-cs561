// Solver.cpp
// Christopher Morgan
// 7468-2726-35
// CSCI 561 Project 1 Fall 2006
//
// Base class for the solver.
// Implements non-abstract functions.

#include "Solver.h"
#include "Node.h"
#include <list>
#include <iostream>

using namespace std;

bool Solver::generalSearch() {

	bool result = false;
	Node n;
	Node m;
	list<Node> l;

	makeQueue();

	while(1) {

		numEvals++;

		if (queue.empty()) { 
			result = false; 
			break; 
		}

		printNodes();

		n = queue.front();
		queue.pop_front();

		if ( goalTest(n) ) {
			result = true;
			break;
		}

		l = expand(n);
		for ( list<Node>::iterator it = l.begin(); it != l.end(); it++ ) {
			
			m = *it;
			queuingFn(m);
		}
	}

	return result;
}

void Solver::printNodes() {

	cout << "[ ";
	for ( list<Node>::iterator it = queue.begin(); it != queue.end(); it++ ) {
			
		(*it).print();
	}
	cout << "]";

	cout << endl;
}
