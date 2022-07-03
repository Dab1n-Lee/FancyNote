package com.example.fancynote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private long backKeyPressedTime = 0;
    private FloatingActionButton fab_main,fab_sub1, fab_sub2;
    private DrawerLayout drawer_main;
    private NavigationView navigationView;
    private final int REQUEST_SET_PW = 1;
    private MenuItem navi_item_setPw;


    private Animation rotateOpen,rotateClose,fromBottom,toBottom;
    private boolean clicked = false;

    private Toolbar toolbar;


    private RecyclerView recyclerView;
    private Adapter adapter;

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference note = database.child("note");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");


        /** 데이터 베이스 구현부 종료 **/

        drawer_main = findViewById(R.id.drawer_main);
        navi_item_setPw = findViewById(R.id.navi_item_setPw);

        // toolbar 구현부
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Activity의 App bar로 지정

        // floatingActionButton 구현부
        fab_main = findViewById(R.id.fab_main);
        fab_sub1 = findViewById(R.id.fab_sub1);
        fab_sub2 = findViewById(R.id.fab_sub2);
        rotateOpen = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(MainActivity.this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(MainActivity.this, R.anim.to_bottom_anim);

        fab_main.setOnClickListener((v)->{
            onAddButtonClicked();

        });

        navigationView = findViewById(R.id.navigationView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24);

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
        
        fab_sub1.setOnClickListener((v)->{
            Intent intent1 = new Intent(getApplicationContext(), AddMemo.class);
            startActivity(intent1);
        });
        fab_sub2.setOnClickListener((v)->{
            Toast.makeText(this, "sub2가 클릭되었습니다.", Toast.LENGTH_SHORT).show();
        });


        //folding Animation 구현부
        recyclerView = findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setFocusable(false);

        adapter = new Adapter(MainActivity.this);
        recyclerView.setAdapter(adapter);
        ArrayList<MemoItem> list = new ArrayList<>();

        note.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    MemoItem memoItem = dataSnapshot.getValue(MemoItem.class);
                    list.add(memoItem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity", error.getMessage());
            }
        });

        adapter.setDataToAdapter(list);
    }


    // Menu Resource 에 있는 xml의 내용을 App bar에 반영

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    // 만약 앱바(Appbar)에 표시된 액션 또는 오버플로우 메뉴가 선택되면
    // 액티비티 onOptionItemSelected() Method가 호출
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer_main.openDrawer(GravityCompat.START);
                // 만약에 좌측의 dehaze 버튼 클릭시의 drawer 페이지 구현부
                return true;
            case R.id.menu_item1:
                return true;
            case R.id.menu_item2:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // 뒤로가기 버튼에 대한 정의

    @Override
    public void onBackPressed() {
        // Navigation Bar가 on 되어 있는 상태에서 뒤로가기를 클릭 할 경우 닫기.
        if (drawer_main.isDrawerOpen(GravityCompat.START)) {
            drawer_main.closeDrawer(GravityCompat.START);
        }else if(System.currentTimeMillis()>backKeyPressedTime+2000){
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }else if(System.currentTimeMillis()<=backKeyPressedTime+2000){
            finishAffinity();
        } else {
            super.onBackPressed();
        }
    }

    // Floating Button 구현부
    private void onAddButtonClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
    }

    /**
     * 보여짐, 사라짐
     * @param clicked 클릭여부
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
     * 애니메이션 효과
     * @param clicked 클릭 여부
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
    
    
    
    /*** 네비게이션 드로어 Item Click Listener 구현부 ***/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.navi_item_setPw:
                Intent intent = new Intent(getApplicationContext(), setPwActivity.class);
                startActivityForResult(intent,REQUEST_SET_PW);
                navi_item_setPw.setChecked(true);
                return true;
            case R.id.navi_item_trash:
                Toast.makeText(this, "trash?", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.navi_item_inquiry:
                Toast.makeText(this, "inquiry", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }

    }

}