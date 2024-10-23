package com.example.accommodate;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import com.example.accommodate.global.*;

public class SearchActivity extends AppCompatActivity {

    private EditText etArea, etPrice, etStars, etGuests;
    private Button btnSearch;
    private DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    private Button startDateButton, endDateButton;

    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize UI elements
        etArea = findViewById(R.id.et_area);
        etPrice = findViewById(R.id.et_price);
        etStars = findViewById(R.id.et_stars);
        etGuests = findViewById(R.id.et_guests);
        btnSearch = findViewById(R.id.btn_search);
        startDateButton = findViewById(R.id.datePickerButtonStart);
        endDateButton = findViewById(R.id.datePickerButtonEnd);

        // Initialize Date Pickers
        initDatePickers();

        // Set default dates
        startDateButton.setText(getTodaysDate());
        endDateButton.setText(getTodaysDate());

        // Set onClick listeners
        startDateButton.setOnClickListener(this::openStartDatePicker);
        endDateButton.setOnClickListener(this::openEndDatePicker);
        btnSearch.setOnClickListener(v -> performSearch());
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(year, month + 1, day); // Year, Month, Day
    }

    private void initDatePickers() {
        DatePickerDialog.OnDateSetListener startDateSetListener = (datePicker, year, month, day) -> {
            String date = makeDateString(year, month + 1, day);
            startDateButton.setText(date);
        };

        DatePickerDialog.OnDateSetListener endDateSetListener = (datePicker, year, month, day) -> {
            String date = makeDateString(year, month + 1, day);
            endDateButton.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        startDatePickerDialog = new DatePickerDialog(this, style, startDateSetListener, year, month, day);
        endDatePickerDialog = new DatePickerDialog(this, style, endDateSetListener, year, month, day);
    }

    private String makeDateString(int year, int month, int day) {
        return String.format("%04d-%02d-%02d", year, month, day); // Format as YYYY-MM-DD
    }

    public void openStartDatePicker(View view) {
        startDatePickerDialog.show();
    }

    public void openEndDatePicker(View view) {
        endDatePickerDialog.show();
    }

    private void performSearch() {
        String area = etArea.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String starsStr = etStars.getText().toString().trim();
        String guestsStr = etGuests.getText().toString().trim();
        String startDateStr = startDateButton.getText().toString();
        String endDateStr = endDateButton.getText().toString();
        DateRange range = new DateRange(LocalDate.now().minusDays(1), LocalDate.now().minusDays(1));
        // Set default values for empty fields
        if (priceStr.isEmpty()) priceStr = "0";
        if (starsStr.isEmpty()) starsStr = "0";
        if (guestsStr.isEmpty()) guestsStr = "0";

        if (startDateStr.isEmpty()) startDateStr = null;
        if (endDateStr.isEmpty()) endDateStr = null;

        try {
            int price = Integer.parseInt(priceStr);
            int stars = Integer.parseInt(starsStr);
            int guests = Integer.parseInt(guestsStr);

            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDate startDate = startDateStr != null ? LocalDate.parse(startDateStr, formatter) : null;
            LocalDate endDate = endDateStr != null ? LocalDate.parse(endDateStr, formatter) : null;

            DateRange dateRange = startDate != null && endDate != null ? new DateRange(startDate, endDate) : null;
            Filter filter = new Filter(area, dateRange, guests, price, stars);
            Message msg = new Message(0, Action.SEARCH, filter);
            String master = "192.168.1.220:4321";
            BookingAgent agent = new BookingAgent(master);
            ArrayList<Room> rooms = agent.search(filter);

            Log.d(TAG, "Number of rooms found: " + rooms.size());
            Intent intent = new Intent(SearchActivity.this, ApartmentListActivity.class);
            intent.putExtra("rooms", rooms);

            startActivity(intent);

            String message = rooms.size()+"Searching for rooms in " + (area != null ? area : "any area") +
                    (startDate != null && endDate != null ? " from " + startDate + " to " + endDate : "") +
                    " for " + price + " with " + stars + " stars.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numeric values for price, stars, and guests.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "NumberFormatException: " + e.getMessage());
        } catch (DateTimeParseException e) {
            Toast.makeText(this, "Invalid date format. Please select the date again.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "DateTimeParseException: " + e.getMessage());
        } catch (Exception e) {
            Toast.makeText(this, "An unexpected error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Exception: " + e.getMessage(), e);
        }
    }
}
