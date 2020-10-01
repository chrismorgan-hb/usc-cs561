// project1.cpp
// Christopher Morgan
// 7468-2726-35
// CSCI 561 Project 1 Fall 2006
//
// Main source file for Project 1
//

#include "Problem.h"
#include "Pair.h"
#include "Triplet.h"
#include "Solver.h"
#include "BFSolver.h"
#include "DFSolver.h"
#include "AStarSolver.h"
#include <map>
#include <string>
#include <fstream>
#include <iostream>

#define MAX_CITIES 52

using namespace std;

map<string,string> handleArgs (int i, char** argList);
int buildMapArr(Triplet* arr, string mapFile);
int buildHeuristicArr(Pair* arr, string heuristicFile);

int main (int argc, char** argv) {

	map<string, string> argMap;

	Triplet mapArr[MAX_CITIES];
	Pair heuristicArr[MAX_CITIES];

	Triplet t('A', 'B', 100);
	Triplet t1 ('B', 'C', 150);

	Triplet ta[2];
	ta[0] = t;
	ta[1] = t1;

	argMap = handleArgs(argc, argv);

	char start;
	char goal;

	int mapSize;
	int hSize;

	start = (argMap["startCity"].c_str())[0];
	goal = (argMap["goalCity"].c_str())[0];

	mapSize = buildMapArr(mapArr, argMap["mapFile"]);
	hSize = buildHeuristicArr(heuristicArr, argMap["heuristicFile"]);

	Problem p(mapArr, mapSize, heuristicArr, hSize, start, goal);
	
	BFSolver bf(p);
	DFSolver df(p);

	if (p.getGoal() != 'B') {

		cout << "A* Search will use B as the goal city instead of " << p.getGoal() << endl;
	}

	char origGoal = p.getGoal();
	
	p.setGoal('B');
	AStarSolver as(p);

	p.setGoal(origGoal);

	cout << "================================" << endl;
	cout << "Breadth-First Search: " << endl;
	bool bfSuccess = bf.generalSearch();
	cout << endl;
	cout << "================================" << endl;
	cout << "Depth-First Search: " << endl;
	bool dfSuccess = df.generalSearch();
	cout << endl;
	cout << "================================" << endl;
	cout << "A* Search: " << endl;
	bool asSuccess = as.generalSearch();
	cout << endl;

	cout << "***Summary***" << endl;
	cout << "--------------------------------" << endl;
	cout << "Problem: " << endl;
	p.print();
	cout << "================================" << endl;
	cout << "Breadth-First:" << endl;
	cout << "Number of nodes evaluated: " << bf.getNumEvals() << endl;
	if (bfSuccess) {
		cout << "Solution: "; 
		bf.getSolution().print();
	}
	else {
		cout << "Breadth-First has no solution.\n";
	}
	cout << endl;
	cout << "================================" << endl;
	cout << "Depth-First:" << endl;
	cout << "Number of nodes evaluated: " << df.getNumEvals() << endl;
	if (dfSuccess) {
		cout << "Solution: "; 
		df.getSolution().print();
	}
	else {
		cout << "Depth-First has no solution.\n";
	}	
	cout << endl;
	cout << "================================" << endl;
	cout << "A* (Goal City = B):" << endl;
	cout << "Number of nodes evaluated: " << as.getNumEvals() << endl;
	cout << "Solution: "; 
	as.getSolution().print();
	cout << endl;
	cout << "================================" << endl;
	cout << endl;

	return 0;
}

int buildMapArr(Triplet* arr, string mapFile) {

	int arrSize = 0;
	int d = 0;
	string c1 = "";
	string c2 = "";

	if ( mapFile.length() == 0 ) { return 0; }

	fstream inFile(mapFile.c_str());

	if (!inFile.is_open()) { cout << "Error opening file." << endl; exit(-1); }

	while ( !inFile.eof() ) {
		
		inFile >> d;
		inFile >> c1;
		inFile >> c2;
		
		Triplet t((char)c1[0], (char)c2[0], d);

		arr[arrSize++] = t;
	}

	return arrSize;
}

int buildHeuristicArr(Pair* arr, string heuristicFile) {

	int arrSize = 0;
	int d = 0;
	string c = "";

	if (heuristicFile.length() == 0) { return 0; }
	fstream inFile(heuristicFile.c_str());

	if (!inFile.is_open()) { cout << "Error opening file." << endl; exit(-1); }

	while ( !inFile.eof() ) {
		
		inFile >> c;
		inFile >> d;
		
		Pair p((char)c[0], d);

		arr[arrSize++] = p;
	}

	return arrSize;
}

map<string, string> handleArgs (int i, char** argList) {

	map <string, string> retMap;

	string arg = "";

	retMap["startCity"] = 'A';
	retMap["goalCity"] = 'B';
	retMap["mapFile"] = "map.txt";
	retMap["heuristicFile"] = "heuristic.txt";

	for ( int j = 0; j < i; j++ ) {

		arg = (string)argList[j];

		if ( (arg.substr(0,1)) == "-" ) {

			// this is an option tag
			if ( arg.substr(1) == "mapFile" ) {
				
				retMap["mapFile"] = (string)argList[++j];
			}

			else if ( arg.substr(1) == "heuristicFile" ) {
				
				retMap["heuristicFile"] = (string)argList[++j];
			}

			else if ( arg.substr(1) == "startCity" ) {
				
				retMap["startCity"] = (string)argList[++j];
			}

			else if ( arg.substr(1) == "goalCity" ) {

				retMap["goalCity"] = (string)argList[++j];
			}
		}
	}

	return retMap;
}

