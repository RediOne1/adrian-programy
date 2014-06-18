#include <stdio.h>
#include <stdlib.h>
int **tab;
int **nominaly;

int main(void) {
	int ilosc_monet;
	int i;
	scanf("%d", &ilosc_monet);
	nominaly = malloc(2 * sizeof(int*));
	nominaly[0] = malloc(ilosc_monet * sizeof(int));
	nominaly[1] = malloc(ilosc_monet * sizeof(int));
	for (i = 0; i < ilosc_monet; i++) {
		int m, l;
		scanf("%d %d", &m, &l);
		nominaly[0][i] = m;
		nominaly[1][i] = l;
	}

	int kwota;
	scanf("%d", &kwota);
	kwota++;
	tab = malloc((ilosc_monet + 1) * sizeof(int*));
	for (i = 0; i <= ilosc_monet; i++)
		tab[i] = malloc(kwota * sizeof(int));

	int wydanie;
	wydanie = reszta(kwota, ilosc_monet);
	if (wydanie > -1)
		printf("%d\n", wydanie);
	else
		printf("NIE\n");
	for (i = 0; i <= ilosc_monet; i++)
		free(tab[i]);
	free(tab);
	free(nominaly[0]);
	free(nominaly[1]);
	free(nominaly);
	return 0;
}
int reszta(int kwota, int ilosc_monet) {
	tab[0][0] = 0;
	int i, j, l;
	for (i = 0; i < ilosc_monet; i++)
		tab[i + 1][0] = nominaly[1][i];
	for (i = 1; i < kwota; i++) {
		tab[0][i] = -1;
	}
	//for(int i=0; i<kwota; i++)
	//printf("%d %d\n", t[0][i], t[1][i]);
	for (i = 1; i < kwota; i++) {
		int min = -1;
		int temp2 = 0;
		int poz = 0;
		for (j = 0; j < ilosc_monet; j++) {
			int temp = nominaly[0][j];
			if (i - temp >= 0) {
				if (tab[j + 1][i - temp] > 0) {
					if (tab[0][i - temp] > -1) {
						if (min > -1) {
							if (tab[0][i - temp] < min) {
								min = tab[0][i - temp];
								temp2 = temp;
								poz = j;
							}
						} else {
							min = tab[0][i - temp];
							temp2 = temp;
							poz = j;
						}
					}
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
					for (l = 1; l <= ilosc_monet; l++) {
						if (poz == l)
							tab[l][i] = tab[l][i - temp2] - 1;
						else
							tab[l][i] = tab[l][i - temp2];
					}
				}
			} else {
				tab[0][i] = min;
				for (l = 1; l <= ilosc_monet; l++) {
					if (poz == l)
						tab[l][i] = tab[l][i - temp2] - 1;
					else
						tab[l][i] = tab[l][i - temp2];
				}
			}
		}
	}
	return tab[0][kwota - 1];
}
