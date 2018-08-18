package com.example.msnma.movienotifier;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieActivity extends BaseActivity {

    private MovieFragment movieFrag;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie, menu);
        menu.findItem(R.id.share)
                .setIcon(new IconicsDrawable(this)
                        .icon(GoogleMaterial.Icon.gmd_share)
                        .color(Color.BLACK)
                        .sizeDp(24));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.share:
                if (movieFrag != null) {
                    movieFrag.shareMovie();
                }
                break;
        }
        return true;
    }

    @Override
    protected void init() {
        movieFrag = new MovieFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, movieFrag)
                .commit();
    }

    //nuovo codice per alert dialog

    public void alertFormElements()
    {

        /*
         * Inflate the XML view. activity_main is in
         * res/layout/form_elements.xml
         */
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.form_elements,
                null, false);

        // You have to list down your form elements
        /*final CheckBox myCheckBox = (CheckBox) formElementsView
                .findViewById(R.id.myCheckBox);*/

        final RadioGroup genderRadioGroup = (RadioGroup) formElementsView
                .findViewById(R.id.NotifyAlertRadioGroup);

        //questo sarà sostituito con un calendario.
        final EditText nameEditText = (EditText) formElementsView
                .findViewById(R.id.nameEditText);

        // the alert dialog
        new AlertDialog.Builder(MovieActivity.this).setView(formElementsView)
                .setTitle("Form Elements")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {

                        String toastString = "";

                        /*
                         * Detecting whether the checkbox is checked or not.
                         */
                        /*if (myCheckBox.isChecked()) {
                            toastString += "Happy is checked!\n";
                        } else {
                            toastString += "Happy IS NOT checked.\n";
                        }*/

                        /*
                         * Getting the value of selected RadioButton.
                         */
                        // get selected radio button from radioGroup
                        int selectedId = genderRadioGroup
                                .getCheckedRadioButtonId();

                        // find the radiobutton by returned id
                        RadioButton selectedRadioButton = (RadioButton) formElementsView
                                .findViewById(selectedId);

                        toastString += "Selected radio button is: "
                                + selectedRadioButton.getText() + "!\n";

                        /*
                         * Getting the value of an EditText.
                         */
                        toastString += "Name is: " + nameEditText.getText()
                                + "!\n";

                        //showToast(toastString);

                        dialog.cancel();
                    }

                }).show();
    }

}
