import static java.awt.event.KeyEvent.VK_0;
import static java.awt.event.KeyEvent.VK_1;
import static java.awt.event.KeyEvent.VK_2;
import static java.awt.event.KeyEvent.VK_3;
import static java.awt.event.KeyEvent.VK_4;
import static java.awt.event.KeyEvent.VK_5;
import static java.awt.event.KeyEvent.VK_6;
import static java.awt.event.KeyEvent.VK_7;
import static java.awt.event.KeyEvent.VK_8;
import static java.awt.event.KeyEvent.VK_9;
import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_AMPERSAND;
import static java.awt.event.KeyEvent.VK_ASTERISK;
import static java.awt.event.KeyEvent.VK_AT;
import static java.awt.event.KeyEvent.VK_B;
import static java.awt.event.KeyEvent.VK_BACK_QUOTE;
import static java.awt.event.KeyEvent.VK_BACK_SLASH;
import static java.awt.event.KeyEvent.VK_C;
import static java.awt.event.KeyEvent.VK_CIRCUMFLEX;
import static java.awt.event.KeyEvent.VK_CLOSE_BRACKET;
import static java.awt.event.KeyEvent.VK_COLON;
import static java.awt.event.KeyEvent.VK_COMMA;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_DOLLAR;
import static java.awt.event.KeyEvent.VK_E;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_EQUALS;
import static java.awt.event.KeyEvent.VK_EXCLAMATION_MARK;
import static java.awt.event.KeyEvent.VK_F;
import static java.awt.event.KeyEvent.VK_G;
import static java.awt.event.KeyEvent.VK_GREATER;
import static java.awt.event.KeyEvent.VK_H;
import static java.awt.event.KeyEvent.VK_I;
import static java.awt.event.KeyEvent.VK_J;
import static java.awt.event.KeyEvent.VK_K;
import static java.awt.event.KeyEvent.VK_L;
import static java.awt.event.KeyEvent.VK_LEFT_PARENTHESIS;
import static java.awt.event.KeyEvent.VK_LESS;
import static java.awt.event.KeyEvent.VK_M;
import static java.awt.event.KeyEvent.VK_MINUS;
import static java.awt.event.KeyEvent.VK_N;
import static java.awt.event.KeyEvent.VK_NUMBER_SIGN;
import static java.awt.event.KeyEvent.VK_O;
import static java.awt.event.KeyEvent.VK_OPEN_BRACKET;
import static java.awt.event.KeyEvent.VK_P;
import static java.awt.event.KeyEvent.VK_PERIOD;
import static java.awt.event.KeyEvent.VK_PLUS;
import static java.awt.event.KeyEvent.VK_Q;
import static java.awt.event.KeyEvent.VK_QUOTE;
import static java.awt.event.KeyEvent.VK_QUOTEDBL;
import static java.awt.event.KeyEvent.VK_R;
import static java.awt.event.KeyEvent.VK_RIGHT_PARENTHESIS;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_SEMICOLON;
import static java.awt.event.KeyEvent.VK_SHIFT;
import static java.awt.event.KeyEvent.VK_SLASH;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_T;
import static java.awt.event.KeyEvent.VK_TAB;
import static java.awt.event.KeyEvent.VK_U;
import static java.awt.event.KeyEvent.VK_UNDERSCORE;
import static java.awt.event.KeyEvent.VK_V;
import static java.awt.event.KeyEvent.VK_W;
import static java.awt.event.KeyEvent.VK_X;
import static java.awt.event.KeyEvent.VK_Y;
import static java.awt.event.KeyEvent.VK_Z;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Keyboard {

	public static Robot robot;

	public Keyboard() throws AWTException {
		this.robot = new Robot();
		robot.delay(5000);
	}

	public Keyboard(Robot robot) {
		this.robot = robot;
	}

	public static void type(CharSequence characters) {
		int length = characters.length();
		for (int i = 0; i < length; i++) {
			char character = characters.charAt(i);
			type(character);
		}
	}

	public static void type(char character) {
		switch (character) {
		case 'a':
			doType(VK_A);
			break;
		case 'b':
			doType(VK_B);
			break;
		case 'c':
			doType(VK_C);
			break;
		case 'd':
			doType(VK_D);
			break;
		case 'e':
			doType(VK_E);
			break;
		case 'f':
			doType(VK_F);
			break;
		case 'g':
			doType(VK_G);
			break;
		case 'h':
			doType(VK_H);
			break;
		case 'i':
			doType(VK_I);
			break;
		case 'j':
			doType(VK_J);
			break;
		case 'k':
			doType(VK_K);
			break;
		case 'l':
			doType(VK_L);
			break;
		case 'm':
			doType(VK_M);
			break;
		case 'n':
			doType(VK_N);
			break;
		case 'o':
			doType(VK_O);
			break;
		case 'p':
			doType(VK_P);
			break;
		case 'q':
			doType(VK_Q);
			break;
		case 'r':
			doType(VK_R);
			break;
		case 's':
			doType(VK_S);
			break;
		case 't':
			doType(VK_T);
			break;
		case 'u':
			doType(VK_U);
			break;
		case 'v':
			doType(VK_V);
			break;
		case 'w':
			doType(VK_W);
			break;
		case 'x':
			doType(VK_X);
			break;
		case 'y':
			doType(VK_Y);
			break;
		case 'z':
			doType(VK_Z);
			break;
		case 'A':
			doType(VK_SHIFT, VK_A);
			break;
		case 'B':
			doType(VK_SHIFT, VK_B);
			break;
		case 'C':
			doType(VK_SHIFT, VK_C);
			break;
		case 'D':
			doType(VK_SHIFT, VK_D);
			break;
		case 'E':
			doType(VK_SHIFT, VK_E);
			break;
		case 'F':
			doType(VK_SHIFT, VK_F);
			break;
		case 'G':
			doType(VK_SHIFT, VK_G);
			break;
		case 'H':
			doType(VK_SHIFT, VK_H);
			break;
		case 'I':
			doType(VK_SHIFT, VK_I);
			break;
		case 'J':
			doType(VK_SHIFT, VK_J);
			break;
		case 'K':
			doType(VK_SHIFT, VK_K);
			break;
		case 'L':
			doType(VK_SHIFT, VK_L);
			break;
		case 'M':
			doType(VK_SHIFT, VK_M);
			break;
		case 'N':
			doType(VK_SHIFT, VK_N);
			break;
		case 'O':
			doType(VK_SHIFT, VK_O);
			break;
		case 'P':
			doType(VK_SHIFT, VK_P);
			break;
		case 'Q':
			doType(VK_SHIFT, VK_Q);
			break;
		case 'R':
			doType(VK_SHIFT, VK_R);
			break;
		case 'S':
			doType(VK_SHIFT, VK_S);
			break;
		case 'T':
			doType(VK_SHIFT, VK_T);
			break;
		case 'U':
			doType(VK_SHIFT, VK_U);
			break;
		case 'V':
			doType(VK_SHIFT, VK_V);
			break;
		case 'W':
			doType(VK_SHIFT, VK_W);
			break;
		case 'X':
			doType(VK_SHIFT, VK_X);
			break;
		case 'Y':
			doType(VK_SHIFT, VK_Y);
			break;
		case 'Z':
			doType(VK_SHIFT, VK_Z);
			break;
		case '`':
			doType(VK_BACK_QUOTE);
			break;
		case '0':
			doType(VK_0);
			break;
		case '1':
			doType(VK_1);
			break;
		case '2':
			doType(VK_2);
			break;
		case '3':
			doType(VK_3);
			break;
		case '4':
			doType(VK_4);
			break;
		case '5':
			doType(VK_5);
			break;
		case '6':
			doType(VK_6);
			break;
		case '7':
			doType(VK_7);
			break;
		case '8':
			doType(VK_8);
			break;
		case '9':
			doType(VK_9);
			break;
		case '-':
			doType(VK_MINUS);
			break;
		case '=':
			doType(VK_EQUALS);
			break;
		case '~':
			doType(VK_SHIFT, VK_BACK_QUOTE);
			break;
		case '!':
			doType(VK_EXCLAMATION_MARK);
			break;
		case '@':
			doType(VK_AT);
			break;
		case '#':
			doType(VK_NUMBER_SIGN);
			break;
		case '$':
			doType(VK_DOLLAR);
			break;
		case '%':
			doType(VK_SHIFT, VK_5);
			break;
		case '^':
			doType(VK_CIRCUMFLEX);
			break;
		case '&':
			doType(VK_AMPERSAND);
			break;
		case '*':
			doType(VK_ASTERISK);
			break;
		case '(':
			doType(VK_LEFT_PARENTHESIS);
			break;
		case ')':
			doType(VK_RIGHT_PARENTHESIS);
			break;
		case '_':
			doType(VK_UNDERSCORE);
			break;
		case '+':
			doType(VK_PLUS);
			break;
		case '\t':
			doType(VK_TAB);
			break;
		case '\n':
			doType(VK_ENTER);
			break;
		case '[':
			doType(VK_OPEN_BRACKET);
			break;
		case ']':
			doType(VK_CLOSE_BRACKET);
			break;
		case '\\':
			doType(VK_BACK_SLASH);
			break;
		case '{':
			doType(VK_SHIFT, VK_OPEN_BRACKET);
			break;
		case '}':
			doType(VK_SHIFT, VK_CLOSE_BRACKET);
			break;
		case '|':
			doType(VK_SHIFT, VK_BACK_SLASH);
			break;
		case ';':
			doType(VK_SEMICOLON);
			break;
		case ':':
			doType(VK_COLON);
			break;
		case '\'':
			doType(VK_QUOTE);
			break;
		case '"':
			doType(VK_QUOTEDBL);
			break;
		case ',':
			doType(VK_COMMA);
			break;
		case '<':
			doType(VK_LESS);
			break;
		case '.':
			doType(VK_PERIOD);
			break;
		case '>':
			doType(VK_GREATER);
			break;
		case '/':
			doType(VK_SLASH);
			break;
		case '?':
			doType(VK_SHIFT, VK_SLASH);
			break;
		case ' ':
			doType(VK_SPACE);
			break;
		default:
			throw new IllegalArgumentException("Cannot type character "
					+ character);
		}
	}

	private static void doType(int... keyCodes) {
		robot.delay(500);
		doType(keyCodes, 0, keyCodes.length);
	}

	private static void doType(int[] keyCodes, int offset, int length) {
		if (length == 0) {
			return;
		}

		robot.keyPress(keyCodes[offset]);
		doType(keyCodes, offset + 1, length - 1);
		robot.keyRelease(keyCodes[offset]);
	}

	private static void tab() {
		robot.keyPress(VK_TAB);
		robot.keyRelease(VK_TAB);
		delay(OCZEKIWANIE);
	}

	private static void space() {
		robot.keyPress(KeyEvent.VK_SPACE);
		robot.keyRelease(KeyEvent.VK_SPACE);
		delay(OCZEKIWANIE);
	}

	private static void down() {
		robot.keyPress(KeyEvent.VK_DOWN);
		robot.keyRelease(KeyEvent.VK_DOWN);
		delay(OCZEKIWANIE);
	}

	private static void enter() {
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		delay(OCZEKIWANIE);
	}

	private static void delay(int x) {
		robot.delay(x);
	}

	private static void odpowiedz10() {
		int x = 3;
		for (int i = 0; i < 11; i++) {
			if ((r.nextFloat() > 0.65) && (x != 0)) {
				space();
				x--;
			}
			tab();
		}
		tab();
	}

	private static boolean checkColor(Color x, Color y) {
		if (x.equals(y)) {
			return true;
		} else
			return false;
	}

	private static boolean sprawdzOdpowiedz(int pytanie, int odpowiedz) {
		pytanie++;
		odpowiedz++;
		switch (pytanie) {
		case 2:
			if (odpowiedz == 1) {
				if (r.nextFloat() < 0.2)
					return true;
				else
					return false;
			}
			break;
		case 6:
			if (odpowiedz == 3) {
				if (r.nextFloat() < 0.4)
					return true;
				else
					return false;
			}
			break;
		case 14:
			if ((odpowiedz == 3) || (odpowiedz == 4))
				return false;
			break;
		case 15:
			if (odpowiedz != 3) {
				if (r.nextFloat() < 0.7)
					return true;
				else
					return false;
			}
			break;
		case 16:
			if ((odpowiedz != 1) && (odpowiedz != 2)) {
				if (r.nextFloat() < 0.4)
					return true;
				else
					return false;
			}
			break;
		}
		if(odpowiedz==0)
			return false;
		return true;
	}

	public static int pytanie[] = { 1, 5, 4, 4, 4, 3, 3, 3, 5, 12, 5, 2, 5, 4,
			4, 5 };

	public static Random r = new Random();

	private static Color startColor = new Color(11, 97, 164);
	private static Color endColor = new Color(232, 238, 247);
	
	private static final int ILOSC = 10;
	private static final int OCZEKIWANIE = 300;

	public static void main(String... args) throws Exception {
		Keyboard keyboard = new Keyboard();
		for (int ile = 0; ile < ILOSC; ile++) {
			while (!checkColor(startColor, robot.getPixelColor(800, 150))) {
				delay(100);
			}
			for (int i = 0; i < pytanie.length; i++) {
				tab();
				if (i == 9) {
					odpowiedz10();
					continue;
				}
				space();
				int x = -1;
				while (!sprawdzOdpowiedz(i, x))
					x = r.nextInt(pytanie[i]);
				for (int j = 0; j < x; j++) {
					down();
				}
				if (i == 6 || i == 7)
					tab();
			}
			enter();
			while (!checkColor(endColor, robot.getPixelColor(500, 500))) {
				delay(100);
			}
			tab();
			enter();
		}
	}
}