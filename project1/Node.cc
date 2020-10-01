// Node.cpp
// Christopher Morgan
// 7468-2726-35
// CSCI 561 Project 1 Fall 2006
//
// Implements Node class

#include "Node.h"
#include <list>
#include <iostream>

using namespace std;

Node::Node(list<char> l, int dist) {

	path = l;
	currentDistance = dist;
}

void Node::print() {

	cout << "(" << currentDistance << ", (";

	list<char>::iterator it = path.begin();

	bool first = true;
	for ( ; it != path.end(); it++ ) {

		if (!first) {
			cout << " ";
		}
		cout << *it;
		first = false;
	}

	cout << ")) ";
}

void Node::addToPath(char c) {

	path.push_back(c);
}

bool Node::cityInPath(char c) {

	bool exists = false;
	
	list<char>::const_iterator lit = path.begin();
	for ( ; lit != path.end(); lit++ ) {
		if ( *lit == c ) {
			exists = true;
			break;
		}
	}

	return exists;
};
