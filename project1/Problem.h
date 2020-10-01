// Problem.h
// Christopher Morgan
// 7468-2726-35
// CSCI 561 Project 1 Fall 2006
//
// Base class for the search problem.
// Contains a data structure for the map &
// heuristic (A*) if necessary.
// Also contains start and stop cities
// (B is default stop city to help A*).

#include "Triplet.h"
#include "Pair.h"
#include <map>
#include <list>

using namespace std;

#ifndef PROBLEM_H
#define PROBLEM_H

class Problem {

		private:
			char start;
			char goal;
			map<char, list<Pair> > myMap;
			map<char, int> myHeuristic;

		public:
			Problem() {}
			Problem( Triplet theMap[], int mapSize, Pair heuristic[], int heuristicSize, char start, char goal = 'B' );
			char getStart() { return start; }
			char getGoal() { return goal; }
			void setGoal(char g) { goal = g; } // used by A*
			map<char, list<Pair> > getMap() { return myMap; }
			map<char, int> getHeuristic() { return myHeuristic; }
			void print(); // debug only
};

#endif
