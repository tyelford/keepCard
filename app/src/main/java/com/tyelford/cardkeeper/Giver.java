package com.tyelford.cardkeeper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tyelford.cardkeeper.data.Card;
import com.tyelford.cardkeeper.data.CardData;

import java.util.List;


public class Giver extends ListActivity {


    List<Card> cards = new CardData().getCards();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giver);

        ArrayAdapter<Card> adapter = new ArrayAdapter<Card>(this, android.R.layout.simple_list_item_1, cards);
        setListAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Card card = cards.get(position);

        Intent intent = new Intent(this, CardActivity.class);
        intent.putExtra("cardGiver", card.cardGiver);
        intent.putExtra("cardFrontImgRes", card.cardFrontImgRes);
        intent.putExtra("cardInsideLeftImgRes", card.cardInsideLeftImgRes);
        intent.putExtra("cardInsideRightImgRes", card.cardInsideRightImgRes);
        intent.putExtra("cardBackImgRes", card.cardBackImgRes);
        intent.putExtra("cardComments", card.cardComments);
        intent.putExtra("incPresent", card.incPresent);
        startActivity(intent);

    }
}
