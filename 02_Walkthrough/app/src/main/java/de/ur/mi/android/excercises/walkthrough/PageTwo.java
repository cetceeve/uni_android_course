package de.ur.mi.android.excercises.walkthrough;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PageTwo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_three);

        Button nextButton = (Button) findViewById(R.id.nextButton);
        Button previousButton = (Button) findViewById(R.id.previousButton);

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent previousIntent = new Intent(PageTwo.this, PageOne.class);
                startActivity(previousIntent);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextIntent = new Intent(PageTwo.this, PageThree.class);
                startActivity(nextIntent);
            }
        });
    }
}
