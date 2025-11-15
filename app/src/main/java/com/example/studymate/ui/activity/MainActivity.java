package com.example.studymate.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration; // ⭐️ THÊM IMPORT NÀY
import androidx.navigation.ui.NavigationUI; // ⭐️ THÊM IMPORT NÀY

import com.example.studymate.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private AppBarLayout appBarLayout;

    // ⭐️ BƯỚC 1: Thêm biến cho AppBarConfiguration
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarLayout = findViewById(R.id.appBarLayout);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            // ⭐️ BƯỚC 2: ĐỊNH NGHĨA CÁC MÀN HÌNH "TOP-LEVEL"
            // (Đây là các màn hình Home, sẽ KHÔNG có mũi tên quay lại)
            Set<Integer> topLevelDestinations = new HashSet<>();
            topLevelDestinations.add(R.id.loginFragment);
            topLevelDestinations.add(R.id.homeAdminFragment);
            topLevelDestinations.add(R.id.homeTeacherFragment);
            topLevelDestinations.add(R.id.homeStudentFragment);

            appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).build();

            // ⭐️ BƯỚC 3: LIÊN KẾT TOOLBAR VỚI NAVCONTROLLER
            // (Hàm này sẽ tự động:
            //   1. Hiển thị/ẩn mũi tên quay lại.
            //   2. Tự động đặt Tiêu đề (Title) dựa trên android:label)
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


            // (Listener của bạn vẫn giữ nguyên để ẨN Toolbar)
            navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                @Override
                public void onDestinationChanged(@NonNull NavController controller,
                                                 @NonNull NavDestination destination,
                                                 @Nullable Bundle arguments) {

                    // (BẠN KHÔNG CẦN DÒNG NÀY NỮA, VÌ BƯỚC 3 ĐÃ LÀM)
                    // if (getSupportActionBar() != null && destination.getLabel() != null) {
                    //    getSupportActionBar().setTitle(destination.getLabel());
                    // }

                    // Logic ẩn/hiện Toolbar (giữ nguyên)
                    if (destination.getId() == R.id.loginFragment) {
                        appBarLayout.setVisibility(View.GONE);
                    } else {
                        appBarLayout.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    // ⭐️ BƯỚC 4: XỬ LÝ SỰ KIỆN KHI BẤM NÚT MŨI TÊN
    @Override
    public boolean onSupportNavigateUp() {
        // (Bảo cho NavigationUI xử lý việc quay lại)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // ⭐️ BƯỚC 5: (Tùy chọn) Xử lý sự kiện khi bấm nút Menu (như Logout)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // (Bảo cho NavigationUI xử lý trước, nếu thất bại thì gọi super)
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }
}

