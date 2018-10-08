package com.example.thamt.daly;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.thamt.daly.Services.MessengingServiceFirestoreDao;
import com.example.thamt.daly.TaskList.TaskFragment;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {
  private static final int RC_SIGN_IN = 1;
  private FirebaseAuth auth;
  private FirebaseUser firebaseUser;

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
    = item -> {
    Bundle bundle = new Bundle();
    switch (item.getItemId()) {
      case R.id.navigation_liling:
        bundle.putString("checklistName", "liling");
        loadTaskFragment(false, bundle);
        return true;
      case R.id.navigation_shared:
        bundle.putString("checklistName", "shared");
        loadTaskFragment(false, bundle);
        return true;
      case R.id.navigation_thomas:
        bundle.putString("checklistName", "thomas");
        loadTaskFragment(false, bundle);
        return true;
    }
    return false;
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tasklist);

    BottomNavigationView navigation = findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    Toolbar myToolbar = findViewById(R.id.my_toolbar);
    setSupportActionBar(myToolbar);

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
      .setTimestampsInSnapshotsEnabled(true)
      .build();
    firestore.setFirestoreSettings(settings);

    auth = FirebaseAuth.getInstance();

    Bundle bundle = new Bundle();
    bundle.putString("checklistName", "shared");
    loadTaskFragment(true, bundle);
  }

  private void loadTaskFragment(boolean initialLoad, Bundle bundle) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    Fragment fragment = new TaskFragment();
    fragment.setArguments(bundle);

    fragmentTransaction.replace(R.id.fragment_container, fragment, getString(R.string.TASK_FRAGMENT_LABEL));
    if (!initialLoad) {
      fragmentTransaction.addToBackStack(null);
    }
    fragmentTransaction.commit();
  }

  @SuppressLint("ApplySharedPref")
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == RC_SIGN_IN) {
      IdpResponse response = IdpResponse.fromResultIntent(data);

      if (resultCode == RESULT_OK) {
        // Successfully signed in
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // Send token to server
        sendTokenToServer();
      } else {
        finishAndRemoveTask();
      }
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    firebaseUser = auth.getCurrentUser();
    if (firebaseUser == null) {
      showLoginScreen();
    } else {
      sendTokenToServer();
    }
  }

  private void sendTokenToServer() {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    String token = sharedPreferences.getString(getString(R.string.user_token), null);
    if (token == null) {
      FirebaseInstanceId.getInstance().getInstanceId()
        .addOnCompleteListener(task -> {
          if (!task.isSuccessful()) {
            return;
          }
          // Get new Instance ID token
          String token1 = task.getResult().getToken();
          sharedPreferences.edit().putString(getString(R.string.user_token), token1).apply();
          MessengingServiceFirestoreDao.setToken(firebaseUser.getUid(), token1);
        });
    } else {
      MessengingServiceFirestoreDao.setToken(firebaseUser.getUid(), token);
    }
  }

  private void showLoginScreen() {
    // Choose authentication providers
    List<AuthUI.IdpConfig> providers = Arrays.asList(
      new AuthUI.IdpConfig.EmailBuilder().build(),
      new AuthUI.IdpConfig.PhoneBuilder().build(),
      new AuthUI.IdpConfig.GoogleBuilder().build());

    startActivityForResult(
      AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build(),
      RC_SIGN_IN);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.action_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.logout:
        auth.signOut();
        showLoginScreen();
        return true;

      default:
        return super.onOptionsItemSelected(item);

    }
  }
}
