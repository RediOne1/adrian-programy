package com.aplikacja.podrywacz;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyDrawer extends Activity implements OnClickListener {

	private Activity root;
	private DrawerLayout mDrawerLayout;
	private View mDrawerPanel;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private LinearLayout zadania, poradnik, main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTitle = mDrawerTitle = getTitle();
		zadania = (LinearLayout) findViewById(R.id.drawer_zadania_linear);
		poradnik = (LinearLayout) findViewById(R.id.drawer_poradnik_linear);
		main = (LinearLayout) findViewById(R.id.drawer_strona_glowna);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerPanel = (View) findViewById(R.id.left_panel);
		main.setOnClickListener(this);
		zadania.setOnClickListener(this);
		poradnik.setOnClickListener(this);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		main.setSelected(true);
		openFragment(new MainFragment());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void openFragment(Fragment f) {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, f)
				.commit();
		mDrawerLayout.closeDrawer(mDrawerPanel);
	}

	@Override
	public void onClick(View v) {
		main.setSelected(false);
		zadania.setSelected(false);
		poradnik.setSelected(false);
		if (v == main) {
			openFragment(new MainFragment());
			mTitle = "Strona Główna";
			main.setSelected(true);
		} else if (v == zadania) {
			openFragment(new ZadaniaFragment());
			mTitle = "Zadania";
			zadania.setSelected(true);
		} else if (v == poradnik) {
			openFragment(new PoradnikFragment());
			mTitle = "Pytania";
			poradnik.setSelected(true);
		}

	}
}
