package com.example.fancynote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    private long backKeyPressedTime = 0;
    private FloatingActionButton fab_main,fab_sub1, fab_sub2;
    private DrawerLayout drawer_main;
    private NavigationView navigationView;
    private final int REQUEST_SET_PW = 1;
    private MenuItem navi_item_setPw;
    private final int REQUEST_PW_REMOVE = 1579;
    private boolean isLocked = false;
    private Vibrator vi;
    private MemoItem memoItem;
    private Intent intent;
    private final int REQUEST_CAMERA = 569;

    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();


    private Animation rotateOpen,rotateClose,fromBottom,toBottom;
    private boolean clicked = false;

    private Toolbar toolbar;
    private ArrayList<MemoItem> list;
    private ArrayList<String> uidList;


    private RecyclerView recyclerView;
    private Adapter adapter;

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference note;

    //TODO 1. Dto ????????? createDate ??? ??????, ????????? ?????? ?????? ??????????????? ?????? ???.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** ??????????????? ?????? ????????? ??????, ?????? ????????? ??????, ?????? ??? ????????? ?????? ?????? ?????? ??? ???????????? **/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]
                        {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }


        /** ????????? ????????? ????????? ?????? **/

        drawer_main = findViewById(R.id.drawer_main);
        navi_item_setPw = findViewById(R.id.navi_item_setPw);

        // toolbar ?????????
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Activity??? App bar??? ??????

        // floatingActionButton ?????????
        fab_main = findViewById(R.id.fab_main);
        fab_sub1 = findViewById(R.id.fab_sub1);
        fab_sub2 = findViewById(R.id.fab_sub2);
        rotateOpen = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(MainActivity.this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(MainActivity.this, R.anim.to_bottom_anim);
        vi = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        fab_main.setOnClickListener(this::onClick);
        fab_sub1.setOnClickListener(this::onClick);
        fab_sub2.setOnClickListener(this::onClick);

        navigationView = findViewById(R.id.navigationView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24);

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        //folding Animation ?????????
        recyclerView = findViewById(R.id.recyclerview);

        Intent intent1 = getIntent();
        String id = intent1.getStringExtra("id");

        note = database.child("note");

        list = new ArrayList<>();
        uidList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setFocusable(false);
        adapter = new Adapter(MainActivity.this);
        recyclerView.setAdapter(adapter);


        // swipe ?????? RecyclerView ?????? ????????? ????????????????????? ????????? ?????? ?????? ??????.
        // ?????? ?????? ID ?????? List
        note.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uidList.clear();
                for (DataSnapshot ds:snapshot.getChildren()) {
                    String uidKey = ds.getKey();
                    uidList.add(uidKey);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Uid Collect Method", error.getMessage());
            }
        });



        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            // database Reference ??? child ?????? ????????? position??? ???????????? id ?????? ???????????? ?????????
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getBindingAdapterPosition();
                vi.vibrate(200);
                StorageReference delImage = storageReference.child("upload/");
                delImage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Storage?????? ???????????? ??????????????? ?????????????????????!", Toast.LENGTH_SHORT);
                    }
                });
                note.child(uidList.get(position)).removeValue();
                note.addListenerForSingleValueEvent(new RecyclerViewDataSync(list, memoItem, adapter));
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        note.addListenerForSingleValueEvent(new RecyclerViewDataSync(list, memoItem, adapter));


        adapter.setDataToAdapter(list);

    }

    /** ?????? ?????? ?????? ?????? **/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] ==
                PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("Log", "Permission : " + permissions[0] + "was " + grantResults[0]);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_main:
                onAddButtonClicked();
                break;
            case R.id.fab_sub1:
                intent = new Intent(getApplicationContext(), AddMemo.class);
                startActivity(intent);
                break;
            case R.id.fab_sub2:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
                break;
        }
    }


    // Menu Resource ??? ?????? xml??? ????????? App bar??? ??????

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    // ?????? ??????(Appbar)??? ????????? ?????? ?????? ??????????????? ????????? ????????????
    // ???????????? onOptionItemSelected() Method??? ??????
    @SuppressLint("NonConstantResourceId")

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer_main.openDrawer(GravityCompat.START);
                return true;
            /** case R.id.menu_item1:
                return true;
            case R.id.menu_item2:
                return true;**/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*** ??????????????? ????????? Item Click Listener ????????? ***/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.navi_item_setPw:
                intent = new Intent(MainActivity.this, PwVerification.class);
                intent.putExtra("request1", "insert");
                startActivityForResult(intent, REQUEST_SET_PW);
                return true;
            case R.id.navi_item_remove:
                intent = new Intent(MainActivity.this, PwVerification.class);
                intent.putExtra("request2", "remove");
                startActivityForResult(intent, REQUEST_PW_REMOVE);
                return true;

            case R.id.navi_item_inquiry:
                intent = new Intent(getApplicationContext(), Inquiry.class);
                startActivity(intent);
                return true;
        }
        return true;
    }

    // ???????????? ????????? ?????? ??????

    @Override
    public void onBackPressed() {
        // Navigation Bar??? on ?????? ?????? ???????????? ??????????????? ?????? ??? ?????? ??????.
        if (drawer_main.isDrawerOpen(GravityCompat.START)) {
            drawer_main.closeDrawer(GravityCompat.START);
        }else if(System.currentTimeMillis()>backKeyPressedTime+2000){
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'??????\' ????????? ?????? ??? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
            return;
        }else if(System.currentTimeMillis()<=backKeyPressedTime+2000){
            finishAffinity();
        } else {
            super.onBackPressed();
        }
    }

    // Floating Button ?????????
    private void onAddButtonClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
    }

    /**
     * ?????????, ?????????
     * @param clicked ????????????
     * **/
    private void setVisibility(boolean clicked){

        if (!clicked) {
            fab_sub1.setVisibility(fab_main.VISIBLE);
            fab_sub2.setVisibility(fab_sub2.VISIBLE);
        } else {
            fab_sub1.setVisibility(fab_main.INVISIBLE);
            fab_sub2.setVisibility(fab_sub2.INVISIBLE);
        }
    }

    /**
     * ??????????????? ??????
     * @param clicked ?????? ??????
     * **/

    private void setAnimation(boolean clicked){
        if (!clicked) {
            fab_sub1.startAnimation(fromBottom);
            fab_sub2.startAnimation(fromBottom);
            fab_main.startAnimation(rotateOpen);
        } else {
            fab_sub1.startAnimation(toBottom);
            fab_sub2.startAnimation(toBottom);
            fab_main.startAnimation(rotateClose);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SET_PW) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "?????? ??????????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_OK) {
                intent = new Intent(MainActivity.this, PasswordInsert.class);
                startActivity(intent);
            }
        }else if (requestCode == REQUEST_PW_REMOVE) {
            if (resultCode == RESULT_OK) {
                intent = new Intent(MainActivity.this, Remove_Password.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "??????????????? ???????????? ?????? ????????????.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK && data != null && data.hasExtra("data")) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                intent = new Intent(MainActivity.this, CameraActivity.class);
                intent.putExtra("Image", bitmap);
                startActivity(intent);
            }
        }
    }

}



class RecyclerViewDataSync implements ValueEventListener{

    private ArrayList list;
    private MemoItem memoItem;
    private Adapter adapter;

    public RecyclerViewDataSync(ArrayList list, MemoItem memoItem, Adapter adapter) {
        this.list = list;
        this.memoItem = memoItem;
        this.adapter = adapter;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        list.clear();
        for (DataSnapshot datasnap:snapshot.getChildren()) {
            MemoItem memoItem = datasnap.getValue(MemoItem.class);
            list.add(memoItem);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.e("MainActivity", error.getMessage());
    }
}