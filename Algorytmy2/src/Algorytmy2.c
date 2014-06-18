/*
 ============================================================================
 Name        : Algorytmy2.c
 Author      : 
 Version     :
 Copyright   : Your copyright notice
 Description : Hello World in C, Ansi-style
 ============================================================================
 */

#include <stdio.h>
int ile;
int min(int *minimalne) {
	int i = 0, j = 0, minimum;
	while (minimalne[j] == -1 && j < ile) {
		j++;
	}
	if (j == ile)
		return -1;
	minimum = minimalne[j];
	for (i = j; i < ile; i++) {
		if (minimalne[i] == -1)
			continue;
		if (minimalne[i] < minimum)
			minimum = minimalne[i];
	}
	if (minimum != -1)
		return minimum + 1;
	return minimum;
}
int main() {
	int *monety, *minimalne;
	int *tab;
	int kwota = 0;
	int i, j;
	scanf("%d", &ile);
	if (ile == 0) {
		printf("NIE");
		return 0;
	}
	monety = (int*) calloc(ile, sizeof(int));
	minimalne = (int*) calloc(ile, sizeof(int));
	for (i = 0; i < ile; i++) {
		scanf("%d", &monety[i]);
	}
	scanf("%d", &kwota);
	tab = (int*) calloc(kwota + 1, sizeof(int));
	tab[0] = 0;
	for (i = 1; i < kwota + 1; i++) {
		tab[i] = -1;
		for (j = 0; j < ile; j++) {
			minimalne[j] = -1;
			if ((i - monety[j] < 0) || (tab[i - monety[j]] == -1))
				continue;
			minimalne[j] = tab[i - monety[j]];
		}
		tab[i] = min(minimalne);
	}
	if (tab[kwota] == -1)
		printf("NIE\n");
	else
		printf("%d\n", tab[kwota]);
	free(minimalne);
	free(tab);
	free(monety);
	return 0;
}
