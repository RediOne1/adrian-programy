#include <stdio.h>
#include <stdlib.h>

struct krawedz {
	int wierzcholekA;
	int wierzcholekB;
	int waga;
};

typedef struct krawedz heap;

int size = 0;
heap *tabEdge;

void swap(int x, int y) {

	heap tmp = tabEdge[x];

	tabEdge[x] = tabEdge[y];

	tabEdge[y] = tmp;

}
void heapifyUp(int key) {
	while (tabEdge[key].waga < tabEdge[key / 2].waga && key > 1) {
		swap(key, key / 2);
		key /= 2;
	}
}

void heapifyDown(int key) {
	while ((2 * key <= size && tabEdge[key].waga > tabEdge[2 * key].waga)
			|| (2 * key + 1 <= size
					&& tabEdge[key].waga > tabEdge[2 * key + 1].waga)) {
		if (tabEdge[2 * key].waga > tabEdge[2 * key + 1].waga) {
			swap(key, 2 * key + 1);
			key = 2 * key + 1;
		} else {
			swap(key, 2 * key);
			key = 2 * key;
		}
	}
}

void push(int wierzcholekA, int wierzcholekB, int waga) {

	size++;
	tabEdge[size].wierzcholekA = wierzcholekA;
	tabEdge[size].wierzcholekB = wierzcholekB;
	tabEdge[size].waga = waga;
	heapifyUp(size);
}

inline heap top() {
	return tabEdge[1];
}

inline void pop() {
	if (size > 0) {
		tabEdge[1] = tabEdge[size];
		size--;
		heapifyDown(1);
	}
}

int *wierzcholki, *ile;

int find(int a) {
	if (wierzcholki[a] == a)
		return a;
	int fa = find(wierzcholki[a]);
	wierzcholki[a] = fa;
	return fa;
}

int Union(int x, int y) {
	x = find(x);
	y = find(y);

	if (x == y) {
		return 0;
	}

	if (ile[x] <= ile[y]) {
		ile[y] += ile[x];
		wierzcholki[x] = y;
	} else {
		ile[x] += ile[y];
		wierzcholki[y] = x;
	}
	return 1;
}
int kruskala() {
	int cost = 0;
	while (size > 0) {
		heap tmp = top();
		pop();
		if (Union(tmp.wierzcholekA, tmp.wierzcholekB) == 1)
			cost += tmp.waga;
	}
	return cost;
}
int main() {
	int m;
	int n;
	scanf("%d %d", &n, &m);
	tabEdge = calloc(m, sizeof(heap) + 1);
	wierzcholki = calloc(n, sizeof(int));

	ile = malloc(n * sizeof(int));

	int i;
	for (i = 0; i < n; i++) {
		wierzcholki[i] = i;
		ile[i] = 1;
	}
	int a, b, c;
	for (i = 0; i < m; i++) {
		scanf("%d %d %d", &a, &b, &c);
		push(a, b, c);
	}

	printf("%d\n", kruskala());
	return 0;
}
