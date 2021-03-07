package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


//import com.nbsp.materialfilepicker.MaterialFilePicker;
//import com.nbsp.materialfilepicker.ui.FilePickerActivity;

public class MainActivity extends Activity {
    private GUI gui;
    private int SAVE = 1;
    private int LOAD = 2;

    public void on_click_any(View v){
        int id = v.getId();
        if(id != R.id.btnSave && id != R.id.btnLoad){
            gui.on_click();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.err.println("save");
        if (requestCode == SAVE && resultCode == RESULT_OK) {
            try {
                Uri u = data.getData();
                ParcelFileDescriptor pfd = this.getContentResolver().openFileDescriptor(u, "w");
                FileOutputStream fos = new FileOutputStream(pfd.getFileDescriptor());
                new Save_to_CSV(gui.get_calibration(), fos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        else if (requestCode == LOAD && resultCode == RESULT_OK) {

            try {
                Uri u = data.getData();
                ParcelFileDescriptor pfd = this.getContentResolver().openFileDescriptor(u, "r");
                FileInputStream fis = new FileInputStream(pfd.getFileDescriptor());
                gui.set_calibration(Load_from_CSV.load_from_CSV(fis, this));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
// инициализация
        tabHost.setup();
        TabHost.TabSpec tabSpec;
// создаем вкладку и указываем тег
        tabSpec = tabHost.newTabSpec("tag1");
// название вкладки
        tabSpec.setIndicator("Points");
// указываем id компонента из FrameLayout, он и станет содержимым
        tabSpec.setContent(R.id.tvTab1);
// добавляем в корневой элемент
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("tag2");
// указываем название и картинку
// в нашем случае вместо картинки идет xml-файл,
// который определяет картинку по состоянию вкладки
        tabSpec.setIndicator("Line");
        tabSpec.setContent(R.id.tvTab2);
        tabHost.addTab(tabSpec);

// вторая вкладка будет выбрана по умолчанию
        tabHost.setCurrentTabByTag("tag1");

        gui = new GUI(this);


    }
}