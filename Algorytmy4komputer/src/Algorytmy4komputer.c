/*
 ============================================================================
 Name        : Algorytmy4komputer.c
 Author      : Adrian Kuta
 Version     :
 Copyright   : Your copyright notice
 Description : Hello World in C, Ansi-style
 ============================================================================
 */

#include <stdio.h>

int main(){
	int i,n,p,q,min;
	min=1000000;
	int result=0;
	scanf("%d",&n); // liczba elementow
	for(i=0;i<n;i++){
		scanf("%d %d",&p,&q);
		if(q<min)
			min=q;
		result +=p;
	}
	result +=min;
	printf("%d \n",result);
	return 0;
}
