/*
 ============================================================================
 Name        : Algorytmy3.c
 Author      : 
 Version     :
 Copyprawy   : your copyprawy notice
 Description : Hello World in C, Ansi-style
 ============================================================================
 */

#include <stdio.h>
int CZERWONY = 1;
int CZARNY = 0;

typedef struct node {
	int key;
	int kolor;
	struct node *lewy;
	struct node *prawy;
	struct node *rodzic;
} node;

node *korzen = NULL;
node S;
int bool = 1;

void rot_L(node* A) {
	node* B;
	node* p;
	B = A->prawy;
	if (B != &S) {
		p = A->rodzic;
		A->prawy = B->lewy;
		if (A->prawy != &S)
			A->prawy->rodzic = A;

		B->lewy = A;
		B->rodzic = p;
		A->rodzic = B;

		if (p != &S) {
			if (p->lewy == A)
				p->lewy = B;
			else
				p->prawy = B;
		} else
			korzen = B;
	}
}
void rot_R(node* A) {
	node* B;
	node* p;
	B = A->lewy;
	if (B != &S) {
		p = A->rodzic;
		A->lewy = B->prawy;
		if (A->lewy != &S)
			A->lewy->rodzic = A;

		B->prawy = A;
		B->rodzic = p;
		A->rodzic = B;

		if (p != &S) {
			if (p->lewy == A)
				p->lewy = B;
			else
				p->prawy = B;
		} else
			korzen = B;
	}
}
void insert(int k) {
	node * X = malloc(sizeof(node));
	node * Y;       // Tworzymy nowy węzeł
	X->lewy = &S;          // Inicjujemy pola
	X->prawy = &S;
	X->rodzic = korzen;
	X->key = k;
	if (X->rodzic == &S)
		korzen = X; // X staje się korzeniem
	else
		while (1)             // Szukamy liścia do zastąpienia przez X
		{
			if (k == X->rodzic->key)
				return;
			if (k < X->rodzic->key) {
				if (X->rodzic->lewy == &S) {
					X->rodzic->lewy = X;  // X zastępuje lewy liść
					break;
				}
				X->rodzic = X->rodzic->lewy;
			} else {
				if (X->rodzic->prawy == &S) {
					X->rodzic->prawy = X; // X zastępuje prawy liść
					break;
				}
				X->rodzic = X->rodzic->prawy;
			}
		}
	X->kolor = CZERWONY;         // Węzeł kolorujemy na czerwono
	while ((X != korzen) && (X->rodzic->kolor == CZERWONY)) {
		if (X->rodzic == X->rodzic->rodzic->lewy) {
			Y = X->rodzic->rodzic->prawy; // Y -> wujek X

			if (Y->kolor == CZERWONY)  // Przypadek 1
					{
				X->rodzic->kolor = CZARNY;
				Y->kolor = CZARNY;
				X->rodzic->rodzic->kolor = CZERWONY;
				X = X->rodzic->rodzic;
				continue;
			}

			if (X == X->rodzic->prawy) // Przypadek 2
					{
				X = X->rodzic;
				rot_L(X);
			}

			X->rodzic->kolor = CZARNY; // Przypadek 3
			X->rodzic->rodzic->kolor = CZERWONY;
			rot_R(X->rodzic->rodzic);
			break;
		} else {                  // Przypadki lustrzane
			Y = X->rodzic->rodzic->lewy;

			if (Y->kolor == CZERWONY) // Przypadek 1
					{
				X->rodzic->kolor = CZARNY;
				Y->kolor = CZARNY;
				X->rodzic->rodzic->kolor = CZERWONY;
				X = X->rodzic->rodzic;
				continue;
			}

			if (X == X->rodzic->lewy) // Przypadek 2
					{
				X = X->rodzic;
				rot_R(X);
			}

			X->rodzic->kolor = CZARNY; // Przypadek 3
			X->rodzic->rodzic->kolor = CZERWONY;
			rot_L(X->rodzic->rodzic);
			break;
		}
	}
	korzen->kolor = CZARNY;
}
node* max(node* p) {
	if (p != &S)
		while (p->prawy != &S)
			p = p->prawy;
	return p;
}
node* min(node* p) {
	if (p != &S)
		while (p->lewy != &S)
			p = p->lewy;
	return p;
}
node * nastepnik(node * p) {
	node * r;

	if (p != &S) {
		if (p->prawy != &S)
			return min(p->prawy);
		else {
			r = p->rodzic;
			while ((r != &S) && (p == r->prawy)) {
				p = r;
				r = r->rodzic;
			}
			return r;
		}
	}
	return &S;
}
void delete(node* X) {
	node * W, *Y, *Z;
	if ((X->lewy == &S) || (X->prawy == &S))
		Y = X;
	else
		Y = nastepnik(X);

	if (Y->lewy != &S)
		Z = Y->lewy;
	else
		Z = Y->prawy;

	Z->rodzic = Y->rodzic;

	if (Y->rodzic == &S)
		korzen = Z;
	else if (Y == Y->rodzic->lewy)
		Y->rodzic->lewy = Z;
	else
		Y->rodzic->prawy = Z;

	if (Y != X)
		X->key = Y->key;

	if (Y->kolor == CZARNY)   // Naprawa struktury drzewa czerwono-czarnego
		while ((Z != korzen) && (Z->kolor == CZARNY))
			if (Z == Z->rodzic->lewy) {
				W = Z->rodzic->prawy;

				if (W->kolor == CZERWONY) {              // Przypadek 1
					W->kolor = CZARNY;
					Z->rodzic->kolor = CZERWONY;
					rot_L(Z->rodzic);
					W = Z->rodzic->prawy;
				}

				if ((W->lewy->kolor == CZARNY) && (W->prawy->kolor == CZARNY)) { // Przypadek 2
					W->kolor = CZERWONY;
					Z = Z->rodzic;
					continue;
				}

				if (W->prawy->kolor == CZARNY) {              // Przypadek 3
					W->lewy->kolor = CZARNY;
					W->kolor = CZERWONY;
					rot_R(W);
					W = Z->rodzic->prawy;
				}

				W->kolor = Z->rodzic->kolor; // Przypadek 4
				Z->rodzic->kolor = CZARNY;
				W->prawy->kolor = CZARNY;
				rot_L(Z->rodzic);
				Z = korzen;         // To spowoduje zakończenie pętli
			} else {                // Przypadki lustrzane
				W = Z->rodzic->lewy;

				if (W->kolor == CZERWONY) {              // Przypadek 1
					W->kolor = CZARNY;
					Z->rodzic->kolor = CZERWONY;
					rot_R(Z->rodzic);
					W = Z->rodzic->lewy;
				}

				if ((W->lewy->kolor == CZARNY) && (W->prawy->kolor == CZARNY)) { // Przypadek 2
					W->kolor = CZERWONY;
					Z = Z->rodzic;
					continue;
				}

				if (W->lewy->kolor == CZARNY) {              // Przypadek 3
					W->prawy->kolor = CZARNY;
					W->kolor = CZERWONY;
					rot_L(W);
					W = Z->rodzic->lewy;
				}

				W->kolor = Z->rodzic->kolor;  // Przypadek 4
				Z->rodzic->kolor = CZARNY;
				W->lewy->kolor = CZARNY;
				rot_R(Z->rodzic);
				Z = korzen;         // To spowoduje zakończenie pętli
			}

	Z->kolor = CZARNY;
}
node *find(int k) {
	node * p;
	p = korzen;
	while ((p != &S) && (p->key != k))
		if (k < p->key)
			p = p->lewy;
		else
			p = p->prawy;
	if (p == &S)
		return NULL;
	return p;
}
void szukaj(int x) {
	if (find(x) != NULL)
		printf("1\n");
	else
		printf("0\n");
}
void inorder(node * x) {
	if (x != &S) {
		inorder(x->lewy);
		printf("%d ", x->key);
		inorder(x->prawy);
	}
}
int main() {
	//int liczby[] = {15, 23, 43, 23, 14, 51, 54, 643, 746, 4, 64, 6432, 24, 123 , 6, 243, 212};
	int ile = 0;
	int i, j;
	char c[50];
	S.kolor = CZARNY;
	S.rodzic = &S;
	S.lewy = &S;
	S.prawy = &S;
	//srand(time(NULL));
	korzen = &S;
	int wynik;
	//for (i = 0; i < 10000; i++)
	//	insert(rand() % 10000);
	//inorder(korzen);
	scanf("%d", &ile);
	for (i = 0; i < ile; i++) {
		scanf("%s", c);
		switch ((char) c[0]) {
		case 'i':
			if (c[2] == 's') {
				scanf("%d", &j);
				insert(j);
			} else {
				if (korzen != &S) {
					inorder(korzen);
					printf("\n");
				} else
					printf("\n");
			}
			break;
		case 'm':
			if (c[1] == 'i') {
				wynik = min(korzen)->key;
			} else
				wynik = max(korzen)->key;
			if (korzen == &S)
				printf("\n");
			else
				printf("%d\n", wynik);
			break;
		case 'f':
			scanf("%d", &j);
			szukaj(j);
			break;
		case 'd':
			scanf("%d", &j);
			node * temp = find(j);
			if (temp == NULL)
				break;
			delete(temp);
			break;
		}
	}
	return 0;
}
