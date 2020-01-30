package org.digitalcampus.oppia.fragments.register;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;

import com.badoualy.stepperindicator.StepperIndicator;

import org.digitalcampus.mobile.learning.R;
import org.digitalcampus.oppia.application.MobileLearning;
import org.digitalcampus.oppia.model.Country;
import org.digitalcampus.oppia.model.District;
import org.digitalcampus.oppia.model.User;
import org.digitalcampus.oppia.utils.ui.ValidableTextInputLayout;

import java.util.ArrayList;
import java.util.List;


public class RegisterCHFragment extends RegisterBaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String ARG_ROLE = "arg_role";
    private int currentScreen = 0;

    private TextView tvExplanationRegScreen;
    private LinearLayout viewRegChScreen1;
    private ValidableTextInputLayout editRegChFirstName;
    private ValidableTextInputLayout editRegChLastName;
    private ValidableTextInputLayout editRegChEmployeeId;
    private LinearLayout viewRegChScreen2;
    private ValidableTextInputLayout editRegChPassword;
    private ValidableTextInputLayout editRegChPasswordAgain;
    private LinearLayout viewRegChScreen3;
    private AppCompatSpinner spinnerCountries;
    private AppCompatSpinner spinnerDistricts;
    private TextView btnRegisterChPrevious;
    private TextView btnRegisterChNext;
    private List<Country> countries = new ArrayList<>();
    private ArrayAdapter<Country> adapterCountries;
    private List<District> districts = new ArrayList<>();
    private ArrayAdapter<District> adapterDistricts;
    private StepperIndicator stepperIndicator;
    private AppCompatButton btnRegisterPerform;
    private String role;

    private void findViews(View layout) {
        tvExplanationRegScreen = layout.findViewById(R.id.tv_explanation_reg_screen);
        viewRegChScreen1 = layout.findViewById(R.id.view_reg_ch_screen_1);
        editRegChFirstName = layout.findViewById(R.id.edit_reg_ch_first_name);
        editRegChLastName = layout.findViewById(R.id.edit_reg_ch_last_name);
        editRegChEmployeeId = layout.findViewById(R.id.edit_reg_ch_employee_id);
        viewRegChScreen2 = layout.findViewById(R.id.view_reg_ch_screen_2);
        editRegChPassword = layout.findViewById(R.id.edit_reg_ch_password);
        editRegChPasswordAgain = layout.findViewById(R.id.edit_reg_ch_password_again);
        viewRegChScreen3 = layout.findViewById(R.id.view_reg_ch_screen_3);
        spinnerCountries = layout.findViewById(R.id.spinner_countries);
        spinnerDistricts = layout.findViewById(R.id.spinner_districts);
        btnRegisterChPrevious = layout.findViewById(R.id.btn_register_ch_previous);
        stepperIndicator = layout.findViewById(R.id.stepper_indicator);
        btnRegisterChNext = layout.findViewById(R.id.btn_register_ch_next);
        btnRegisterPerform = layout.findViewById(R.id.btn_register_perform);

        btnRegisterChPrevious.setOnClickListener(this);
        btnRegisterChNext.setOnClickListener(this);
        btnRegisterPerform.setOnClickListener(this);
    }

    public static RegisterCHFragment getInstance(String role) {
        RegisterCHFragment fragment = new RegisterCHFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ROLE, role);
        fragment.setArguments(args);
        return fragment;
    }

    public RegisterCHFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_register_ch, container, false);
        findViews(layout);

        role = getArguments().getString(ARG_ROLE);
        if (role == null) {
            throw new IllegalArgumentException("Role argument missing");
        }

        configureCountries();

        updateScreen();

        return layout;
    }

    private void configureCountries() {

        countries.clear();
        countries.addAll(Country.getDemoCountries());
        countries.add(0, new Country(getString(R.string.select_country)));

        adapterCountries = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, countries);
        spinnerCountries.setAdapter(adapterCountries);
        spinnerCountries.setOnItemSelectedListener(this);

        adapterDistricts = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, districts);
        districts.add(0, new District(getString(R.string.select_district)));
        spinnerDistricts.setAdapter(adapterDistricts);
        spinnerDistricts.setEnabled(false);

    }

    private void updateScreen() {

        viewRegChScreen1.setVisibility(currentScreen == 0 ? View.VISIBLE : View.GONE);
        viewRegChScreen2.setVisibility(currentScreen == 1 ? View.VISIBLE : View.GONE);
        viewRegChScreen3.setVisibility(currentScreen == 2 ? View.VISIBLE : View.GONE);


        stepperIndicator.setCurrentStep(currentScreen);

//        btnRegisterChPrevious.setVisibility(currentScreen > 0 ? View.VISIBLE : View.GONE);
        btnRegisterChNext.setVisibility(currentScreen < 2 ? View.VISIBLE : View.GONE);

        showRegisterButton(currentScreen == 2);

        String[] explanations = getResources().getStringArray(R.array.registration_screens_explanations);
        tvExplanationRegScreen.setText(explanations[currentScreen]);


    }

    private void showRegisterButton(boolean show) {
        btnRegisterPerform.setVisibility(show ? View.VISIBLE : View.GONE);
        btnRegisterChNext.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_register_ch_previous:
                currentScreen--;
                if (currentScreen == -1) {
                    ((RegisterMainFragment) getParentFragment()).goBack();
                } else {
                    updateScreen();
                }
                break;

            case R.id.btn_register_ch_next:
                if (validateFieldsOfCurrentScreen()) {
                    currentScreen++;
                    updateScreen();
                }
                break;

            case R.id.btn_register_perform:
                ((RegisterMainFragment) getParentFragment()).registerUser(getUserData());
                break;
        }

    }

    private User getUserData() {

        User user = new User();
        user.setFirstname(editRegChFirstName.getCleanedValue());
        user.setLastname(editRegChLastName.getCleanedValue());
        user.setEmployeeID(editRegChEmployeeId.getCleanedValue());
        user.autogenerateUsername();
        user.setPassword(editRegChPassword.getCleanedValue());
        user.setPasswordAgain(editRegChPasswordAgain.getCleanedValue());
        user.setJobTitle(role);
        user.setCountry(countries.get(spinnerCountries.getSelectedItemPosition()).getName());
        user.setDistrict(districts.get(spinnerDistricts.getSelectedItemPosition()).getName());
        return user;

    }


    // Spinner Countries selection
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        spinnerDistricts.setSelection(0);

        if (position == 0) {
            spinnerDistricts.setEnabled(false);
        } else {
            spinnerDistricts.setEnabled(true);
            Country country = countries.get(position);

            districts.clear();
            districts.addAll(country.getDistricts());
            districts.add(0, new District(getString(R.string.select_district)));
            adapterDistricts.notifyDataSetChanged();

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean validateFieldsOfCurrentScreen() {

        switch (currentScreen) {
            case 0:
                return editRegChFirstName.validate() && editRegChLastName.validate() && editRegChEmployeeId.validate();

            case 1:
                if (editRegChPassword.validate() && editRegChPasswordAgain.validate()) {

                    String password = editRegChPassword.getCleanedValue();
                    String passwordAgain = editRegChPasswordAgain.getCleanedValue();

                    // check password length
                    if (password.length() < MobileLearning.PASSWORD_MIN_LENGTH) {
                        editRegChPassword.setErrorEnabled(true);
                        editRegChPassword.setError(getString(R.string.error_register_password, MobileLearning.PASSWORD_MIN_LENGTH));
                        editRegChPassword.requestFocus();
                        return false;
                    }

                    // check password match
                    if (!password.equals(passwordAgain)) {
                        editRegChPasswordAgain.setErrorEnabled(true);
                        editRegChPasswordAgain.setError(getString(R.string.error_register_password_no_match));
                        editRegChPasswordAgain.requestFocus();
                        return false;
                    }

                    return true;
                }
                break;

            case 2:
                if (spinnerCountries.getSelectedItemPosition() == 0 || spinnerDistricts.getSelectedItemPosition() == 0) {
                    toast(R.string.please_select_country_district);
                } else {
                    return true;
                }
                break;
        }

        return false;
    }


}
