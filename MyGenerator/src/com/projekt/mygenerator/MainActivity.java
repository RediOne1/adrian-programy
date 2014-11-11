package com.projekt.mygenerator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Główny widok z guzikami itp. Odpala {@link Display} czy coś w tym stylu.
 */
public class MainActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.generatorsSpinner);
        ArrayAdapter<Generator> adapter = new ArrayAdapter<Generator>(this,
                android.R.layout.simple_list_item_1, generators);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedGenerator = (Generator) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        if (Build.VERSION.SDK_INT >= 17)
            getWindowManager().getDefaultDisplay().getRealSize(screensize);
        else {
            screensize.x = getWindowManager().getDefaultDisplay().getWidth();
            screensize.y = getWindowManager().getDefaultDisplay().getHeight();
        }

    }


    public void generate(View _)
    {
        EditText et = (EditText) findViewById(R.id.seedInput);
        long seed = Long.parseLong(et.getText().toString());

        Intent intent = new Intent(this, Display.class);
        intent.putExtra("width", screensize.x).putExtra("height", screensize.y)
                .putExtra("seed", seed).putExtra("generator", selectedGenerator);
        startActivity(intent);
    }

    static Generator[] generators = {new LinesGenerator(), new ExampleGenerator(), new FreakyGenerator()};
    Generator selectedGenerator = generators[0];

    Point screensize = new Point();

}