/*
 ============================================================================
 Name        : Algorytmy4_cykl_w_grafie.c
 Author      : Adrian Kuta
 Version     :
 Copyright   : Your copyright notice
 Description : Hello World in C, Ansi-style
 ============================================================================
 */

#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>


bool cykle(int v, bool odwiedzony[], bool stos[], int *adj[],int lk[]){
    int i,j;
    if(odwiedzony[v] == false){
        odwiedzony[v] = true;
        stos[v] = true;
        for(j=0;j<lk[v]; j++){
						i = adj[v][j];
					  if ( !odwiedzony[i] && cykle(i, odwiedzony, stos,adj,lk) )
                return true;
            else if (stos[i])
                return true;
        }
    }
    stos[v] = false;  // remove the vertex from recursion stack
    return false;
}

bool czyCykl(int n,int *adj[],int* lk){
		int i;
    bool visited[n+1];
    bool recStack[n+1];
    for(i = 1; i <= n; i++){
        visited[i] = false;
        recStack[i] = false;
   }
   for(i=1; i <= n; i++)
        if (cykle(i, visited, recStack,adj,lk))
            return true;
    return false;
}


int main(){
	int m,n,u,v,i;
	scanf("%d %d", &n,&m);
  int **graf;
  graf =(int**)malloc((n+1)*sizeof(int));
	int lk[n+1];
	for(i=0;i<=n;i++){
		lk[i]=0;
		graf[i] =(int*)malloc(sizeof(int));
		graf[i][0]=0;
	}
	for(i=0;i<m;i++){
		scanf("%d %d",&u,&v);
		graf[u] = realloc(graf[u],sizeof(int*)*(lk[u]+1));
		graf[u][lk[u]]=v;
		lk[u]++;
	}
	if(czyCykl(n,graf,lk))
		printf("TAK");
  else
    printf("NIE");
	return 0;
}

