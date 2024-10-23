package com.example.accommodate;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.accommodate.global.Action;
import com.example.accommodate.global.DateRange;
import com.example.accommodate.global.Pair;
import com.example.accommodate.global.Message;
import com.example.accommodate.global.Room;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BookRoomActivity extends AppCompatActivity {

    private ImageView apartmentImage;
    private TextView apartmentArea, apartmentPrice, apartmentDates, apartmentPeople, apartmentName;
    private RatingBar apartmentRating;
    private Button bookButton;
    private DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    private Button datePickerButtonStart, datePickerButtonEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_room);

        // Initialize UI elements
        apartmentName = findViewById(R.id.apartment_name);
        apartmentImage = findViewById(R.id.apartment_image);
        apartmentArea = findViewById(R.id.apartment_area);
        apartmentPrice = findViewById(R.id.apartment_price);

        apartmentPeople = findViewById(R.id.apartment_people);
        apartmentRating = findViewById(R.id.apartment_rating);
        datePickerButtonStart = findViewById(R.id.datePickerButtonStart);
        datePickerButtonEnd = findViewById(R.id.datePickerButtonEnd);

        // Initialize Date Pickers
        initDatePickers();

        // Set default dates
        datePickerButtonStart.setText(getTodaysDate());
        datePickerButtonEnd.setText(getTodaysDate());

        // Set onClick listeners
        datePickerButtonStart.setOnClickListener(this::openStartDatePicker);
        datePickerButtonEnd.setOnClickListener(this::openEndDatePicker);
        bookButton = findViewById(R.id.book_button);

        // Get the room details from the intent
        Room room = (Room) getIntent().getSerializableExtra("room");

        if (room != null) {
            apartmentName.setText(room.NAME);
            // Set the room details to the UI elements
            apartmentArea.setText("Area: " + room.AREA);
            apartmentPrice.setText("Price: $" + room.PRICE);
            apartmentPeople.setText("Max People: " + room.GUESTS);
            apartmentRating.setRating(room.STARS);

            // Load the image into the ImageView
            // Assuming you have a drawable resource ID or a URI in the IMAGE field of Room class
            // If it's a URI, use Uri.parse(room.IMAGE)
            apartmentImage.setImageResource(getResources().getIdentifier(room.IMAGE, "drawable", getPackageName()));

            // Handle book button click
            bookButton.setOnClickListener(v -> {
                // Implement your booking logic here
                // For example, starting another activity or showing a confirmation dialog
            });
        } else {
            // Handle the case where room is null
            finish(); // Close the activity and return to the previous one
        }

        // Set the listener for the RatingBar
        apartmentRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                // User changed the rating, handle it here

                String master = "192.168.1.220:4321";
                BookingAgent agent = new BookingAgent(master);
                Pair<String, Integer> pair = new Pair<>(apartmentName.getText().toString(), (int) rating);
                String response = agent.review(pair);

                Log.d(TAG, response);
                Toast.makeText(BookRoomActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        });

        bookButton.setOnClickListener(v -> performBook());
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
            datePickerButtonStart.setText(date);
        };

        DatePickerDialog.OnDateSetListener endDateSetListener = (datePicker, year, month, day) -> {
            String date = makeDateString(year, month + 1, day);
            datePickerButtonEnd.setText(date);
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

    private void performBook() {

        String startDateStr = datePickerButtonStart.getText().toString();
        String endDateStr = datePickerButtonEnd.getText().toString(); // Fixed this line to get the correct end date
        DateRange range = new DateRange(LocalDate.now().minusDays(1), LocalDate.now().minusDays(1));
        // Set default values for empty fields

        if (startDateStr.isEmpty()) startDateStr = null;
        if (endDateStr.isEmpty()) endDateStr = null;

        try {

            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDate startDate = startDateStr != null ? LocalDate.parse(startDateStr, formatter) : null;
            LocalDate endDate = endDateStr != null ? LocalDate.parse(endDateStr, formatter) : null;

            DateRange dateRange = startDate != null && endDate != null ? new DateRange(startDate, endDate) : null;
            startDate = LocalDate.of(2024, 9, 12);
            endDate = LocalDate.of(2024, 9, 15);

            // If using startDate and endDate directly, make sure they are of type LocalDate
            dateRange = new DateRange(startDate, endDate);
            Message msg = new Message(0, Action.BOOK, dateRange);
            String master = "192.168.1.220:4321";
            BookingAgent agent = new BookingAgent(master);
            Pair<String, DateRange> pair = new Pair<>(apartmentName.getText().toString(), dateRange);
            String response = agent.book(pair);

            Log.d(TAG, response);
            Toast.makeText(this, "Booking response: " + response, Toast.LENGTH_LONG).show();

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
