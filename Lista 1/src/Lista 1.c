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
void algorytm(int n, int m, char *T, char *pattern) {
	int i = 0, j = 0;
	for (i = 0, j = 0; i < n; i++, j++) {
		if (j == m)
			printf("\nZnaleziono wzorzec %d", i - m + 1);

		while ((j >= 0) && (T[i] != pattern[j]))
			j = temp[j];

	}
	if (j == m)
		printf("\nZnaleziono wzorzec %d", i - m + 1);
	else
		printf("\nzle slowo");
}

void przejscia(int n, char *pattern) {
	int i = 0, j = -1;
	temp[0] = -1;
	while(i < n){
		while(j >=0){
			if(pattern[i] != pattern[j])
				j = temp[j];
			else
				break;
		}
		temp[++i] = ++j;
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
	printf("%d   %d\n", m, n);
	temp = calloc(n + 1, sizeof(int));
	przejscia(n, pattern);
	algorytm(m, n, T, pattern);
	return EXIT_SUCCESS;
}
