
#include <stdlib.h>
#include <iostream> 
#include <stdexcept>
#include "MyNote.h"
using namespace std;
using namespace System;
using namespace System::Threading;
using namespace System::Runtime;
using namespace System::Runtime::Serialization;




void luepa_tiedosto_ja_tulosta(std::string nimi)
{
	MyNote testi(nimi);
	testi.read();
	testi.parse();
	testi.write();
	Threading::Thread::Sleep(1000);
}
int main(int argc, char** args)
{
	string nimi;
	cout << "MITAPA LOMALAINEN TEKISI: anna tiedoston nimi!"<<endl;
	cin >> nimi;
	try {
	cout << " avataan " << nimi << endl;
		luepa_tiedosto_ja_tulosta(nimi);
	}

	catch (regex_error ex)
	{
		cout << "alustusvirhe: regex pakkaus!" << endl;
	}
	catch (std::exception e)
	{
		cout << "virhe: " << e.what() << " :type:" << typeid(e).name() << std::endl;
	}
	cout << "tiedosto kasitelty" << endl;
	Threading::Thread::Sleep(1000);
}