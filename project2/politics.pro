/* Christopher Morgan
 * CSCI 561 Project 2 - Prolog
 * I was only able to get the world defined.
 * I did not have time to do queries in Prolog
 * since I spent a majority of the time on 
 * the Powerloom part of the project. 
 */
hasRules(myAna,myNoRules).
noRules(myNoRules).

state(X) :-
	hasP(X,_);
	hasB(X,_);
	sovereignState(X).
people(Y) :-
	hasP(_,Y).
boundary(Y) :-
	hasB(_,Y).

sovereignState(X) :-
	hasS(X,_);
	nationState(X).

sovereignty(Y) :-
	hasS(_,Y);
	via(Y,_);
	politicalSovereignty(Y);
	economicSovereignty(Y).

system(X) :-
	via(_,X);
	politicalSystem(X);
	religiousSystem(X);
	economicSystem(X).

politicalSystem(Y) :-
	of(Y,_);
	by(Y,_);
	for(Y,_);
	hasRules(Y,_);
	anarchy(Y);
	democracy(Y);
	oligarchy(Y).
	
nationState(X) :-
	hasS(X,Y),
	politicalSovereignty(Y).

politicalSovereignty(Y) :-
	hasS(X,Y),
	nationState(X).

anarchy(X) :-
	hasRules(X,Y),
	noRules(Y).

democracy(X) :-
	of(X,Y),
	by(X,Y),
	for(X,Y),
	people(Y).

democracy(X) :-
	constitutionalDemocracy(X).

constitutionalDemocracy(X) :-
	hasRules(X,Y),
	law(Y).

oligarchy(X) :-
	family(X);
	monarchy(X);
	theocracy(X);
	totalitarian(X);
	goodCommunism(X).

oligarchy(X) :-
	of(X,Y),
	by(X,Y),
	smallGroup(Y).

smallGroup(X) :-
	parents(X);
	royalFamily(X);
	religiousLeaders(X).

monarchy(X) :-
	of(X,Y),
	by(X,Y),
	royalFamily(Y).

theocracy(X) :-
	of(X,Y),
	by(X,Y),
	for(X,Z),
	religiousLeaders(Y),
	people(Z).

family(X) :-
	of(X,Y),
	by(X,Y),
	for(X,Z),
	parents(Y),
	familyMembers(Y).

economicSystem(X) :-
	implements(X,_);
	freeMarket(X);
	socialism(X).

freeMarket(X) :-
	implements(X,Y),
	capitalism(Y).

socialism(X) :-
	implements(X,Y),
	marxism(Y).

socialism(X) :-
	goodCommunism(X);
	badCommunism(X).

hybridEconomy(X) :-
	freeMarket(X),
	socialism(X).

totalitarian(X) :-
	dictatorship(X);
	badCommunism(X).

totalitarian(X) :-
	of(X,Y),
	for(X,Y),
	by(X,Y),
	politicalLeaders(Y).

dictatorship(X) :-
	of(X,Y),
	by(X,Y),
	for(X,Y),
	dictator(Y).

politicalLeaders(X) :-
	communistParty(X).

goodCommunism(X) :-
	of(X,Y),
	by(X,Y),
	for(X,Z),
	communistParty(Y),
	people(Z).

/* US is a nation state that has pol sov via const dem
 * and econ sov via free market
 * Recommended queries:
 * nationState(US). = Yes
 * freeMarket(USEcon). = Yes
 * constitutionalDemocracy(USsystem). = Yes 
 */

hasP(US,p1). 
hasS(US,USps).
politicalSovereignty(USps).

:- unknown(_,fail).
