/*
 ============================================================================
 Name        : Algorytmy4b.c
 Author      :
 Version     :
 Copyright   : Your copyright notice
 Description : Hello World in C, Ansi-style
 ============================================================================
 */

#include <stdio.h>

void merge(int *arr, int size1, int size2, int *inversions) {
    int temp[size1+size2];
    int ptr1=0, ptr2=0;

    while (ptr1+ptr2 < size1+size2) {
        if (ptr1 < size1 && arr[ptr1] <= arr[size1+ptr2] || ptr1 < size1 && ptr2 >= size2)
            temp[ptr1+ptr2] = arr[ptr1++];

        if (ptr2 < size2 && arr[size1+ptr2] < arr[ptr1] || ptr2 < size2 && ptr1 >= size1) {
            temp[ptr1+ptr2] = arr[size1+ptr2++];
            *inversions += size1-ptr1;
        	printf("%d\n", *inversions);
        }
    }
    int i;
    for (i=0; i < size1+size2; i++)
        arr[i] = temp[i];
}

void mergeSort(int *arr, int size, int* inversions) {
    if (size == 1)
        return;

    int size1 = size/2, size2 = size-size1;
    mergeSort(arr, size1, inversions);
    mergeSort(arr+size1, size2, inversions);
    merge(arr, size1, size2, inversions);
}

int main(void) {
	int n = 0, i;
	scanf("%d", &n);
	int *liczby = calloc(n, sizeof(int));
	for (i = 0; i < n; i++) {
		scanf("%d", &liczby[i]);
	}
	int inwersje = 0;
	mergeSort(liczby, n, &inwersje);
	//printf("%d\n", inwersje);
	return 0;
}
