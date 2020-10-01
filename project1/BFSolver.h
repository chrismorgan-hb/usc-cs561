// BFSolver.h
// Christopher Morgan
// 7468-2726-35
// CSCI 561 Project 1 Fall 2006
//
// Breadth-First Solver.
// Inherits from Solver.

#include "Solver.h"
#include "Node.h"
#include <list>

#ifndef BFSOLVER_H
#define BFSOLVER_H

class BFSolver : public Solver {

		public:

			BFSolver() {}
			BFSolver(Problem p) : Solver(p) {}
			void makeQueue();
			void queuingFn(Node n);
			bool goalTest(Node n);
			list<Node> expand(Node n);
};

#endif
