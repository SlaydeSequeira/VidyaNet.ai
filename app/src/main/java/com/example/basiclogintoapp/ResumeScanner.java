package com.example.basiclogintoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.IOException;

public class ResumeScanner extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_PDF = 100;
    private static final String TAG = "ResumeScanner";
    TextView t1;
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_scanner);
        t1 = findViewById(R.id.text);
        selectPdfFile();
    }

    private void selectPdfFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_PICK_PDF);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_PDF && resultCode == RESULT_OK && data != null) {
            Uri pdfUri = data.getData();
            if (pdfUri != null) {
                extractTextFromPdf(pdfUri);
            } else {
                Toast.makeText(this, "No PDF file selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void extractTextFromPdf(Uri pdfUri) {
        try {
            // Using a content resolver to get an InputStream from the URI
            PdfReader reader = new PdfReader(getContentResolver().openInputStream(pdfUri));

            StringBuilder pdfText = new StringBuilder();
            int pageCount = reader.getNumberOfPages();

            for (int i = 1; i <= pageCount; i++) {
                pdfText.append(PdfTextExtractor.getTextFromPage(reader, i)).append("\n");
            }

            reader.close();

            // Log or display the extracted text
            Log.d(TAG, "Extracted PDF text: " + pdfText.toString());
            Toast.makeText(this, "PDF text extracted. Check log for details.", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error reading PDF file", Toast.LENGTH_SHORT).show();
        }
    }
}
