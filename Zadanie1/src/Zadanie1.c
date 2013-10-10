#include<stdio.h>
#include<stdlib.h>
#include<string.h>
int reszta_z_dzielenia(int x) {
	int pomocnicza = x >> 1;
	pomocnicza = pomocnicza << 1;
	return (x - pomocnicza);
}
int zamien_na_binarny(int argument) {
	int reszta[32] = { 0 }, index = 0;
	int x = argument;
	while (x != 0) {
		reszta[index] = reszta_z_dzielenia(x);
		x = x >> 1;
		index++;
	}
	int wynik = 0;
	for (int i = index - 1; i >= 0; i--) {
		wynik += reszta[i];
		wynik *= 10;
	}
	wynik /= 10;
	return wynik;

}
int zamien_dziesietne_na_binarny(int argument) {
	int ilosc_cyfr = 0;
	int x = argument;
	bool infinity = false;
	int reszta[32] = { 0 };
	int koncowki[32] = { 0 };
	int dzielnik = 1;
	while (x >= 1) {
		ilosc_cyfr++;
		x /= 10;
	}
	int j = 0;
	for (int i = 0; i < ilosc_cyfr; i++)
		dzielnik *= 10;
	while (argument != 0) {
		argument *= 2;
		for (int index = 0; index < j - 1; index++) {
			if (koncowki[index] == argument) {
				for (int i = 0; i < j - 1; i++) {
					if (i == index - 1)
						printf("(%d", reszta[i]);
					else
						printf("%d", reszta[i]);
				}
				printf(")");
				infinity = true;
				argument = 0;
				break;
			}
		}
		if ((argument / dzielnik) > 0) {
			reszta[j] = 1;
		} else
			reszta[j] = 0;
		argument %= dzielnik;
		koncowki[j] = argument;
		j++;
	}
	int wynik = 0;
	if (!infinity) {
		for (int i = 0; i < j; i++) {
			printf("%d", reszta[i]);
			wynik += reszta[i];
			wynik *= 10;
		}
	} else {
		for (int i = 0; i < j - 1; i++) {
			wynik += reszta[i];
			wynik *= 10;
		}
	}
	wynik /= 10;
	return wynik;
}
int main(int argc, char *argv[]) {
	if (argv[1] == NULL) {
		printf("Nie podano zadnego argumentu");
		return 0;
	}
	char *argument = argv[1];
	size_t len = strlen(argument);
	int czesc = 1;
	int x = 0, y = 0;
	for (int i = 0; i < len; i++) {
		if (argument[i] == '.') {
			czesc = 2;
			continue;
		}
		if (czesc == 1)
			x++;
		else
			y++;
	}
	char calkowite[x];
	char dziesietne[y];
	for (int i = 0; i < x; i++) {
		calkowite[i] = argument[i];
	}
	for (int i = 0; i < y; i++) {
		dziesietne[i] = argument[i + x + 1];
	}
	int LiczbaCalkowite = atoi(calkowite);
	int LiczbaDziesietne = atoi(dziesietne);
	printf("%d.%d = ", LiczbaCalkowite, LiczbaDziesietne);
	printf("%d.", zamien_na_binarny(LiczbaCalkowite));
	printf("\n\n\n%d", zamien_dziesietne_na_binarny(LiczbaDziesietne));
	return 0;
}
