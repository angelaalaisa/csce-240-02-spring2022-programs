#include <iostream>
#include <string>
#include <regex>
#include <chrono>
#include <ctime>
using namespace std;

void combination(int num1, int num2){

    int fact1 = 1;      // factorial for n
    int fact2 = 1;      // factorial for r
    int fact3 = 1;      // factorial for (n-r)
    int i;
    int j;
    int k;

    for(i=1; i<=num1; i++){
        fact1 *= i;
    }
    for(j=1; j<=num2; j++){
        fact2 *= j;
    }
    for(k=1; k<=(num1-num2); k++){
        fact3 *= k;
    }

    int comb = fact1/(fact2*fact3);     //combination

    cout << "The combination of " << num1 << " and " << num2 << " is: " << comb;

}

void factorial(int num){

    int i;
    int fact=1;
    for(i=1; i<=num; i++){
        fact *= i;
    }

    cout << "\nThe factorial of " << num << " is: " << fact;
}

void getFunction(string input){ 
    
    regex regex1("([0-9])\\s([0-9])");   // input has two numbers
    regex regex2("([0-9])");            // input has one number
    smatch match;
    
    if(regex_match(input, match, regex1)) {
        
        int num1 = stoi(match[1]);      //convert input from string to int
        int num2 = stoi(match[2]);
        
        if(num1 >= 0 && num2 >= 0 && num1 > num2){        // error-checking for negative values
            
            combination(num1, num2);

        }
        else{
            
            cout << "First number must be larger than second number for combination";

        }

    } else if(regex_match(input, match, regex2)) {

        int num = stoi(match.str(1));

        if(num > 0){

            factorial(num);

        }
        else{
            
            cout << "Please enter a POSITIVE integer";

        }
    } else {
        
        cout << "invalid input";
    }

}

main() {
    
    string input;
    cout << "Please enter: One integer - factorial or two integers - combination\n";
    getline (cin, input);

    auto start = std::chrono::steady_clock::now();
    getFunction(input);
    auto end = std::chrono::steady_clock::now();

    cout << "\nTime for processing: " << std::chrono::duration_cast<std::chrono::nanoseconds> (end - start).count() << " nanoseconds\n";

}
