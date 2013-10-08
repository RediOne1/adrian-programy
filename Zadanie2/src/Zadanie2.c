#include <stdio.h>
#include <stdlib.h>		//atoi()
int reszta_z_dzielenia(int x){
	int pomocnicza = x>>1;
	pomocnicza = pomocnicza << 1;
	return (x - pomocnicza);
}
int main(int argc, char *argv[]){
	char *liczba = argv[1];
	int reszta[32] = {0}, index =0;
	int x = atoi(liczba);
	while(x != 0){
		reszta[index] = reszta_z_dzielenia(x);
		printf("%5d : 2 = %5d reszta %d\n", x, x>>1, reszta[index]);
		x = x>>1;
		index++;
	}

	printf("\n\nWynik: ");
	for(int i=index-1;i>=0;i--)
		printf("%d", reszta[i]);
}
