/*
 * Zadanie1.c
 *
 *  Created on: 07-10-2013
 *      Author: Adrian
 */
#include <stdio.h>
#include <stdlib.h>		//atoi()
#include <ctype.h>		//isdigit()
#include <math.h>
#include <string.h>
int main(int argc, char *argv[]){
	char *znaki[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a",
				"b", "c", "d", "e", "f" };
	if(argc != 4){
	printf("Podano zla ilosc argumentow");
	return 0;
	}
  char *liczba = argv[1];
  char *podstawa_wejsciowa = argv[2];
  char *podstawa_wyjsciowa = argv[3];
  if(!isdigit(*podstawa_wejsciowa)){
	printf("Podano zla podstawe wejsciowa");
	return 0;
  }

  int podst_wejsc = atoi(podstawa_wejsciowa);

  if(!isdigit(*podstawa_wyjsciowa)){
	printf("Podano zla podstawe wyjsciowa");
	return 0;
  }

  int podst_wyjsc = atoi(podstawa_wyjsciowa);
  int i=0;
  bool pomocnicza;
  while(liczba[i] != '\0'){
  	pomocnicza = false;
  	for(int j = 0; j < podst_wejsc; j++){
  		if(liczba[i] == *znaki[j])
  		pomocnicza = true;
  	}
  	if(!pomocnicza){
  		printf("Podana liczba ma inna podstawe");
		return 0;
  	}
	i++;
  }
  int dziesietny;			//liczba zapisana w systemie dziesietnym
  if(podst_wejsc == 10)
  	dziesietny = atoi(liczba);
  else if(podst_wejsc >=2 && podst_wejsc <= 16){
  	dziesietny = 0;
	for(int x = 0; x < i; x++){
	  	for(int j = 0; j < 16; j++){
	  		if(liczba[x] == *znaki[j])
	  		dziesietny += j * pow(podst_wejsc, i - x - 1);
	  	}
  	}
  }
   int reszta[20]={0};
   int m = dziesietny;
   int x = podst_wyjsc;
   i = 0;
   	while(m != 0){
   		reszta[i] = m%x;
   		m/=x;
   		i++;
   	}
	//printf("%d", i);
	for(int j=i-1;j>=0;j--){
		printf("%c", *znaki[reszta[j]]);
	}
return 0;
}


