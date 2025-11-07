package com.example.studymate.ui.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.studymate.R;
import com.example.studymate.repository.UserRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Nếu có toolbar trong layout:
        // MaterialToolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            showLogoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logout)
                .setMessage(R.string.logout_confirm)
                .setPositiveButton(R.string.ok, (d, w) -> {
                    // chỉ clear session
                    new UserRepository(getApplicationContext()).logout();
                    Toast.makeText(this, R.string.logged_out, Toast.LENGTH_SHORT).show();

                    // quay về Login
                    NavHostFragment host = (NavHostFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.nav_host_fragment);
                    if (host != null) {
                        NavController nav = host.getNavController();
                        // xóa backstack và về login
                        nav.popBackStack(R.id.loginFragment, false);
                        nav.navigate(R.id.loginFragment);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
