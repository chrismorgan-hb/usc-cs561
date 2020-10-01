// Solver.h
// Christopher Morgan
// 7468-2726-35
// CSCI 561 Project 1 Fall 2006
//
// Base class for the solver.
// Abstract except for GeneralSearch
// Parent class for all 3 classes of
// search.

#include "Problem.h"
#include <list>
#include "Node.h"

using namespace std;

#ifndef SOLVER_H
#define SOLVER_H

class Solver {

		protected: 
			Problem problem;
			list<Node> queue;
			int numEvals;
			Node solution;

		public:

			Solver() { numEvals = 0; }
			Solver(Problem p) { problem = p; numEvals = 0; }
			void setProblem (Problem p) { problem = p; }
			virtual void makeQueue() = 0;
			virtual bool generalSearch();
			void printNodes();
			virtual void queuingFn(Node n) { ; }
			virtual bool goalTest(Node n) { return true; }
			virtual list<Node> expand(Node n) { ; } 
			int getNumEvals() { return numEvals; }
			list<Node> getQueue() { return queue; }
			Node getSolution() { return solution; }
};

#endif
