package com.example.qrcode;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {

    private EditText editTextProductId;
    private Button buttonGenerate, buttonScan;
    private ImageView imageViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextProductId = findViewById(R.id.editTextProductId);
        imageViewResult   = findViewById(R.id.imageViewResult);
        buttonGenerate    = findViewById(R.id.buttonGenerate);
        buttonScan        = findViewById(R.id.buttonScan);

        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                try {
                    BitMatrix bitMatrix = qrCodeWriter.encode(editTextProductId.getText().toString(), BarcodeFormat.QR_CODE, 200, 200);
                    Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);
                    for (int x = 0; x<200; x++){
                        for (int y=0; y<200; y++){
                            bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                        }
                    }
                    imageViewResult.setImageBitmap(bitmap);
                } catch (Exception e) { e.printStackTrace(); }

//                try {
//                    String productId = editTextProductId.getText().toString();
//                    Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
//                    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
//                    Writer codeWriter;
//                    codeWriter = new Code128Writer();
//                    BitMatrix byteMatrix = codeWriter.encode(productId, BarcodeFormat.CODE_128,400, 200, hintMap);
//                    int width = byteMatrix.getWidth();
//                    int height = byteMatrix.getHeight();
//                    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//                    for (int i = 0; i < width; i++) {
//                        for (int j = 0; j < height; j++) {    bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);    }
//                    }
//                    imageViewResult.setImageBitmap(bitmap);
//                } catch (Exception e) {    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();    }
            }
        });
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.setCameraId(0);
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            String productId = intentResult.getContents();
            Toast.makeText(getApplicationContext(), productId, Toast.LENGTH_LONG).show();
        }
        else{ super.onActivityResult(requestCode, resultCode, data); }
    }
}