package technologia.android.pokermobile;
public class Figury {
	Figury() {

	}

	private static final int Poker = 8;
	private static final int Czworka = 7;
	private static final int Ful = 6;
	private static final int Kolor = 5;
	private static final int Strit = 4;
	private static final int Trojka = 3;
	private static final int Dwie_pary = 2;
	private static final int Para = 1;
	private static final int Brak_figur = 0;

	public int[] wyszukajFigure(Karta karta[]) {
		int[] liczba = new int[13];

		for (int x = 0; x < 5; x++) {
			liczba[karta[x].getLiczba()]++;
			// zwiekszamy wartosc o 1 dla liczby ktora wystepuje w tych kartach
		}
		int takie_same_karty = 1, takie_same_karty2 = 1;
		int wieksza_grupa_kart = 0, mniejsza_grupa_kart = 0;

		for (int x = 0; x < 13; x++) {
			if (liczba[x] > takie_same_karty) {
				if (takie_same_karty != 1)
				// Jesli juz pierwsze takie same karty sa zapamietane
				{
					takie_same_karty2 = takie_same_karty;
					mniejsza_grupa_kart = wieksza_grupa_kart;
				}

				takie_same_karty = liczba[x];
				wieksza_grupa_kart = x;

			} else if (liczba[x] > takie_same_karty2) {
				takie_same_karty2 = liczba[x];
				mniejsza_grupa_kart = x;
			}
		}

		/**
		 * Sprawdzamy czy z kart mozna ulozyc Kolor
		 */
		boolean kolor = true;
		for (int x = 0; x < 4; x++) {
			if (karta[x].getKolor() != karta[x + 1].getKolor())
				kolor = false;
		}
		/**
		 * Sprawdzamy czy w kartach wystepuje Strit
		 */
		int najwyzsza_wartosc_strit = 0;
		boolean strit = false; // zakladamy ze nie ma Strita
		for (int x = 0; x < 9; x++)
		{
			if (liczba[x] == 1 && liczba[x + 1] == 1 && liczba[x + 2] == 1
					&& liczba[x + 3] == 1 && liczba[x + 4] == 1) {
				strit = true;
				najwyzsza_wartosc_strit = x + 4;
				break;
			}
		}
		/**
		 * Tutaj powoli przechodzimy do porownywania kart
		 * Zapisujemy wartosci poszczegolnych kart w kolejnosci od najwiekszej do najmniejszej
		 */
		int[] orderedRanks = new int[5];
		int index=0;

		for (int x=0; x<13; x++)
		{
		    if (liczba[x]==1)
		    {
		    	orderedRanks[index]=x; 
		        index++;
		    }
		}
		int wartosc[] = new int[6];
		//start hand evaluation
		if ( takie_same_karty==1 ) {    //if we have no pair...
		    wartosc[0]=Brak_figur;       //this is the lowest type of hand, so it gets the lowest value
		    wartosc[1]=orderedRanks[0];  //the first determining factor is the highest card,
		    wartosc[2]=orderedRanks[1];  //then the next highest card,
		    wartosc[3]=orderedRanks[2];  //and so on
		    wartosc[4]=orderedRanks[3];
		    wartosc[5]=orderedRanks[4];
		}

		if (takie_same_karty==2 && takie_same_karty2==1) //if 1 pair
		{
		    wartosc[0]=Para;                //pair ranked higher than high card
		    wartosc[1]=wieksza_grupa_kart;   //rank of pair
		    wartosc[2]=orderedRanks[0];  //next highest cards.
		    wartosc[3]=orderedRanks[1];
		    wartosc[4]=orderedRanks[2];
		}

		if (takie_same_karty==2 && takie_same_karty2==2) //two pair
		{
		    wartosc[0]=Dwie_pary;
		    //rank of greater pair
		    wartosc[1]= wieksza_grupa_kart>mniejsza_grupa_kart ? wieksza_grupa_kart : mniejsza_grupa_kart;
		    //rank of smaller pair
		    wartosc[2]= wieksza_grupa_kart<mniejsza_grupa_kart ? wieksza_grupa_kart : mniejsza_grupa_kart;
		    wartosc[3]=orderedRanks[0];  //extra card
		}

		if (takie_same_karty==3 && takie_same_karty2!=2)
		//three of a kind (not full house)
		{
		    wartosc[0]=Trojka;
		    wartosc[1]= wieksza_grupa_kart;
		    wartosc[2]=orderedRanks[0];
		    wartosc[3]=orderedRanks[1];
		}

		if (strit)
		{
		    wartosc[0]=Strit;
		    wartosc[1]=najwyzsza_wartosc_strit;
		    //if we have two straights, 
		    //the one with the highest top cards wins
		}

		if (kolor)   
		{
		    wartosc[0]=Kolor;
		    wartosc[1]=orderedRanks[0]; //tie determined by ranks of cards
		    wartosc[2]=orderedRanks[1];
		    wartosc[3]=orderedRanks[2];
		    wartosc[4]=orderedRanks[3];
		    wartosc[5]=orderedRanks[4];
		}

		if (takie_same_karty==3 && takie_same_karty2==2)  //full house
		{
		    wartosc[0]=Ful;
		    wartosc[1]=wieksza_grupa_kart;
		    wartosc[2]=mniejsza_grupa_kart;
		}

		if (takie_same_karty==4)  //four of a kind
		{
		    wartosc[0]=Czworka;
		    wartosc[1]=wieksza_grupa_kart;
		    wartosc[2]=orderedRanks[0];
		}

		if (strit && kolor)  //Poker
		{
		    wartosc[0]=Poker;
		    wartosc[1]=najwyzsza_wartosc_strit;
		}
		/*for(int i = 0;i<6;i++){
			System.out.println(wartosc[i]);
		}*/
		return wartosc;
	}
}
