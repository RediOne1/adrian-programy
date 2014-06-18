/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.List;

public class Medium {

	List<String> komunikaty = new ArrayList<String>();
	int succ = 0;
	public boolean wolny = true;

	public boolean czyWolny() {
		return wolny;
	}

	public void add(String s) {
		this.komunikaty.add(s);
		System.out.println(s);
	}
}
