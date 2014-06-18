#include <stdio.h>
#include <stdlib.h>

#define MX 100000

int* map;

struct wierzcholek {
	int wierzcholekA;
	int waga;
};

typedef struct wierzcholek kopiec;

int size = 0;

kopiec *wierzcholki;

void swap(int a, int b) {
	int x, y, tmp1;
	x = wierzcholki[a].wierzcholekA;
	y = wierzcholki[b].wierzcholekA;
	//tmp1 = map[x];
	map[x] = b;
	map[y] = a;
	kopiec tmp = wierzcholki[a];
	wierzcholki[a] = wierzcholki[b];
	wierzcholki[b] = tmp;

	//map[tab[a].wierzcholekA]=b;
	//map[wierzcholki[b].wierzcholekA]=a;

}

void heapifyUp(int key) {
	while (wierzcholki[key].waga < wierzcholki[key / 2].waga && key > 1) {
		swap(key, key / 2);
		key /= 2;
	}
}

void heapifyDown(int key) {
	while ((2 * key <= size && wierzcholki[key].waga > wierzcholki[2 * key].waga)
			|| (2 * key + 1 <= size && wierzcholki[key].waga > wierzcholki[2 * key + 1].waga)) {
		if (wierzcholki[2 * key].waga > wierzcholki[2 * key + 1].waga) {
			swap(key, 2 * key + 1);
			key = 2 * key + 1;
		} else {
			swap(key, 2 * key);
			key = 2 * key;
		}
	}
}

void push(int wierzcholekA, int waga) {

	size++;
	wierzcholki[size].wierzcholekA = wierzcholekA;
	wierzcholki[size].waga = waga;
	map[wierzcholekA] = size;
	heapifyUp(size);
}

inline kopiec top() {
	return wierzcholki[1];
}

inline void pop() {
	int x;
	if (size > 0) {
		wierzcholki[1] = wierzcholki[size];
		x = wierzcholki[1].wierzcholekA;
		map[x] = 1;
		size--;
		heapifyDown(1);
	}
}

struct Node {
	int node;
	int lista_waga;
	struct Node * next;
};

void add(struct Node**L, int x, int y, int waga) {
	struct Node *p;
	p = L[x];
	if (p == NULL) {
		L[x] = malloc(sizeof(struct Node));
		L[x]->next = NULL;
		L[x]->node = y;
		L[x]->lista_waga = waga;
	} else {
		struct Node *new_node = (struct Node*) malloc(sizeof(struct Node));
		new_node->node = y;
		new_node->lista_waga = waga;
		new_node->next = L[x];
		L[x] = new_node;
	}
}

void prim(struct Node **L) {
	struct Node *v;
	int i;
	int j;
	int ak_waga;
	int cost = 0;
	int visited[size + 1];

	for (i = 0; i <= size; i++)
		visited[i] = 0;
	while (size > 0) {
		kopiec tmp = top();
		pop();
		i = (int) tmp.wierzcholekA;
		visited[i] = 1;
		cost += tmp.waga;
		v = L[i];
		/*
		 while(v)
		 {
		 ak_waga = v->lista_waga; //aktualna waga
		 for(j = 1; j <= size; j++)
		 {
		 if(tab[j].wierzcholekA == v->node && ak_waga < tab[j].waga)
		 {
		 tab[j].waga = ak_waga;
		 heapifyUp(j);
		 }
		 }
		 v = v->next;
		 }*/

		while (v) {
			ak_waga = v->lista_waga; //aktualna waga
			if (visited[v->node] == 0 && ak_waga < wierzcholki[map[v->node]].waga) {
				wierzcholki[map[v->node]].waga = ak_waga;
				heapifyUp(map[v->node]);
			}
			v = v->next;
		}

	}

	printf("%d", cost);
}

int main() {
	int i, x, y, n, m;
	int waga;
	struct Node **L;
	scanf("%d %d", &n, &m);
	map = calloc(n + 1, sizeof(int));
	wierzcholki = calloc(n + 1, sizeof(kopiec));
	L = calloc(n + 1, sizeof(struct Node));
	int e;
	push(1, 0);
	for (e = 2; e <= n; e++) {
		push(e, MX);
	}
	for (i = 1; i <= n; i++)
		L[i] = NULL;
	for (i = 0; i < m; i++) {
		scanf("%d" "%d" "%d", &x, &y, &waga);
		add(L, y, x, waga); //dodawanie do listy sasiedztwa
		add(L, x, y, waga);
	}
	prim(L);

	return 0;
}
