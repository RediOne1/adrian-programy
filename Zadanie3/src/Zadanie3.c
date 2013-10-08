/*
 ============================================================================
 Name        : Zadanie3.c
 Author      : 
 Version     :
 Copyright   : Your copyright notice
 Description : Hello World in C, Ansi-style
 ============================================================================
 */

#include <stdio.h>
int main() {
	/** Char */
	{
		printf("char: ");
		char x = 1, y;
		while (x > 0) {
			y = x;
			x++;
		}
		printf("%5d", y);
	}
	{
		char x = -1, y;
		while (x < 0) {
			y = x;
			x--;
		}
		printf("%15d\n", y);
	}
	/**unsigned char */
	{
		printf("unsigned char: ");
		unsigned char x = 1, y;
		while (x > 0) {
			y = x;
			x++;
		}
		printf("%5u\n\n", y);
	}
	/** short */
	{
		printf("short: ");
		short x = 1, y;
		while (x > 0) {
			y = x;
			x++;
		}
		printf("%5d", y);
	}
	{
		short x = -1, y;
		while (x < 0) {
			y = x;
			x--;
		}
		printf("%15d\n", y);
	}
	/**unsigned short */
	{
		printf("unsigned short: ");
		unsigned short x = 1, y;
		while (x > 0) {
			y = x;
			x++;
		}
		printf("%5u\n\n", y);
	}
	/** int */
	{
		printf("int: ");
		int x = 1, y;
		while (x > 0) {
			y = x;
			x++;
		}
		printf("%5d", y);
	}
	{
		int x = -1, y;
		while (x < 0) {
			y = x;
			x--;
		}
		printf("%15d\n", y);
	}
	/**unsigned short */
	{
		printf("unsigned int: ");
		unsigned int x = 1, y;
		while (x > 0) {
			y = x;
			x++;
		}
		printf("%5u\n\n", y);
	}
	/** long */
	{
		printf("long: ");
		long x = 1, y;
		while (x > 0) {
			y = x;
			x++;
		}
		printf("%5ld", y);
	}
	{
		long x = -1, y;
		while (x < 0) {
			y = x;
			x--;
		}
		printf("%15ld\n", y);
	}
	/**unsigned long */
	{
		printf("unsigned long: ");
		unsigned long x = 1, y;
		while (x > 0) {
			y = x;
			x++;
		}
		printf("%5lu\n\n", y);
	}
	/** long long int
	{
		printf("long long int: ");
		long long int x = 1, y;
		while (x > 0) {
			y = x;
			x+=9023372036850775807;
		}
		printf("%5d", y);
	}
	{
		long long int x = -1, y;
		while (x < 0) {
			y = x;
			x-=9023372036850775807;
		}
		printf("%15d\n", y);
	}
	/**unsigned long long int
	{
		printf("unsigned long long int: ");
		unsigned long long int x = 1, y;
		while (x > 0) {
			y = x;
			x+=9023372036850775807;
		}
		printf("%5u\n\n", y);
	}*/
}

