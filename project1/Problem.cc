// Problem.cc
// Christopher Morgan
// 7468-2726-35
// CSCI 561 Project 1 Fall 2006
//
// Implements the search problem.

#include "Problem.h"
#include "Triplet.h"
#include "Pair.h"
#include <map>
#include <iostream>

using namespace std;

void Problem::print() {

	cout << "Start City: " << start << endl;
	cout << "Goal City: " << goal << endl;
/*	
	map<char, list<Pair> >::iterator it = myMap.begin();
	list<Pair>::iterator lit;

	for ( it = myMap.begin(); it != myMap.end(); it++ ) {

		cout << "City " << (*it).first << ":" << endl;
		
		lit = ((*it).second).begin();

		for ( ; lit != ((*it).second).end(); lit++ ) {

			cout << (*lit).city << ", " << (*lit).dist << "; ";
		}

		cout << endl;
	}
*/
}

Problem::Problem ( Triplet theMap [], int mapSize, Pair heuristic [], int heuristicSize, char theStart, char theGoal ) {

	char city1;
	char city2;
	int distance;

	list <Pair> tempList;
	Pair tempPair ('?', 0);
	map<char, list<Pair> >::iterator it;

	start = theStart;
	goal = theGoal;
	int i = 0;

	if (theMap) {
		for ( i = 0; i < mapSize; i++ ) {

			tempList.clear();
			
			// if we don't already have a list started for C1,
			// start one
			if ( myMap.find( theMap[i].C1 ) == myMap.end() ) {
				
				tempPair.city = theMap[i].C2;
				tempPair.dist = theMap[i].dist;
				tempList.push_back(tempPair);
				myMap[theMap[i].C1] = tempList;
			}

			// if there is a list for C1, add to it

			else {
				
				it = myMap.find( theMap[i].C1 );
				tempPair.city = theMap[i].C2;
				tempPair.dist = theMap[i].dist;

				((*it).second).push_back(tempPair);
			}

			tempList.clear();

			// ditto above for C2
			if ( myMap.find( theMap[i].C2 ) == myMap.end() ) {
				
				tempPair.city = theMap[i].C1;
				tempPair.dist = theMap[i].dist;
				tempList.push_back(tempPair);
				myMap[theMap[i].C2] = tempList;
			}

			else {
				
				it = myMap.find( theMap[i].C2 );
				tempPair.city = theMap[i].C1;
				tempPair.dist = theMap[i].dist;

				((*it).second).push_back(tempPair);
			}
		}
	}

	if (heuristic) {

		for ( i = 0; i < heuristicSize; i++ ) {
			myHeuristic[heuristic[i].city] = heuristic[i].dist;
		}
	}
}
