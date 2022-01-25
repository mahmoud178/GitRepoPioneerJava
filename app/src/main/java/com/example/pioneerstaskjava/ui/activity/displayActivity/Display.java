package com.example.pioneerstaskjava.ui.activity.displayActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pioneerstaskjava.R;
import com.example.pioneerstaskjava.data.model.Item;
import com.example.pioneerstaskjava.databinding.ActivityDisplayBinding;
import com.example.pioneerstaskjava.ui.activity.adapter.GitAdapter;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Display extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private DisplayViewModel viewModel;
    private ActivityDisplayBinding binding;
    private Boolean itemsVisibility = false;
    private DatePickerDialog datePickerDialog;
    private List<String> programmingLangItems;
    private String programmingLang = "";
    private Calendar newDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_display);
        setupUi();
        setupViewModel();
        setUpObservers();

    }

    private void setupUi() {
        Calendar myCalendar = Calendar.getInstance();
        programmingLangItems = Arrays.asList("All Languages",
                "java",
                "kotlin",
                "swift",
                "TypeScript",
                "Go",
                "Rust",
                "Python",
                "JavaScript",
                "Julia"
        );

        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                programmingLangItems);
        binding.spinner.setAdapter(spinnerAdapter);
        binding.spinner.setOnItemSelectedListener(this);
        binding.selectedDateTxt.setText(updateView(myCalendar.getTime()));
        newDate = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            newDate.set(year, monthOfYear, dayOfMonth);
            binding.selectedDateTxt.setText(updateView(newDate.getTime()));
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        binding.closeFilter.setOnClickListener(this);
        binding.filterClick.setOnClickListener(this);
        binding.dateLayout.setOnClickListener(this);
        binding.applyFilterClick.setOnClickListener(this);
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(DisplayViewModel.class);
    }

    private void setUpObservers() {
        viewModel.getDataSuccess().observe(this, gitModel -> {
                    binding.pbHeaderProgress.setVisibility(View.GONE);

                    if (!gitModel.getItems().isEmpty()) {
                        provideAdapterData(gitModel.getItems());
                    } else {
                        Toast.makeText(this, "No Data To Be Previewed", Toast.LENGTH_SHORT).show();

                    }
                }
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        if (view.equals(binding.applyFilterClick)) {
            validateInputs();
        } else if (view.equals(binding.closeFilter)) {
            itemsVisibility = true;
            binding.closeFilter.setVisibility(View.GONE);
            binding.filterComponents.setVisibility(View.GONE);

        } else if (view.equals(binding.filterClick)) {
            if (itemsVisibility) {
                binding.closeFilter.setVisibility(View.VISIBLE);
                binding.filterComponents.setVisibility(View.VISIBLE);
            }

        } else if (view.equals(binding.dateLayout)) {
            datePickerDialog.show();

        }
    }

    private void validateInputs() {
        if (binding.itemsCountEdit.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter Items Count To Be Viewed", Toast.LENGTH_SHORT).show();
        } else if (Integer.parseInt(binding.itemsCountEdit.getText().toString()) > 100 ||
                Integer.parseInt(binding.itemsCountEdit.getText().toString()) < 1) {
            Toast.makeText(this, "Items Count must be in range of 1-100", Toast.LENGTH_SHORT).show();
        } else {
            if (programmingLang.isEmpty()) {
                programmingLang = "language";

            } else {
                programmingLang = "language:" + programmingLang;
            }
            binding.pbHeaderProgress.setVisibility(View.VISIBLE);
            viewModel.getData("created:>" + updateView(newDate.getTime()),
                    binding.itemsCountEdit.getText().toString(), programmingLang);
            resetViews();

        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private String updateView(Date time) {
        String myFormatApi = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormatApi, Locale.US);
        return sdf.format(time);
    }

    private void resetViews() {
        programmingLang = "";
        binding.itemsCountEdit.setText("", TextView.BufferType.EDITABLE);
        binding.selectedLangTxt.setText(programmingLang);
        hideKeyboard();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {
            programmingLang = "";
        } else {
            programmingLang = programmingLangItems.get(i);
        }
        binding.selectedLangTxt.setText(programmingLang);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void provideAdapterData(List<Item> list) {
        GitAdapter gitAdapter = new GitAdapter(this, list);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.displayRecycler.setLayoutManager(manager);
        binding.displayRecycler.setAdapter(gitAdapter);
    }
}