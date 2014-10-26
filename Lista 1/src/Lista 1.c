/*
 ============================================================================
 Name        : Jezyki.c
 Author      : Adrian Kuta
 Description : Lista 1
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int *temp;
void algorytm(int n, int m){

}

void przejscia(int n, char *pattern) {
	int i = 0, j = -1;
	temp[0] = -1;
	for (i = 0, j = -1; i < n; j++, i++, temp[i] = j) {
		while (j >= 0 && (pattern[i] != pattern[j])) {
			j = temp[j];
		}
	}
	for (i = 0; i <= n; i++) {
		printf("%d ", temp[i]);
	}
}

int main(int argc, char **argv) {
	char *pattern = argv[1];
	char *T = argv[2];
	int m = strlen(T);
	int n = strlen(pattern);
	printf("%d\n", n);
	temp = calloc(n+1, sizeof(int));
	przejscia(n, pattern);
	return EXIT_SUCCESS;
}
