package rs.ac.uns.ftn.findaroommate.activity;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.chip.ChipGroup.OnCheckedChangeListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import rs.ac.uns.ftn.findaroommate.MainActivity;
import rs.ac.uns.ftn.findaroommate.R;
import rs.ac.uns.ftn.findaroommate.model.Language;
import rs.ac.uns.ftn.findaroommate.model.UserCharacteristic;
import rs.ac.uns.ftn.findaroommate.provider.GenericFileProvider;
import rs.ac.uns.ftn.findaroommate.utils.Mockup;

public class ProfileFormActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser loggedUser;

    DatePickerDialog picker;
    TextInputEditText dateEditText;
    TextInputEditText languageEditText;

    TextInputEditText firstNameEdit;

    ChipGroup personalityChipGroup;
    ChipGroup lifestyleChipGroup;
    ChipGroup musicChipGroup;
    ChipGroup filmChipGroup;
    ChipGroup sportChipGroup;


    Button saveButton;
    Button uploadOptionButton;
    Button galleryOptionButton;


    ImageButton personalityButton;
    ImageButton lifestyleButton;
    ImageButton sportButton;
    ImageButton filmButton;
    ImageButton musicButton;

    ImageButton uploadPhotoButton;
    private Uri file;
    ImageView photoView;

    AlertDialog alertDialog;

    private static final int LANGUAGE_CHOSER_ACTIVITY = 99;
    private static final int USER_ATTRIBUTES_ACTIVITY = 100;
    private static final int TAKE_PHOTO_ACTIVITY = 101;
    private static final int UPLOAD_PHOTO_ACTIVITY = 102;


    private ClickListener clickListener = new ClickListener();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == LANGUAGE_CHOSER_ACTIVITY) {
            if (data.hasExtra("selectedLanguages")) {
                CharSequence[] a = data.getExtras().getCharSequenceArray("selectedLanguages");
                StringBuilder builder = new StringBuilder();
                for(int i = 0; i < a.length; i++){
                    builder.append(a[i]);
                    if( i < a.length - 1)
                        builder.append(", ");
                }
                languageEditText.setText(builder.toString());
            }
        }

        if (resultCode == RESULT_OK && requestCode == USER_ATTRIBUTES_ACTIVITY) {
            CharSequence[] characteritics = data.getExtras().getCharSequenceArray("selectedPersonalities");
            personalityChipGroup.removeAllViews();
            createLanguageChips(characteritics, personalityChipGroup);

            characteritics = data.getExtras().getCharSequenceArray("selectedLifestyles");
            lifestyleChipGroup.removeAllViews();
            createLanguageChips(characteritics, lifestyleChipGroup);

            characteritics = data.getExtras().getCharSequenceArray("selectedMusics");
            musicChipGroup.removeAllViews();
            createLanguageChips(characteritics, musicChipGroup);

            characteritics = data.getExtras().getCharSequenceArray("selectedFilms");
            filmChipGroup.removeAllViews();
            createLanguageChips(characteritics, filmChipGroup);

            characteritics = data.getExtras().getCharSequenceArray("selectedSport");
            sportChipGroup.removeAllViews();
            createLanguageChips(characteritics, sportChipGroup);
        }

        if(resultCode == RESULT_OK && requestCode == TAKE_PHOTO_ACTIVITY){
            photoView.setImageURI(file);
        }

        if(resultCode == RESULT_OK && requestCode == UPLOAD_PHOTO_ACTIVITY){
            if(data!= null){
                Uri selectedImage = data.getData();
                photoView.setImageURI(selectedImage);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_form);

        mAuth = FirebaseAuth.getInstance();
        loggedUser = mAuth.getCurrentUser();


        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_form_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner genderSpinner = (Spinner) findViewById(R.id.profile_form_gender_spinner);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_values, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
        // SETOVANJE...
        int spinnerPosition = genderAdapter.getPosition("Female");
        genderSpinner.setSelection(spinnerPosition);

        Spinner ocupationSpinner = (Spinner) findViewById(R.id.profile_form_occupation_spinner);
        ArrayAdapter<CharSequence> occupationAdapter = ArrayAdapter.createFromResource(this,
                R.array.occupation_values, android.R.layout.simple_spinner_item);
        occupationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ocupationSpinner.setAdapter(occupationAdapter);

        Spinner studyLevelSpinner = (Spinner) findViewById(R.id.profile_form_study_level_spinner);
        ArrayAdapter<CharSequence> studyLevelAdapter = ArrayAdapter.createFromResource(this,
                R.array.study_level_values, android.R.layout.simple_spinner_item);
        studyLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        studyLevelSpinner.setAdapter(studyLevelAdapter);

        //Viktor added
        firstNameEdit = (TextInputEditText) findViewById(R.id.edit_first_name);

        dateEditText= (TextInputEditText) findViewById(R.id.input_date_picker);
        dateEditText.setInputType(InputType.TYPE_NULL);
        //dateEditText.setOnClickListener(clickListener);
        dateEditText.setOnFocusChangeListener(new FocusListener());

        languageEditText= (TextInputEditText) findViewById(R.id.input_language);
        languageEditText.setOnFocusChangeListener(new FocusListener());
        languageEditText.setInputType(InputType.TYPE_NULL);

        saveButton = (Button) findViewById(R.id.profile_save_button);
        saveButton.setOnClickListener(clickListener);

        uploadPhotoButton = (ImageButton) findViewById(R.id.btn_upload_photo);
        uploadPhotoButton.setOnClickListener(clickListener);
        photoView = (ImageView) findViewById(R.id.profile_picture);

        personalityButton = (ImageButton) findViewById(R.id.btn_personality_select);
        lifestyleButton = (ImageButton) findViewById(R.id.btn_lifestyle_select);
        sportButton = (ImageButton) findViewById(R.id.btn_sport_select);
        filmButton = (ImageButton) findViewById(R.id.btn_film_select);
        musicButton = (ImageButton) findViewById(R.id.btn_music_select);

        personalityButton.setOnClickListener(clickListener);
        lifestyleButton.setOnClickListener(clickListener);
        sportButton.setOnClickListener(clickListener);
        filmButton.setOnClickListener(clickListener);
        musicButton.setOnClickListener(clickListener);


        personalityChipGroup = (ChipGroup) findViewById(R.id.personality_chips);
        lifestyleChipGroup = (ChipGroup) findViewById(R.id.lifestyle_chips);
        musicChipGroup = (ChipGroup) findViewById(R.id.music_chips);
        filmChipGroup = (ChipGroup) findViewById(R.id.film_chips);
        sportChipGroup = (ChipGroup) findViewById(R.id.sport_chips);

        // ako nema u manifestu ovo zabrani na dugmetu da se slika...
        // nekad se otvori dijalog pa na osnovu odgovora se poziva listener ispod i interpretira
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            uploadPhotoButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            // ako su ove obe stvari u manifestu prikazi dugme, u suprotnom zadrzi onemoguceno
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                uploadPhotoButton.setEnabled(true);
            }
        }
    }

    private void createLanguageChips(CharSequence[] characteristics, ChipGroup chips){
        for(int i = 0; i < characteristics.length; i++){
            Chip c = (Chip) this.getLayoutInflater().inflate(R.layout.chip_tag, null, false);
            c.setText(characteristics[i]);
            chips.addView(c);
        }
    }

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            boolean t = true;
            if(v == dateEditText){
                TextInputEditText vo = (TextInputEditText)v;
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ProfileFormActivity.this, null, year, month, day);
                picker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateEditText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                });
                picker.show();
            } else if(v == saveButton){
                Toast.makeText(ProfileFormActivity.this, getString(R.string.profile_form_success), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ProfileFormActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            } else if(v == uploadPhotoButton) {
                // dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileFormActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.upload_dialog, viewGroup, false);
                builder.setView(dialogView);
                alertDialog = builder.create();
                galleryOptionButton = dialogView.findViewById(R.id.btn_upload_option);
                uploadOptionButton = dialogView.findViewById(R.id.btn_take_photo_option);
                galleryOptionButton.setOnClickListener(new UploadDialogClick());
                uploadOptionButton.setOnClickListener(new UploadDialogClick());
                alertDialog.show();
            } else {
                Intent intent = new Intent(getApplicationContext(), UserAttributesActivity.class);
                intent.putExtra("key", "value");
                startActivityForResult(intent, USER_ATTRIBUTES_ACTIVITY);
            }
        }
    }

    class UploadDialogClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();

            switch (id){
                case R.id.btn_upload_option:
                    alertDialog.cancel();
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, UPLOAD_PHOTO_ACTIVITY);

                    break;
                case R.id.btn_take_photo_option:
                    alertDialog.cancel();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    File outputMediaFile = getOutputMediaFile();
                    if(outputMediaFile != null){
                        file = FileProvider.getUriForFile(ProfileFormActivity.this, GenericFileProvider.MY_PROVIDER, outputMediaFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, file); //za skladistenje andoidovog serializable, tj parceable

                        startActivityForResult(intent, TAKE_PHOTO_ACTIVITY);
                    }
                    break;
            }
        }
    }

    class FocusListener implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(v == dateEditText){
                if(hasFocus){
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    picker = new DatePickerDialog(ProfileFormActivity.this, null, year, month, day);
                    picker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            dateEditText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        }
                    });
                    picker.show();
                }
            } else if (v == languageEditText){
                if(hasFocus){
                    Intent intent = new Intent(getApplicationContext(), LanguageChooserActivity.class);
                    intent.putExtra("key", "value");
                    startActivityForResult(intent, LANGUAGE_CHOSER_ACTIVITY);
                }
            }
        }
    }

    private static File getOutputMediaFile(){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "FindARoommate");

        String path = mediaStorageDir.getPath();

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdir()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

}
