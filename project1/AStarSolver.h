// AStarSolver.h
// Christopher Morgan
// 7468-2726-35
// CSCI 561 Project 1 Fall 2006
//
// A* Solver.
// Inherits from Solver.

#include "Solver.h"
#include "Node.h"
#include "AStarNode.h"
#include <list>

#ifndef ASTARSOLVER_H
#define ASTARSOLVER_H

class AStarSolver : public Solver {


		private:

			list<AStarNode> queue;

		public:

			AStarSolver() {}
			AStarSolver(Problem p) : Solver(p) {}
			void makeQueue();
			void queuingFn(AStarNode n);
			bool goalTest(AStarNode n);
			list<AStarNode> expand(AStarNode n);
			bool generalSearch();
			void printNodes();
};

#endif
