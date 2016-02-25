package com.unoapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.shaojin.unoapp.R;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private Animation animRotate;
    private List<DetailObject> detailObjects;
    private ProgressBar progressBar;
    private LinearLayout imageHolder;
    private FileService fileService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileService = new FileService(this);
        setContentView(R.layout.activity_main);
        animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_rotation);
        String url = getResources().getString(R.string.base_url);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        imageHolder = (LinearLayout) findViewById(R.id.images);
        new DownloadFileAsync().execute(url);
    }

    public final List<DetailObject> readCsv() {
        List<DetailObject> detailObjects = new ArrayList<DetailObject>();
        try {
            String a = "/sdcard/data.csv";
            File file = new File(a);
            InputStream inputStream = new FileInputStream(file);
            BufferedReader book = new BufferedReader(new InputStreamReader(inputStream));
            Iterable<CSVRecord> csvRecords = CSVFormat.DEFAULT.parse(book);
            for (CSVRecord csvRecord : csvRecords) {
                String id = csvRecord.get(0);
                String image = csvRecord.get(1);
                String type = csvRecord.get(2);
                DetailObject detailObject = new DetailObject(id, image, type);
                detailObjects.add(detailObject);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return detailObjects;
    }


    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;
            try {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                int lenghtOfFile = conexion.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream("/sdcard/data.csv");
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
        }

        @Override
        protected void onPostExecute(String unused) {
            detailObjects = readCsv();
            new LoadingImagesThread(detailObjects, getApplicationContext()).start();
        }
    }

    @Subscribe
    public void storeImageSuccess(SaveImageFinishEvent saveImageFinishEvent) {
        progressBar.setVisibility(View.GONE);
        imageHolder.setVisibility(View.VISIBLE);
        List<DetailObject> detailObjects = saveImageFinishEvent.getDetailObjects();
        for (int i = 1; i < detailObjects.size(); i++) {
            ImageView imageView = new ImageView(MainActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
            imageView.setLayoutParams(layoutParams);
            Picasso.with(this).load(fileService.getFileFromStorage(detailObjects.get(i).getId())).into(imageView);
            imageHolder.addView(imageView);
            imageView.setAnimation(animRotate);
        }
    }
}
