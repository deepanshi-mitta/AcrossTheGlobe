package com.example.acrosstheglobe;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.raed.drawingview.DrawingView;

public class MainActivity extends AppCompatActivity {
    DrawingView drawingView;
    Button sync;
    Button mUndoButton;
    Button mRedoButton;
    String S;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawingView = findViewById(R.id.drawingView);
        text = findViewById(R.id.ourText);


        sync = findViewById(R.id.syncDrawingBtn);
        mUndoButton = findViewById(R.id.mUndoButton);
        mRedoButton = findViewById(R.id.mRedoButton);

        sync.setOnClickListener(view -> {
            Bitmap bitmap = drawingView.exportDrawing();
            TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
            if (!textRecognizer.isOperational()) {
                Toast.makeText(this, "Could not get text", Toast.LENGTH_SHORT).show();
            } else {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<TextBlock> items = textRecognizer.detect(frame);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < items.size(); i++) {
                    TextBlock myitem = items.valueAt(i);
                    sb.append(myitem.getValue());
                }
                S = (sb.toString());
                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                final TextView text = mView.findViewById(R.id.ourText);
                Button btn_ok = mView.findViewById(R.id.buttonOk);
                alert.setView(mView);
                text.setText(S);
                final AlertDialog alertDialog = alert.create();
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();

            }
        });


        drawingView.setUndoAndRedoEnable(true);

        mUndoButton.setOnClickListener(view -> {
            drawingView.undo();
            drawingView.setEnabled(drawingView.isUndoStackEmpty());
            drawingView.setEnabled(!drawingView.isRedoStackEmpty());
        });

        mRedoButton.setOnClickListener(view -> {
            drawingView.redo();

        });

    }

}

