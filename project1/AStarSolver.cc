// AStarSolver.cpp
// Christopher Morgan
// 7468-2726-35
// CSCI 561 Project 1 Fall 2006
//
// A* Solver implementation.

#include "AStarSolver.h"
#include "Node.h"
#include "AStarNode.h"
#include "Pair.h"
#include <list>
#include <iostream>
#include <climits>

using namespace std;

bool AStarSolver::generalSearch() {

	bool result = false;
	AStarNode n;
	AStarNode m;
	list<AStarNode> l;

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
		for ( list<AStarNode>::iterator it = l.begin(); it != l.end(); it++ ) {
			
			m = *it;
			queuingFn(m);
		}
	}

	return result;
}

void AStarSolver::makeQueue() {

	list<char> l;
	l.push_front(problem.getStart());
	char start = problem.getStart();
	int h = problem.getHeuristic()[problem.getStart()];
	AStarNode s(l, 0, h); 

	queue.clear();
	queue.push_back(s);
}

void AStarSolver::queuingFn(AStarNode n) {

	queue.push_front(n);
	queue.sort();
}

list<AStarNode> AStarSolver::expand(AStarNode n) {

	list<AStarNode> expanded;
	list<Pair> newCities = problem.getMap()[n.getPath().back()];
	int newH = 0;

	for ( list<Pair>::iterator it = newCities.begin(); it != newCities.end(); it++ ) {
		
		AStarNode m(n.getPath(), n.getCurrentDistance(), 0);

		// only try if not already on list
		
		if (!m.cityInPath((*it).city)) {
			m.addToPath((*it).city);
			newH = problem.getHeuristic()[(*it).city];
			m.addToCurrentDistance((*it).dist, newH);		
			expanded.push_back(m);
		}
	}

	return expanded;
}

bool AStarSolver::goalTest(AStarNode n) {

	if ( n.getPath().back() == problem.getGoal() ) {
		solution = n;
		return true;
	}

	else {
		return false;
	}
}


void AStarSolver::printNodes() {

	cout << "[ ";
	for ( list<AStarNode>::iterator it = queue.begin(); it != queue.end(); it++ ) {
			
		(*it).print();
	}
	cout << "]";

	cout << endl;
}
