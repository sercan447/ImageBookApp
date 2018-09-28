package sercandevops.com.professionalartbook;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> artNameList;
   static ArrayList<Bitmap> artImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Listele();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
                intent.putExtra("info","old");
                intent.putExtra("name",artNameList.get(position));
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });

    }

    public void Listele(){
        listView =  findViewById(R.id.listview);
        artNameList = new ArrayList<String>();
        artImageList = new ArrayList<Bitmap>();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,artNameList);
        listView.setAdapter(arrayAdapter);

        String Url = "content://sercandevops.com.professionalartbook.ArtContentProvider";
        Uri artUri = Uri.parse(Url);

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(artUri,null,null,null,"name");

        if (cursor != null) {
            // Toast.makeText(getApplicationContext(),"DOLU",Toast.LENGTH_LONG).show();
            while (cursor.moveToNext()) {

                artNameList.add(cursor.getString(cursor.getColumnIndex("name")));
                byte[] bytes = cursor.getBlob(cursor.getColumnIndex("image"));
                Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                artImageList.add(image);

                arrayAdapter.notifyDataSetChanged();

            }
        }else{
            Toast.makeText(getApplicationContext(),"BOŞ",Toast.LENGTH_LONG).show();
        }


    }//FUNC

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_art,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() == R.id.add_artmenu){
            Intent intent = new Intent(MainActivity.this,Main2Activity.class);
            intent.putExtra("info","new");
            startActivityForResult(intent,3);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Toast.makeText(MainActivity.this,"OK GELDI",Toast.LENGTH_LONG).show();
        }else if(resultCode == RESULT_CANCELED){
            Toast.makeText(MainActivity.this,"CANCELED GELDI",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(MainActivity.this,"else  GELDI : "+requestCode,Toast.LENGTH_LONG).show();
        }

    }
}
