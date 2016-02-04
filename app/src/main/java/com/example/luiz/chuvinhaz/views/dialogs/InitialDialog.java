package com.example.luiz.chuvinhaz.views.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.luiz.chuvinhaz.R;
import com.example.luiz.chuvinhaz.models.User;


public class InitialDialog extends DialogFragment {

    private final String INDOORS_LAYOUT = "indoors_layout";
    private final String OUTDOORS_LAYOUT = "outdoors_layout";
    private final String CAR_LAYOUT = "car_layout";

    private View air_layout;
    private View wind_layout;

    private EditText users_name;

    private SeekBar rainLevelSeekbar;

    private SeekBar tempLevelSeekbar;
    private TextView tempLevelProgress;

    private SeekBar humidityLevelSeekbar;



    private SeekBar windLevelSeekbar;


    private ImageView[] weatherImages;
    private ImageView[] humidityImages;
    private ImageView[] windImages;

    private RadioGroup indoorOrOutdoorRadioGroup;
    private RadioGroup dayOrNightRadioGroup;
    private RadioGroup airOnOrOffRadioGroup;

    private static int weather_last_selected = 0;
    private static int humidity_last_selected = 0;
    private static int wind_last_selected = 0;

    public interface InitialDialogListener {
        public void onInitialDialogPositiveClick(String name, int location, int day_or_night, int rainLevel, int temp, int windLevel, int humidityLevel, int air);
    }

    InitialDialogListener mListener;

