/*
 ============================================================================
 Name        : Algorytmy4_podciag.c
 Author      : Adrian Kuta
 Version     :
 Copyright   : Your copyright notice
 Description : Hello World in C, Ansi-style
 ============================================================================
 */

#include <string.h>
#include <stdio.h>
#include <stdlib.h>

int CeilIndex(int A[], int l, int r, int key) {
	int m;
  while( r - l > 1 ) {
		m = l + (r - l)/2;
    if(A[m]>=key)
			r=m;
		else
			l=m;
	}
  return r;
}

int length(int A[], int size) {
	int i;
  int *tailTable   = (int*)malloc(sizeof(int)*size);
  int len;
  memset(tailTable, 0, sizeof(tailTable[0])*size);
  tailTable[0] = A[0];
  len = 1;
  for(i = 1; i < size; i++ ) {
		if( A[i] < tailTable[0] )
			tailTable[0] = A[i];
      else if( A[i] > tailTable[len-1] )
				tailTable[len++] = A[i];
      else
				tailTable[CeilIndex(tailTable, -1, len-1, A[i])] = A[i];
  }
  free( tailTable);
  return len;
}

int main() {
	int n,i,t;
  scanf("%d",&n); // liczba elementow
  int b[n];
	for(i=0;i<n;i++){
		scanf("%d",&t);
		b[i]=t;
	}
  printf("%d\n",length(b, n)); // wynik
	return 0;
}
