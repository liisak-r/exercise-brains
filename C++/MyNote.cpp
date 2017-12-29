#include "MyNote.h"
#include <iostream>
#include <fstream>
#include <regex>
#include <string>

using std::exception;
using std::iostream;
using std::ifstream;
using std::ios; 
using std::ios_base;

using std::ofstream;
using std::string;
using std::basic_string;
using std::regex;
using std::cout;

MyNote::MyNote()
{
	oname = std::string("");
	id = -1;
	buffer = string("");
	token = string("");
}

MyNote::MyNote(std::string fromfile):oname(fromfile),id(0)
{
	buffer = string("");
}


MyNote::~MyNote()
{
}

int MyNote::read()
{
	ifstream inputfile;
	int counter = 0;

	try {
		if (id < 0)
			return -1;
	else if (oname.compare("")==0) {
		inputfile.open("json1.json");
	}
	else inputfile.open(oname);
	}
	catch (ios::ios_base::failure ex)
	{
		std::cout << "FAILURE" << std::endl;
	}
	catch (exception ex)
	{
		std::cout << "exception: " << ex.what();
	}

	if (inputfile.is_open())
	{
		while (!inputfile.eof())
		{
			std::string temp;
			inputfile >> temp;
			buffer.append(temp).append("\n");
			counter++;
		}
		inputfile.close();
	}
	
	std::cout << "counter: " << counter << std::endl;
	return 0;
}

int MyNote::parse()
{
	index = 0;
	/*basic_string <char>::size_type first, last;*/
	const char *tokenstring = buffer.c_str();
	string elem=string("");
	regex::flag_type flag = regex::extended;
	nextElem= CstringIter(tokenstring, tokenstring+buffer.length(), MyNote::spaces_separator,flag);
	/*first = 0; last = buffer.length();*/

	if (parse_object(elem)== true)
	{
		current = OBJECTD;
		return 0;
	}
	else {
		current = UNKNOWN;
		return -1;
	}
}

int MyNote::write()
{
	if (buffer.empty())
	{
		std::cout << "buffer empty" << std::endl;
	}
	else {
		ofstream outputfile;
		outputfile.open("kopioko.txt");
		outputfile << buffer;
		outputfile.close();
	}
	return 0;
}


bool MyNote::parse_object(string &elem)
{
	static const basic_string <char>::size_type npos = -1;
	
	const int limit = this->buffer.length();
	bool OK = true;
	/*basic_string <char>::size_type*/

	char startChar, lastElemChar;
/*	atElem = this->buffer.find_first_of("{", first,last);
	closingElem =this->buffer.find_last_of("}",first, last);
	*/
	
	MyNote::JSONElem lookahead;
	lookahead= next_token(startChar,lastElemChar,elem);
	if (lastElemChar != -1 &&OK==true)
	{
		if (startChar == '{' )
		{
			if (lookahead==JSONElem::SEPARATOR)
				lookahead = next_token(startChar, lastElemChar, elem);
			if (elem != "" )
				OK = parse_list(elem);
		    if (elem.back()=='}')
					return OK;
			else return false;
		}
		else if (startChar == '[')
		{
			if (lookahead == JSONElem::SEPARATOR)
				lookahead = next_token(startChar, lastElemChar, elem);
			
			OK = parse_array(elem);
			if (elem.back() == ']') return OK;
			else return false;
		}
		else if (lookahead == MyNote::NAME)
		{
			OK = parse_pair(elem);
		}

	    else /*empty object*/
		{
				OK = true;

		}

	}
	return OK;
}

bool MyNote::parse_list(string &elem)
{
	char separator=',';
	bool ok=parse_pair(elem);
	if (ok)
	separator = elem.back();
	while (separator == ',')
	{
		parse_pair(elem);
	}
	return false;
}

bool MyNote::parse_pair(string &elem)
{
	bool ok;
	char startChar, lastElemChar;
	/*	atElem = this->buffer.find_first_of("{", first,last);
	closingElem =this->buffer.find_last_of("}",first, last);
	*/
	string firsttoken;
	MyNote::JSONElem lookahead;
	if (current == JSONElem::NAME)
	{

		lookahead = next_token(startChar, lastElemChar, elem);
		if (lookahead == JSONElem::SEPARATOR && startChar == ':')
			lookahead = next_token( startChar, lastElemChar, elem);
		if (startChar == '{' || startChar == ']')
		{
			ok=parse_object(elem);
			cout << "pair name: object; " << firsttoken << ":" << elem << std::endl;
		}
		else {
			cout << "pair name: value; " << firsttoken << ":" << elem << std::endl;
			ok = true;
			lookahead = next_token(startChar, lastElemChar, elem);
		}
		
	}

	
	return ok;
}

bool MyNote::parse_array(string &elem)
{
	bool ok = true;
	char separator = ' ';
	parse_object(elem);
	separator = elem.back();
	while (ok && separator == ',')
	{
		ok=parse_object(elem);
		separator = elem.back();

	}
	return ok;
}

MyNote::JSONElem MyNote::next_token(char & startingChar, char & closingChar, string &elem)
{
	char atIt[2] = {'\0','\0'};


	CstringIter end;
	elem = "";
	if (token == "") {
		nextElem++;
		if (nextElem == end) {
			closingChar = -1;
			startingChar = -1;
			return JSONElem::UNKNOWN;
		}
		startingChar = nextElem->str().front();
		closingChar = nextElem->str().back();
		token = nextElem->str();
		it = token.begin();
	}
	std::cout << "LOOKAHEAD: " << token << std::endl;
	while (it !=token.end())
	{
		if (*it == '[' || *it == '{' || *it == ',' || *it == ']' || *it == '}' || *it==':') {
			current = JSONElem::SEPARATOR;
			startingChar = *it;
			closingChar = *it;
			atIt[0]=*it;
			elem.append(atIt);
			it++;
		}
		/*restriction*/
		else if (*it == '"') {
			startingChar = *it;
			while (it != token.end() && *it != '"')
			{
				atIt[0]=*it;
				elem.append(atIt);
				it++;
			}
			if (it != token.end() && *it == '"') {
				atIt[0]=*it;
				elem.append(atIt);
				closingChar = *it;
				it++;

			}
			if (it == token.end()) token = "";
			return JSONElem::NAME;
		}
		else {

			while (it != token.end() && ! (*it == '[' || *it == '{' || *it == ',' || *it == ']' || *it == '}' || *it == ':'))
			{
				atIt[0] = *it;
				elem.append(atIt);
				closingChar = *it;
				it++;
			}
			
			  return JSONElem::VALUE;
		}
	}
}

CstringIter::regex_type MyNote::spaces_separator = CstringIter::regex_type("([^[:blank:] | |[^ [:cntrl:] ])");
