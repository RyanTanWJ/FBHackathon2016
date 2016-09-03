package com.hackathon.walrus.pigeon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Walrus on 9/3/2016.
 */
public class ConvertToGIF {
    public ConvertToGIF() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.setDelay(100);  // ディレイ 500/ms
        encoder.setRepeat(0);   // 0:ループする -1:ループしない
        encoder.start(bos);     // gitデータ生成先ををbosに設定

        try {
            Bitmap bmp1, bmp2, bmp3;

            // ファイルの読み込み
            bmp1 = BitmapFactory.decodeStream(new FileInputStream("/sdcard/PigeonMessenger/images/target1.png"));
            encoder.addFrame(bmp1);  // gifに追加
            bmp1.recycle();

            bmp2 = BitmapFactory.decodeStream(new FileInputStream("/sdcard/PigeonMessenger/images/target2.png"));
            encoder.addFrame(bmp2);  // gifに追加
            bmp2.recycle();

            bmp3 = BitmapFactory.decodeStream(new FileInputStream("/sdcard/PigeonMessenger/images/target3.png"));
            encoder.addFrame(bmp3);  // gifに追加
            bmp3.recycle();

        } catch (FileNotFoundException e) {
        }

        encoder.finish();  // 終了

        File filePath = new File("/sdcard/PigeonMessenger/images/", "sample.gif");
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(filePath);
            // bosに生成されたgifデータをファイルに吐き出す
            outputStream.write(bos.toByteArray());
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }
}
