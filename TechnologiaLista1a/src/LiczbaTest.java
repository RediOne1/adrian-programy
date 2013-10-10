import junit.framework.TestCase;


public class LiczbaTest extends TestCase {
	Liczba l;
	public void setUp(){
		l = new Liczba(5);
	}

	public void testLiczba() {
		
	}

	public void testLiczbaInt() {
		
	}

	public void testZamiana() {
		assertEquals(""+5, l.Zamiana(10));		
	}
	
	public void testgetDziesietny(){
		assertSame(5, l.getDziesietny());
	}
	public void testzamieniono(){
		l.Zamiana(10);
		assertTrue(l.zamieniono());
	}

}
