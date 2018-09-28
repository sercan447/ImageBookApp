package sercandevops.com.professionalartbook;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Main2Activity extends AppCompatActivity {

        ImageView imageView;
        EditText editText;
        Button saveButton,deleteButton,updateButton;
        Bitmap selectedImage;

        String firstName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        imageView = (ImageView)findViewById(R.id.imgresim);
        editText = (EditText)findViewById(R.id.edtname);
        saveButton = findViewById(R.id.btnSave);
        deleteButton = findViewById(R.id.btndelete);
        updateButton = findViewById(R.id.btnupdate);

        deleteButton.setVisibility(View.INVISIBLE);
        updateButton.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        String info = intent.getStringExtra("info");


        if(info.matches("new")){

            Bitmap background = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.ic_launcher_background);
            imageView.setImageBitmap(background);
            editText.setText("");

            saveButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
            updateButton.setVisibility(View.INVISIBLE);

        }else{
            String name = intent.getStringExtra("name");
            editText.setText(name);
            firstName = name;
            int position = intent.getIntExtra("position",0);
            imageView.setImageBitmap(MainActivity.artImageList.get(position));

            saveButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            updateButton.setVisibility(View.VISIBLE);
        }


    }

    public void saveRecord(View view) {

        String artName = editText.getText().toString();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG,50,outputStream);
        byte[] bytes = outputStream.toByteArray();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ArtContentProvider.NAME,artName);
        contentValues.put(ArtContentProvider.IMAGE,bytes);

        getContentResolver().insert(ArtContentProvider.CONTENT_URI,contentValues);

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);


    }

    public void deleteRecord(View view) {

       String recordName = editText.getText().toString();
        String[] selectionArgumantes = {recordName};

        getContentResolver().delete(ArtContentProvider.CONTENT_URI,"name=?",selectionArgumantes);

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }

    public void updateRecord(View view) {
        String artName = editText.getText().toString();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,outputStream);
        byte[] bytes = outputStream.toByteArray();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ArtContentProvider.NAME,artName);
        contentValues.put(ArtContentProvider.IMAGE,bytes);

        String[] selectionArguments = {firstName};

        getContentResolver().update(ArtContentProvider.CONTENT_URI,contentValues,"name=?",selectionArguments);

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }

    public void select(View view) {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Main2Activity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,3);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,3);

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 3 && resultCode == RESULT_OK && data != null){
            Uri image = data.getData();

            try{
                 selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),image);
                imageView.setImageBitmap(selectedImage);

            }catch (IOException e){
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
        }
}
