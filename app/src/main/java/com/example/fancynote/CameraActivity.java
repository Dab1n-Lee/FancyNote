package com.example.fancynote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;

import android.graphics.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity{
    private final static int TAKE_PICTURE = 1;
    private final static int GET_GALLERY_IMAGE = 2;

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference(); // 데이터베이스 접근

    private EditText et_title, et_content;
    private ImageView imageView;
    private Uri selectedImageUri;
    private Intent intent;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        imageView = findViewById(R.id.imageView);

        intent = getIntent();
        if (intent.getParcelableExtra("Image") != null) {
            bitmap = intent.getParcelableExtra("Image");
            imageView.setImageBitmap(bitmap);
            String imageSaveUri = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "사진 저장",
                    "찍은 사진이 저장되었습니다");
            selectedImageUri = Uri.parse(imageSaveUri);

        }
    }

    @Override
    public void onBackPressed() {
        if (et_title.getText().toString() != null & et_content != null && imageView != null) {


            // clickUpload 함수를 따로 생성해서 Refactoring 할 예정
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
            String filename = sdf.format(new Date())+".png";

            StorageReference imgRef = firebaseStorage.getReference("uploads/" + filename);

            // 업로드 결과 보기
            UploadTask uploadTask = imgRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String title = et_title.getText().toString();
                            String content = et_content.getText().toString();
                            String imagePath = uri.toString();

                            String id = database.push().getKey();

                            MemoItem memoItem = new MemoItem(title, content, imagePath);

                            database.child("note").child(id).setValue(memoItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(CameraActivity.this, "성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            selectedImageUri = null;
                            Intent intent = new Intent(CameraActivity.this, MainActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                            finishAffinity();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CameraActivity.this, "업로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "CameraActivity-onFailure() call");
                }
            });
            // 여기까지

        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            Glide.with(getApplicationContext()).load(selectedImageUri).into(imageView);
        } else if (requestCode == TAKE_PICTURE) {
            if (resultCode == RESULT_OK && data.hasExtra("data")) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    String imageSaveUri = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "사진 저장",
                            "찍은 사진이 저장되었습니다");
                    selectedImageUri = Uri.parse(imageSaveUri);
                    Log.e("TAG", "CameraActivity - onActivityResult() called" + selectedImageUri);
                }
            }
        }
    }

    public void clickUpload(){

    }
}