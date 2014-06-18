/*
 ============================================================================
 Name        : Algorytmy4_inwersje.c
 Author      : Adrian Kuta
 Version     :
 Copyright   : Your copyright notice
 Description : Hello World in C, Ansi-style
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>

#include <stdio.h>

int szukaj(int l, int p, int szukana, int tab[]) {
	if (l > p)
		return -1;

	int sr = (l + p) / 2;

	if ((2 * szukana < tab[sr]) && (2 * szukana >= tab[sr - 1]))
		return sr;

	if (2 * szukana < tab[sr])
		return szukaj(l, sr - 1, szukana, tab); //przeszukujemy lewą część tablicy
	else
		return szukaj(sr + 1, p, szukana, tab); //przeszukujemy prawą część tablicy
}

int merge(int array[], int first, int last) {
	int mid = (first + last) / 2;
	int ai = first;
	int bi = mid + 1;
	int final[last - first + 1], finali = 0;
	int inversion = 0, i;
	while (ai <= mid && bi <= last) {
		if (array[ai] < array[bi]) {
			final[finali++] = array[ai++];
		} else {

			if (array[ai] > 2 * array[bi])
				inversion += mid - ai + 1;
			else {
				if (array[mid] > 2 * array[bi]) {
					int x = szukaj(ai, mid, array[bi], array);
					inversion += mid - x + 1;
				}
			}
			final[finali++] = array[bi++];
		}
	}
	while (ai <= mid)
		final[finali++] = array[ai++];
	while (bi <= last)
		final[finali++] = array[bi++];
	for (i = 0; i < last - first + 1; i++)
		array[i + first] = final[i];
	return inversion;
}

int mergesort(int array[], int a, int b) {
	int x, y, z, mid;
	if (a >= b)
		return 0;
	mid = (a + b) / 2;
	x = mergesort(array, a, mid);
	y = mergesort(array, mid + 1, b);
	z = merge(array, a, b);
	return x + y + z;
}

int main() {
	int i, l, n;
	scanf("%d", &n); // liczba elementow
	int a[n];
	for (i = 0; i < n; i++) {
		scanf("%d", &l);
		a[i] = l;
	}
	int inversion = mergesort(a, 0, n - 1);
	printf("%d \n", inversion);
	return 0;
}
