/**
 * The spec is from http://codingdojo.org/kata/DictionaryReplacer/

 * KataDictionaryReplacer

This kata is about making a simple string replacer. It is inspired by Corey Haines Lightning talk about practicing. (aac2009.confreaks.com/06-feb-2009-20-30-lightning-talk-under-your-fingers-corey-haines.html)

Create a method that takes a string and a dictionary, and replaces every key in the dictionary pre and suffixed with a dollar sign, with the corresponding value from the Dictionary.
 */
/* toteutus KATA-harjoitukseen by Liisa Räihä */
function dict_replacer(input, dict)
{
	var output="";
	var value="";
	var resultset=input.split('\$');
	if (input==='') return '';
	if (dict.empty) return input;
	var pre=false;
    console.log("input:"+input);
    console.log("dict:"+ dict);
	for (let key of resultset)
	{
	  if (key!= undefined && key != '') {
		  value=dict.get(key);
		  if (value != undefined)
		  	{  output=output+value; pre=true;}
		  else {
			  if (output=="")
			  {output=key;}
			  else { 
				  if (pre==false) {
				  output=output+'$'+key;
				  } else
					  {
					  output=output+key;
					  }}
			  pre=false;
			  }
	  }
	  else {
		  value=undefined;
		  if (pre==false && output != '') {
		  output=output+'\$';
		  }
	  }	 
	  console.log(output);
	  console.log("\n");
	}
	return output;
}
function test_1() {
	var dict=new Map();
	var input="";
	var output=dict_replacer(input, dict);
	if (output==="") return "SUCCESS";
	else return "FAIL";
};
function test_2()
{
	var input ="\$temp\$";
	var pair = ['temp', 'temporary'];
	var dict = new Map([ pair ]);
	var output=dict_replacer(input, dict);
	if (output== 'temporary') return "SUCCESS";
	else return "FAIL";
}
function test_3()
{
	var input ="\$temp\$ here comes the name \$name\$";
	var pair = ['temp', 'temporary'];
	var dict = new Map([ pair,['name','John Doe'] ]);
	var output=dict_replacer(input, dict);
	if (output== 'temporary here comes the name John Doe') return "SUCCESS";
	else return "FAIL";
}

function run()
{
	console.log("testiajot:"+new Date());
	console.log("testi 1:");console.log(test_1());
	/*document.getElementById("output1").innerHTML=test_1();*/
	console.log("testi 2:");console.log(test_2());
	/*document.getElementById("output2").innerHTML=test_2();*/
	console.log("testi 3:");console.log(test_3());
	/*document.getElementById("output3").innerHTML=test_3();*/	
}
run();