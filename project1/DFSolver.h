// DFSolver.h
// Christopher Morgan
// 7468-2726-35
// CSCI 561 Project 1 Fall 2006
//
// Depth-First Solver.
// Inherits from Solver.

#include "Solver.h"
#include "Node.h"
#include <list>

#ifndef DFSOLVER_H
#define DFSOLVER_H

class DFSolver : public Solver {

		public:

			DFSolver() {}
			DFSolver(Problem p) : Solver(p) {}
			void makeQueue();
			void queuingFn(Node n);
			bool goalTest(Node n);
			list<Node> expand(Node n);
};

#endif
