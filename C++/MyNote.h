#pragma once
#include <string>
#include <regex>
using std::basic_string;
using std::regex;
using std::regex_token_iterator; 
using std::string; 

typedef std::regex_token_iterator<const char *> CstringIter;

class MyNote
{
public:
	MyNote();
	MyNote(std::string fromfile);
	virtual ~MyNote();
	int read();
	int parse();
	int write();

private:
	std::string oname;
	long id;
	std::string buffer;
	int index;
	enum JSONElem:int { UNKNOWN, SEPARATOR, NAME, VALUE, PAIR, OBJECTD, LIST , ARRAYOFOBJD};
	bool parse_object( std::string &elem );
	bool parse_list(std::string &elem );
	bool parse_pair(std::string &elem);
	bool parse_array(std::string &elem);
	JSONElem next_token(char&startingChar,char& closingChar, std::string & elem);
	JSONElem current;
	std::string token;
	basic_string <char>::iterator it;
	CstringIter nextElem; 
	static  CstringIter::regex_type spaces_separator;
	
};

