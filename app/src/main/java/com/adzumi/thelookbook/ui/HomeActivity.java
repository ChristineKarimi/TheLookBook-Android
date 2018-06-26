package com.adzumi.thelookbook.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.adzumi.thelookbook.Constants;
import com.adzumi.thelookbook.R;
import com.adzumi.thelookbook.adapters.CustomAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mSearchedAuthorReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

//    @BindView(R.id.introductionTextView) TextView introductionTextView;
//    @BindView(R.id.winnersTextView) TextView mWinnersTextView;
    @BindView(R.id.listView) ListView mListView;
//    @BindView(R.id.booksTextView) TextView mBooksTextView;
//    @BindView(R.id.searchAuthorEditText) EditText mSearchAuthorEditText;
//    @BindView(R.id.searchAuthorButton) Button mSearchAuthorButton;

    public static final String TAG = HomeActivity.class.getSimpleName();

    private String[] books = new String[] {"NON-FICTION", "FICTION",
            "YOUNG ADULT", "THRILLER", "FANTASY"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSearchedAuthorReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.FIREBASE_CHILD_SEARCHED_BOOK);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
//                    getSupportActionBar().setTitle("Welcome, " + user.getDisplayName() + "!");
                } else {

                }
            }
        };

//        Typeface openSansFontLight = Typeface.createFromAsset(getAssets(), "fonts/opensans_light.ttf");
//        introductionTextView.setTypeface(openSansFontLight);
//
//        Typeface openSansFontItalic = Typeface.createFromAsset(getAssets(), "fonts/opensans_italic.ttf");
//        mWinnersTextView.setTypeface(openSansFontItalic);
//
//        Typeface openSansFontSemiBold = Typeface.createFromAsset(getAssets(), "fonts/opensans_semibold.ttf");
//        mBooksTextView.setTypeface(openSansFontSemiBold);

        ArrayAdapter adapter = new CustomAdapter(this, books);
        mListView.setAdapter(adapter);

//        mSearchAuthorButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
//        if(v == mSearchAuthorButton){
//            String author = mSearchAuthorEditText.getText().toString();
//            saveAuthorToFirebase(author);
//            Intent intent = new Intent(HomeActivity.this, FaveAuthorDetailActivity.class);
//            intent.putExtra("author", author);
//            startActivity(intent);
//        }
    }

    public void saveAuthorToFirebase(String author) {
        mSearchedAuthorReference.setValue(author);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Title, author, or ISBN");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(HomeActivity.this, SearchResultsActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