    public static InitialDialog newInstance(User user) {
        InitialDialog f = new InitialDialog();


        Bundle args = new Bundle();
        args.putString("name", user.getName());
        args.putInt("location", user.getUser_indoor_or_outdoor());
        args.putInt("day_or_night", user.getUser_day_or_night());
        args.putInt("rain_level", user.getUser_rain_level());
        args.putInt("temp_level", user.getUser_temp());
        args.putInt("wind_level", user.getUser_wind_level());
        args.putInt("humidity_level", user.getUser_humidity_level());
        args.putInt("air", user.getUser_air());

        f.setArguments(args);

        return f;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (InitialDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public void showLayout(String layout)
    {
        if(layout.equals(INDOORS_LAYOUT))
        {
            wind_layout.setVisibility(View.GONE);
            air_layout.setVisibility(View.VISIBLE);
        }
        else if(layout.equals(OUTDOORS_LAYOUT))
        {
            air_layout.setVisibility(View.GONE);
            wind_layout.setVisibility(View.VISIBLE);
        }
        else if(layout.equals(CAR_LAYOUT))
        {
            air_layout.setVisibility(View.VISIBLE);
            wind_layout.setVisibility(View.VISIBLE);
        }
    }

    public void setEvents()
    {
        for(int i = 0; i < weatherImages.length; ++i)
        {
            final int index = i;
            weatherImages[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setAlpha(1.0f);
                    weatherImages[weather_last_selected].setAlpha(0.5f);
                    rainLevelSeekbar.setProgress(index);
                    weather_last_selected = index;
                }
            });
        }

        for(int i = 0; i < humidityImages.length; ++i)
        {
            final int index = i;
            humidityImages[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setAlpha(1.0f);
                    humidityImages[humidity_last_selected].setAlpha(0.5f);
                    humidityLevelSeekbar.setProgress(index);
                    humidity_last_selected = index;
                }
            });
        }

        for(int i = 0; i < windImages.length; ++i)
        {
            final int index = i;
            windImages[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setAlpha(1.0f);
                    windImages[wind_last_selected].setAlpha(0.5f);
                    windLevelSeekbar.setProgress(index);
                    wind_last_selected = index;
                }
            });
        }

        tempLevelSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tempLevelProgress.setText((progress) + "°C");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        rainLevelSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                weatherImages[progress].setAlpha(1.0f);
                weatherImages[weather_last_selected].setAlpha(0.5f);

                weather_last_selected = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        humidityLevelSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                humidityImages[progress].setAlpha(1.0f);
                humidityImages[humidity_last_selected].setAlpha(0.5f);

                humidity_last_selected = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        windLevelSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                windImages[progress].setAlpha(1.0f);
                windImages[wind_last_selected].setAlpha(0.5f);

                wind_last_selected = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        indoorOrOutdoorRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.indoor)
                    showLayout(INDOORS_LAYOUT);
                else if (checkedId == R.id.outdoor)
                    showLayout(OUTDOORS_LAYOUT);
                else
                    showLayout(CAR_LAYOUT);
            }
        });

    }

    public void getWidgets(View view)
    {
        air_layout = view.findViewById(R.id.has_air_layout);
        wind_layout = view.findViewById(R.id.wind_layout);

        users_name = (EditText) view.findViewById(R.id.users_name_edit_text);
        rainLevelSeekbar = (SeekBar) view.findViewById(R.id.rain_level_seekbar);
        tempLevelProgress = (TextView) view.findViewById(R.id.temp_seekbar_progress);
        tempLevelSeekbar = (SeekBar) view.findViewById(R.id.temp_seekbar);
        windLevelSeekbar = (SeekBar) view.findViewById(R.id.wind_level_seekbar);
        humidityLevelSeekbar = (SeekBar) view.findViewById(R.id.humidity_level_seekbar);

        weatherImages = new ImageView[6];

        weatherImages[0] = (ImageView) view.findViewById(R.id.clear);
        weatherImages[1] = (ImageView) view.findViewById(R.id.mostly_cloud);
        weatherImages[2] = (ImageView) view.findViewById(R.id.cloudy);
        weatherImages[3] = (ImageView) view.findViewById(R.id.drizzle);
        weatherImages[4] = (ImageView) view.findViewById(R.id.showers);
        weatherImages[5] = (ImageView) view.findViewById(R.id.thunderstorms);

        humidityImages = new ImageView[4];
        humidityImages[0] = (ImageView) view.findViewById(R.id.cactus);
        humidityImages[1] = (ImageView) view.findViewById(R.id.drop_1);
        humidityImages[2] = (ImageView) view.findViewById(R.id.drop_3);
        humidityImages[3] = (ImageView) view.findViewById(R.id.drop_10);

        windImages = new ImageView[3];
        windImages[0] = (ImageView) view.findViewById(R.id.weak_wind);
        windImages[1] = (ImageView) view.findViewById(R.id.moderate_wind);
        windImages[2] = (ImageView) view.findViewById(R.id.strong_wind);


        indoorOrOutdoorRadioGroup = (RadioGroup) view.findViewById(R.id.indoor_or_outdoor_radio_group);


        dayOrNightRadioGroup = (RadioGroup) view.findViewById(R.id.day_or_night_radio_group);


        airOnOrOffRadioGroup = (RadioGroup) view.findViewById(R.id.has_air_radio_group);
    }

    public void setUserInformation(String name, int location, int day_or_night, int air, int rain_level, int temp_level, int wind_level, int humidity_level)
    {
        if(name != null && !name.equals(""))
            users_name.setText(name);

        if(location != -1)
        {
            int pos = (2*location) + 1;
            ((RadioButton) indoorOrOutdoorRadioGroup.getChildAt(pos)).setChecked(true);
        }

        if(day_or_night != -1)
        {
            int pos = day_or_night == 0 ? 1 : 3;
            ((RadioButton) dayOrNightRadioGroup.getChildAt(pos)).setChecked(true);
        }

        if(air != -1)
        {
            air = air == 0 ? 1 : 3;
            ((RadioButton) airOnOrOffRadioGroup.getChildAt(air)).setChecked(true);
        }

        if(rain_level != -1)
        {
            rainLevelSeekbar.setProgress(rain_level);
            weatherImages[rain_level].setAlpha(1.0f);
        }

        if(temp_level != -1)
        {
            tempLevelSeekbar.setProgress(temp_level);
            tempLevelProgress.setText(temp_level + "°C");
        }

        if(wind_level != -1)
        {
            windLevelSeekbar.setProgress(wind_level);
            windImages[wind_level].setAlpha(1.0f);
        }

        if(humidity_level != -1)
        {
            humidityLevelSeekbar.setProgress(humidity_level);
            humidityImages[humidity_level].setAlpha(1.0f);
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();

        String name = args.getString("name");
        int location = args.getInt("location");
        int day_or_night = args.getInt("day_or_night");
        int rain_level = args.getInt("rain_level");
        int temp_level = args.getInt("temp_level");
        int wind_level = args.getInt("wind_level");
        int humidity_level = args.getInt("humidity_level");
        int air = args.getInt("air");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_perfil, null);

        getWidgets(view);

        setUserInformation(name, location, day_or_night, air, rain_level, temp_level, wind_level, humidity_level);

        setEvents();

        if(location == 0 || location == -1)
            showLayout(INDOORS_LAYOUT);
        else if(location == 1)
            showLayout(OUTDOORS_LAYOUT);
        else
            showLayout(CAR_LAYOUT);


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.others_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        String usersName = String.valueOf(users_name.getText());

                        int location_pos = indoorOrOutdoorRadioGroup.indexOfChild(view.findViewById(indoorOrOutdoorRadioGroup.getCheckedRadioButtonId()));
                        int location =  (location_pos-1)/2;
                        int dayOrNight = dayOrNightRadioGroup.indexOfChild(view.findViewById(dayOrNightRadioGroup.getCheckedRadioButtonId())) == 1 ? 0 : 1;
                        int humidityLevel = humidityLevelSeekbar.getProgress();
                        int rainLevel = rainLevelSeekbar.getProgress();
                        int temp = tempLevelSeekbar.getProgress();
                        int airOnOrOff = -1;
                        int windLevel = -1;

                        if(location == 0)
                            airOnOrOff = airOnOrOffRadioGroup.indexOfChild(view.findViewById(airOnOrOffRadioGroup.getCheckedRadioButtonId())) == 1 ? 0 : 1;
                        else if(location == 1)
                            windLevel = windLevelSeekbar.getProgress();
                        else
                        {
                            airOnOrOff = airOnOrOffRadioGroup.indexOfChild(view.findViewById(airOnOrOffRadioGroup.getCheckedRadioButtonId())) == 1 ? 0 : 1;
                            windLevel = windLevelSeekbar.getProgress();
                        }

                        mListener.onInitialDialogPositiveClick(usersName, location, dayOrNight, airOnOrOff, rainLevel, temp, windLevel, humidityLevel);
                    }
                });

        Dialog dialog = builder.create();
        return dialog;
    }
}
