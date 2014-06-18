#include <stdio.h>
#include <stdlib.h>
int main(void) {
	int ile;
	int i, j, l;
	int **tab;
	int **monety;
	scanf("%d", &ile);
	monety = calloc(2, sizeof(int*));
	monety[0] = calloc(ile, sizeof(int));
	monety[1] = calloc(ile, sizeof(int));
	for (i = 0; i < ile; i++) {
		int m, l;
		scanf("%d %d", &m, &l);
		monety[0][i] = m;
		monety[1][i] = l;
	}
	//for(int i=0; i<ilosc_monet; i++)
	//printf("%d %d\n", monety[0][i], monety[1][i]);
	int kwota;
	scanf("%d", &kwota);
	kwota++;
	tab = malloc((ile + 1) * sizeof(int*));
	for (i = 0; i <= ile; i++)
		tab[i] = malloc(kwota * sizeof(int));

	tab[0][0] = 0;
	for (i = 0; i < ile; i++)
		tab[i + 1][0] = monety[1][i];
	for (i = 1; i < kwota; i++) {
		tab[0][i] = -1;
	}
	//for(int i=0; i<kwota; i++)
	//printf("%d %d\n", t[0][i], t[1][i]);
	for (i = 1; i < kwota; i++) {
		int min = -1;
		int ilosc = 0;
		int poz = 0;
		for (j = 0; j < ile; j++) {
			int a = monety[0][j];
			if ((i - a >= 0) && (tab[j + 1][i - a] > 0)
					&& (tab[0][i - a] > -1)) {
				if (min > -1) {
					if (tab[0][i - a] < min) {
						min = tab[0][i - a];
						ilosc = a;
						poz = j;
					}
				} else {
					min = tab[0][i - a];
					ilosc = a;
					poz = j;
				}
			}
		}
		min++;
		poz++;
		//printf("%d min\n", min);
		if (min > 0) {
			if (tab[0][i] > -1) {
				if (tab[0][i] > min) {
					tab[0][i] = min;
					for (l = 1; l <= ile; l++) {
						if (poz == l)
							tab[l][i] = tab[l][i - ilosc] - 1;
						else
							tab[l][i] = tab[l][i - ilosc];
					}
				}
			} else {
				tab[0][i] = min;
				for (l = 1; l <= ile; l++) {
					if (poz == l)
						tab[l][i] = tab[l][i - ilosc] - 1;
					else
						tab[l][i] = tab[l][i - ilosc];
				}
			}
		}
	}
	//printf("%d", wydanie);
	if (tab[0][kwota - 1] > -1)
		printf("%d\n", tab[0][kwota - 1]);
	else
		printf("NIE\n");
	//for(int i=0; i<kwota; i++)
	//printf("%d\n", t[0][i]);
	for (i = 0; i <= ile; i++)
		free(tab[i]);
	free(tab);
	free(monety[0]);
	free(monety[1]);
	free(monety);
	return 0;
}
